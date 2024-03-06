package poly.edu.entity;



import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FlashSaleDelay {
    private long delay;
    private List<Flashsale> flashsale;
}
