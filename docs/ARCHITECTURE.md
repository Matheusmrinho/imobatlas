# Arquitetura inicial

## Monólito modular

O backend é uma aplicação Spring Boot organizada por funcionalidade. Evita infraestrutura distribuída prematura, mantendo módulos claros para catálogo, cálculo e futura integração de mercado.

Fluxo de simulação: Angular → controller com Bean Validation → serviço → catálogo → calculadora pura → DTO de resultado.

## Persistência

`funds` guarda cadastro e um snapshot de preço/distribuição. `properties` guarda imóveis, com vínculo ao fundo e região. A modelagem inicial admite um fundo por registro de imóvel; copropriedade exigirá uma tabela de participações na evolução do modelo.

Flyway cria o schema; Hibernate apenas valida. `open-in-view` está desativado. Entity graphs carregam os imóveis usados nos DTOs dentro da transação. Entidades JPA não são expostas diretamente pela API.

Na demo, o filtro é feito em memória sobre quatro fundos. Com uma base real, migrar para consultas paginadas no banco. Não existem percentuais regionais sem uma base de ponderação auditável.

## Fontes de mercado

`MarketDataProvider` é somente um contrato para a próxima etapa; não há implementação, job de sincronização ou chamada externa ativa. O modelo futuro deve guardar histórico, data de observação, provedor e moeda, separar preço de distribuição e impedir duplicação na ingestão.

## Frontend

Componentes standalone, rotas carregadas sob demanda, signals para estado e formulários reativos no simulador. O cálculo permanece no servidor. Ao alterar as entradas, o resultado anterior é removido para evitar exibir uma simulação desatualizada. Durante o cálculo o formulário fica desabilitado.

Proxy local via Angular CLI; no Compose, Nginx serve o build e encaminha `/api`. Não há URLs de serviços externos ou segredos no frontend.

## Limites operacionais

Ambiente demonstrativo local: sem login, persistência de simulações, dados internacionais, mapa, cotações reais ou atualização agendada. Uma implantação pública requer uma etapa própria de autenticação quando aplicável, limites de requisição, configuração de HTTPS e gerenciamento de segredos.
