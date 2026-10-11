package br.com.imobatlas.fund;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FundRepository extends JpaRepository<Fund, Long> {
    @EntityGraph(attributePaths = "properties")
    List<Fund> findAllByOrderByTickerAsc();
    @EntityGraph(attributePaths = "properties")
    Optional<Fund> findByTickerIgnoreCase(String ticker);
}
