package QuickPay.demo.repository;

import QuickPay.demo.model.Card;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CardsRepository extends MongoRepository<Card, String> {
}
