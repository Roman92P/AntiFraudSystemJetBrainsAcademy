package antifraud.app.service;

import antifraud.app.model.Stolencard;

import java.util.List;

/**
 * Roman Pashkov created on 06.09.2022 inside the package - antifraud.app.service
 */
public interface StolencardService {
    Stolencard addNewStolencard(Stolencard stolencard);

    boolean stolencardExist(String number);

    void removeStolenCard(String number);

    List<Stolencard> getStolenCards();
}