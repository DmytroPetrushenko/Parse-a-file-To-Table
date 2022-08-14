package knubisoft.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Person {
    private Long id;
    private String name;
    private LocalDate birthDay;
}
