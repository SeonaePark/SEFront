package sogon.booksys.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public class ReservationDto {

    private Long id;

    @Min(value = 1, message = "최소 1개 이상 입력해주세요.")
    private int covers; //예약인원

    @FutureOrPresent(message = "현재 시간 이후에 예약해야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;  //예약한 날짜 및 시간

    @FutureOrPresent(message = "현재 시간 이후에 예약해야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime closeTime;  //예약이 끝나는 날짜 및 시간

    @Min(value = 30, message = "최소 30분 이상 입력해주세요.")
    private int term;

    @NotNull(message = "테이블을 선택해주세요.")
    private Long tableId;

    //비즈니스로직
    public void setCloseTime(int term){
        this.term = term;
        closeTime = startTime.plusMinutes(term);
    }
    public void setCloseTime(LocalDateTime closeTime){
        this.closeTime = closeTime;
    }
}
