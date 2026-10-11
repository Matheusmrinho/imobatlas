package br.com.imobatlas.fund;

import br.com.imobatlas.shared.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class FundService {
    private final FundRepository repository;
    public FundService(FundRepository repository) { this.repository = repository; }

    public List<FundResponse> list(String search, Region region) {
        String term = search == null ? "" : search.strip().toLowerCase(Locale.ROOT);
        return repository.findAllByOrderByTickerAsc().stream()
                .filter(f -> term.isEmpty() || (f.getTicker() + " " + f.getName()).toLowerCase(Locale.ROOT).contains(term))
                .filter(f -> region == null || f.getProperties().stream().anyMatch(p -> p.getRegion() == region))
                .map(FundResponse::from).toList();
    }
    public FundResponse get(String ticker) {
        return repository.findByTickerIgnoreCase(ticker).map(FundResponse::from)
                .orElseThrow(() -> new NotFoundException("Fundo não encontrado: " + ticker));
    }
}
