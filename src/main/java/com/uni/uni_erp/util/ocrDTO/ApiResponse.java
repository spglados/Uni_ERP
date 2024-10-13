package com.uni.uni_erp.util.ocrDTO;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse {
    private List<Responses> responses;

    @Data
    public static class Responses {
        private List<TextAnnotation> textAnnotations;
    }

    @Data
    public static class TextAnnotation {
        private String description;
        private BoundingPoly boundingPoly;
    }

    @Data
    public static class BoundingPoly {
        private List<Vertice> vertices;
    }

    @Data
    public static class Vertice {
        int x;
        int y;
    }
}