import { Component, DestroyRef, inject, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ApiService } from '../../core/api.service';
import { Fund, REGIONS } from '../../core/models';

@Component({ selector:'app-catalog', imports:[CurrencyPipe, DatePipe, FormsModule, RouterLink], templateUrl:'./catalog.component.html' })
export class CatalogComponent {
  private readonly api = inject(ApiService);
  private readonly destroyRef = inject(DestroyRef);
  readonly funds = signal<Fund[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly regions = REGIONS;
  search = ''; region = '';
  constructor() { this.load(); }
  load() {
    if (this.loading()) return;
    this.loading.set(true); this.error.set('');
    this.api.funds(this.search, this.region).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: funds => { this.funds.set(funds); this.loading.set(false); },
      error: () => { this.error.set('Não foi possível carregar o catálogo. Verifique se a API está disponível e tente novamente.'); this.loading.set(false); }
    });
  }
  places(fund:Fund) { return fund.properties.map(p => p.city + ' / ' + p.state).join(' · '); }
  clear() { this.search = ''; this.region = ''; this.load(); }
}
