package com.uni.uni_erp.service;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.UserDTO;
import com.uni.uni_erp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public User login(UserDTO.JoinDTO dto) {
        return userRepository.findByEmailAndPassword(dto.getEmail(), dto.getPassword());
    }

}
