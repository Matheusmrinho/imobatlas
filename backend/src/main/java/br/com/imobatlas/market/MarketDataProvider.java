package br.com.imobatlas.market;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

/** Contrato para futura integração. Nenhum provedor externo está ativo nesta versão. */
public interface MarketDataProvider {
    Optional<Quote> latestQuote(String ticker);
    record Quote(String ticker, String currency, BigDecimal price, Instant observedAt, String source) { }
}
