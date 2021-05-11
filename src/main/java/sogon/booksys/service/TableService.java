package sogon.booksys.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogon.booksys.domain.Table;
import sogon.booksys.repository.TableRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;

    @Transactional
    public Table save(Table table){
        return tableRepository.save(table);
    }

    public Optional<Table> findByNumber(int number){
        return tableRepository.findByNumber(number);
    }

    public List<Table> findAll(){
        return tableRepository.findAll();
    }

    public List<Table> findAllOrderByNumber(){
        Sort sort = Sort.by("number").ascending();
        return tableRepository.findAll(sort);
    }

}
