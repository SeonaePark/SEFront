package sogon.booksys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogon.booksys.domain.Table;
import sogon.booksys.domain.WalkIn;

import java.util.List;

public interface WalkInRepository extends JpaRepository<WalkIn, Long> {
    List<WalkIn> findAllByTable (Table table);
}
