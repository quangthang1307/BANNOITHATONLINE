package poly.edu.controller;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.edu.Service.EmployeeService;
import poly.edu.entity.Account;
import poly.edu.entity.AccountRole;
import poly.edu.entity.Employee;
import poly.edu.entity.Role;
import poly.edu.repository.AccountRepository;
import poly.edu.repository.AccountRoleRepository;
import poly.edu.repository.RoleRepository;

@Controller
@RequestMapping("admin")
public class EmployeeController {

  @Autowired
  EmployeeService employeeService;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  AccountRoleRepository accountRoleRepository;

  @Autowired
  RoleRepository roleRepository;

  @GetMapping("/employee")
  public String showList(
    @ModelAttribute("employee") Employee employee,
    Model model
  ) {
    List<Employee> list = employeeService.findAll();
    model.addAttribute("employees", list);
    return "admin/employee";
  }

  @RequestMapping("/deleteEmployee/{employeeID}")
  public String deleteemployee(
    @PathVariable("employeeID") Integer employeeID,
    RedirectAttributes redirectAttributes
  ) {
    try {
      employeeService.delete(employeeID);
      redirectAttributes.addFlashAttribute(
        "deletesuccessMessage",
        "Xóa thành công!"
      );
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute(
        "deleteerrorMessage",
        "Xóa thất bại: " + e.getMessage()
      );
    }
    return "redirect:/admin/employee";
  }

  // edit
  @RequestMapping("/editEmployee/{employeeID}")
  public String editemployee(
    @PathVariable("employeeID") Integer employeeID,
    Model model
  ) {
    Employee employee = employeeService.findById(employeeID);
    model.addAttribute("employee", employee);
    return "admin/editemployee";
  }

  @PostMapping("/updateEmployee")
  public String updateemployee(
    @ModelAttribute("employee") Employee employee,
    BindingResult bindingResult,
    RedirectAttributes redirectAttributes
  ) {
    try {
      if (employee.getName().isEmpty()) {
        bindingResult.rejectValue(
          "name",
          "error.employee",
          "Tên nhân viên không hợp lệ"
        );
        return "/admin/editemployee";
      }
      if (employee.getName().length() > 255) {
        bindingResult.rejectValue(
          "name",
          "error.employee",
          "Tên nhân viên không quá 255 ký tự"
        );
        return "/admin/editemployee";
      }
      String phoneRegex = "^[0-9]+$";
      if (
        employee.getPhone().length() < 8 || employee.getPhone().length() > 12
      ) {
        bindingResult.rejectValue(
          "phone",
          "error.employee",
          "Số điện thoại không hợp lệ (chỉ chứa số)"
        );
        return "/admin/addemployee";
      } else {
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(employee.getPhone());
        if (!matcher.matches()) {
          bindingResult.rejectValue(
            "phone",
            "error.employee",
            "Số điện thoại không hợp lệ (chỉ chứa số)"
          );
          return "/admin/addemployee";
        }
      }
      employeeService.update(employee);
      redirectAttributes.addFlashAttribute(
        "successMessage",
        "Cập nhật thành công!"
      );
      return "redirect:/admin/employee";
    } catch (Exception e) {
      e.printStackTrace();
      redirectAttributes.addFlashAttribute(
        "errorMessage",
        "Cập nhật thất bại: " + e.getMessage()
      );
      return "redirect:/admin/editemployee";
    }
  }

  @GetMapping("/addEmployee")
  public String showAddemployeeForm(Model model) {
    Employee employee = new Employee();
    model.addAttribute("employee", employee);
    List<Account> acc = accountRepository.findAccountsWithoutRoles();
    model.addAttribute("accounts", acc);
    return "admin/addemployee";
  }

  @PostMapping("/addEmployee")
  public String addemployee(
    @ModelAttribute("employee") Employee employee,
    @RequestParam(value = "account", required = false) Integer account,
    RedirectAttributes redirectAttributes,
    BindingResult bindingResult,
    Model model
  ) {
    List<Account> accc = accountRepository.findAccountsWithoutRoles();
    model.addAttribute("accounts", accc);
    if (employee.getName().isEmpty()) {
      bindingResult.rejectValue(
        "name",
        "error.employee",
        "Tên nhân viên không hợp lệ"
      );
      return "/admin/addemployee";
    }
    if (bindingResult.hasErrors()) {
      model.addAttribute("employee", employee);
      List<Account> acc = accountRepository.findAccountsWithoutRoles();
      model.addAttribute("accounts", acc);
      return "admin/addemployee";
    }
    String phoneRegex = "^[0-9]+$";
    if (employee.getPhone().length() < 8 || employee.getPhone().length() > 12) {
      bindingResult.rejectValue(
        "phone",
        "error.employee",
        "Số điện thoại không hợp lệ (chỉ chứa số)"
      );
      return "/admin/addemployee";
    } else {
      Pattern pattern = Pattern.compile(phoneRegex);
      Matcher matcher = pattern.matcher(employee.getPhone());
      if (!matcher.matches()) {
        bindingResult.rejectValue(
          "phone",
          "error.employee",
          "Số điện thoại không hợp lệ (chỉ chứa số)"
        );
        return "/admin/addemployee";
      }
    }
    // if (employee.getAccount() == null) {
    //   redirectAttributes.addFlashAttribute("error", "Thất bại!");
    //   return "redirect:/admin/employee";
    // }
    try {
      Optional<Account> Optional = accountRepository.findById(account);
      Optional<Role> OptionalR = roleRepository.findById(2);
      // Kiểm tra xem có tồn tại hay không
      if (Optional.isPresent() && OptionalR.isPresent()) {
        Account acc = Optional.get();
        employee.setAccount(acc);
        Role r = OptionalR.get();
        AccountRole role = new AccountRole();
        int maxId = accountRoleRepository.getMaxId();
        if (maxId == 0) {
          maxId = 1;
        } else {
          maxId++;
        }
        role.setId(maxId);
        role.setAccount(acc);
        role.setRole(r);
        accountRoleRepository.save(role);
      }
      employeeService.create(employee);
      redirectAttributes.addFlashAttribute(
        "addsuccessMessage",
        "Thêm thành công!"
      );
      return "redirect:/admin/employee"; // Điều hướng người dùng đến trang hiển thị danh sách kho sau khi thêm thành
      // công
    } catch (Exception e) {
      e.printStackTrace();
      redirectAttributes.addFlashAttribute(
        "adderrorMessage",
        "Thêm kho thất bại: " + e.getMessage()
      );
      return "redirect:/admin/addemployee"; // Nếu có lỗi, điều hướng người dùng đến trang thêm kho để thử lại
    }
  }
}
