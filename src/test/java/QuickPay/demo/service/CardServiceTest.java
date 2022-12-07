package QuickPay.demo.service;

import QuickPay.demo.model.Card;
import QuickPay.demo.repository.CardsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardsRepository cardsRepository;

    @InjectMocks
    private CardService cardService;

    private Card card;

    @BeforeEach
    void setUp() {
        card = new Card("123", "Alice", "12/2026", 212, 220, "seattle", "Credit", "654321");
    }

    @Test
    void givenCreditCard_whenAddCreditCard_thenReturnSavedCreditCard() {
        given(cardsRepository.insert(card)).willReturn(card);
        Card savedCard = cardService.addCreditCard(card);
        Assertions.assertNotNull(savedCard);
    }

    @Test
    void givenCreditCard_whenUpdateCreditCard_thenReturnCard() {
        given(cardsRepository.findById(card.getId())).willReturn(Optional.of(card));
        card.setCardType("Credit");
        card.setHolderName("Lily");

        Card updateCreditCard = cardService.updateCreditCard(card);

        Assertions.assertEquals("Credit", updateCreditCard.getCardType());
        Assertions.assertEquals("Lily", updateCreditCard.getHolderName());
    }

    @Test
    void givenCardList_whenGetAllCreditCards_thenReturnCreditCardsList() {
        Card card1 = new Card("321", "Jack", "12/2026", 212, 220, "seattle", "Credit", "654321");

        given(cardsRepository.findAll()).willReturn(List.of(card, card1));

        List<Card> list = cardService.getAllCreditCards();

        Assertions.assertEquals(2, list.size());
    }

    @Test
    void deleteCreditCard() {
        willDoNothing().given(cardsRepository).deleteById("321");
        cardService.deleteCreditCard("321");
        verify(cardsRepository, times(1)).deleteById("321");
    }
}