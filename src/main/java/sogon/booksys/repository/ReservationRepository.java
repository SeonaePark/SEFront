package sogon.booksys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogon.booksys.domain.Reservation;
import sogon.booksys.domain.Table;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByTable (Table table);
    int countAllByTableAndStartTimeBetween(Table table, LocalDateTime start, LocalDateTime end);
}
