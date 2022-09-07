package antifraud.app.repo;

import antifraud.app.model.Stolencard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Roman Pashkov created on 06.09.2022 inside the package - antifraud.app.repo
 */
@Repository
public interface StolencardRepository extends CrudRepository<Stolencard, Long> {

    boolean existsStolencardByNumber(String number);

    Stolencard findByNumber(String number);


}
