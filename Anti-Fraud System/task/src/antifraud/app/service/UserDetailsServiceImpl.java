package antifraud.app.service;

import antifraud.app.model.User;
import antifraud.app.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Roman Pashkov created on 26.08.2022 inside the package - antifraud.app.service
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Not founs: "+ username);
        }

        return new UserDetailsImpl(user);
    }
}
