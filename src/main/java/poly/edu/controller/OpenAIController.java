package poly.edu.controller;

import java.nio.charset.Charset;

import javax.print.attribute.standard.Media;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public String callAPI() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        headers.set("OpenAI-Beta", "assistants=v1");

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<String> response = restTemplate.exchange(OPENAI_API_URL_THREADS, HttpMethod.POST, entity,
                String.class);

        ObjectMapper mapper = new ObjectMapper();
        String threadId = "";
        try {
            JsonNode root = mapper.readTree(response.getBody());
            threadId = root.path("id").asText();
            System.out.println("MA ID CUA THREAD: " + root);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.saveThreadId = threadId;
        return threadId;
    }

    @PostMapping("/sendMessageApi")
    public String sendMessageApi() {
        String url = "https://api.openai.com/v1/threads/" + this.saveThreadId + "/messages";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        headers.set("OpenAI-Beta", "assistants=v1");

        String jsonbody = "{\"role\": \"user\", \"content\": \"Cho tôi giá tiền của iphone 9?\", \"file_ids\": [\"file-PQeqheySy2be7PYRIuVVgQNQ\"]}";

        HttpEntity<String> entity = new HttpEntity<>(jsonbody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    @PostMapping("/runThread")
    public String runThred(){
        String url = "https://api.openai.com/v1/threads/" + this.saveThreadId + "/runs";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        headers.set("OpenAI-Beta", "assistants=v1");

        String jsonbody = "{\"assistant_id\": \"asst_XxVYkcs7AGlf2FIelzXB76o8\", \"instructions\": \"Bạn là một bot tư vấn sản phẩm, Hãy đề xuất cho khách hàng những món hàng dựa trên API và trả lời những câu hỏi của họ\"}";

        HttpEntity<String> entity = new HttpEntity<>(jsonbody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        String runThreadId = "";
        try {
            JsonNode root = mapper.readTree(response.getBody());
            runThreadId = root.path("id").asText();
            System.out.println("MA CUA RUNTHEARDID LA: " + runThreadId);
            System.out.println("THONG TIN CUA QUA TRINH RUNTHREADID: " + root);
        } catch (Exception e) {
            // TODO: handle exception
        }

        this.saveRunThreadId = runThreadId;
        return response.getBody();
    }

    @GetMapping("/checkRunThreadId")
    public String checkRunThreadId(){
        String url = "https://api.openai.com/v1/threads/" + this.saveThreadId + "/runs/" + this.saveRunThreadId;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        headers.set("OpenAI-Beta", "assistants=v1");

        HttpEntity<String> entity = new HttpEntity<>("",headers);
        
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

    @GetMapping("/showFeedback")
    public String showFeedback(){
        String url = "https://api.openai.com/v1/threads/" + this.saveThreadId + "/messages";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        headers.set("OpenAI-Beta", "assistants=v1");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}
