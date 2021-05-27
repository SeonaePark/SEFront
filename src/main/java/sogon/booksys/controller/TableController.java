package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sogon.booksys.domain.Role;
import sogon.booksys.domain.Table;
import sogon.booksys.dto.SessionUser;
import sogon.booksys.dto.TableDto;
import sogon.booksys.repository.UserRepository;
import sogon.booksys.service.TableService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Vector;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TableController {

    private final TableService tableService;
    private final HttpSession httpSession;
    private final UserRepository userRepository;

    @GetMapping("/tables")
    public String tableListForm(Model model){
        List<Table> all = tableService.findAllOrderByNumber();

        model.addAttribute("tables", all);

        return "/table/tableList";
    }

    @PostMapping("/tables/new")
    public String create(@Valid TableDto tableDto, BindingResult result){
        if(result.hasErrors()){
            return "/table/createTableForm";
        }

        Table table = Table.builder()
                .number(tableDto.getNumber())
                .seats(tableDto.getSeats())
                .build();
        try {
            tableService.save(table);
        }catch (Exception e){
            result.rejectValue("number", "fieldError", "중복된 테이블 번호입니다.");
            return "/table/createTableForm";
        }

        return "redirect:/tables";
    }

    @GetMapping("/tables/{tableId}/edit")
    public String editTableForm(@PathVariable Long tableId, Model model){
        Table table = tableService.findById(tableId).get();
        TableDto tableDto = new TableDto();
        tableDto.setId(table.getId());
        tableDto.setNumber(table.getNumber());
        tableDto.setSeats(table.getSeats());

        model.addAttribute("table", tableDto);
        return "/table/updateTableForm";
    }

    @PostMapping("/tables/{tableId}/edit")
    public String editTable(@PathVariable Long tableId, @Valid @ModelAttribute("table") TableDto table,
                            BindingResult result){
        if(result.hasErrors()){
            return "/table/updateTableForm";
        }

        Table newTable = tableService.findById(tableId).get();
        tableService.save(newTable.update(table.getSeats()));

        return "redirect:/tables";
    }

    @PostMapping("/tables/{tableId}/delete")
    public String deleteTable(@PathVariable Long tableId){
        Table table = tableService.findById(tableId).get();
        tableService.delete(table);

        return "redirect:/tables";
    }

}
