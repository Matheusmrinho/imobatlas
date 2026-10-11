package br.com.imobatlas.simulation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/** Núcleo puro: sem Spring, banco ou requisições HTTP. */
public final class SimulationCalculator {
    private SimulationCalculator() { }
    public static List<SimulationResult.Month> calculate(BigDecimal initial, BigDecimal contribution,
            int months, boolean reinvest, BigDecimal price, BigDecimal distributionPerUnit) {
        if (initial.signum() < 0 || contribution.signum() < 0 || price.signum() <= 0
                || distributionPerUnit.signum() < 0 || months < 1 || months > 360) {
            throw new IllegalArgumentException("Parâmetros de simulação inválidos.");
        }
        long units = initial.divide(price, 0, RoundingMode.DOWN).longValueExact();
        BigDecimal cash = initial.subtract(price.multiply(BigDecimal.valueOf(units)));
        BigDecimal contributed = initial;
        BigDecimal withdrawn = BigDecimal.ZERO;
        List<SimulationResult.Month> timeline = new ArrayList<>();
        timeline.add(row(0, units, contributed, BigDecimal.ZERO, cash, withdrawn, price));
        for (int month = 1; month <= months; month++) {
            // Convenção: cotas existentes no início do mês recebem a distribuição;
            // aporte e novas compras acontecem no fim do mês.
            BigDecimal distribution = money(distributionPerUnit.multiply(BigDecimal.valueOf(units)));
            if (reinvest) cash = cash.add(distribution);
            else withdrawn = withdrawn.add(distribution);
            cash = cash.add(contribution);
            contributed = contributed.add(contribution);
            long purchased = cash.divide(price, 0, RoundingMode.DOWN).longValueExact();
            units = Math.addExact(units, purchased);
            cash = cash.subtract(price.multiply(BigDecimal.valueOf(purchased)));
            timeline.add(row(month, units, contributed, distribution, cash, withdrawn, price));
        }
        return List.copyOf(timeline);
    }
    private static SimulationResult.Month row(int month, long units, BigDecimal contributed,
            BigDecimal distribution, BigDecimal cash, BigDecimal withdrawn, BigDecimal price) {
        BigDecimal invested = price.multiply(BigDecimal.valueOf(units));
        return new SimulationResult.Month(month, units, money(contributed), money(distribution),
                money(cash), money(withdrawn), money(invested), money(invested.add(cash).add(withdrawn)));
    }
    private static BigDecimal money(BigDecimal value) { return value.setScale(2, RoundingMode.HALF_UP); }
}
