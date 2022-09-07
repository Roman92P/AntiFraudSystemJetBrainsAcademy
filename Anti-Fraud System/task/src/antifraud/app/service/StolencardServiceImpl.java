package antifraud.app.service;

import antifraud.app.exception.DuplicationEntityException;
import antifraud.app.model.Stolencard;
import antifraud.app.repo.StolencardRepository;
import antifraud.app.util.CardValidator;
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
public class StolencardServiceImpl implements StolencardService{

    @Autowired
    StolencardRepository stolencardRepository;
    @Override
    public Stolencard addNewStolencard(Stolencard stolencard) {
        if (stolencardRepository.existsStolencardByNumber(stolencard.getNumber())) {
            throw new DuplicationEntityException("This card already exists");
        }
        if (!stolencard.getNumber().matches("\\d{16}") || !CardValidator.providedCardNumberIsValid(Optional.ofNullable(stolencard.getNumber()))) {
            throw  new IllegalArgumentException();
        }
        Stolencard save = stolencardRepository.save(stolencard);
        return save;
    }

    @Override
    public boolean stolencardExist(String number) {
        return stolencardRepository.existsStolencardByNumber(number);
    }

    @Override
    public void removeStolenCard(String number) {
        if (!number.matches("\\d{16}") || !CardValidator.providedCardNumberIsValid(Optional.ofNullable(number))) {
            throw  new IllegalArgumentException();
        }
        if (!stolencardRepository.existsStolencardByNumber(number)) {
            throw  new EntityNotFoundException();
        }
        stolencardRepository.delete(stolencardRepository.findByNumber(number));
    }

    @Override
    public List<Stolencard> getStolenCards() {
        Iterable<Stolencard> all = stolencardRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
    }
}
