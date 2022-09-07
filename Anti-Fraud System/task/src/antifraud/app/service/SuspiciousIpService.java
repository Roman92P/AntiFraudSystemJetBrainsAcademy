package antifraud.app.service;

import antifraud.app.model.SuspiciousIp;

import java.util.List;
import java.util.Optional;

/**
 * Roman Pashkov created on 06.09.2022 inside the package - antifraud.app.service
 */
public interface SuspiciousIpService {

    SuspiciousIp addNewSuspiciousIp (SuspiciousIp suspiciousIp);
    
    Optional<SuspiciousIp> findSuspiciousIpByIp(String ip);

    void deleteSuspiciousIp(SuspiciousIp suspiciousIp);

    List<SuspiciousIp> getAllIps();
}
