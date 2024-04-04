package poly.edu.RestController;

import org.springframework.web.bind.annotation.RestController;

import jakarta.websocket.server.PathParam;
import lombok.experimental.PackagePrivate;
import poly.edu.dto.EvaluateDTO;
import poly.edu.entity.Evaluate;
import poly.edu.repository.EvaluateRepository;
import poly.edu.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class EvaluateRestController {
    @Autowired
    EvaluateRepository evaluateRepository;

    @GetMapping("/rest/evaluate/{productID}")
    public ResponseEntity<?> getMethodName(@PathVariable Integer productID) {
        ModelMapper modelMapper = new ModelMapper();
        Optional<List<Evaluate>> evaluatestars = evaluateRepository.findByProductId(productID);
        if (evaluatestars.isPresent()) {
            return ResponseEntity.ok(evaluatestars.get().stream().map(e -> modelMapper.map(e, EvaluateDTO.class)));

        } else {
            return ResponseEntity.ok(evaluatestars);
        }

    }

}
