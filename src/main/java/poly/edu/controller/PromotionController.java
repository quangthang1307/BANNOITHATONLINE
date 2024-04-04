package poly.edu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("promotion")
    public String showPromotion(Model model) {
        List<Discount> discounts = discountService.findAll();
        model.addAttribute("discounts", discounts);
        return "admin/promotion";
    }

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
        products.forEach(p -> {
            if (p.getProductid() == sale.get().getProductID()) {
                productselect.add(p);
            }
        });
        if (!productselect.isEmpty()) {
            System.out.println("Khong trong");
            for (Product ps : productselect) {
                products.removeIf(p -> p.getProductid() == ps.getProductid());
            }
        }
        model.addAttribute("sale", sale);
        model.addAttribute("products", products);
        model.addAttribute("productselect", productselect);

        return "admin/promotionsaleform";
    }

    @PostMapping("/savepromotion")
    public String savePromotion(Model model, Sale sale) {

        for (Product ps : productselect) {
            Sale s = new Sale();
            s.setPercent(sale.getPercent());
            s.setDayStart(sale.getDayStart());
            s.setDayEnd(sale.getDayEnd());
            s.setStatus(sale.getStatus());
            s.setProductID(ps.getProductid());
            saleService.save(s);
        }

        return "redirect:/admin/promotionsale";
    }

    @PostMapping("/selectproduct")
    public String selectProduct(
            @RequestParam(name = "selectedProducts", required = false) List<String> selectedProducts, Model model) {

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

    // FlashSale

    @GetMapping("promotion/flashsale")
    public String showPromotionFlashSale(Model model) {
        productselectflashsale.removeAll(productselectflashsale);
        if (productselect.isEmpty()) {
            System.out.println("product select is empty");
        }

        List<FlashSaleHour> flashSaleHour = flashSaleHourService.findFlashSaleHours();
        model.addAttribute("flashsalehour", flashSaleHour);
        return "admin/promotionFlashsale";
    }

    @GetMapping("formpromotionflashsale")
    public String showPromotionFlashSaleForm(Model model) {
        products = productService.findAllNoActive();
        String sizeproduct = "chưa chọn sản phẩm nào";
        if(productselectflashsale.size() > 0){
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
    public String showPromotionFlashSaleFormEdit(@PathVariable("flashsalehourId") Integer flashsalehourId, Model model) {
        products = productService.findAllNoActive();
        Optional<FlashSaleHour> flashhsale = flashSaleHourService.findbyId(flashsalehourId);
        List<Flashsale> flashsales = flashSaleService.findByFlashsaleHour(flashsalehourId);
        flashhsale.get().setFrequencyFor(flashhsale.get().getFrequencyFor().trim());
        for (Flashsale flashsale : flashsales) {
            productselectflashsale.add(flashsale);
        }
        if (!productselectflashsale.isEmpty()) {
            System.out.println("Khong trong");
            for (Flashsale ps : productselectflashsale) {
                products.removeIf(p -> p.getProductid() == ps.getProduct().getProductid());
            }
        }
       
        model.addAttribute("flashhsales", flashhsale);
        model.addAttribute("products", products);
        model.addAttribute("productselect", productselectflashsale);

        return "admin/flashsaleform";
    }

    @PostMapping("/savepromotionflashsale")
    public String savePromotionFlashsale(HttpServletRequest request, Model model, FlashSaleHour flashSaleHour,
    @RequestParam(value = "checkboxagain", required = false) String checkboxagain) {

        FlashSaleHour s = new FlashSaleHour();
        s.setDateStart(flashSaleHour.getDateStart());
        s.setHourStart(flashSaleHour.getHourStart());
        s.setHourEnd(flashSaleHour.getHourEnd());
       
        
        System.out.println("checkbox"+checkboxagain);
        if (checkboxagain == null) {
            s.setFrequencyFor(flashSaleHour.getFrequencyFor());
            s.setFrequency(flashSaleHour.getFrequency());
        }else{     

            s.setFrequencyFor("");
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

        return "redirect:/admin/promotionsale";
    }

    @PostMapping("/selectproductflashsale")
    public String selectProductFlashsale(
            @RequestParam(name = "selectedProducts", required = false) List<String> selectedProducts) {

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
        return "redirect:/admin/formpromotionflashsale";
    }

    @GetMapping("/deleteproductselected/{productId}")
    public String deleteProductSelected(@PathVariable("productId") Integer productId) {

        productselectflashsale.removeIf(p -> p.getProduct().getProductid() == productId);
        return "redirect:/admin/formpromotionflashsale";
    }

    

}
