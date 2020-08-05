package work1.project1.package1.dto.request;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class KafkaRequestConsume  implements Serializable {

        private static final long serialVersionUID = 1L;
        @Getter @Setter private Long companyId;
        @Getter @Setter private Long departmentId;
        @Getter @Setter private Long employeeId;
        @Getter @Setter private String empName;
        @Getter @Setter private String phone;
        @Getter @Setter private Long salary;

        public KafkaRequestConsume()
        {

        }
}
