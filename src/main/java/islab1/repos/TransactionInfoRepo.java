package islab1.repos;

import islab1.models.TransactionInfo;
import islab1.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionInfoRepo extends JpaRepository<TransactionInfo, Long>{

    List<TransactionInfo> getTransactionInfosByCreator(User user);

}
