package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sogon.booksys.domain.Role;
import sogon.booksys.domain.User;
import sogon.booksys.dto.SessionUser;
import sogon.booksys.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @GetMapping("/admin")
    public String adminForm(Model model){
        List<User> all = userRepository.findAll();
        for (User user : all) {
            if(user.getRole() == Role.ADMIN)
                return "/admin/adminExist";
        }

        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        User update = userRepository.findByEmail(user.getEmail()).get().update(Role.ADMIN);
        userRepository.save(update);

        model.addAttribute("userEmail", update.getEmail());

        return "/admin/adminSuccess";
    }
}
