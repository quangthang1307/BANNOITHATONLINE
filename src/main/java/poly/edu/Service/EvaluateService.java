package poly.edu.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import poly.edu.entity.Evaluate;
import poly.edu.entity.Product;
import poly.edu.repository.ProductRepository;

public interface EvaluateService {

	public List<Evaluate> findAll();

	public Evaluate findById(Integer evaluateId);

	public Evaluate create(Evaluate evaluate);
}
