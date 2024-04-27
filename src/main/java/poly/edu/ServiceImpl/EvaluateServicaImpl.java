package poly.edu.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import poly.edu.Service.EvaluateService;
import poly.edu.entity.Evaluate;
import poly.edu.entity.Product;
import poly.edu.repository.EvaluateRepository;
import poly.edu.repository.ProductRepository;

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

	@Override
	public Evaluate create(Evaluate evaluate) {
		// TODO Auto-generated method stub
		return evaluateRepository.save(evaluate);
	}

}
