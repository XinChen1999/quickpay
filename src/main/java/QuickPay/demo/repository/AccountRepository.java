package QuickPay.demo.repository;

import QuickPay.demo.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository <Account, String> {

}
