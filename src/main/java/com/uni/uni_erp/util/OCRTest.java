package com.uni.uni_erp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.uni_erp.util.ocrDTO.ApiResponse;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

public class OCRTest {

    private static final String API_URL = "https://vision.googleapis.com/v1/images:annotate?key=";

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Path imagePath = Paths.get("c://image/20241010_173941.jpg");

            byte[] imageBytes = Files.readAllBytes(imagePath);
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            String requestBody = "{ \"requests\": [{ \"image\": { \"content\": \"" + base64Image + "\" }, " +
                    "\"features\": [{ \"type\": \"TEXT_DETECTION\" }], " +
                    "\"imageContext\": { \"languageHints\": [\"ko\", \"en\"] } }] }";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<ApiResponse> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, ApiResponse.class);
            List<ApiResponse.TextAnnotation> list = response.getBody().getResponses().get(0).getTextAnnotations();
            System.out.println("리스트 길이 : " + list.size());
            for (int i = 1; i < list.size(); i++) {
                ApiResponse.TextAnnotation textAnnotation = list.get(i);
                String description = textAnnotation.getDescription();
                System.out.println("텍스트 : " + description);
                List<ApiResponse.Vertice> xy = textAnnotation.getBoundingPoly().getVertices();
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < xy.size(); j++) {
                    sb.append("(").append(xy.get(j).getX()).append(",").append(xy.get(j).getY()).append(")");
                }
                System.out.println(sb.toString());
            }
            // 7. API 응답 출력
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
