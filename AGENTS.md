# Orientações para trabalhar no ImobAtlas

- Backend: Java 17 e Spring Boot; frontend: Angular standalone com TypeScript estrito.
- Organize por funcionalidade; mantenha controllers pequenos e regras no módulo correspondente.
- Valores financeiros são calculados no backend com BigDecimal. Nunca use double para regras monetárias.
- Mantenha SimulationCalculator independente de Spring, banco, rede e relógio.
- Mudanças de regra devem atualizar docs/SIMULATION.md e testes com exemplos numéricos verificáveis.
- Não apresente fixtures como dados reais; preserve demo, source e asOf.
- Nenhum scraper ou API de mercado deve ser ativado sem validar cobertura, contrato, limites e datas dos dados.
- Use migrations novas para mudanças de schema; não reescreva migrations já publicadas.
- Não inclua .env, senhas, node_modules, target ou dist nos commits.
- Antes de entregar: backend/./mvnw verify e, em frontend, npm ci e npm run build.
- Consulte README.md e docs/ARCHITECTURE.md antes de ampliar escopo.
