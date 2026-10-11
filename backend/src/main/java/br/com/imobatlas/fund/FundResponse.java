package br.com.imobatlas.fund;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FundResponse(String ticker, String name, String segment, String currency,
        String country, BigDecimal price, BigDecimal monthlyDistribution, LocalDate asOf,
        String source, boolean demo, List<PropertyResponse> properties) {
    public record PropertyResponse(Long id, String name, String city, String state, String country, Region region) { }
    static FundResponse from(Fund fund) {
        return new FundResponse(fund.getTicker(), fund.getName(), fund.getSegment(), fund.getCurrency(),
                fund.getCountry(), fund.getPrice(), fund.getMonthlyDistribution(), fund.getAsOf(),
                fund.getSource(), fund.isDemo(), fund.getProperties().stream().map(p ->
                    new PropertyResponse(p.getId(), p.getName(), p.getCity(), p.getState(), p.getCountry(), p.getRegion())).toList());
    }
}
