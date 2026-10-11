import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Fund, SimulationRequest, SimulationResult } from './models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);
  funds(search = '', region = '') {
    let params = new HttpParams();
    if (search.trim()) params = params.set('search', search.trim());
    if (region) params = params.set('region', region);
    return this.http.get<Fund[]>('/api/v1/funds', { params });
  }
  simulate(body:SimulationRequest) { return this.http.post<SimulationResult>('/api/v1/simulations', body); }
}
