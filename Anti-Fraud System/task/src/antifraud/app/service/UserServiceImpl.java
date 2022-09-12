package antifraud.app.service;

import antifraud.app.DTO.UserEnableDTO;
import antifraud.app.DTO.UserUpdateRoleDTO;
import antifraud.app.exception.DuplicationEntityException;
import antifraud.app.model.ENABLE_OPERATION;
import antifraud.app.model.Role;
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
        Iterable<User> all = userRepository.findAll();
        if (StreamSupport.stream(all.spliterator(), false).count() == 0) {
            user.setRole(Role.ADMINISTRATOR);
            user.setEnabled(true);
        } else {
            user.setRole(Role.MERCHANT);
            user.setEnabled(false);
        }
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

    @Override
    public void updateUserRole(UserUpdateRoleDTO userUpdateRoleDTO) throws IllegalAccessException {
        User userByUsername = userRepository.findUserByUsername(userUpdateRoleDTO.getUsername());
        if (!userRepository.existsUserByUsername(userUpdateRoleDTO.getUsername())) {
            throw new EntityNotFoundException();
        }
        if (!userUpdateRoleDTO.getRole().equals(Role.MERCHANT) && !userUpdateRoleDTO.getRole().equals(Role.SUPPORT)) {
            throw new IllegalArgumentException();
        }
        if (userUpdateRoleDTO.getRole().equals(userByUsername.getRole())) {
            throw new DuplicationEntityException("This role already assigned to this user");
        }
        userByUsername.setRole(userUpdateRoleDTO.getRole());
        userRepository.save(userByUsername);
    }

    @Override
    public String enableDisableUser(UserEnableDTO userEnableDTO) {
        User userByUsername = userRepository.findUserByUsername(userEnableDTO.getUsername());
        if (!userRepository.existsUserByUsername(userEnableDTO.getUsername())) {
            throw new EntityNotFoundException();
        }
        if (userByUsername.getRole().equals(Role.ADMINISTRATOR)) {
            throw new IllegalArgumentException();
        }
        switch (userEnableDTO.getOperation()){
            case LOCK:
                userByUsername.setEnabled(false);
                break;
            case UNLOCK:
                userByUsername.setEnabled(true);
                break;
        }
        userRepository.save(userByUsername);
        return userEnableDTO.getOperation() == ENABLE_OPERATION.LOCK ? String.format("User %s locked!", userByUsername.getUsername()) : String.format("User %s unlocked!", userByUsername.getUsername());
    }
}