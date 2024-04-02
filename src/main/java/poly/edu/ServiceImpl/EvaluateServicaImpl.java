package poly.edu.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.EvaluateService;
import poly.edu.entity.Evaluate;

import poly.edu.repository.EvaluateRepository;


@Service
public class EvaluateServicaImpl implements EvaluateService {

	 @Autowired
	   EvaluateRepository evaluateRepository;
	
	@Override
	public List<Evaluate> findAll() {
		// TODO Auto-generated method stub
		return evaluateRepository.findAll();
	}

	@Override
	public Evaluate findById(Integer evaluateId) {
		// TODO Auto-generated method stub
		return evaluateRepository.getById(evaluateId);
	}

}
