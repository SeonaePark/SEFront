package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import sogon.booksys.domain.Table;
import sogon.booksys.dto.TableDto;
import sogon.booksys.service.TableService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @GetMapping("/tables")
    public String tableListForm(Model model){
        List<Table> all = tableService.findAllOrderByNumber();

        model.addAttribute("tables", all);

        return "/table/tableList";
    }

    @GetMapping("/table")
    public String createForm(Model model){
        TableDto tableDto = new TableDto();
        model.addAttribute(tableDto);
        return "/table/tableForm";
    }

    @PostMapping("/table")
    public String create(@Valid TableDto tableDto, BindingResult result){
        if(result.hasErrors()){
            return "/table/tableForm";
        }

        Table table = Table.builder()
                .number(tableDto.getNumber())
                .seats(tableDto.getSeats())
                .build();
        try {
            tableService.save(table);
        }catch (Exception e){
            result.rejectValue("number", "fieldError", "중복된 테이블 번호입니다.");
            return "/table/tableForm";
        }

        return "redirect:/tables";
    }
}
