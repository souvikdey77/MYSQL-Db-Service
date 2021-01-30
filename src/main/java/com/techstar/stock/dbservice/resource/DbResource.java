package com.techstar.stock.dbservice.resource;

import com.techstar.stock.dbservice.model.Quote;
import com.techstar.stock.dbservice.model.Quotes;
import com.techstar.stock.dbservice.repository.QuotesRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/db")
public class DbResource {

    private QuotesRepository quotesRepository;

    public DbResource(QuotesRepository quotesRepository) {
        this.quotesRepository = quotesRepository;
    }

    @GetMapping("/{username}")
    public List<String> getQuotes(@PathVariable("username") final String userName){
        return getQuotesByUserName(userName);
    }

    @PostMapping("/add")
    public List<String> addUser(@RequestBody final Quotes quotes){
        quotes.getQuotes().stream()
                .map(quote -> new Quote(quotes.getUserName(),quote))
                .forEach(quote -> quotesRepository.save(quote));
        return getQuotesByUserName(quotes.getUserName());
    }

    @DeleteMapping("delete/{username}")
    public List<String> deleteUser(@PathVariable("username") final String username){
        List<Quote> quotes = quotesRepository.findByUserName(username);
        for(Quote quote:quotes){
            quotesRepository.delete(quote);
        }
        return getQuotesByUserName(username);
    }

    private List<String> getQuotesByUserName(String userName) {
        return quotesRepository.findByUserName(userName)
                .stream()
                .map(Quote::getQuote)
                .collect(Collectors.toList());
    }
}
