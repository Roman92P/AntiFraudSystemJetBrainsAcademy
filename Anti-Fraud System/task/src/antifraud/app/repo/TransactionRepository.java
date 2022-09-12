package antifraud.app.repo;

import antifraud.app.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    //@Query("SELECT t FROM Transaction t WHERE t.number = :number AND t.date BETWEEN :date1 AND :date2 AND t.region != :region")
    @Query(value = "SELECT * FROM transaction  WHERE region != :region AND  number = :number AND date between :date1 and :date2",
            nativeQuery = true)
    Set<Transaction> getSimilarTransactionsFromDifferentRegions(@Param("number") String number, @Param("date1") Date date1,
                                                                @Param("date2") Date date2, @Param("region") String region);

    //@Query("SELECT t FROM Transaction t WHERE t.number = :number AND t.date BETWEEN :date1 AND :date2 AND t.ip != :ip")
    @Query(value = "SELECT * FROM transaction  WHERE ip != :ip AND number = :number AND date between :date1 and :date2",
            nativeQuery = true)
    Set<Transaction> getSimilarTransactionsWithDifIpFromLastHour(@Param("number") String number,
                                                                 @Param("date1") Date date1, @Param("date2") Date date2, @Param("ip") String ip);

    @Query(value = "SELECT CASE WHEN EXISTS ( SELECT * FROM transaction WHERE transactionId = transactionId AND feedback != '')" +
            " THEN 'TRUE' ELSE 'FALSE' END" ,nativeQuery = true)
    boolean existTransactionWithFeedbackByTransactionId(@Param("transactionId") Long transactionId);

    @Query(value = "SELECT t FROM Transaction t WHERE t.transactionId = :id AND t.feedback IS NOT NULL")
    Optional<Transaction> selectTransactionByIDWithNotNullFeedback(@Param("id")Long id);

    @Query("SELECT t FROM Transaction t WHERE t.number = :cardNumber")
    List<Transaction> findByNumber(String cardNumber);

}