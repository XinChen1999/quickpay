package QuickPay.demo.service;

import QuickPay.demo.model.Card;
import QuickPay.demo.repository.CardsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    private final CardsRepository cardsRepository;
    public CardService(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    public Card addCreditCard(Card card) {
        return cardsRepository.insert(card);
    }

    public Card updateCreditCard(Card card) {
        String id = card.getId();
        Card savedCard = cardsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot find Credit Card by ID: %s", id)));
        savedCard.setBankId(card.getBankId());
        savedCard.setLocation(card.getLocation());
        savedCard.setExpirationTime(card.getExpirationTime());
        savedCard.setHolderName(card.getHolderName());
        savedCard.setSecurityCode(card.getSecurityCode());

        cardsRepository.save(savedCard);

        return savedCard;
    }

    public List<Card> getAllCreditCards() {
        return cardsRepository.findAll();
    }

    public void deleteCreditCard(String id) {
        cardsRepository.deleteById(id);
    }

}
