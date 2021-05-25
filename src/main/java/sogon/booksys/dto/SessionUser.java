package sogon.booksys.dto;

import lombok.Getter;
import sogon.booksys.domain.Role;
import sogon.booksys.domain.User;

@Getter
public class SessionUser {

    private String name;
    private String email;
    private String picture;
    private Role role;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.role = user.getRole();
    }
}
