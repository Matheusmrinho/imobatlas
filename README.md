# ImobAtlas

**Fundos imobiliários sob uma perspectiva geográfica.**

Projeto de portfólio full stack em **Java 17 + Spring Boot 4.0.8**, **Angular 21.2** e **PostgreSQL 17**. Explore fundos pela localização dos imóveis e simule aportes com regras financeiras explícitas.

> **Versão 0.1 demonstrativa.** Os quatro fundos, tickers, imóveis, preços e rendimentos são fictícios. Não há conexão com cotações reais. A simulação é um cenário de preço e distribuição constantes, não uma previsão de retorno ou recomendação de investimento.

## Funcionalidades disponíveis

- Catálogo consultado pela API, com busca por nome/ticker e filtro pelas cinco regiões do Brasil.
- Relação entre fundos e seus imóveis; um fundo pode aparecer em várias regiões.
- Detalhamento de imóveis, origem dos dados e data de referência.
- Simulação de um fundo por vez: capital inicial, aporte mensal, prazo e reinvestimento.
- Compra de cotas inteiras e conservação do saldo não utilizado.
- Gráfico de evolução, indicadores e tabela mensal.
- Validação no frontend e backend; erros da API em formato Problem Details.
- Migrations Flyway, perfis H2/PostgreSQL, Maven Wrapper, Docker Compose e CI.
- Testes unitários do cálculo e integração HTTP com banco H2.

## Começar com Docker

Pré-requisito: Docker Engine/Desktop com Compose v2. Na raiz do projeto:

```bash
cp .env.example .env
# No PowerShell: Copy-Item .env.example .env
docker compose up --build
```

Escolha uma senha local em `.env` antes de executar. O Compose inicia o PostgreSQL, aplica as migrations e carrega os dados fictícios.

| Serviço | Endereço |
| --- | --- |
| Aplicação | http://localhost:4200 |
| Catálogo da API | http://localhost:8080/api/v1/funds |
| Saúde da API | http://localhost:8080/actuator/health |

Para parar: `docker compose down`. O volume preserva os dados. As portas publicadas ficam restritas ao computador local; este Compose é um ambiente de desenvolvimento.

## Desenvolvimento sem Docker

Requisitos: **JDK 17**, **Node.js 24** e acesso à internet para baixar dependências. O Maven Wrapper dispensa instalar Maven globalmente.

Terminal 1:

```bash
cd backend
./mvnw spring-boot:run
# No Windows: .\mvnw.cmd spring-boot:run
```

O perfil padrão `demo` usa H2 em memória e perde os dados ao encerrar. Não exige PostgreSQL.

Terminal 2:

```bash
cd frontend
npm ci
npm start
```

Abra http://localhost:4200. O proxy do Angular encaminha `/api` para a porta 8080, sem necessidade de CORS permissivo.

### PostgreSQL fora do Compose

Defina `SPRING_PROFILES_ACTIVE=postgres`, `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`. Nesse perfil são aplicadas somente as migrations estruturais: o catálogo começa vazio.

Para carregar os dados demonstrativos em um banco de desenvolvimento novo, adicione `SPRING_FLYWAY_LOCATIONS=classpath:db/migration,classpath:db/demo`. Mantenha essa localização configurada enquanto esse banco contiver a migration demonstrativa. Dados reais devem usar um banco separado; não remova migrations já aplicadas.

## Organização

| Caminho | Responsabilidade |
| --- | --- |
| `backend/.../fund` | Entidades, consultas, DTOs e endpoints do catálogo |
| `backend/.../simulation` | Contrato HTTP e motor financeiro puro |
| `backend/.../market` | Interface para futura fonte de cotações |
| `backend/.../shared` | Tratamento de erros |
| `frontend/src/app/core` | Modelos TypeScript e cliente HTTP |
| `frontend/src/app/features/catalog` | Catálogo e filtros |
| `frontend/src/app/features/simulator` | Formulário e visualização de resultados |
| `docs/` | Contrato da API, decisões e regras de cálculo |

As pastas Java acima ficam em `backend/src/main/java/br/com/imobatlas/`.

## Como o simulador calcula

1. No mês zero, o investimento inicial compra o máximo de cotas inteiras ao preço fixo. A sobra permanece em caixa.
2. Em cada mês, as cotas existentes no início do período recebem a distribuição hipotética.
3. Com reinvestimento, o rendimento entra no caixa; sem reinvestimento, é acumulado em `withdrawn`.
4. O aporte mensal entra ao final do mês. Caixa disponível compra novas cotas, que só recebem a partir do mês seguinte.
5. O valor total é **valor das cotas + caixa + rendimentos retirados**. Os retirados são contabilizados como riqueza acumulada fora da carteira, sem remuneração adicional.

O prazo de 24 meses inclui 24 aportes mensais, além do investimento inicial. Os rendimentos acumulados não são somados novamente ao total quando já reinvestidos. O cálculo usa `BigDecimal`, arredondamento monetário `HALF_UP` e cotas inteiras. Veja [as regras completas](docs/SIMULATION.md).

## API

| Método | Endpoint | Finalidade |
| --- | --- | --- |
| GET | `/api/v1/funds?search=Atlas&region=NORDESTE` | Filtrar catálogo |
| GET | `/api/v1/funds/DEMO11` | Consultar fundo e imóveis |
| POST | `/api/v1/simulations` | Calcular um cenário |
| GET | `/actuator/health` | Verificar disponibilidade |

Exemplo de corpo para simulação:

```json
{
  "ticker": "DEMO11",
  "initialInvestment": 10000,
  "monthlyContribution": 500,
  "months": 24,
  "reinvestDistributions": true
}
```

[Exemplos HTTP e limites](docs/API.md).

## Verificação

```bash
cd backend
./mvnw verify
cd ../frontend
npm ci
npm run build
```

O workflow `.github/workflows/ci.yml` executa os testes Java e a compilação estrita do Angular em pushes e pull requests. A configuração do workflow só é executada remotamente após publicar o repositório.

## Próximas entregas

- [ ] Provedor de cotações reais, com contrato/licença e cobertura verificados.
- [ ] Histórico de preços e distribuições, atualização agendada e idempotente.
- [ ] Importação de informes CVM e revisão da localização dos imóveis.
- [ ] Mapa de imóveis e percentuais geográficos com base de ponderação definida.
- [ ] Carteiras com vários fundos e comparação de cenários.
- [ ] Histórico de simulações e autenticação.
- [ ] REITs e outros mercados, moedas, câmbio e tratamento de eventos corporativos.
- [ ] Testes com PostgreSQL via Testcontainers e testes E2E contínuos.

## Obter o código

```bash
git clone https://github.com/Matheusmrinho/imobatlas.git
cd imobatlas
```

## Licença

[MIT](LICENSE) — Copyright (c) 2026 Matheus Romero.

## Referências técnicas

- [Spring Boot — requisitos](https://docs.spring.io/spring-boot/4.0/system-requirements.html)
- [Angular — compatibilidade](https://angular.dev/reference/versions)
- [CVM — informes mensais de FIIs](https://dados.cvm.gov.br/dataset/fii-doc-inf_mensal)
- [CVM — informes trimestrais de FIIs](https://dados.cvm.gov.br/dataset/fii-doc-inf_trimestral)

As fontes CVM são candidatas à etapa de ingestão, não alimentam os dados desta versão.
