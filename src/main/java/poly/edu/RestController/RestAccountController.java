package poly.edu.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import poly.edu.repository.AccountRepository;



@CrossOrigin("*")
@RestController
public class RestAccountController {


    @Autowired AccountRepository accountRepository;


    
    
}
