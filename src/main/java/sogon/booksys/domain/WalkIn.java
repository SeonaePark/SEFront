package sogon.booksys.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalkIn {

    @Id
    @GeneratedValue
    @Column(name = "walkin_id")
    private Long id;

    private int covers; //예약인원
    private LocalDateTime startTime;  //예약한 날짜 및 시간
    private LocalDateTime closeTime;    //예약이 끝나는 날짜 및 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private Table table;

    //연관관계 메서드
    public void setTable(Table table){
        this.table = table;
        table.getWalkIns().add(this);
    }

    //생성 메서드
    public static WalkIn createWalkIn(Table table, LocalDateTime time, int term, int userCount){
        WalkIn walkIn = new WalkIn();
        walkIn.setTable(table);
        walkIn.setStartTime(time);
        walkIn.setCloseTime(time.plusMinutes(term));
        walkIn.setCovers(userCount);

        return walkIn;
    }
}
