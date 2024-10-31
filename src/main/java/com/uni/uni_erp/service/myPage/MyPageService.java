package com.uni.uni_erp.service.myPage;

import com.uni.uni_erp.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyPageService {

    @Autowired
    private UserRepository userRepository;

    public void updateUserEmailByUserId(String email, int userId) {
        userRepository.updateEmailByUserId(email, userId); // 레포지토리 메서드 호출
    }
}