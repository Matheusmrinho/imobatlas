# Verificação da entrega inicial

Executada em 11/10/2026:

| Verificação | Resultado |
| --- | --- |
| Maven verify, Java 17 / Spring Boot 4.0.8 | Passou; JAR executável gerado |
| Testes unitários financeiros | 7 passaram |
| Testes de integração HTTP + H2 + Flyway | 4 passaram |
| Compilação Angular de produção | Passou, incluindo templates e tipos estritos |
| Maven Wrapper 3.9.11 | Executado com sucesso; checksum da distribuição configurado |

Os testes de integração cobrem catálogo, fundo por ticker, filtro regional, busca vazia, 404, parâmetros inválidos e simulação via HTTP. Os testes financeiros cobrem cotas inteiras, saldo, reinvestimento, retiradas, momento do aporte, arredondamento e conservação do total em 360 meses.

## Limitações da verificação

- Docker/Compose e PostgreSQL não foram executados neste ambiente; a execução integrada usou H2 em modo PostgreSQL. O Compose está fornecido para validação local.
- Não foi possível concluir a inspeção visual e o teste em navegador: o download do Chromium do ambiente falhou. O build Angular passou, mas não substitui validação visual/E2E.
- A CI está configurada, mas ainda não foi executada no GitHub.
- Não há teste de integração com provedor externo, pois a versão não possui um provedor ativo.
