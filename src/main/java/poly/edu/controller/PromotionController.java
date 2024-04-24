package poly.edu.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.text.DateFormatter;

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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import poly.edu.Service.DiscountService;
import poly.edu.Service.FlashSaleHourService;
import poly.edu.Service.FlashSaleService;
import poly.edu.Service.ProductService;
import poly.edu.Service.SaleService;
import poly.edu.entity.Discount;
import poly.edu.entity.FlashSaleHour;
import poly.edu.entity.Flashsale;
import poly.edu.entity.Product;
import poly.edu.entity.Sale;

@Controller
@RequestMapping("/admin")
public class PromotionController {

    @Autowired
    DiscountService discountService;

    @Autowired
    SaleService saleService;

    @Autowired
    FlashSaleHourService flashSaleHourService;

    @Autowired
    FlashSaleService flashSaleService;

    @Autowired
    ProductService productService;

    List<Product> productselect = new ArrayList<>();
    List<Product> products = new ArrayList<>();
    List<Flashsale> productselectflashsale = new ArrayList<>();
    String messageDaystart = "";
    String messageDaysend = "";

    // String messageDaystart = "";
    // MÃ GIẢM GIÁ
    @GetMapping("promotion")
    public String showPromotion(Model model) {
        List<Discount> discounts = discountService.findAll();
        model.addAttribute("discounts", discounts);
        return "admin/promotion";
    }

    @GetMapping("promotion/form")
    public String showPromotionForm(Model model) {

        model.addAttribute("discount", new Discount());
        return "admin/promotionform";
    }

    @GetMapping("promotion/form/{promotionid}")
    public String showPromotionFormEdit(@PathVariable("promotionid") Integer promotionid, Model model) {
        Optional<Discount> discount = discountService.findById(promotionid);

        model.addAttribute("discount", discount);
        model.addAttribute("daystart", discount.get().getStartDate());
        model.addAttribute("dayend", discount.get().getEndDate());
        return "admin/promotionform";
    }

    // Hàm kiểm tra xem ngày bắt đầu có trước ngày kết thúc hay không
    public boolean isStartDateBeforeEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate.isBefore(endDate);
    }

    // Hàm kiểm tra xem ngày kết thúc có sau ngày bắt đầu hay không
    public boolean isEndDateAfterStartDate(LocalDateTime endDate, LocalDateTime startDate) {
        return endDate.isAfter(startDate);
    }

    // Hàm kiểm tra xem ngày có trong quá khứ hay không
    public boolean isDateInPast(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        return date.isBefore(now);
    }

    @PostMapping("promotion/savepromotion")
    public String savePromotion(HttpServletRequest request, Model model,
            @Valid @ModelAttribute("discount") Discount discount, BindingResult result) {

        String dayStartValue = request.getParameter("daystart");
        String dayEndValue = request.getParameter("dayend");

        if (result.hasErrors() || dayEndValue.isEmpty() || dayStartValue.isEmpty()) {
            System.out.println("Có lỗi");
            return "admin/promotionform";

        }
        // Định dạng chuỗi ngày giờ
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        // Chuyển đổi chuỗi thành LocalDateTime
        LocalDateTime dayStartDateTime = LocalDateTime.parse(dayStartValue, formatter);
        LocalDateTime dayEndDateTime = LocalDateTime.parse(dayEndValue, formatter);
        System.out.println(isStartDateBeforeEndDate(dayStartDateTime, dayEndDateTime));
        // System.out.println(isEndDateAfterStartDate(dayEndDateTime,
        // dayStartDateTime));
        System.out.println(isDateInPast(dayEndDateTime));
        System.out.println(isDateInPast(dayStartDateTime));
        if (result.hasErrors() || !isStartDateBeforeEndDate(dayStartDateTime, dayEndDateTime)
                || !isEndDateAfterStartDate(dayEndDateTime, dayStartDateTime) || isDateInPast(dayEndDateTime)
                || isDateInPast(dayStartDateTime) || discount.getMaxUsage() > discount.getQuantity()) {
            discount.setStartDate(dayStartDateTime);
            discount.setEndDate(dayEndDateTime);
            System.out.println("Có lỗi");
            return "admin/promotionform";
        }

        discount.setStartDate(dayStartDateTime);
        discount.setEndDate(dayEndDateTime);
        discount.setQuantityUsed(0);
        discountService.save(discount);
        return "redirect:/admin/promotion";
    }

    @GetMapping("promotion/delete/{promotionId}")
    public String deletePromotion(@PathVariable("promotionId") Integer promotionId) {

        discountService.delete(promotionId);
        return "redirect:/admin/promotion";
    }
    //

    // GIẢM GIÁ THÔNG THƯỜNG
    @GetMapping("promotionsale")
    public String showPromotionSale(Model model) {
        productselect.removeAll(productselect);
        if (productselect.isEmpty()) {
            System.out.println("product select is empty");
        }

        List<Sale> sales = saleService.findAll();
        model.addAttribute("sales", sales);
        return "admin/promotionsale";
    }

    @GetMapping("formpromotionsale")
    public String showPromotionSaleForm(Model model) {
        products = productService.findAllNoActive();

        if (!productselect.isEmpty()) {
            System.out.println("Khong trong");
            for (Product ps : productselect) {
                products.removeIf(p -> p.getProductid() == ps.getProductid());
            }
        }
        Sale sale = new Sale();
        sale.setStatus(false);
        model.addAttribute("sale", sale);
        model.addAttribute("products", products);
        model.addAttribute("productselect", productselect);

        return "admin/promotionsaleform";
    }

    @GetMapping("formpromotionsale/{saleId}")
    public String edit(@PathVariable("saleId") Integer saleId, Model model) {
        products = productService.findAllNoActive();

        Optional<Sale> sale = saleService.findById(saleId);
        if (!productselect.isEmpty()) {
            System.out.println("Khong trong");
            for (Product ps : productselect) {
                products.removeIf(p -> p.getProductid() == ps.getProductid());
            }
        }
        products.forEach(p -> {
            if (p.getProductid() == sale.get().getProductID()) {
                productselect.add(p);
            }
        });

        model.addAttribute("sale", sale.get());
        model.addAttribute("daystart", sale.get().getDayStart());
        model.addAttribute("dayend", sale.get().getDayEnd());
        model.addAttribute("products", products);
        model.addAttribute("productselect", productselect);

        return "admin/promotionsaleform";
    }

    @PostMapping("/savepromotion")
    public String savePromotionSale(HttpServletRequest request, Model model, Sale sale) {
        String dayStartValue = request.getParameter("daystart");
        String dayEndValue = request.getParameter("dayend");
        // Định dạng chuỗi ngày giờ
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        // Chuyển đổi chuỗi thành LocalDateTime
        LocalDateTime dayStartDateTime = LocalDateTime.parse(dayStartValue, formatter);
        LocalDateTime dayEndDateTime = LocalDateTime.parse(dayEndValue, formatter);
        for (Product ps : productselect) {
            Sale s = new Sale();
            s.setSaleID(sale.getSaleID());
            s.setPercent(sale.getPercent());
            s.setDayStart(dayStartDateTime);
            s.setDayEnd(dayEndDateTime);
            s.setStatus(sale.getStatus());
            s.setProductID(ps.getProductid());
            saleService.save(s);
        }

        return "redirect:/admin/promotionsale";
    }

    @PostMapping("/selectproduct")
    public String selectProduct(
            @RequestParam(name = "selectedProducts", required = false) List<String> selectedProducts, Model model,
            Sale sale) {

        if (selectedProducts != null) {
            // Xử lý các sản phẩm đã được chọn ở đây
            List<Integer> productidselect = new ArrayList<Integer>();

            System.out.println(products.get(0).getProductname());
            for (String productId : selectedProducts) {
                System.out.println(productId);
                int value = Integer.parseInt(productId);
                productidselect.add(value);
            }
            List<Product> list = productService.findAllByAllId(productidselect);
            for (Product product : list) {
                productselect.add(product);
            }
        }
        if (sale.getSaleID() != null) {
            return "redirect:/admin/formpromotionsale/" + sale.getSaleID();
        }
        return "redirect:/admin/formpromotionsale";
    }

    @GetMapping("/deletesale/{saleId}")
    public String deleteProduct(@PathVariable("saleId") Integer saleId) {

        saleService.delete(saleId);
        return "redirect:/admin/promotionsale";
    }

    @GetMapping("/deleteproductselectedsale/{productId}")
    public String deleteProductSelectedSale(@PathVariable("productId") Integer productId) {

        productselect.removeIf(p -> p.getProductid() == productId);
        return "redirect:/admin/formpromotionsale";
    }
    //

    // FlashSale

    @GetMapping("promotion/flashsale")
    public String showPromotionFlashSale(Model model) {
        productselectflashsale.removeAll(productselectflashsale);

        List<FlashSaleHour> flashSaleHour = flashSaleHourService.findFlashSaleHoursAll();

        model.addAttribute("flashsalehour", flashSaleHour);
        return "admin/promotionFlashsale";
    }

    @GetMapping("formpromotionflashsale")
    public String showPromotionFlashSaleForm(Model model) {
        products = productService.findAllNoActive();
        String sizeproduct = "chưa chọn sản phẩm nào";
        if (productselectflashsale.size() > 0) {
            sizeproduct = String.valueOf(productselectflashsale.size());
        }

        if (!productselectflashsale.isEmpty()) {
            System.out.println("Khong trong");
            for (Flashsale ps : productselectflashsale) {
                products.removeIf(p -> p.getProductid() == ps.getProduct().getProductid());
            }
        }
        FlashSaleHour flashhsale = new FlashSaleHour();
        flashhsale.setStatus(false);
        flashhsale.setFrequencyFor("");
        model.addAttribute("flashhsales", flashhsale);
        model.addAttribute("products", products);
        model.addAttribute("productselect", productselectflashsale);
        model.addAttribute("sizeproduct", sizeproduct);

        return "admin/flashsaleform";
    }

    @GetMapping("formpromotionflashsale/{flashsalehourId}")
    public String showPromotionFlashSaleFormEdit(@PathVariable("flashsalehourId") Integer flashsalehourId,
            Model model) {
        products = productService.findAllNoActive();
        Optional<FlashSaleHour> flashhsale = flashSaleHourService.findbyId(flashsalehourId);
        List<Flashsale> flashsales = flashSaleService.findByFlashsaleHour(flashsalehourId);
        flashhsale.get().setFrequencyFor(flashhsale.get().getFrequencyFor().trim());

        if (!productselectflashsale.isEmpty()) {
            System.out.println("Khong trong");
            for (Flashsale ps : flashsales) {
                productselectflashsale.removeIf(p -> p.getProduct().getProductid() == ps.getProduct().getProductid());
            }
        }
        for (Flashsale flashsale : flashsales) {
            productselectflashsale.add(flashsale);
        }

        String sizeproduct = "chưa chọn sản phẩm nào";
        if (productselectflashsale.size() > 0) {
            sizeproduct = String.valueOf(productselectflashsale.size());
        }
        boolean checknone = false;
        if (flashhsale.get().getFrequencyFor().trim().equals("NONE")) {
            checknone = true;
        }
        model.addAttribute("checknone", checknone);
        model.addAttribute("daystart", flashhsale.get().getDateStart());
        model.addAttribute("flashhsales", flashhsale);
        model.addAttribute("products", products);
        model.addAttribute("productselect", productselectflashsale);
        model.addAttribute("sizeproduct", sizeproduct);

        return "admin/flashsaleform";
    }

    @PostMapping("/savepromotionflashsale")
    public String savePromotionFlashsale(HttpServletRequest request, Model model, FlashSaleHour flashSaleHour,
            @RequestParam(value = "checkboxagain", required = false) String checkboxagain) {

        String sdaystart = request.getParameter("daystart");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(sdaystart, formatter);
        FlashSaleHour s = new FlashSaleHour();
        s.setID(flashSaleHour.getID());
        s.setDateStart(date);
        s.setHourStart(flashSaleHour.getHourStart());
        s.setHourEnd(flashSaleHour.getHourEnd());

        System.out.println("checkbox" + checkboxagain);
        if (checkboxagain == null) {
            s.setFrequencyFor(flashSaleHour.getFrequencyFor());
            s.setFrequency(flashSaleHour.getFrequency());
        } else {

            s.setFrequencyFor("NONE");
            s.setFrequency(0);
        }

        s.setStatus(flashSaleHour.getStatus());

        flashSaleHourService.update(s);

        for (Flashsale ps : productselectflashsale) {

            ps.setFlashSaleHourID(s.getID());
            ps.setStatus(true);
            String percent = request.getParameter("percent" + ps.getProduct().getProductid());
            ps.setPercent(Integer.parseInt(percent));
            flashSaleService.SaveAndUpdate(ps);
        }

        return "redirect:/admin/promotion/flashsale";
    }

    @PostMapping("/selectproductflashsale")
    public String selectProductFlashsale(
            @RequestParam(name = "selectedProducts", required = false) List<String> selectedProducts, FlashSaleHour p) {
        System.out.println("DAY LA ID " + p.getID());
        if (selectedProducts != null) {
            // Xử lý các sản phẩm đã được chọn ở đây
            List<Integer> productidselect = new ArrayList<Integer>();

            for (String productId : selectedProducts) {
                System.out.println(productId);
                int value = Integer.parseInt(productId);
                productidselect.add(value);
            }
            List<Product> list = productService.findAllByAllId(productidselect);
            for (Product product : list) {
                Flashsale f = new Flashsale();
                f.setProduct(product);
                productselectflashsale.add(f);
            }
        }

        if (p.getID() != null) {
            return "redirect:/admin/formpromotionflashsale/" + p.getID();
        }
        return "redirect:/admin/formpromotionflashsale";
    }

    @GetMapping("/deleteproductselected/{productId}")
    public String deleteProductSelected(@PathVariable("productId") Integer productId) {

        productselectflashsale.removeIf(p -> p.getProduct().getProductid() == productId);
        return "redirect:/admin/formpromotionflashsale";
    }

}
