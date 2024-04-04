package poly.edu.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.entity.Product;
import poly.edu.entity.ProductImage;

@Service
public class ImageService {
    @Autowired
	private ProductImageService productimageService;

    @Value("${upload.path}") // Đường dẫn tới thư mục lưu trữ hình ảnh trong application.properties
    private String uploadPath;

    public ProductImage updateImage(MultipartFile file, Product product) throws IOException {
        // Tạo tên file duy nhất
        String fileName =  UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Lưu file vào thư mục
        Path filePath = Path.of(uploadPath, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Tạo đối tượng ImageEntity và lưu vào cơ sở dữ liệu
        ProductImage imageEntity = new ProductImage();
        imageEntity.setProduct(product);
        imageEntity.setImage("images/product/" +fileName);
        productimageService.save(imageEntity);
        // Lưu vào cơ sở dữ liệu (thực hiện lưu vào cơ sở dữ liệu ở đây)

        return imageEntity;
    }

    public String saveImage(MultipartFile file) throws IOException {
        // Tạo tên file duy nhất
        String fileName =  UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        System.out.println(fileName);
        // Lưu file vào thư mục
        Path filePath = Path.of(uploadPath, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return fileName;
       
        

     
    }
}
