package lk.epic.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class ISO8583FieldsDTO {
    String f0;
    String f3;
    String f4;
    String f11;
    String f22;
    String f24;
    String f25;
    String f35;
    String f41;
    String f42;
    String f55;
    String f62;
}
