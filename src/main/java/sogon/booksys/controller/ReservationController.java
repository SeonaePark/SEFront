package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sogon.booksys.domain.Reservation;
import sogon.booksys.domain.Table;
import sogon.booksys.domain.User;
import sogon.booksys.dto.ReservationDto;
import sogon.booksys.dto.SessionUser;
import sogon.booksys.dto.UserDto;
import sogon.booksys.exception.DuplicateReserveException;
import sogon.booksys.exception.SeatExcessException;
import sogon.booksys.repository.UserRepository;
import sogon.booksys.service.ReservationService;
import sogon.booksys.service.TableService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
        } catch (DuplicateReserveException e){
            result.rejectValue("startTime", "fieldError", e.getMessage());
        } catch (SeatExcessException e){
            result.rejectValue("covers", "fieldError", e.getMessage());
        }
        if(result.hasErrors()){
            return "/reservation/createReservationForm";
        }

        return "redirect:/reservations";
    }

    @GetMapping("/reservations/{reservationId}/edit")
    public String editForm(@PathVariable Long reservationId, Model model){
        Reservation reservation = reservationService.findById(reservationId).get();
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setCovers(reservation.getCovers());
        dto.setStartTime(reservation.getStartTime());
        dto.setTerm(reservation.getTerm());
        dto.setTableId(reservation.getTable().getId());

        model.addAttribute("reservation",dto);

        List<Table> allTables = tableService.findAllOrderByNumber();
        model.addAttribute("tables", allTables);
        return "/reservation/updateReservation";
    }

    @PostMapping("/reservations/{reservationId}/edit")
    public String editReservation(@PathVariable Long reservationId,
                                  @Valid @ModelAttribute("reservation") ReservationDto dto,
                                  BindingResult result, Model model){
        List<Table> allTables = tableService.findAllOrderByNumber();
        model.addAttribute("tables", allTables);

        if(result.hasErrors()){
            return "/reservation/updateReservation";
        }

        Reservation reservation = reservationService.findById(reservationId).get();
        try {
            if (!dto.getTableId().equals(reservation.getTable().getId())) {
                reservationService.moveTable(reservationId, dto.getTableId());
            }
            reservationService.updateReservation(reservationId, dto.getStartTime(), dto.getTerm(), dto.getCovers());
        } catch (DuplicateReserveException e){
            result.rejectValue("startTime", "fieldError", e.getMessage());
        } catch (SeatExcessException e){
            result.rejectValue("covers", "fieldError", e.getMessage());
        }

        if(result.hasErrors()){
            return "/reservation/updateReservation";
        }

        return "redirect:/reservations";
    }

    @PostMapping("/reservations/{reservationId}/delete")
    public String delete(@PathVariable Long reservationId){
        reservationService.cancelReservation(reservationId);
        return "redirect:/reservations";
    }
}
