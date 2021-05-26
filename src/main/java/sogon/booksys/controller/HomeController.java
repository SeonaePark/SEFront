package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sogon.booksys.domain.Role;
import sogon.booksys.dto.SessionUser;
import sogon.booksys.repository.UserRepository;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
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
            log.info("Role = {}", role);
            if(role == Role.ADMIN)
                model.addAttribute("userRole", role);
        }
        return "home";
    }
    @GetMapping("/fragments/common")
    public String common(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user!=null){
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());

            Role role = userRepository.findByEmail(user.getEmail()).get().getRole();
            log.info("Role = {}", role);
            if(role == Role.ADMIN)
                model.addAttribute("userRole", role);
        }
        return "fragments/common";
    }

    @GetMapping("/membership/card")
    public String card(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user!=null) {
            model.addAttribute("userName", user.getName());

            model.addAttribute("userEmail", user.getEmail());

            Role role = userRepository.findByEmail(user.getEmail()).get().getRole();
            log.info("Role = {}", role);
            if(role == Role.ADMIN)
                model.addAttribute("userRole", role);
        }
        return "membership/card";
    }
    @GetMapping("/membership/event")
    public String event(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user!=null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());

            Role role = userRepository.findByEmail(user.getEmail()).get().getRole();
            log.info("Role = {}", role);
            if(role == Role.ADMIN)
                model.addAttribute("userRole", role);
        }
        return "membership/event";
    }
    @GetMapping("/membership/coupon")
    public String coupon(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user!=null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());

            Role role = userRepository.findByEmail(user.getEmail()).get().getRole();
            log.info("Role = {}", role);
            if(role == Role.ADMIN)
                model.addAttribute("userRole", role);
        }
        return "membership/coupon";
    }
    @GetMapping("/customercenter/center")
    public String center(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user!=null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());
            Role role = userRepository.findByEmail(user.getEmail()).get().getRole();
            log.info("Role = {}", role);
            if(role == Role.ADMIN)
                model.addAttribute("userRole", role);
        }
        return "customercenter/center";
    }
    @GetMapping("/customercenter/onetoone")
    public String onetoone(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user!=null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());
            Role role = userRepository.findByEmail(user.getEmail()).get().getRole();
            log.info("Role = {}", role);
            if(role == Role.ADMIN)
                model.addAttribute("userRole", role);
        }
        return "customercenter/onetoone";
    }
    @GetMapping("/restaurant/Hi")
    public String Hi(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user!=null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());
            Role role = userRepository.findByEmail(user.getEmail()).get().getRole();
            log.info("Role = {}", role);
            if(role == Role.ADMIN)
                model.addAttribute("userRole", role);
        }
        return "restaurant/Hi";
    }
    @GetMapping("/restaurant/introduce")
    public String introduce(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user!=null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());
            Role role = userRepository.findByEmail(user.getEmail()).get().getRole();
            log.info("Role = {}", role);
            if(role == Role.ADMIN)
                model.addAttribute("userRole", role);
        }
        return "restaurant/introduce";
    }
    @GetMapping("/restaurant/location")
    public String location(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user!=null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());
            Role role = userRepository.findByEmail(user.getEmail()).get().getRole();
            log.info("Role = {}", role);
            if(role == Role.ADMIN)
                model.addAttribute("userRole", role);
        }
        return "restaurant/location";
    }
    @GetMapping("/menu/lunch")
    public String lunch(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user!=null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());
            Role role = userRepository.findByEmail(user.getEmail()).get().getRole();
            log.info("Role = {}", role);
            if(role == Role.ADMIN)
                model.addAttribute("userRole", role);
        }
        return "menu/lunch";
    }
    @GetMapping("/menu/dinner")
    public String dinner(Model model){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user!=null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());
            Role role = userRepository.findByEmail(user.getEmail()).get().getRole();
            log.info("Role = {}", role);
            if(role == Role.ADMIN)
                model.addAttribute("userRole", role);
        }
        return "menu/dinner";
    }
}
