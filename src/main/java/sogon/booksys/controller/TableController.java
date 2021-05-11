package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/tables/new")
    public String createForm(Model model){
        TableDto tableDto = new TableDto();
        model.addAttribute(tableDto);
        return "/table/tableForm";
    }

    @PostMapping("/tables/new")
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
