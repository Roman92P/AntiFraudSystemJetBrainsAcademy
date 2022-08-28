package antifraud.app.service;

import antifraud.app.exception.DuplicationEntityException;
import antifraud.app.model.User;
import antifraud.app.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Roman Pashkov created on 26.08.2022 inside the package - antifraud.app.service
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @Override
    public void registerNewUser(User user) {
        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new DuplicationEntityException("There is such user already");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        Iterable<User> all = userRepository.findAll();
        List<User> allUsers = StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
        return allUsers.stream().sorted().collect(Collectors.toList());
    }

    @Override
    public void removeUser(String username) {
        if (userRepository.findUserByUsername(username) == null) {
            throw new EntityNotFoundException();
        }
        userRepository.delete(userRepository.findUserByUsername(username));
    }

    @Override
    public User getUserByUsername(String username) {
        if (!userRepository.existsUserByUsername(username)) {
            throw new EntityNotFoundException();
        }
        return userRepository.findUserByUsername(username);
    }
}
