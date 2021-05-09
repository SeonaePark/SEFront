package sogon.booksys.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sogon.booksys.domain.Table;
import sogon.booksys.repository.TableRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TableController {

    private final TableRepository tableRepository;

    @GetMapping("/tables")
    public String tableListForm(Model model){
        List<Table> all = tableRepository.findAll();

        model.addAttribute("tables", all);

        return "/table/tableList";
    }
}
