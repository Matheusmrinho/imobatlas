package br.com.imobatlas.fund;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "funds")
public class Fund {
    @Id private Long id;
    @Column(nullable = false, unique = true) private String ticker;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String segment;
    @Column(nullable = false) private String currency;
    @Column(nullable = false) private String country;
    @Column(nullable = false, precision = 19, scale = 4) private BigDecimal price;
    @Column(nullable = false, precision = 19, scale = 4) private BigDecimal monthlyDistribution;
    @Column(nullable = false) private LocalDate asOf;
    @Column(nullable = false) private String source;
    @Column(nullable = false) private boolean demo;
    @OneToMany(mappedBy = "fund", fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private List<Property> properties = new ArrayList<>();

    protected Fund() { }
    public Long getId() { return id; }
    public String getTicker() { return ticker; }
    public String getName() { return name; }
    public String getSegment() { return segment; }
    public String getCurrency() { return currency; }
    public String getCountry() { return country; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getMonthlyDistribution() { return monthlyDistribution; }
    public LocalDate getAsOf() { return asOf; }
    public String getSource() { return source; }
    public boolean isDemo() { return demo; }
    public List<Property> getProperties() { return properties; }
}
