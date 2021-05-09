package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import sogon.booksys.repository.TableRepository;

@Controller
@RequiredArgsConstructor
public class TableController {

    private final TableRepository tableRepository;
}
