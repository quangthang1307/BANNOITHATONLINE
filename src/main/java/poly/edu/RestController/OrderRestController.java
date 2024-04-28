package poly.edu.RestController;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import poly.edu.Service.CartService;
import poly.edu.Service.DiscountService;
import poly.edu.Service.OrderService;
import poly.edu.entity.Address;
import poly.edu.entity.Cart;
import poly.edu.entity.Discount;
import poly.edu.entity.DiscountUsage;
import poly.edu.entity.Evaluate;
import poly.edu.entity.FlashSaleHour;
import poly.edu.entity.Flashsale;
import poly.edu.entity.Order;
import poly.edu.entity.Orderdetails;
import poly.edu.entity.Orderstatus;
import poly.edu.entity.Payment;
import poly.edu.entity.Product;
import poly.edu.entity.ProductImage;
import poly.edu.entity.Sale;
import poly.edu.entity.Telegram;
import poly.edu.entity.Transaction;
import poly.edu.repository.DiscountUsageRepository;
import poly.edu.repository.EvaluateRepository;
import poly.edu.repository.FlashSaleHourRepository;
import poly.edu.repository.FlashSaleRepository;
import poly.edu.repository.OrderDetailRepository;
import poly.edu.repository.OrderRepository;
import poly.edu.repository.OrderStatusRepository;
import poly.edu.repository.ProductImageRepository;
import poly.edu.repository.ProductRepository;
import poly.edu.repository.SaleRepository;
import poly.edu.repository.TelegramRepository;
import poly.edu.repository.TransactionRepository;

@CrossOrigin("*")
@RestController
public class OrderRestController {
    @Autowired
    OrderService orderService;
    @Autowired
    DiscountService discountService;
    @Autowired
    CartService cartService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DiscountUsageRepository discountUsageRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    OrderStatusRepository orderStatusRepository;

    @Autowired
    ProductImageRepository productImageRepository;

    @Autowired
    SaleRepository saleRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    FlashSaleRepository flashSaleRepository;

    @Autowired
    FlashSaleHourRepository flashSaleHourRepository;

    @Autowired TelegramRepository telegramRepository;

    @Autowired EvaluateRepository evaluateRepository;

    @PostMapping("/rest/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        order.setTime(LocalDateTime.now());
        orderService.createdOrder(order);
        try {
            if (order.getDiscount().getDiscountId() != null) {
                Optional<Discount> discount = discountService.findById(order.getDiscount().getDiscountId());
                List<DiscountUsage> usageList = discountUsageRepository
                        .findAllByCustomerId(order.getCustomer().getCustomerId());

                int countDiscount = 0;
                for (DiscountUsage list : usageList) {
                    if (list.getDiscount().getCode().equals(order.getDiscount().getCode())) {
                        countDiscount++;
                    }
                    if (countDiscount >= discount.get().getMaxUsage()) {
                        break;
                    }
                }

                if (countDiscount >= discount.get().getMaxUsage()) {
                    if (discount.isPresent()) {
                        discount.get().setQuantityUsed(discount.get().getQuantityUsed() + 1);
                        discountService.update(discount.get());
                        DiscountUsage usage = new DiscountUsage();
                        usage.setCustomer(order.getCustomer());
                        usage.setDiscount(order.getDiscount());
                        usage.setUseddate(LocalDateTime.now());
                        discountUsageRepository.save(usage);
                    }
                }
            }

        } catch (Exception e) {
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/rest/editOrder")
    public ResponseEntity<?> editOrder(@RequestBody Order order) {
        order.setTime(LocalDateTime.now());
        Optional<Order> orderfind = orderRepository.findById(order.getOrderID());
        if (orderfind.isPresent()) {
            orderfind.get().setAddress(order.getAddress());
            orderfind.get().setDiscount(order.getDiscount());
            orderfind.get().setPayment(order.getPayment());
            orderfind.get().setSumpayment(order.getSumpayment());
            orderfind.get().setTime(order.getTime());
        }
        orderRepository.save(orderfind.get());

        try {
            if (order.getDiscount().getDiscountId() != null) {
                Optional<Discount> discount = discountService.findById(order.getDiscount().getDiscountId());
                List<DiscountUsage> usageList = discountUsageRepository
                        .findAllByCustomerId(order.getCustomer().getCustomerId());

                int countDiscount = 0;
                for (DiscountUsage list : usageList) {
                    if (list.getDiscount().getCode().equals(order.getDiscount().getCode())) {
                        countDiscount++;
                    }
                    if (countDiscount >= discount.get().getMaxUsage()) {
                        break;
                    }
                }

                if (countDiscount < discount.get().getMaxUsage()) {
                    if (discount.isPresent()) {
                        discount.get().setQuantityUsed(discount.get().getQuantityUsed() + 1);
                        discountService.update(discount.get());
                        DiscountUsage usage = new DiscountUsage();
                        usage.setCustomer(order.getCustomer());
                        usage.setDiscount(order.getDiscount());
                        usage.setUseddate(LocalDateTime.now());
                        discountUsageRepository.save(usage);
                    }
                }
            }

        } catch (Exception e) {
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/rest/deleteProductInCartByCustomerId")
    public ResponseEntity<?> deleteProductInCartByCustomerId(@RequestParam Integer customerId,
            @RequestParam Integer productId) {
        Optional<Cart> cart = cartService.findByCustomerAndProduct(customerId, productId);
        if (cart.isPresent()) {
            cartService.delete(cart.get().getCartId());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rest/orderByCustomer")
    public ResponseEntity<?> orderByCustomer(@RequestParam Integer customerId) {
        List<Order> order = orderService.getOrderListByCustomerId(customerId);
        List<?> evaluate = evaluateRepository.disableBTN(customerId, order.get(0).getCustomer().getCustomerId());
        System.out.println(evaluate)
        if (order.size() > 0) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.noContent().build();
        }

    }

    @GetMapping("/rest/findOrder")
    public ResponseEntity<?> getOrderOne(@RequestParam Integer orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.noContent().build();
        }

    }

    @PutMapping("/rest/order/huydon")
    public void edit(@RequestParam Integer orderId) {
        String username = null;
        String sanpham = null;
        String phone = null;
        String tongtien = null;
        String time = null;
        String name = null;
        String link = null;
        Optional<Order> order = orderRepository.findById(orderId);

        List<Orderdetails> orderDetail = orderDetailRepository.getOrderdetailsByOrderID(orderId);
        if (orderDetail.size() == 1) {
            sanpham = orderDetail.get(0).getProduct().getProductname();
        } else {
            StringBuilder concatenatedNames = new StringBuilder();
            // Duyệt qua từng order detail để lấy tên sản phẩm và nối vào chuỗi
            for (Orderdetails orderdetails : orderDetail) {
                // Lấy tên sản phẩm và nối vào chuỗi
                concatenatedNames.append(orderdetails.getProduct().getProductname()).append(" x ");
                // Lấy số lượng sản phẩm và nối vào chuỗi
                concatenatedNames.append(orderdetails.getProductquantity()).append(", ");
            }
            // Xóa dấu phẩy cuối cùng và khoảng trắng thừa
            String finalNames = concatenatedNames.toString().replaceAll(", $", "");
            sanpham = finalNames;
        }

        if (order.isPresent()) {
            if (order.get().getCustomer().getAccount().getUsername() != null
                    || !order.get().getCustomer().getAccount().getUsername().isEmpty()) {
                username = order.get().getCustomer().getAccount().getUsername();
            }
            if (order.get().getCustomer().getPhone() != null || !order.get().getCustomer().getPhone().isEmpty()) {
                phone = order.get().getCustomer().getPhone();
            }
            if (order.get().getTime() != null) {
                time = order.get().getTime().toString().substring(0, 10);
            }
            if (order.get().getCustomer().getName() != null || !order.get().getCustomer().getName().isEmpty()) {
                name = order.get().getCustomer().getName();
            }

            DecimalFormat decimalFormat = new DecimalFormat("#,### VND");
            tongtien = decimalFormat.format(Double.parseDouble(order.get().getSumpayment().toString()));
            String message = "Đơn hủy !\n"
                    + "*Mã đơn hàng:* " + orderId + "\n"
                    + "*Họ và tên:* " + name + "\n"
                    + "*Username:* " + username + "\n"
                    + "*Số điện thoại:* " + phone + "\n"
                    + "*Ngày đặt hàng:* " + time + "\n"
                    + "*Sản phẩm:* " + sanpham + "\n"
                    + "*Tổng tiền:* " + tongtien + "\n";

            Telegram telegram = telegramRepository.findByMission(Telegram.MissionType.HUYHANG);
            if (telegram != null) {
                try {
                    TelegramBot bot = new TelegramBot(telegram.getBotToken());
                    SendMessage send = new SendMessage(telegram.getChatId(), message).parseMode(ParseMode.Markdown);
                    SendResponse response = bot.execute(send);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }

        if (order.isPresent()) {
            Orderstatus orderStatus = orderStatusRepository.findByOrderStatusName("Đã hủy");
            order.get().setOrderstatus(orderStatus);
            orderRepository.save(order.get());
        }

    }

    // @DeleteMapping("/rest/deleteOrder")
    // public void deleteOrder(@RequestParam Integer orderId) {
    // List<Orderdetails> orderDetails =
    // orderDetailRepository.getOrderdetailsByOrderID(orderId);
    // Optional<Order> order = orderRepository.findById(orderId);

    // Transaction transaction = transactionRepository.findByOrder(order.get());
    // if (transaction != null) {
    // transactionRepository.delete(transaction);
    // orderDetailRepository.deleteAll(orderDetails);
    // orderRepository.deleteById(orderId);
    // } else {
    // orderDetailRepository.deleteAll(orderDetails);
    // orderRepository.deleteById(orderId);
    // }

    // }

    @GetMapping("/rest/options")
    public ResponseEntity<?> getOrder(@RequestParam String statusName, @RequestParam Integer customerId) {
        List<JSONObject> response = new ArrayList<>();
        Orderstatus orderStatus = orderStatusRepository.findByOrderStatusName(statusName);
        List<Order> orders = orderRepository.findByOrderstatus_OrderstatusID_CustomerID(orderStatus.getOrderstatusID(),
                customerId);

        for (Order order : orders) {
            JSONObject orderJson = new JSONObject();

            orderJson.put("sumpayment", order.getSumpayment());
            orderJson.put("orderID", order.getOrderID());

            JSONObject orderStatusJson = new JSONObject();
            Orderstatus orderstatus = order.getOrderstatus();
            orderStatusJson.put("orderstatusname", orderstatus.getOrderstatusname());
            orderJson.put("orderstatus", orderStatusJson);

            JSONObject paymentJson = new JSONObject();
            Payment payment = order.getPayment();
            paymentJson.put("paymentname", payment.getPaymentname());
            orderJson.put("payment", paymentJson);

            JSONObject addressJson = new JSONObject();
            Address address = order.getAddress();
            addressJson.put("addressID", address.getAddressID());
            addressJson.put("duong", address.getDuong());
            addressJson.put("phuongxa", address.getPhuongxa());
            addressJson.put("quanhuyen", address.getQuanhuyen());
            addressJson.put("sonha", address.getSonha());
            addressJson.put("tinhthanhpho", address.getTinhthanhpho());
            orderJson.put("address", addressJson);

            List<Orderdetails> orderDetails = orderDetailRepository.getOrderdetailsByOrderID(order.getOrderID());
            JSONArray orderDetailsJsonArray = new JSONArray();

            for (Orderdetails orderDetail : orderDetails) {
                JSONObject orderDetailJson = new JSONObject();
                // Thêm thông tin chi tiết đơn hàng vào JSONObject
                orderDetailJson.put("orderDetailID", orderDetail.getOrderdetailsID());
                orderDetailJson.put("productName", orderDetail.getProduct().getProductname());

                List<ProductImage> product = productImageRepository
                        .getProductImageById(orderDetail.getProduct().getProductid());

                orderDetailJson.put("imageUrl", product.get(0).getImage());
                orderDetailJson.put("name", product.get(0).getProduct().getProductname());
                orderDetailJson.put("quantity", orderDetail.getProductquantity());
                orderDetailJson.put("price", orderDetail.getPrice());

                orderDetailsJsonArray.add(orderDetailJson);
            }

            orderJson.put("products", orderDetailsJsonArray);

            response.add(orderJson);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/rest/payment/again")
    public ResponseEntity<?> paymentAgain(@RequestParam Integer orderId) {
        JSONArray response = new JSONArray();
        Optional<Order> order = orderRepository.findById(orderId);
        List<Orderdetails> orderDetails = orderDetailRepository.getOrderdetailsByOrderID(orderId);
        List<Flashsale> flash = new ArrayList<>();

        for (Orderdetails orderDetail : orderDetails) {
            JSONObject orderDetailJSON = new JSONObject();
            Sale sale = saleRepository.findByProductID(orderDetail.getProduct().getProductid());
            List<Flashsale> falshSale = flashSaleRepository.findByProduct(orderDetail.getProduct());
            boolean giamgia = false;

            if (falshSale.size() > 0) {
                for (Flashsale f : falshSale) {
                    if (f.getStatus()) {
                        flash.add(f);
                        break;
                    }
                }
            }

            if (falshSale != null && flash.get(0).getStatus()) {
                Optional<FlashSaleHour> flashSaleHour = flashSaleHourRepository
                        .findById(flash.get(0).getFlashSaleHourID());
                if (flashSaleHour.isPresent()) {
                    if (flashSaleHour.get().getStatus()
                            && flashSaleHour.get().getDateStart().isEqual(LocalDate.now())
                            && flashSaleHour.get().getHourStart().isBefore(LocalTime.now())
                            && flashSaleHour.get().getHourEnd().isAfter(LocalTime.now())) {
                        Optional<Product> product = productRepository.findById(sale.getProductID());
                        product.get().setPricexuat(product.get().getPricexuat()
                                - (product.get().getPricexuat() * flash.get(0).getPercent() / 100));
                        orderDetail.setProduct(product.get());
                        giamgia = true;
                    }
                }
            }

            if (sale != null && !giamgia) {
                if (LocalDateTime.now().isAfter(sale.getDayStart())
                        && LocalDateTime.now().isBefore(sale.getDayEnd())) {
                    Optional<Product> product = productRepository.findById(sale.getProductID());
                    product.get().setPricexuat(product.get().getPricexuat()
                            - (product.get().getPricexuat() * sale.getPercent() / 100));
                    orderDetail.setProduct(product.get());
                }

            }

            JSONObject productObject = new JSONObject();
            productObject.put("product", orderDetail.getProduct());
            productObject.put("imageUrl", orderDetail.getProduct().getProductimages().get(0).getImage());
            productObject.put("quantity", orderDetail.getProductquantity());
            JSONArray productArray = new JSONArray();
            productArray.add(productObject); // Thêm đối tượng sản phẩm vào mảng

            orderDetailJSON.put("Product", productArray);
            orderDetailJSON.put("TotalPayment", order.get().getSumpayment());

            response.add(orderDetailJSON);
        }

        return ResponseEntity.ok(response);
    }

}
