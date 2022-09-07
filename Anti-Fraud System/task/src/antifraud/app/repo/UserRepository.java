package antifraud.app.repo;

import antifraud.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Roman Pashkov created on 26.08.2022 inside the package - antifraud.app.repo
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    <S extends User> S save(S entity);

    public User findUserByUsername(String username);

    public boolean existsUserByUsername(String username);

    @Override
    Iterable<User> findAll();
}
