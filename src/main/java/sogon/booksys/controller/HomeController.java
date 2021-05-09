package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sogon.booksys.domain.Role;
import sogon.booksys.dto.SessionUser;
import sogon.booksys.repository.UserRepository;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String home(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user!=null){
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());

            Role role = userRepository.findByEmail(user.getEmail()).get().getRole();
            if(role == Role.ADMIN)
                model.addAttribute("userRole", role);
        }
        return "home";
    }
}
