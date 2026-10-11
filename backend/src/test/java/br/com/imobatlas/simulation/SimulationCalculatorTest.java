package br.com.imobatlas.simulation;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

class SimulationCalculatorTest {
    private BigDecimal d(String value) { return new BigDecimal(value); }
    @Test void buysWholeUnitsAndPreservesChange() {
        var rows = SimulationCalculator.calculate(d("1050"), d("0"), 1, true, d("100"), d("0"));
        assertThat(rows.get(0).units()).isEqualTo(10);
        assertThat(rows.get(0).cash()).isEqualByComparingTo("50");
        assertThat(rows.get(1).totalValue()).isEqualByComparingTo("1050");
    }
    @Test void reinvestsOnlyAfterPayingExistingUnits() {
        var rows = SimulationCalculator.calculate(d("950"), d("0"), 2, true, d("100"), d("10"));
        assertThat(rows.get(1).distribution()).isEqualByComparingTo("90");
        assertThat(rows.get(1).units()).isEqualTo(10);
        assertThat(rows.get(2).distribution()).isEqualByComparingTo("100");
        assertThat(rows.get(2).units()).isEqualTo(11);
        assertThat(rows.get(2).cash()).isEqualByComparingTo("40");
        assertThat(rows.get(2).totalValue()).isEqualByComparingTo("1140");
    }
    @Test void withdrawalsAreNotReinvestedOrDoubleCounted() {
        var rows = SimulationCalculator.calculate(d("950"), d("0"), 2, false, d("100"), d("10"));
        var last = rows.get(2);
        assertThat(last.units()).isEqualTo(9);
        assertThat(last.withdrawn()).isEqualByComparingTo("180");
        assertThat(last.cash()).isEqualByComparingTo("50");
        assertThat(last.totalValue()).isEqualByComparingTo("1130");
    }
    @Test void contributionsBuyAtMonthEndAndCashAccumulates() {
        var rows = SimulationCalculator.calculate(d("0"), d("50"), 3, true, d("100"), d("1"));
        assertThat(rows.get(1).units()).isZero();
        assertThat(rows.get(2).units()).isEqualTo(1);
        assertThat(rows.get(2).distribution()).isEqualByComparingTo("0");
        assertThat(rows.get(3).distribution()).isEqualByComparingTo("1");
        assertThat(rows.get(3).totalValue()).isEqualByComparingTo("151");
    }
    @Test void usesDecimalArithmeticAndExplicitRounding() {
        var last = SimulationCalculator.calculate(d("0.30"), d("0"), 1, false, d("0.10"), d("0.015")).get(1);
        assertThat(last.units()).isEqualTo(3);
        assertThat(last.distribution()).isEqualByComparingTo("0.05");
        assertThat(last.totalValue()).isEqualByComparingTo("0.35");
    }
    @Test void rejectsZeroPriceAndInvalidHorizon() {
        assertThatIllegalArgumentException().isThrownBy(() -> SimulationCalculator.calculate(d("1"), d("0"), 1, true, d("0"), d("0")));
        assertThatIllegalArgumentException().isThrownBy(() -> SimulationCalculator.calculate(d("1"), d("0"), 361, true, d("1"), d("0")));
    }
    @Test void conservesContributionsPlusDistributionsAcrossLongScenarios() {
        for (boolean reinvest : new boolean[]{true, false}) {
            var rows = SimulationCalculator.calculate(d("10000"), d("500"), 360, reinvest, d("85"), d("0.60"));
            BigDecimal cumulative = BigDecimal.ZERO;
            for (var row : rows) {
                cumulative = cumulative.add(row.distribution());
                assertThat(row.totalValue()).isEqualByComparingTo(row.contributed().add(cumulative));
                assertThat(row.cash()).isGreaterThanOrEqualTo(BigDecimal.ZERO).isLessThan(d("85"));
            }
        }
    }
}
