package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sogon.booksys.domain.Role;
import sogon.booksys.domain.User;
import sogon.booksys.dto.SessionUser;
import sogon.booksys.repository.UserRepository;
import sogon.booksys.service.CustomOAuth2UserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final CustomOAuth2UserService authService;
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @GetMapping("/admin")
    public String adminForm(){
        List<User> all = userRepository.findAll();
        for (User user : all) {
            log.debug("for UserId = {}", user.getId());
            log.debug("for Role = {}", user.getRole());
            if(user.getRole() == Role.ADMIN) {
                return "/admin/adminExist";
            }
        }
        log.debug("for end");
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        User user = userRepository.findByEmail(sessionUser.getEmail()).get();

        authService.updateAdmin(user);

        return "/admin/adminSuccess";
    }
}
