package sogon.booksys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogon.booksys.domain.Table;

public interface TableRepository extends JpaRepository<Table, Long> {
}
