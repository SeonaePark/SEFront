package sogon.booksys.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@javax.persistence.Table(name = "tables")
@Getter
@NoArgsConstructor
public class Table {

    @Id
    @GeneratedValue
    @Column(name = "table_id")
    private Long id;

    @Column(unique = true)
    private int number; //테이블 번호, 즉 위치
    private int seats; //테이블에 앉을수있는 사람 수, 즉 좌석 수

    @OneToMany(mappedBy = "table")
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "table")
    private List<WalkIn> walkIns = new ArrayList<>();

    @Builder
    public Table(int number, int seats) {
        this.number = number;
        this.seats = seats;
    }

    //비즈니스 로직
    //테이블에 앉을수 있는 사람 수 보다 예약한 인원수가 많은지를 체크
    public boolean canReserve(int userCount){
        return this.seats>=userCount;
    }
}
