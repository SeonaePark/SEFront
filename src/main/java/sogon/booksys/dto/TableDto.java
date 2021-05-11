package sogon.booksys.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class TableDto {

    @Min(value = 0, message = "최소 0 이상 입력해주세요.")
    private int number;

    @Min(value = 1, message = "최소 1개 이상 입력해주세요.")
    private int seats;
}
