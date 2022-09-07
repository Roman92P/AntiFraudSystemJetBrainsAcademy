package antifraud.app.repo;

import antifraud.app.model.SuspiciousIp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Roman Pashkov created on 06.09.2022 inside the package - antifraud.app.repo
 */
@Repository
public interface SuspiciousIpRepository extends CrudRepository<SuspiciousIp, Long> {

    Optional<SuspiciousIp> findByIp(String ip);

    public boolean existsSuspiciousIpByIp(String ip);
}
