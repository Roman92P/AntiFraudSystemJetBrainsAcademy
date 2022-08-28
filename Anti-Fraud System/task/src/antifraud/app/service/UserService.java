package antifraud.app.service;

import antifraud.app.model.User;

import java.util.List;

/**
 * Roman Pashkov created on 26.08.2022 inside the package - antifraud.app.service
 */
public interface UserService {
    void registerNewUser(User user);
    List<User> getAllUsers();

    void removeUser(String username);

    User getUserByUsername(String username);
}
