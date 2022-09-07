package antifraud.app.service;

import antifraud.app.exception.DuplicationEntityException;
import antifraud.app.model.SuspiciousIp;
import antifraud.app.repo.SuspiciousIpRepository;
import antifraud.app.util.SimpleIpValidator;
import antlr.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Roman Pashkov created on 06.09.2022 inside the package - antifraud.app.service
 */
@Service
public class SuspiciousIpServiceImpl implements SuspiciousIpService{

    @Autowired
    SuspiciousIpRepository suspiciousIpRepository;

    @Override
    public SuspiciousIp addNewSuspiciousIp(SuspiciousIp suspiciousIp) {
        if (suspiciousIpRepository.existsSuspiciousIpByIp(suspiciousIp.getIp())) {
            throw  new DuplicationEntityException("This ip already exist");
        }
        if (!SimpleIpValidator.providedCorrectIp(Optional.ofNullable(suspiciousIp.getIp()))) {
            throw new IllegalArgumentException();
        }
        suspiciousIpRepository.save(suspiciousIp);
        return suspiciousIpRepository.findByIp(suspiciousIp.getIp()).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Optional<SuspiciousIp> findSuspiciousIpByIp(String ip) {
        return suspiciousIpRepository.findByIp(ip);
    }

    @Override
    public void deleteSuspiciousIp(SuspiciousIp suspiciousIp) {
        suspiciousIpRepository.delete(suspiciousIp);
    }

    @Override
    public List<SuspiciousIp> getAllIps() {
        Iterable<SuspiciousIp> all = suspiciousIpRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
    }
}
