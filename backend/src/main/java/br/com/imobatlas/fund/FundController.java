package br.com.imobatlas.fund;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/funds")
public class FundController {
    private final FundService service;
    public FundController(FundService service) { this.service = service; }
    @GetMapping
    public List<FundResponse> list(@RequestParam(required = false) String search,
                                  @RequestParam(required = false) Region region) {
        return service.list(search, region);
    }
    @GetMapping("/{ticker}")
    public FundResponse get(@PathVariable String ticker) { return service.get(ticker); }
}
