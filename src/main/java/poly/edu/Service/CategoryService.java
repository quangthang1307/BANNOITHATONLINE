package poly.edu.Service;

import java.util.List;
import poly.edu.entity.Categoryproduct;


public interface CategoryService {
   public List<Categoryproduct> findAllCapCon();

   public List<Categoryproduct> findCategoryByCapCha(List<Integer> category);
}