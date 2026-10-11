import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({ selector: 'app-root', imports: [RouterLink, RouterLinkActive, RouterOutlet], template: `
  <a class="skip" href="#main">Pular para o conteúdo</a>
  <header class="topbar"><a class="brand" routerLink="/fundos" aria-label="ImobAtlas, início"><span class="brand-icon">IA</span> Imob<span>Atlas</span></a>
    <nav aria-label="Navegação principal"><a routerLink="/fundos" routerLinkActive="active">Explorar fundos</a><a routerLink="/simulador" routerLinkActive="active">Simulador</a></nav>
    <span class="tag">PORTFÓLIO · V0.1</span></header>
  <div class="demo-banner"><span class="dot"></span><strong>Ambiente demonstrativo</strong><span>Fundos, imóveis e valores fictícios. Nenhuma cotação de mercado está conectada.</span></div>
  <main id="main" tabindex="-1"><router-outlet /></main>
  <footer><span>ImobAtlas / investimentos em perspectiva</span><span>Brasil na primeira versão · expansão internacional planejada</span></footer>
` })
export class AppComponent { }
