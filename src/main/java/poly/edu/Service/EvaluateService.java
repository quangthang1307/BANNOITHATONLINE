package poly.edu.Service;

import java.util.List;


import poly.edu.entity.Evaluate;



public interface EvaluateService {

	 public List<Evaluate> findAll();
	 public Evaluate findById(Integer evaluateId);
	
}
