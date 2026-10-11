package br.com.imobatlas.simulation;

import br.com.imobatlas.fund.FundService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class SimulationService {
    private final FundService funds;
    public SimulationService(FundService funds) { this.funds = funds; }
    public SimulationResult simulate(SimulationRequest request) {
        var fund = funds.get(request.ticker());
        var timeline = SimulationCalculator.calculate(request.initialInvestment(), request.monthlyContribution(),
                request.months(), request.reinvestDistributions(), fund.price(), fund.monthlyDistribution());
        var last = timeline.get(timeline.size() - 1);
        var distributions = timeline.stream().map(SimulationResult.Month::distribution).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new SimulationResult(fund.ticker(), fund.currency(), fund.demo(), fund.asOf(), fund.price(),
                fund.monthlyDistribution(),
                "Cenário hipotético com preço e distribuição constantes. Cotas inteiras; aportes no fim do mês; "
                + "novas cotas recebem apenas a partir do mês seguinte. Sem custos, impostos ou inflação. "
                + "Total inclui cotas, caixa e rendimentos retirados. Não é previsão nem backtest.",
                last.contributed(), distributions, last.totalValue(), timeline);
    }
}
