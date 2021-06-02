package sogon.booksys.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogon.booksys.domain.User;
import sogon.booksys.domain.Reservation;
import sogon.booksys.domain.Table;
import sogon.booksys.exception.DuplicateReserveException;
import sogon.booksys.exception.SeatExcessException;
import sogon.booksys.repository.UserRepository;
import sogon.booksys.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final TableService tableService;

    //예약 (시작 시간과 몇 분 예약할지를 인자로 받음)
    @Transactional
    public Long reserve(Long userId, Long tableId, LocalDateTime time, int term, int userCount){
        User user = userRepository.findById(userId).get();

        Table table = judgeAndAssignTable(tableId, time, term, userCount);

        Reservation reservation = Reservation.createReservation(user, table, time, term, userCount);
        reservationRepository.save(reservation);
        return reservation.getId();
    }

    //예약 (시작 시간과 끝나는 시간을 인자로 받음)
    @Transactional
    public Long reserve(Long userId, Long tableId, LocalDateTime startTime, LocalDateTime closeTime, int userCount){
        User user = userRepository.findById(userId).get();
        int term = (int) ChronoUnit.MINUTES.between(startTime, closeTime);

        Table table = judgeAndAssignTable(tableId, startTime, term, userCount);

        Reservation reservation = Reservation.createReservation(user, table, startTime, term, userCount);
        reservationRepository.save(reservation);
        return reservation.getId();
    }

    //예약 테이블 변경
    @Transactional
    public Long moveTable(Long reservationId, Long newTableId){
        Reservation reservation = reservationRepository.findById(reservationId).get();
        LocalDateTime startTime = reservation.getStartTime();
        LocalDateTime closeTime = reservation.getCloseTime();
        int term = (int)ChronoUnit.MINUTES.between(startTime, closeTime);

        Table newTable = tableService.findById(newTableId).get();
        List<Reservation> allTable = reservationRepository.findAllByTable(newTable);

        judgeTableCount(reservation.getCovers(), newTable);
        judgeDuplicateTime(startTime, term, allTable);

        reservation.setTable(newTable);
        Reservation savedReservation = reservationRepository.save(reservation);

        return savedReservation.getId();
    }

    //예약 변경 (시작 시간과 몇 분 간격인지를 인자로 받음)
    @Transactional
    public Long updateReservation(Long reservationId, LocalDateTime time, int term, int userCount){
        Reservation reservation = reservationRepository.findById(reservationId).get();
        Table table = reservation.getTable();

        List<Reservation> allTable = reservationRepository.findAllByTable(table);

        judgeTableCount(userCount, table);
        judgeDuplicateTime(time, term, allTable);

        reservation.setStartTime(time);
        reservation.setCloseTime(time.plusMinutes(term));
        reservation.setCovers(userCount);
        Reservation save = reservationRepository.save(reservation);

        return save.getId();
    }

    //예약 변경 (시작 시간과 끝나는 시간을 인자로 받음)
    @Transactional
    public Long updateReservation(Long reservationId, LocalDateTime startTime, LocalDateTime closeTime, int userCount){
        Reservation reservation = reservationRepository.findById(reservationId).get();
        Table table = reservation.getTable();
        int term = (int) ChronoUnit.MINUTES.between(startTime, closeTime);

        List<Reservation> allTable = reservationRepository.findAllByTable(table);

        judgeTableCount(userCount, table);
        judgeDuplicateTime(startTime, term, allTable);

        reservation.setStartTime(startTime);
        reservation.setCloseTime(closeTime);
        reservation.setCovers(userCount);
        Reservation save = reservationRepository.save(reservation);

        return save.getId();
    }

    @Transactional
    public void cancelReservation(Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId).get();
        reservationRepository.delete(reservation);
        reservation.getUser().deleteReservation(reservation);
    }

    public int countTableBetweenDate(Table table, LocalDateTime start, LocalDateTime end){
        return reservationRepository.countAllByTableAndStartTimeBetween(table, start, end);
    }

    public List<Reservation> findAllByTable(Table table){
        return reservationRepository.findAllByTable(table);
    }

    public Optional<Reservation> findById(Long reservationId){
        return reservationRepository.findById(reservationId);
    }

    public List<Reservation> findAll(){
        return reservationRepository.findAll();
    }

    //테이블 좌석수 확인
    private void judgeTableCount(int userCount, Table table) {
        if(!table.canReserve(userCount)) {
            throw new SeatExcessException("테이블의 좌석 수가 예약하려는 인원수보다 적습니다.");
        }
    }

    //예약 시간이 겹치는지 확인
    private void judgeDuplicateTime(LocalDateTime time, int term, List<Reservation> allTable) {
        for (Reservation reservation : allTable) {
            LocalDateTime arrivalTime = reservation.getStartTime();
            LocalDateTime closeTime = reservation.getCloseTime();

            if(time.isAfter(arrivalTime) && time.isBefore(closeTime) || time.isEqual(arrivalTime) || time.isEqual(closeTime)){
                throw new DuplicateReserveException(arrivalTime.getHour() + "시 " + arrivalTime.getMinute() + "분부터 "
                        + closeTime.getHour() + "시 " + closeTime.getMinute() + "분 까지는 " + "이미 예약된 시간입니다.");
            } else if(time.plusMinutes(term).isAfter(arrivalTime) && time.plusMinutes(term).isBefore(closeTime)){
                throw new DuplicateReserveException(arrivalTime.getHour() + "시 " + arrivalTime.getMinute() + "분부터 "
                        + closeTime.getHour() + "시 " + closeTime.getMinute() + "분 까지는 " + "이미 예약된 시간입니다.");
            } else if(time.isBefore(arrivalTime) && time.plusMinutes(term).isAfter(closeTime)){
                throw new DuplicateReserveException(arrivalTime.getHour() + "시 " + arrivalTime.getMinute() + "분부터 "
                        + closeTime.getHour() + "시 " + closeTime.getMinute() + "분 까지는 " + "이미 예약된 시간입니다.");
            }
        }
    }

    //테이블이 null일 때 자동으로 배정해주는 메소드
    private Table judgeAndAssignTable(Long tableId, LocalDateTime time, int term, int userCount){
        Table table = new Table();
        RuntimeException lastException = new RuntimeException();

        if(tableId != null){
            table = tableService.findById(tableId).get();
            List<Reservation> allTable = reservationRepository.findAllByTable(table);

            judgeDuplicateTime(time, term, allTable);
            judgeTableCount(userCount, table);
        } else {
            List<Table> all = tableService.findAllOrderByNumber();
            for (Table findTable : all) {
                List<Reservation> allReservation = reservationRepository.findAllByTable(findTable);
                try {
                    judgeDuplicateTime(time, term, allReservation);
                    judgeTableCount(userCount, findTable);
                    tableId = findTable.getId();
                    table = findTable;
                    break;
                } catch (RuntimeException e){
                    lastException = e;
                }
            }
            if(tableId == null){
                throw lastException;
            }
        }

        return table;
    }

}
