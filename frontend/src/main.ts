import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { registerLocaleData } from '@angular/common';
import pt from '@angular/common/locales/pt';
import { LOCALE_ID, provideZonelessChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';

registerLocaleData(pt);
bootstrapApplication(AppComponent, { providers: [provideHttpClient(), provideRouter(routes),
  provideZonelessChangeDetection(), { provide: LOCALE_ID, useValue: 'pt-BR' }] }).catch(console.error);
