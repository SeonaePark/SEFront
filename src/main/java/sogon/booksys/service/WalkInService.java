package sogon.booksys.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogon.booksys.domain.Table;
import sogon.booksys.domain.WalkIn;
import sogon.booksys.exception.DuplicateReserveException;
import sogon.booksys.exception.SeatExcessException;
import sogon.booksys.repository.TableRepository;
import sogon.booksys.repository.WalkInRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalkInService {

    private final WalkInRepository walkInRepository;
    private final TableRepository tableRepository;

    @Transactional
    public Long walkIn(Long tableId, LocalDateTime time, int term, int userCount){
        Table table = tableRepository.findById(tableId).get();

        List<WalkIn> allTable = walkInRepository.findAllByTable(table);

        judgeTableCount(userCount, table);
        judgeDuplicateTime(time, term, allTable);

        WalkIn walkIn = WalkIn.createWalkIn(table, time, term, userCount);
        walkInRepository.save(walkIn);
        return walkIn.getId();

    }

    @Transactional
    public Long moveTable(Long walkInId, Long newTableId){
        WalkIn walkIn = walkInRepository.findById(walkInId).get();
        LocalDateTime startTime = walkIn.getStartTime();
        LocalDateTime closeTime = walkIn.getCloseTime();
        int term = (int) ChronoUnit.MINUTES.between(startTime, closeTime);

        Table newTable = tableRepository.findById(newTableId).get();
        List<WalkIn> allTable = walkInRepository.findAllByTable(newTable);

        judgeTableCount(walkIn.getCovers(), newTable);
        judgeDuplicateTime(startTime, term, allTable);

        walkIn.setTable(newTable);
        WalkIn save = walkInRepository.save(walkIn);

        return save.getId();
    }

    @Transactional
    public Long updateWalkIn(Long WalkInId, LocalDateTime time, int term, int userCount){
        WalkIn walkIn = walkInRepository.findById(WalkInId).get();
        Table table = walkIn.getTable();

        List<WalkIn> allTable = walkInRepository.findAllByTable(table);

        judgeTableCount(userCount, table);
        judgeDuplicateTime(time, term, allTable);

        walkIn.setStartTime(time);
        walkIn.setCloseTime(time.plusMinutes(term));
        walkIn.setCovers(userCount);
        WalkIn save = walkInRepository.save(walkIn);

        return save.getId();
    }

    @Transactional
    public void cancelWalkIn(Long walkInId){
        Optional<WalkIn> walkIn = walkInRepository.findById(walkInId);
        walkInRepository.delete(walkIn.get());
    }

    public List<WalkIn> findAllByTable(Table table){
        return walkInRepository.findAllByTable(table);
    }

    public Optional<WalkIn> findById(Long walkInId){
        return walkInRepository.findById(walkInId);
    }

    public List<WalkIn> findAll(){
        return walkInRepository.findAll();
    }

    //테이블 좌석수 확인
    private void judgeTableCount(int userCount, Table table) {
        if(!table.canReserve(userCount)) {
            throw new SeatExcessException("테이블의 좌석 수가 예약하려는 인원수보다 적습니다.");
        }
    }

    //예약 시간이 겹치는지 확인
    private void judgeDuplicateTime(LocalDateTime time, int term, List<WalkIn> allTable) {
        for (WalkIn walkIn : allTable) {
            LocalDateTime arrivalTime = walkIn.getStartTime();
            LocalDateTime closeTime = walkIn.getCloseTime();

            if(time.isAfter(arrivalTime) && time.isBefore(closeTime)){
                throw new DuplicateReserveException("이미 예약된 시간입니다.");
            } else if(time.plusMinutes(term).isAfter(arrivalTime) && time.plusMinutes(term).isBefore(closeTime)){
                throw new DuplicateReserveException("이미 예약된 시간입니다.");
            } else if(time.isBefore(arrivalTime) && time.plusMinutes(term).isAfter(closeTime)){
                throw new DuplicateReserveException("이미 예약된 시간을 포함하고 있습니다.");
            }
        }
    }
}
