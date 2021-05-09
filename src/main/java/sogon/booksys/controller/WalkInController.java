package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import sogon.booksys.service.WalkInService;

@Controller
@RequiredArgsConstructor
public class WalkInController {

    private final WalkInService walkInService;
}
