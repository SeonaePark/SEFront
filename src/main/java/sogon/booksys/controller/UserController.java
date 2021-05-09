package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import sogon.booksys.repository.UserRepository;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
}
