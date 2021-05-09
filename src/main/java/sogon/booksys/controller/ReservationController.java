package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sogon.booksys.domain.Reservation;
import sogon.booksys.service.ReservationService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservations")
    public String reservationListForm(Model model){
        List<Reservation> reservations = reservationService.findAll();

        model.addAttribute("reservations", reservations);

        return "/reservation/reservationList";
    }
}
