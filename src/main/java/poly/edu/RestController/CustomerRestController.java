package poly.edu.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import poly.edu.Service.CustomerService;
import poly.edu.Service.ParamService;
import poly.edu.entity.Account;
import poly.edu.entity.Address;
import poly.edu.entity.Customer;
import poly.edu.oauth2.CustomerOauth2User;
import poly.edu.repository.AccountRepository;
import poly.edu.repository.AddressRepository;
import poly.edu.repository.CustomerRepository;
import poly.edu.utils.validUtil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RestController
public class CustomerRestController {
    @Autowired
    CustomerRepository customerRepository;
    // edit profile
    @Autowired
    CustomerService customerService;
    // edit address
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ParamService paramService;

    @GetMapping("/rest/customer")
    public ResponseEntity<Customer> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            if (oauthToken.getPrincipal() instanceof OidcUser) {
                OidcUser oidcUser = (OidcUser) oauthToken.getPrincipal();
                String email = oidcUser.getAttribute("email");
                Account Oauth2GoogleAccount = accountRepository.findByUsername(email);
                if (Oauth2GoogleAccount != null) {
                    Customer customer = customerRepository.getCustomerID(Oauth2GoogleAccount.getAccountId());
                    return ResponseEntity.ok(customer);
                } else {
                    Account newAccount = new Account();
                    newAccount.setUsername(email);
                    newAccount.setEmail(email);
                    newAccount.setActive(true);
                    newAccount.setPassword(generateRandomString());
                    accountRepository.save(newAccount);

                    Customer newCustomer = new Customer();
                    newCustomer.setAccount(newAccount);
                    customerRepository.save(newCustomer);
                    return ResponseEntity.ok(newCustomer);
                }
            }
        }
        try {
            Account account = accountRepository.findByUsername(username);
            if (account != null) {
                Customer customer = customerRepository.getCustomerID(account.getAccountId());
                return ResponseEntity.ok(customer);
            } else {

                Object principal = authentication.getPrincipal();
                if (principal instanceof CustomerOauth2User) {
                    CustomerOauth2User customerOauth2User = (CustomerOauth2User) principal;

                    String email = customerOauth2User.getAttribute("email");
                    // String id = customerOauth2User.getAttribute("id");
                    Account Oauth2Account = accountRepository.findByUsername(email);
                    if (Oauth2Account != null) {
                        System.out.println(authentication);
                        Customer customer = customerRepository.getCustomerID(Oauth2Account.getAccountId());
                        return ResponseEntity.ok(customer);
                    } else {
                        Account newAccount = new Account();
                        newAccount.setUsername(email);
                        newAccount.setEmail(email);
                        newAccount.setActive(true);
                        newAccount.setPassword(generateRandomString());
                        accountRepository.save(newAccount);

                        Customer newCustomer = new Customer();
                        newCustomer.setAccount(newAccount);
                        customerRepository.save(newCustomer);
                        return ResponseEntity.ok(newCustomer);
                    }
                }
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        // System.out.println("Logged in user: " + username);
        return ResponseEntity.notFound().build();
    }

    public static String generateRandomString() {
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numericChars = "0123456789";
        String allChars = lowercaseChars + uppercaseChars + numericChars;
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();
        randomString.append(uppercaseChars.charAt(random.nextInt(uppercaseChars.length())));
        randomString.append(numericChars.charAt(random.nextInt(numericChars.length())));
        int randomLength = random.nextInt(5) + 8;
        for (int i = 2; i < randomLength; i++) {
            randomString.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        return randomString.toString();
    }

    // Edit profile
    @GetMapping("/rest/customers")
    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/rest/customers/{id}")
    public Customer getCustomerById(@PathVariable Integer id) {
        Optional<Customer> customer = customerService.findById(id);
        if (!customer.isPresent()) {
            System.out.println("Customer not found");
        }
        return customer.get();

    }

    @PutMapping("/rest/customers/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer customerId,
            @RequestBody Customer newCustomer) {
        Optional<Customer> customer = customerService.findById(customerId);
        if (!customer.isPresent()) {
            System.out.println("Customer not found");
        }
        Customer currentCustomer = customer.get();
        currentCustomer.setName(newCustomer.getName());
        currentCustomer.setPhone(newCustomer.getPhone());
        Customer updatedCustomer = customerRepository.save(currentCustomer);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }


    @DeleteMapping("/api/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id) {
        customerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rest/profile/customers/{customerId}/addresses")
    public ResponseEntity<List<Address>> getAddressesByCustomerId(@PathVariable Integer customerId) {
        List<Address> addresses = addressRepository.findAddressesByCustomerId(customerId);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @PostMapping("/rest/profile/customers/{customerId}/addresses")
    public ResponseEntity<Address> addAddress(@PathVariable Integer customerId, @RequestBody Address newAddress) {
        Optional<Customer> customer = customerService.findById(customerId);
        if (!customer.isPresent()) {
            System.out.println("Customer not found");
        }
        newAddress.setCustomer(customer.get());
        Address addedAddress = addressRepository.save(newAddress);
        return new ResponseEntity<>(addedAddress, HttpStatus.CREATED);
    }

    @GetMapping("/rest/profile/customer")
    public Customer getCurrentCustomer() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Customer customer = customerRepository.findByAccountUsername(name);
        return customer;
    }

    @PutMapping("/rest/profile/customers/{customerId}/addresses/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable Integer customerId, @PathVariable Integer addressId,
            @RequestBody Address newAddress) {
        Optional<Customer> customer = customerService.findById(customerId);
        if (!customer.isPresent()) {
            System.out.println("Customer not found");
        }
        Optional<Address> address = addressRepository.findById(addressId);
        if (!address.isPresent()) {
            System.out.println("Address not found");
        }
        Address currentAddress = address.get();
        currentAddress.setTinhthanhpho(newAddress.getTinhthanhpho());
        currentAddress.setQuanhuyen(newAddress.getQuanhuyen());
        currentAddress.setPhuongxa(newAddress.getPhuongxa());
        currentAddress.setSonha(newAddress.getSonha());
        currentAddress.setDuong(newAddress.getDuong());
        Address updatedAddress = addressRepository.save(currentAddress);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/rest/profile/customers/{customerId}/addresses/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Integer customerId, @PathVariable Integer addressId) {
        Optional<Customer> customer = customerService.findById(customerId);
        if (!customer.isPresent()) {
            System.out.println("Customer not found");
        }
        Optional<Address> address = addressRepository.findById(addressId);
        if (!address.isPresent()) {
            System.out.println("Address not found");
        }
        addressRepository.delete(address.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // @PutMapping("/rest/profile/customers/{customerId}/password")
    // public ResponseEntity<?> changePassword(@PathVariable Integer customerId,
    // @RequestBody Map<String, String> passwords) {
    // Optional<Customer> customer = customerService.findById(customerId);
    // if (!customer.isPresent()) {
    // System.out.println("Customer not found");
    // }
    // Account account = customer.get().getAccount();
    // String currentPassword = passwords.get("currentPassword");
    // String newPassword = passwords.get("newPassword");
    // String confirmPassword = passwords.get("confirmPassword");
    // if (!account.getPassword().equals(currentPassword)) {
    // System.out.println("Current password is incorrect");
    // }
    // if (!newPassword.equals(confirmPassword)) {
    // System.out.println("New password and confirm password do not match");
    // }
    // account.setPassword(newPassword);
    // customerService.create(customer.get());
    // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }
    // cập nhật ảnh người dùng
   
    @PutMapping("/rest/customer/{customerId}")
    public ResponseEntity<?> update(@RequestBody Customer newCustomer,Account newAccount,  @PathVariable Integer customerId) {
        Account accountCustomer = accountRepository.findByCustomerId(customerId);
        Map<String, String> errors = new HashMap<>();
        try {
            if (!customerRepository.existsById(customerId)) {
                return ResponseEntity.notFound().build();
            }

            // if (newCustomer.getName().isBlank()) {
            //     errors.put("name", "Vui lòng nhập họ tên");
            // } else if (validUtil.containsSpecialCharacters(newCustomer.getName())
            //         || validUtil.containsNumber(newCustomer.getName())) {
            //     errors.put("name", "Họ tên không được chứa số và kí tự đặt biệt");
            // }else if(newCustomer.getName().length() < 3){
            //     errors.put("name", "Họ tên phải có ít nhất 3 ký tự");
            // }
            // if (newCustomer.getAccount().getEmail().isBlank()) {
            //     errors.put("email", "Vui lòng nhập email");
            // } 
            // if (!accountRepository.findByEmailAccount(newCustomer.getAccount().getEmail()).isEmpty()) {
            //     errors.put("email", "Email đã tồn tại");
            // }
            // if (newCustomer.getPhone().isBlank()) {
            //     errors.put("phone", "Vui lòng nhập số điện thoại");
            // }else if (validUtil.containsSpecialCharacters(newCustomer.getPhone())) {
            //     errors.put("phone", "Số điện thoại không được chứa kí tự đặt biệt");
            // }
            //  if (!customerRepository.findByPhoneCustomer(newCustomer.getPhone()).isEmpty()) {
            //     errors.put("phone", "Số điện thoại đã tồn tại");
            // }
            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(errors);
            } else {
                newCustomer.setName(newCustomer.getName());
                newCustomer.setPhone(newCustomer.getPhone());
                accountCustomer.setEmail(newCustomer.getAccount().getEmail());
                customerRepository.save(newCustomer);
                return ResponseEntity.ok(newCustomer);
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(null);
        }

    }
    @PutMapping(value = "/rest/customer/update-avatar/{id}")
    public ResponseEntity<?> putMethodName(@PathVariable int id, @RequestBody MultipartFile avatar) {
        try {
            Customer customer = customerRepository.findById(id).get();
            String avatarName = paramService.saveFile(avatar, "/avatar").getName();
            customer.setImage(avatarName);
            System.out.println(avatarName);
            Map<String, String> rs = new HashMap<String, String>();
            rs.put("avatarName", avatarName);
            customerRepository.save(customer);
            return ResponseEntity.ok(rs);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        }
    }
}
    