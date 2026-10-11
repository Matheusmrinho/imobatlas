package br.com.imobatlas.simulation;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record SimulationRequest(
        @NotBlank @Size(max = 20) String ticker,
        @NotNull @DecimalMin("0.00") @DecimalMax("100000000.00") @Digits(integer = 9, fraction = 2) BigDecimal initialInvestment,
        @NotNull @DecimalMin("0.00") @DecimalMax("1000000.00") @Digits(integer = 7, fraction = 2) BigDecimal monthlyContribution,
        @NotNull @Min(1) @Max(360) Integer months,
        @NotNull Boolean reinvestDistributions) { }
