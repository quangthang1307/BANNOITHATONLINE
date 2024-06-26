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

import org.antlr.v4.runtime.misc.Interval;
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
import poly.edu.repository.FlashSaleHourRepository;
import poly.edu.repository.FlashSaleRepository;
import poly.edu.repository.ProductRepository;

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

    @Autowired
    ProductRepository productRepository;

    @Autowired
    FlashSaleRepository flashSaleRepository;

    @Autowired
    FlashSaleHourRepository flashSaleHourRepository;

    List<Product> productselect = new ArrayList<>();
    List<Product> products = new ArrayList<>();
    List<Flashsale> productselectflashsale = new ArrayList<>();
    String messageDaystart = "";
    String messageDaysend = "";
    String messageHourend = "";
    String messageHourstart = "";

    // String messageDaystart = "";
    // MÃ GIẢM GIÁ
    @GetMapping("/promotion")
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

        // xóa thông báo lỗi
        messageDaysend = "";
        messageDaystart = "";
        //
        String dayStartValue = request.getParameter("daystart");
        String dayEndValue = request.getParameter("dayend");

        if (result.hasErrors() || dayEndValue.isEmpty() || dayStartValue.isEmpty()) {

            System.out.println("Có lỗi ngày trống");
            if (dayStartValue.isEmpty()) {
                messageDaystart = "Vui lòng chọn ngày bắt đầu";
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime dayStartDateTime = LocalDateTime.parse(dayStartValue, formatter);
                discount.setStartDate(dayStartDateTime);
                System.out.println(discount.getStartDate());
                model.addAttribute("daystart", discount.getStartDate());

            }
            if (dayEndValue.isEmpty()) {
                messageDaysend = "Vui lòng chọn ngày kết thúc";
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime dayEndDateTime = LocalDateTime.parse(dayEndValue, formatter);
                discount.setEndDate(dayEndDateTime);
                model.addAttribute("dayend", discount.getEndDate());
            }

            model.addAttribute("messdaystart", messageDaystart);
            model.addAttribute("messdayend", messageDaysend);

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
            System.out.println("có lỗi liên quan đến ngày");
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
        products = productRepository.findProductAllNoSale();

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
    public String savePromotionSale(HttpServletRequest request, Model model, @Valid @ModelAttribute("sale") Sale sale,
            BindingResult result) {
        // xóa thông báo lỗi
        messageDaysend = "";
        messageDaystart = "";
        //
        String dayStartValue = request.getParameter("daystart");
        String dayEndValue = request.getParameter("dayend");
        if (sale.getSaleID() == null) {
            if (result.hasErrors() || dayStartValue.isEmpty() || dayEndValue.isEmpty()) {

                if (dayStartValue.isEmpty()) {
                    messageDaystart = "Vui lòng chọn ngày bắt đầu";
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    LocalDateTime dayStartDateTime = LocalDateTime.parse(dayStartValue, formatter);
                    sale.setDayStart(dayStartDateTime);
                    model.addAttribute("daystart", sale.getDayStart());

                }
                if (dayEndValue.isEmpty()) {
                    messageDaysend = "Vui lòng chọn ngày kết thúc";
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    LocalDateTime dayEndDateTime = LocalDateTime.parse(dayEndValue, formatter);
                    sale.setDayEnd(dayEndDateTime);
                    model.addAttribute("dayend", sale.getDayEnd());
                }

                model.addAttribute("messdaystart", messageDaystart);
                model.addAttribute("messdayend", messageDaysend);

                products = productRepository.findProductAllNoSale();

                if (!productselect.isEmpty()) {
                    System.out.println("Khong trong");
                    for (Product ps : productselect) {
                        products.removeIf(p -> p.getProductid() == ps.getProductid());
                    }
                }
                if (sale.getSaleID() != null) {

                    Optional<Sale> sale3 = saleService.findById(sale.getSaleID());
                    products.forEach(p -> {
                        if (p.getProductid() == sale3.get().getProductID()) {
                            productselect.add(p);
                        }
                    });
                    model.addAttribute("sale", sale3.get());
                    model.addAttribute("daystart", sale3.get().getDayStart());
                    model.addAttribute("dayend", sale3.get().getDayEnd());
                }

                model.addAttribute("products", products);
                model.addAttribute("productselect", productselect);

                return "admin/promotionsaleform";
            }
        }
        // Định dạng chuỗi ngày giờ
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        // Chuyển đổi chuỗi thành LocalDateTime
        LocalDateTime dayStartDateTime = LocalDateTime.parse(dayStartValue, formatter);
        LocalDateTime dayEndDateTime = LocalDateTime.parse(dayEndValue, formatter);

        if (sale.getSaleID() == null) {
            if (result.hasErrors() || !isStartDateBeforeEndDate(dayStartDateTime, dayEndDateTime)
                    || !isEndDateAfterStartDate(dayEndDateTime, dayStartDateTime) || isDateInPast(dayEndDateTime)
                    || isDateInPast(dayStartDateTime)) {
                sale.setDayStart(dayStartDateTime);
                sale.setDayEnd(dayEndDateTime);
                System.out.println("có lỗi liên quan đến ngày");
                products = productService.findAllNoActive();

                if (!productselect.isEmpty()) {
                    System.out.println("Khong trong");
                    for (Product ps : productselect) {
                        products.removeIf(p -> p.getProductid() == ps.getProductid());
                    }
                }

                model.addAttribute("products", products);
                model.addAttribute("productselect", productselect);
                return "admin/promotionsaleform";
            }
        }
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
        if (sdaystart.isEmpty() || flashSaleHour.getHourStart() == null || flashSaleHour.getHourEnd() == null) {
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
            if (flashSaleHour.getID() == null) {
                FlashSaleHour flashhsale = new FlashSaleHour();
                flashhsale.setStatus(false);
                flashhsale.setFrequencyFor("");
                model.addAttribute("flashhsales", flashhsale);
            } else {
                Optional<FlashSaleHour> flashhsale = flashSaleHourService.findbyId(flashSaleHour.getID());
                List<Flashsale> flashsales = flashSaleService.findByFlashsaleHour(flashSaleHour.getID());
                flashhsale.get().setFrequencyFor(flashhsale.get().getFrequencyFor().trim());

                for (Flashsale flashsale : flashsales) {
                    productselectflashsale.add(flashsale);
                }

                boolean checknone = false;
                if (flashhsale.get().getFrequencyFor().trim().equals("NONE")) {
                    checknone = true;
                }
                model.addAttribute("checknone", checknone);
                model.addAttribute("daystart", flashhsale.get().getDateStart());
            }
            if (sdaystart.isEmpty()) {
                messageDaystart = "Vui lòng chọn ngày bắt đầu";
            }
            if (flashSaleHour.getHourStart() == null) {
                messageHourstart = "Vui lòng chọn giờ bắt đầu";
            }
            if (flashSaleHour.getHourEnd() == null) {
                messageHourend = "Vui lòng chọn giờ kết thúc";
            }

            model.addAttribute("messdaystart", messageDaystart);
            model.addAttribute("messagehourstart", messageHourstart);
            model.addAttribute("messagehourend", messageHourend);
            model.addAttribute("products", products);
            model.addAttribute("productselect", productselectflashsale);
            model.addAttribute("sizeproduct", sizeproduct);

            return "admin/flashsaleform";

        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(sdaystart, formatter);
        FlashSaleHour s = new FlashSaleHour();
        s.setID(flashSaleHour.getID());
        s.setDateStart(date);
        LocalDate daynow = LocalDate.now();
        if (flashSaleHour.getID() == null) {
            if (date.isBefore(daynow)) {

                System.out.println("có lỗi liên quan đến ngày");
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
                if (flashSaleHour.getID() == null) {
                    FlashSaleHour flashhsale = new FlashSaleHour();
                    flashhsale.setStatus(false);
                    flashhsale.setFrequencyFor("");
                    model.addAttribute("flashhsales", flashhsale);
                } else {
                    Optional<FlashSaleHour> flashhsale = flashSaleHourService.findbyId(flashSaleHour.getID());
                    List<Flashsale> flashsales = flashSaleService.findByFlashsaleHour(flashSaleHour.getID());
                    flashhsale.get().setFrequencyFor(flashhsale.get().getFrequencyFor().trim());

                    for (Flashsale flashsale : flashsales) {
                        productselectflashsale.add(flashsale);
                    }

                    boolean checknone = false;
                    if (flashhsale.get().getFrequencyFor().trim().equals("NONE")) {
                        checknone = true;
                    }
                    model.addAttribute("checknone", checknone);
                    model.addAttribute("daystart", flashhsale.get().getDateStart());
                }

                model.addAttribute("products", products);
                model.addAttribute("productselect", productselectflashsale);
                model.addAttribute("sizeproduct", sizeproduct);
                return "admin/flashsaleform";
            }
        }
        List<FlashSaleHour> flashSaleHours = flashSaleHourService.findFlashSaleHoursAll();

        // kiểm tra trùng ngày
        if (flashSaleHour.getID() == null) {
            if (checkOverlap(flashSaleHour, flashSaleHours)) {
                System.out.println("Trùng lặp");
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
                if (flashSaleHour.getID() == null) {
                    FlashSaleHour flashhsale = new FlashSaleHour();
                    flashhsale.setDateStart(date);
                    flashhsale.setStatus(false);
                    flashhsale.setFrequencyFor("");
                    model.addAttribute("flashhsales", flashhsale);
                } else {
                    Optional<FlashSaleHour> flashhsale = flashSaleHourService.findbyId(flashSaleHour.getID());
                    List<Flashsale> flashsales = flashSaleService.findByFlashsaleHour(flashSaleHour.getID());
                    flashhsale.get().setFrequencyFor(flashhsale.get().getFrequencyFor().trim());

                    for (Flashsale flashsale : flashsales) {
                        productselectflashsale.add(flashsale);
                    }

                    boolean checknone = false;
                    if (flashhsale.get().getFrequencyFor().trim().equals("NONE")) {
                        checknone = true;
                    }
                    model.addAttribute("checknone", checknone);

                }

                model.addAttribute("daystart", date);
                model.addAttribute("messagehourstart", "Khung giờ đã tồn tại");
                model.addAttribute("messagehourend", "Khung giờ đã tồn tại");
                model.addAttribute("products", products);
                model.addAttribute("productselect", productselectflashsale);
                model.addAttribute("sizeproduct", sizeproduct);
                return "admin/flashsaleform";
            }
        }
        //
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

    public boolean checkOverlap(FlashSaleHour newInterval, List<FlashSaleHour> existingIntervals) {

        System.out.println("Size" + existingIntervals.size());
        System.out.println(newInterval.getHourStart().getHour());
        System.out.println(newInterval.getHourEnd().getHour());
        int newStart = newInterval.getHourStart().getHour();
        int newEnd = newInterval.getHourEnd().getHour();

        for (FlashSaleHour interval : existingIntervals) {
            System.out.println(interval.getHourStart().getHour());
            System.out.println(interval.getHourEnd().getHour());
            int existingStart = interval.getHourStart().getHour();
            int existingEnd = interval.getHourEnd().getHour();

            // Kiểm tra trùng lặp
            if ((newStart >= existingStart && newStart < existingEnd) ||
                    (newEnd > existingStart && newEnd <= existingEnd) ||
                    (newStart <= existingStart && newEnd >= existingEnd)) {
                return true; // Trùng lặp
            }
        }
        return false;
    }

    @GetMapping("/deleteflashsale/{flashsalehourid}")
    public String deleteFlashsale(@PathVariable("flashsalehourid") Integer flashsalehourid) {

        List<Flashsale> fs = flashSaleService.findByFlashsaleHour(flashsalehourid);
        for (Flashsale flashsale : fs) {
            flashSaleRepository.delete(flashsale);
        }
        flashSaleHourRepository.deleteById(flashsalehourid);
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
