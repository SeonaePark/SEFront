package sogon.booksys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogon.booksys.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
