package com.uni.uni_erp.service.common;

import com.uni.uni_erp.domain.entity.Contact;
import com.uni.uni_erp.dto.ContactDTO;
import com.uni.uni_erp.repository.common.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final SupportRepository supportRepository;

    public void save(ContactDTO dto) {
        Contact contact = Contact.builder().name(dto.getName()).email(dto.getEmail()).tel(dto.getTel()).content(dto.getContent()).build();
        supportRepository.save(contact);
    }

}
