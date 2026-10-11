package br.com.imobatlas.simulation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SimulationResult(String ticker, String currency, boolean demo, LocalDate asOf,
        BigDecimal fixedPrice, BigDecimal fixedMonthlyDistribution, String assumptions,
        BigDecimal totalContributed, BigDecimal totalDistributions, BigDecimal finalValue,
        List<Month> timeline) {
    public record Month(int month, long units, BigDecimal contributed, BigDecimal distribution,
                        BigDecimal cash, BigDecimal withdrawn, BigDecimal investedValue, BigDecimal totalValue) { }
}
