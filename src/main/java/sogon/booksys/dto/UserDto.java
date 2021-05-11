package sogon.booksys.dto;

import lombok.Getter;
import lombok.Setter;
import sogon.booksys.domain.Role;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class UserDto {

    private Long id;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    private String email;
    private String picture;
    private Role role;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    private String phoneNumber;
}
