package sogon.booksys.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sogon.booksys.domain.User;
import sogon.booksys.domain.Reservation;
import sogon.booksys.domain.Table;
import sogon.booksys.exception.DuplicateReserveException;
import sogon.booksys.exception.SeatExcessException;
import sogon.booksys.repository.UserRepository;
import sogon.booksys.repository.TableRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TableRepository tableRepository;
    @Autowired
    ReservationService reservationService;

    @Test
    void 예약_테스트(){
        //given
        User user = new User();
        Table table = Table.builder().number(1).seats(4).build();
        LocalDateTime time = LocalDateTime.now();
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table);
        Long reserveId = reservationService.reserve(user.getId(), table.getId(), time, term, 4);

        //then
        Reservation savedReservation = reservationService.findById(reserveId).get();
        assertEquals(savedReservation.getId(), reserveId);
    }

    @Test
    void 같은시간_예약(){
        //given
        User user = new User();
        Table table = Table.builder().number(1).seats(4).build();
        LocalDateTime time1 = LocalDateTime.of(2021,1,30,12,30);
        LocalDateTime time2 = LocalDateTime.of(2021,1,30,12,40);
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table);
        reservationService.reserve(user.getId(), table.getId(), time1, term, 4);

        //then
        assertThrows(DuplicateReserveException.class,
                ()->{reservationService.reserve(user.getId(), table.getId(), time2, term, 4);});
    }

    @Test
    void 인원초과_예약() {
        //given
        User user = new User();
        Table table = Table.builder().number(1).seats(4).build();
        LocalDateTime time = LocalDateTime.now();
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table);

        //then
        assertThrows(SeatExcessException.class,
                ()->{reservationService.reserve(user.getId(), table.getId(), time, term, 5);});
    }

    @Test
    void 예약취소_테스트(){
        //given
        User user = new User();
        Table table = Table.builder().number(1).seats(4).build();
        LocalDateTime time = LocalDateTime.now();
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table);
        Long reserveId = reservationService.reserve(user.getId(), table.getId(), time, term, 4);
        Reservation reservation = reservationService.findById(reserveId).get();

        reservationService.cancelReservation(reserveId);

        List<Reservation> all = reservationService.findAll();
        //then
        assertFalse(all.contains(reservation));
    }

    @Test
    void findByTable_테스트(){
        //given
        User user = new User();
        Table table = Table.builder().number(1).seats(4).build();
        LocalDateTime time = LocalDateTime.now();
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table);
        Long reserveId = reservationService.reserve(user.getId(), table.getId(), time, term, 4);
        Reservation reservation = reservationService.findById(reserveId).get();
        List<Reservation> byTable = reservationService.findAllByTable(table);

        //then
        assertTrue(byTable.contains(reservation));
    }

    @Test
    void 예약테이블_변경_테스트(){
        //given
        User user = new User();
        Table table1 = Table.builder().number(1).seats(4).build();
        Table table2 = Table.builder().number(2).seats(5).build();
        LocalDateTime time = LocalDateTime.now();
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table1);
        tableRepository.save(table2);
        Long reserveId = reservationService.reserve(user.getId(), table1.getId(), time, term, 4);
        Long updateId = reservationService.moveTable(reserveId, table2.getId());

        //then
        Reservation preReservation = reservationService.findById(reserveId).get();
        Reservation updateReservation = reservationService.findById(updateId).get();
        assertEquals(preReservation.getId(), updateReservation.getId());
        assertEquals(updateReservation.getTable().getSeats(), 5);
    }

    @Test
    void 예약테이블_변경_인원초과_테스트(){
        //given
        User user = new User();
        Table table1 = Table.builder().number(1).seats(5).build();
        Table table2 = Table.builder().number(2).seats(4).build();
        LocalDateTime time = LocalDateTime.now();
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table1);
        tableRepository.save(table2);
        Long reserveId = reservationService.reserve(user.getId(), table1.getId(), time, term, 5);

        //then
        assertThrows(SeatExcessException.class, ()->{reservationService.moveTable(reserveId, table2.getId());});
    }

    @Test
    void 예약테이블_변경_같은시간_테스트(){
        User user = new User();
        Table table1 = Table.builder().number(1).seats(4).build();
        Table table2 = Table.builder().number(2).seats(5).build();
        LocalDateTime time1 = LocalDateTime.of(2021,1,30,12,30);
        LocalDateTime time2 = LocalDateTime.of(2021,1,30,12,40);
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table1);
        tableRepository.save(table2);
        Long reserveId = reservationService.reserve(user.getId(), table1.getId(), time1, term, 4);
        reservationService.reserve(user.getId(), table2.getId(), time2, term, 4);

        //then
        assertThrows(DuplicateReserveException.class, ()->{reservationService.moveTable(reserveId, table2.getId());});
    }

    @Test
    void 예약_변경_테스트(){
        //given
        User user = new User();
        Table table = Table.builder().number(1).seats(4).build();
        LocalDateTime time1 = LocalDateTime.of(2021,1,30,12,30);
        LocalDateTime time2 = LocalDateTime.of(2021,2,1,12,30);
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table);
        Long reserveId = reservationService.reserve(user.getId(), table.getId(), time1, term, 4);
        Long updateId = reservationService.updateReservation(reserveId, time2, term, 3);

        //then
        Reservation reservation = reservationService.findById(updateId).get();
        assertEquals(reservation.getStartTime(), time2);
    }

    @Test
    void 예약_변경_같은시간_테스트(){
        //given
        User user = new User();
        Table table = Table.builder().number(1).seats(4).build();
        LocalDateTime time1 = LocalDateTime.of(2021,1,30,12,30);
        LocalDateTime time2 = LocalDateTime.of(2021,1,30,12,40);
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table);
        Long reserveId = reservationService.reserve(user.getId(), table.getId(), time1, term, 4);

        //then
        assertThrows(DuplicateReserveException.class, ()->{reservationService.updateReservation(reserveId, time2, term, 3);});
    }

    @Test
    void 예약_변경_인원초과_테스트(){
        //given
        User user = new User();
        Table table = Table.builder().number(1).seats(4).build();
        LocalDateTime time1 = LocalDateTime.of(2021,1,30,12,30);
        LocalDateTime time2 = LocalDateTime.of(2021,2,1,12,40);
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table);
        Long reserveId = reservationService.reserve(user.getId(), table.getId(), time1, term, 4);

        //then
        assertThrows(SeatExcessException.class, ()->{reservationService.updateReservation(reserveId, time2, term, 5);});
    }

    @Test
    void 예약통계테스트(){
        //given
        User user = new User();
        Table table = Table.builder().number(1).seats(4).build();
        LocalDateTime time = LocalDateTime.now();
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table);
        reservationService.reserve(user.getId(), table.getId(), time, term, 4);
        reservationService.reserve(user.getId(), table.getId(), time.plusMinutes(60), term, 4);

        //then
        int count = reservationService.countTableBetweenDate(table, time.minusDays(1), time.plusDays(1));
        assertEquals(count, 2);
    }

    @Test
    void 테이블_자동배정_테스트(){
        //given
        User user = new User();
        Table table1 = Table.builder().number(1).seats(4).build();
        Table table2 = Table.builder().number(2).seats(5).build();
        LocalDateTime time = LocalDateTime.now();
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table1);
        tableRepository.save(table2);
        reservationService.reserve(user.getId(), table1.getId(), time, term, 4);

        Long reserveId = reservationService.reserve(user.getId(), null, time, term, 4);

        //then
        Reservation reservation = reservationService.findById(reserveId).get();
        assertEquals(table2, reservation.getTable());
    }

    @Test
    void 테이블_자동배정_빈테이블이_없을때(){
        //given
        User user = new User();
        Table table1 = Table.builder().number(1).seats(4).build();
        Table table2 = Table.builder().number(2).seats(5).build();
        LocalDateTime time = LocalDateTime.now();
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table1);
        tableRepository.save(table2);
        reservationService.reserve(user.getId(), table1.getId(), time, term, 4);
        reservationService.reserve(user.getId(), table2.getId(), time, term, 4);

        //then
        assertThrows(DuplicateReserveException.class, ()->reservationService.reserve(user.getId(), null, time, term, 4));
    }

    @Test
    void 테이블_자동배정_인원초과일때(){
        //given
        User user = new User();
        Table table1 = Table.builder().number(1).seats(4).build();
        Table table2 = Table.builder().number(2).seats(5).build();
        LocalDateTime time = LocalDateTime.now();
        int term = 30;

        //when
        userRepository.save(user);
        tableRepository.save(table1);
        tableRepository.save(table2);
        reservationService.reserve(user.getId(), table1.getId(), time, term, 4);

        //then
        assertThrows(SeatExcessException.class, () -> reservationService.reserve(user.getId(), null, time, term, 50));
    }

}