package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sogon.booksys.domain.Role;
import sogon.booksys.domain.User;
import sogon.booksys.dto.SessionUser;
import sogon.booksys.dto.UserDto;
import sogon.booksys.repository.UserRepository;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @GetMapping("/users")
    public String userListForm(Model model){
        List<User> all = userRepository.findAll();

        model.addAttribute("users", all);

        return "/user/userList";
    }

    @GetMapping("/users/{userId}/edit")
    public String editForm(@PathVariable Long userId, Model model){
        User user = userRepository.findById(userId).get();
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setPicture(user.getPicture());
        userDto.setRole(user.getRole());
        if(user.getPhoneNumber() != null) {
            userDto.setPhoneNumber(user.getPhoneNumber());
        }

        model.addAttribute("user",userDto);
        return "/user/updateUserForm";
    }

    @PostMapping("/users/{userId}/edit")
    public String edit(@PathVariable Long userId, @Valid @ModelAttribute("user") UserDto userDto,
                       BindingResult result){
        if(result.hasErrors()){
            return "/user/updateUserForm";
        }

        User user = userRepository.findById(userId).get();
        user.update(userDto.getName(), userDto.getPhoneNumber());
        userRepository.save(user);

        return "redirect:/users";
    }

    @PostMapping("/users/{userId}/delete")
    public String delete(@PathVariable Long userId){
        userRepository.deleteById(userId);

        return "redirect:/users";
    }
}
