package br.com.imobatlas.fund;

import jakarta.persistence.*;

@Entity
@Table(name = "properties")
public class Property {
    @Id private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fund_id", nullable = false) private Fund fund;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String city;
    @Column(nullable = false) private String state;
    @Column(nullable = false) private String country;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Region region;

    protected Property() { }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getCountry() { return country; }
    public Region getRegion() { return region; }
}
