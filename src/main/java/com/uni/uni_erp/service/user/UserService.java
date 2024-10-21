package com.uni.uni_erp.service.user;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.UserDTO;
import com.uni.uni_erp.exception.errors.Exception404;
import com.uni.uni_erp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String fromPhoneNumber;

    public void save(User user) {

        userRepository.save(user);
    }

    public User login(UserDTO.JoinDTO dto) {
        return userRepository.findByEmailAndPassword(dto.getEmail(), dto.getPassword());
    }

    public User findById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception404("회원정보를 찾을 수 없습니다"));

        return user;
    }

    public boolean checkDuplicateEmail(String email) {
        boolean isUse = userRepository.existsByEmail(email);
        return isUse;
    }

    public boolean checkDuplicatePhone(String phone) {
        boolean isUse = userRepository.existsByPhone(phone);
        return isUse;
    }

    public void certifiedPhoneNumber(String userPhoneNumber, int randomNumber) {
        Message coolsms = new Message(apiKey, apiSecret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", userPhoneNumber);    // 수신전화번호
        params.put("from", fromPhoneNumber);    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "[TEST] 인증번호는" + "[" + randomNumber + "]" + "입니다."); // 문자 내용 입력
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }

    }
}
