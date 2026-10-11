import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'fundos' },
  { path: 'fundos', title: 'Explorar fundos | ImobAtlas', loadComponent: () => import('./features/catalog/catalog.component').then(m => m.CatalogComponent) },
  { path: 'simulador', title: 'Simulador | ImobAtlas', loadComponent: () => import('./features/simulator/simulator.component').then(m => m.SimulatorComponent) },
  { path: '**', redirectTo: 'fundos' }
];
