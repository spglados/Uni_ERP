package com.uni.uni_erp.service.admin;

import com.uni.uni_erp.domain.entity.admin.Admin;
import com.uni.uni_erp.dto.admin.AdminDTO;
import com.uni.uni_erp.repository.admin.AdminRepository;
import com.uni.uni_erp.util.str.PasswordUtil;
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
                return AdminDTO.builder().name(admin.getName()).username(admin.getUsername()).build();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
