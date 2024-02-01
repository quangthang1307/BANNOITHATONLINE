package poly.edu.controller;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.standard.Media;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

@RestController
@CrossOrigin("*")
public class OpenAIController {

    // private final String OPENAI_API_URL_ASSISTANTS =
    // "https://api.openai.com/v1/assistants";
    private final String OPENAI_API_URL_THREADS = "https://api.openai.com/v1/threads";
    private final String OPENAI_API_KEY = "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";

    private String saveThreadId;
    private String saveRunThreadId;

    // @PostMapping("/createAssistant")
    // public String createAssistant(@RequestBody String requestBody) {
    // // Implement createAssistant logic as shown in the previous example
    // return performRequest(OPENAI_API_URL_ASSISTANTS, requestBody);
    // }

    @PostMapping("/createThread")
    public ResponseEntity<?> callAPI() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        headers.set("OpenAI-Beta", "assistants=v1");

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(OPENAI_API_URL_THREADS, HttpMethod.POST, entity,
                    String.class);

            if (response.hasBody()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                String threadId = root.path("id").asText();
                System.out.println("=========================================================");
                System.out.println("++ID của Thread: " + threadId);

                this.saveThreadId = threadId;
                System.out.println("++GIA TRI KHI LUU THREADID LA: " + saveThreadId);

                Map<String, String> result = Collections.singletonMap("threadId", threadId);
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dữ liệu trống");
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi xử lý JSON");
        }
    }

    // @PostMapping("/sendMessageApi")
    // public String sendMessageApi() {
    // String url = "https://api.openai.com/v1/threads/" + this.saveThreadId +
    // "/messages";

    // RestTemplate restTemplate = new RestTemplate();

    // HttpHeaders headers = new HttpHeaders();
    // headers.setContentType(MediaType.APPLICATION_JSON);
    // headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
    // headers.set("OpenAI-Beta", "assistants=v1");

    // String jsonbody = "{\"role\": \"user\", \"content\": \"Cho tôi giá tiền của
    // iphone 9?\", \"file_ids\": [\"file-PQeqheySy2be7PYRIuVVgQNQ\"]}";

    // HttpEntity<String> entity = new HttpEntity<>(jsonbody, headers);

    // ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,
    // entity, String.class);

    // return response.getBody();
    // }

    @PostMapping("/sendMessageApi")
    public ResponseEntity<?> sendMessageApi(@RequestBody Map<String, String> requestBody) {
        String userMessage = requestBody.get("content");

        String url = "https://api.openai.com/v1/threads/" + this.saveThreadId + "/messages";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        headers.set("OpenAI-Beta", "assistants=v1");

        Map<String, Object> jsonBody = new HashMap<>();
        jsonBody.put("role", "user");
        jsonBody.put("content", userMessage);
        jsonBody.put("file_ids", Arrays.asList("file-PQeqheySy2be7PYRIuVVgQNQ"));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(jsonBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return ResponseEntity.ok(Collections.singletonMap("message", "Message sent successfully"));
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @PostMapping("/runThread")
    public ResponseEntity<?> runThred() {
        String url = "https://api.openai.com/v1/threads/" + this.saveThreadId + "/runs";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        headers.set("OpenAI-Beta", "assistants=v1");

        Map<String, Object> jsonBody = new HashMap<>();
        jsonBody.put("assistant_id", "asst_XxVYkcs7AGlf2FIelzXB76o8");
        jsonBody.put("instructions",
                "Bạn là một bot tư vấn sản phẩm, Hãy đề xuất cho khách hàng những món hàng dựa trên API và trả lời những câu hỏi của họ");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(jsonBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (response.hasBody()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                String runThreadId = root.path("id").asText();
                System.out.println("=========================================================");
                System.out.println("++MA CUA RUNTHEARDID LA: " + runThreadId);
                System.out.println("++THONG TIN CUA QUA TRINH RUNTHREADID: " + root);

                this.saveRunThreadId = runThreadId;
                System.out.println("++GIA TRI KHI LUU SAVERUNTHREADID LA: " + saveRunThreadId);
                Map<String, String> result = Collections.singletonMap("runthreadId", runThreadId);
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dữ liệu trống");
            }
        } catch (HttpStatusCodeException e) {
            // TODO: handle exception
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        } catch (JsonProcessingException e) {
            // TODO: handle exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi xử lý JSON");
        }
    }

    @GetMapping("/checkRunThreadId")
    public ResponseEntity<?> checkRunThreadId() {
        String url = "https://api.openai.com/v1/threads/" + this.saveThreadId + "/runs/" + this.saveRunThreadId;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        headers.set("OpenAI-Beta", "assistants=v1");

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.hasBody()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dữ liệu trống");
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @GetMapping("/showFeedback")
    public ResponseEntity<String> showFeedback() {
        String url = "https://api.openai.com/v1/threads/" + this.saveThreadId + "/messages";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        headers.set("OpenAI-Beta", "assistants=v1");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

}
