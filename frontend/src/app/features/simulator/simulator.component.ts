import { Component, DestroyRef, computed, inject, signal } from '@angular/core';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ApiService } from '../../core/api.service';
import { Fund, SimulationResult } from '../../core/models';

@Component({ selector:'app-simulator', imports:[ReactiveFormsModule, CurrencyPipe, DecimalPipe], templateUrl:'./simulator.component.html' })
export class SimulatorComponent {
  private readonly api = inject(ApiService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly route = inject(ActivatedRoute);
  readonly funds = signal<Fund[]>([]);
  readonly loadingFunds = signal(true);
  readonly busy = signal(false);
  readonly error = signal('');
  readonly result = signal<SimulationResult | null>(null);
  readonly form = inject(FormBuilder).nonNullable.group({
    ticker:['', Validators.required],
    initialInvestment:[10000, [Validators.required, Validators.min(0), Validators.max(100000000), Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
    monthlyContribution:[500, [Validators.required, Validators.min(0), Validators.max(1000000), Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
    months:[24, [Validators.required, Validators.min(1), Validators.max(360), Validators.pattern(/^\d+$/)]],
    reinvestDistributions:[true]
  });
  readonly chartRows = computed(() => {
    const rows = this.result()?.timeline ?? [];
    const step = Math.max(1, Math.ceil((rows.length - 1) / 10));
    return rows.filter((r, i) => r.month % step === 0 || i === rows.length - 1);
  });
  readonly last = computed(() => this.result()?.timeline.at(-1));
  constructor() {
    this.loadFunds();
    this.form.valueChanges.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(() => this.result.set(null));
  }
  loadFunds() {
    this.loadingFunds.set(true); this.error.set('');
    this.api.funds().pipe(takeUntilDestroyed(this.destroyRef)).subscribe({next: funds => {
      this.funds.set(funds);
      const requested = this.route.snapshot.queryParamMap.get('ticker');
      this.form.controls.ticker.setValue(funds.find(f => f.ticker === requested)?.ticker ?? funds[0]?.ticker ?? '');
      this.loadingFunds.set(false);
    }, error: () => { this.loadingFunds.set(false); this.error.set('Não foi possível carregar os fundos. Verifique a conexão com a API.'); }});
  }
  simulate() {
    if (this.form.invalid || this.busy()) { this.form.markAllAsTouched(); return; }
    const body = this.form.getRawValue();
    this.busy.set(true); this.result.set(null); this.error.set(''); this.form.disable({emitEvent:false});
    this.api.simulate(body).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({next: result => {
      this.result.set(result); this.busy.set(false); this.form.enable({emitEvent:false});
    }, error: () => { this.error.set('Não foi possível simular. Confira os valores e tente novamente.'); this.busy.set(false); this.form.enable({emitEvent:false}); }});
  }
  barWidth(value:number) { return Math.max(0, Math.min(100, value / (this.result()?.finalValue || 1) * 100)); }
}
