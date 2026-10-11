# Regras do simulador v0.1

## Escopo

Um fundo brasileiro em BRL por cenário, sem variação do preço ou da distribuição por cota. Sem conversão cambial, impostos, taxas, inflação, amortizações ou eventos corporativos. As distribuições são parâmetros fictícios da base de demonstração e não taxas de retorno previstas.

## Convenções

- Investimento inicial: de R$ 0 a R$ 100.000.000,00.
- Aporte mensal: de R$ 0 a R$ 1.000.000,00.
- Entradas monetárias: até duas casas decimais.
- Prazo: inteiro de 1 a 360 meses, com um aporte ao fim de cada mês.
- Reinvestimento: booleano obrigatório; não é inferido se omitido.
- Preço: estritamente positivo. Distribuição por cota: não negativa.
- Compras: `floor(caixa / preço)` cotas. A sobra nunca é descartada.
- Rendimentos: número de cotas no início do mês × distribuição por cota; arredondamento para centavos com `HALF_UP`.
- Saldo interno: preserva precisão decimal. Campos monetários da resposta: duas casas decimais.
- Novas cotas só recebem no próximo período.

## Exemplo auditável

Investimento inicial R$ 950, cota R$ 100, distribuição R$ 10 por cota, sem aportes:

| Mês | Reinvestir | Cotas finais | Rendimento do mês | Caixa | Retirados acumulados | Valor total |
| --- | --- | --- | --- | --- | --- | --- |
| 0 | Sim | 9 | 0 | 50 | 0 | 950 |
| 1 | Sim | 10 | 90 | 40 | 0 | 1.040 |
| 2 | Sim | 11 | 100 | 40 | 0 | 1.140 |
| 2 | Não | 9 | 90 | 50 | 180 | 1.130 |

São números artificiais para demonstrar o algoritmo. Em todos os períodos, com o preço constante, o total corresponde ao aportado mais todas as distribuições acumuladas. A soma dos rendimentos não deve ser adicionada novamente ao total final.

## Decisão de arquitetura

`SimulationCalculator` é Java puro: nenhum acesso ao banco, ao relógio ou à rede. `SimulationService` obtém os dados do fundo e acrescenta metadados. `SimulationController` valida o contrato HTTP. Isso permite testar as regras sem subir o Spring.

Uma futura simulação histórica precisará de preços, datas de elegibilidade e pagamento, eventos corporativos e dados sem viés de antecipação. Ela deve ser um modo separado, não uma troca silenciosa das regras desta versão.
