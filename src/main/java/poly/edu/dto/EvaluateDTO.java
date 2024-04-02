package poly.edu.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EvaluateDTO {

    private Integer evaluateId;

    private Integer productId;

    private Integer customerId;

    private Integer evaluateStar;

    private LocalDateTime evaluateDate;

    private String description;

    private String image1;

    private String image2;

    private String image3;
}
