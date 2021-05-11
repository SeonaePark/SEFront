package sogon.booksys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogon.booksys.domain.Table;

import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<Table, Long> {
    Optional<Table> findByNumber (int number);
}
