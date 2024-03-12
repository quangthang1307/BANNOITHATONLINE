package poly.edu.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FlashSaleEndDelay {
    private long delay;
    private FlashSaleHour flashsalehour;

}
