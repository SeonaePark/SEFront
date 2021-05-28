package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sogon.booksys.domain.Table;
import sogon.booksys.dto.StatisticDto;
import sogon.booksys.service.ReservationService;
import sogon.booksys.service.TableService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StatisticController {

    private final TableService tableService;
    private final ReservationService reservationService;

    @GetMapping("/statistic")
    public String statisticForm(Model model){
        List<Table> allTables = tableService.findAllOrderByNumber();
        model.addAttribute("tables", allTables);

        StatisticDto statisticDto = new StatisticDto();
        model.addAttribute("statistic", statisticDto);

        return "/statistic/statisticForm";
    }

    @PostMapping("/statistic")
    public String reservationStatistic(@RequestParam("tableId") Long tableId,
                                       @ModelAttribute("statistic") StatisticDto statisticDto,
                                       Model model){
        Table table = tableService.findById(tableId).get();
        int count = reservationService.countTableBetweenDate(table, statisticDto.getStartTime(), statisticDto.getEndTime());
        model.addAttribute("table", table);
        model.addAttribute("count", count);
        model.addAttribute("statistic", statisticDto);

        return "/statistic/reservationStatistic";
    }
}
