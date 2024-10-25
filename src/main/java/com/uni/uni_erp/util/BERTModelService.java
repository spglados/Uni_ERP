package com.uni.uni_erp.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BERTModelService {
    // Python 코드 호출을 통해 BERT 모델 실행 (예시)
    public static String getBERTPrediction(String inputText) {
        String prediction = "";
        try {
            // Python 스크립트를 호출하여 BERT 예측 수행
            String command = "python3 run_bert_model.py " + inputText;
            Process process = Runtime.getRuntime().exec(command);

            // Python 출력 결과를 읽어옴
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                prediction += line;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prediction;
    }
}