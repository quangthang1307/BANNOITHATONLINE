package poly.edu.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale.Category;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import poly.edu.Service.BrandService;
import poly.edu.Service.CategoryService;
import poly.edu.Service.ImageService;
import poly.edu.Service.ProductImageService;
import poly.edu.Service.ProductService;
import poly.edu.Service.UpdateHistoryProductService;
import poly.edu.entity.Brands;
import poly.edu.entity.Employee;
import poly.edu.entity.Product;
import poly.edu.entity.ProductImage;
import poly.edu.entity.UpdateHistoryProduct;

@Controller
@RequestMapping("/admin")
public class ProductController {
	@Autowired
	ProductService prooductService;

	@Autowired
	BrandService brandService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	UpdateHistoryProductService historyProductService;

	@Autowired
	ProductImageService PimageService;

	@Autowired
	HttpSession session;

	@Autowired
	ImageService imageService;

	List<Integer> lImages = new ArrayList<>();

	@GetMapping("/products")
	public String showList(Model model) {
		lImages.removeAll(lImages);
		if (lImages.isEmpty()) {
			System.out.println("da remove");
		}
		Product product = new Product();
		product.setDescription("abc");
		product.setProductactivate(false);
		model.addAttribute("products", prooductService.findAllNoActive());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("categorys", categoryService.findAllCapCon());
		model.addAttribute("product", product);

		return "admin/products";
	}

	@GetMapping("/products/form/{productid}")
	public String showProductForm(@PathVariable("productid") Integer productid, Model model) {
		Product product;
		if (productid == 0) {
			product = new Product();
			product.setProductname("abc");
			model.addAttribute("product", product);
			model.addAttribute("tiltePage", "Tạo Sản Phẩm Mới");
			model.addAttribute("tilteButton", "Thêm sản phẩm");
		} else {
			Optional<Product> products = prooductService.findById(productid);
			model.addAttribute("product", products.get());
			model.addAttribute("tiltePage", products.get().getProductname());
			model.addAttribute("tilteButton", "Cập nhật sản phẩm");
		}

		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("categorys", categoryService.findAllCapCon());

		return "admin/productform";
	}

	@PostMapping("/saveProduct")
	public String createProduct(@RequestPart("allimage") List<MultipartFile> files, Model model, Product product) {

		System.out.println(product.getProductname());
		Employee employee = (Employee) session.getAttribute("employee");
		System.out.println(employee);
		for (MultipartFile file : files) {
			System.out.println(file.getOriginalFilename());

			// Xử lý mỗi file
		}

		if (product.getProductid() == null) {
			List<ProductImage> images = new ArrayList<>();
			product.setViewcount(0);
			product.setCreatedby(employee);
			product.setCreateddate(LocalDateTime.now());
			for (MultipartFile file : files) {
				try {
					String filename = imageService.saveImage(file);
					ProductImage image = new ProductImage();
					image.setImage("images/product/" + filename);
					image.setProduct(product);
					images.add(image);
					System.out.println(file.getOriginalFilename());
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Xử lý mỗi file
			}
			product.setProductimages(images);

			prooductService.save(product);
		} else {

			if (!lImages.isEmpty()) {
				for (int i : lImages) {
					System.out.println("Xóa");
					PimageService.deleteById(i);
				}

			}
			if (!files.get(0).getOriginalFilename().isEmpty()) {
				for (MultipartFile file : files) {
					System.out.println("Upload");
					try {
						imageService.updateImage(file, product);

						System.out.println(file.getOriginalFilename());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			Optional<Product> p = prooductService.findById(product.getProductid());
			product.setViewcount(p.get().getViewcount());
			product.setCreatedby(p.get().getCreatedby());
			product.setCreateddate(p.get().getCreateddate());
			product.setProductimages(p.get().getProductimages());
			System.out.println("TRUOC SAVE=========================================");
			// UpdateHistoryProduct historyProduct = new UpdateHistoryProduct();
			// historyProduct.setEmployeeID(employee.getEmployeeId());
			// historyProduct.setProductID(product.getProductid());
			// historyProduct.setUpdatedate(LocalDateTime.now());
			// historyProductService.save(historyProduct);

			prooductService.save(product);
		}

		return "redirect:/admin/products";
	}

	@PostMapping("/imagefordelete")
	public String ImageForDelete(@RequestBody String value, Model model) {
		String imageid = value.replaceAll("\"", "");
		lImages.add(Integer.parseInt(imageid));
		System.out.println(value.replaceAll("\"", ""));
		return "redirect:/admin/products";
	}

	@GetMapping("/deleteProduct/{productId}")
	public String deleteProduct(@PathVariable("productId") Integer productId) {

		prooductService.deleteById(productId);

		return "redirect:/admin/products";
	}

	@GetMapping("/deleteImage/{imageId}/{productId}")
	public String deleteImage(@PathVariable("imageId") Integer imageId, @PathVariable("productId") Integer productId) {
		System.out.println("OK");
		PimageService.deleteById(imageId);

		return "redirect:/admin/products/form/" + productId;
	}

	// @GetMapping("/formexit")
	// public String ExitImage() {
	// System.out.println("OK");
	// for(ProductImage p : lImages){
	// PimageService.save(p);
	// }

	// return "redirect:/admin/products";
	// }
}