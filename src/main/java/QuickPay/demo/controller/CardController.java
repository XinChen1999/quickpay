package QuickPay.demo.controller;

import QuickPay.demo.model.Card;
import QuickPay.demo.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/card")
public class CardController {
    private final CardService cardService;
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity addCreditCard(@RequestBody Card card) {
        cardService.addCreditCard(card);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity updateCreditCard(@RequestBody Card card) {
        cardService.updateCreditCard(card);
        return ResponseEntity.ok().build();

    }

    @GetMapping
    public ResponseEntity<List<Card>> getAllCreditCards() {
        return ResponseEntity.ok(cardService.getAllCreditCards());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCreditCard(@PathVariable String id) {
        cardService.deleteCreditCard(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
