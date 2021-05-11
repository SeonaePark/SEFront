package sogon.booksys.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;
    private String email;
    private String picture;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String phoneNumber;    //예약자 휴대폰 번호

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations = new ArrayList<>();

    @Builder
    public User(String name, String email, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String picture){
        this.picture = picture;

        return this;
    }

    public User update(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;

        return this;
    }

    public User update(Role role){
        this.role = role;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

}
