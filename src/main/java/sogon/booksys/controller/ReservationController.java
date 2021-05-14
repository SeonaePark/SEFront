package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sogon.booksys.domain.Reservation;
import sogon.booksys.domain.Table;
import sogon.booksys.domain.User;
import sogon.booksys.dto.ReservationDto;
import sogon.booksys.dto.SessionUser;
import sogon.booksys.dto.TableDto;
import sogon.booksys.dto.UserDto;
import sogon.booksys.exception.DuplicateReserveException;
import sogon.booksys.exception.SeatExcessException;
import sogon.booksys.repository.UserRepository;
import sogon.booksys.service.ReservationService;
import sogon.booksys.service.TableService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;
    private final TableService tableService;
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @GetMapping("/reservations")
    public String reservationListForm(Model model){
        List<Reservation> reservations = reservationService.findAll();
        model.addAttribute("reservations", reservations);

        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        model.addAttribute("userEmail", sessionUser.getEmail());

        return "/reservation/reservationList";
    }

    @GetMapping("/reservations/new")
    public String reserveForm(Model model){
        List<Table> allTables = tableService.findAllOrderByNumber();
        model.addAttribute("tables", allTables);

        ReservationDto reservationDto = new ReservationDto();
        model.addAttribute("reservation", reservationDto);

        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        String email = sessionUser.getEmail();
        User user = userRepository.findByEmail(email).get();
        model.addAttribute("user",user);

        return "/reservation/createReservationForm";
    }

    @PostMapping("/reservations/new")
    public String reserve(@Valid @ModelAttribute("reservation") ReservationDto reservation,
                          BindingResult result,
                          @ModelAttribute("user") UserDto user,
                          @RequestParam("tableId") Long tableId,
                          Model model){
        List<Table> allTables = tableService.findAllOrderByNumber();
        model.addAttribute("tables", allTables);

        if(result.hasErrors()){
            return "/reservation/createReservationForm";
        }

        reservation.setCloseTime(reservation.getTerm());
        try {
            reservationService.reserve(user.getId(), tableId, reservation.getStartTime(),
                    reservation.getCloseTime(), reservation.getCovers());
        }catch (DuplicateReserveException e){
            result.rejectValue("startTime", "fieldError", "이미 예약된 시간입니다.");
            return "/reservation/createReservationForm";
        }catch (SeatExcessException e){
            result.rejectValue("covers", "fieldError", "예약 가능 인원을 초과했습니다.");
            return "/reservation/createReservationForm";
        }


        return "redirect:/reservations";
    }
}
