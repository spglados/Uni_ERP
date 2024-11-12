package com.uni.uni_erp.service.common;

import com.uni.uni_erp.domain.entity.common.Contact;
import com.uni.uni_erp.dto.common.ContactDTO;
import com.uni.uni_erp.repository.common.SupportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final SupportRepository supportRepository;

    @Transactional
    public void save(ContactDTO dto) {
        Contact contact = Contact.builder().id(dto.getId()).userId(dto.getUserId()).title(dto.getTitle()).content(dto.getContent()).build();
        supportRepository.save(contact);
    }

}
