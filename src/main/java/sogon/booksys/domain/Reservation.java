package sogon.booksys.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    private Long id;

    private int covers; //예약인원
    private LocalDateTime startTime;  //예약한 날짜 및 시간
    private LocalDateTime closeTime;  //예약이 끝나는 날짜 및 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;          //다대일 매핑

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private Table table;                //다대일 매핑

    //연간관계 메서드
    public void setUser(User user){
        this.user = user;
        user.getReservations().add(this);
    }

    public void setTable(Table table){
        this.table = table;
        table.getReservations().add(this);
    }

    //생성 메서드
    public static Reservation createReservation(User user, Table table,
                                                LocalDateTime time, int term, int userCount){
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setTable(table);
        reservation.setStartTime(time);
        reservation.setCloseTime(time.plusMinutes(term));
        reservation.setCovers(userCount);

        return reservation;
    }

    //비즈니스 메서드
    public int getTerm(){
        return (int) ChronoUnit.MINUTES.between(startTime, closeTime);
    }
}
