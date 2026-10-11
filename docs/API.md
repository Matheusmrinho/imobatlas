# API v1

Base local: `http://localhost:8080`. JSON UTF-8. Sem autenticação nesta versão local de portfólio.

## Consultar

```bash
curl 'http://localhost:8080/api/v1/funds?region=NORDESTE'
curl 'http://localhost:8080/api/v1/funds?search=DEMO11'
curl 'http://localhost:8080/api/v1/funds/DEMO11'
```

`region`: NORTE, NORDESTE, CENTRO_OESTE, SUDESTE ou SUL. O filtro retorna fundos com pelo menos um imóvel na região, mantendo a lista completa de imóveis do fundo. Não representa exposição de 100% à região selecionada. `search` busca nome ou ticker sem diferenciar maiúsculas. Sem correspondências, retorna `[]`.

A resposta de fundo contém `ticker`, `name`, `segment`, `currency`, `country`, `price`, `monthlyDistribution`, `asOf`, `source`, `demo` e `properties`.

## Simular

```bash
curl -X POST 'http://localhost:8080/api/v1/simulations' \
  -H 'Content-Type: application/json' \
  -d '{"ticker":"DEMO11","initialInvestment":1000,"monthlyContribution":0,"months":1,"reinvestDistributions":false}'
```

O cenário acima produz total aportado 1.000, distribuições acumuladas 8, valor final 1.008, 10 cotas e 8 retirados. Os valores são demonstrativos.

Em PowerShell, use `Invoke-RestMethod`:

```powershell
$body = @{ ticker = 'DEMO11'; initialInvestment = 1000; monthlyContribution = 0; months = 1; reinvestDistributions = $false } | ConvertTo-Json
Invoke-RestMethod -Uri 'http://localhost:8080/api/v1/simulations' -Method Post -ContentType 'application/json' -Body $body
```

Resposta: metadados e premissas, totais e `timeline`, que inclui o mês zero. Os campos da série são `month`, `units`, `contributed`, `distribution`, `cash`, `withdrawn`, `investedValue` e `totalValue`. Valores financeiros são números JSON; o frontend apenas formata os resultados, sem recalcular as regras.

## Erros

- `400`: entrada inválida, JSON malformado, região inexistente ou campos obrigatórios ausentes.
- `404`: fundo não encontrado.
- Formato: `application/problem+json`, com `status`, `title`, `detail` e, quando aplicável, `errors`.

Não há paginação na pequena base demonstrativa. Deve ser adicionada antes de importar catálogos maiores.
