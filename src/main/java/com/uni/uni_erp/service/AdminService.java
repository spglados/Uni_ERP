package com.uni.uni_erp.service;

import com.uni.uni_erp.domain.entity.Admin;
import com.uni.uni_erp.dto.AdminDTO;
import com.uni.uni_erp.repository.AdminRepository;
import com.uni.uni_erp.util.Str.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminDTO login(AdminDTO.LoginDTO dto) {
        try {
            String storedSaltedHash = adminRepository.findPasswordByUsername(dto.getUsername());
            if (PasswordUtil.verify(dto.getPassword(), storedSaltedHash)) {
                Admin admin = adminRepository.findByUsername(dto.getUsername());
                return AdminDTO.builder().username(admin.getUsername()).password(admin.getPassword()).name(admin.getName()).tel(admin.getTel()).email(admin.getEmail()).build();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
