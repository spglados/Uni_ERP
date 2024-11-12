package com.uni.uni_erp.service.common;

import com.uni.uni_erp.domain.entity.common.Contact;
import com.uni.uni_erp.dto.common.ContactDTO;
import com.uni.uni_erp.repository.common.ContactRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public Page<ContactDTO> getContacts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending()); // Sort by an existing field
        Page<Contact> contact = contactRepository.findAllByStatus(pageable);
        return contact.map(this::convertToDTO);
    }

    private ContactDTO convertToDTO(Contact contact) {
        ContactDTO dto = new ContactDTO();
        dto.setId(contact.getId());
        dto.setUserId(contact.getUserId());
        dto.setTitle(contact.getTitle());
        dto.setContent(contact.getContent());
        return dto;
    }

    public ContactDTO findById(Integer contactId) {
        Optional<Contact> contact = contactRepository.findById(contactId);
        return ContactDTO.builder().id(contact.get().getId()).userId(contact.get().getUserId()).title(contact.get().getTitle()).content(contact.get().getContent()).build();
    }

    @Transactional
    public void updateContactStatusById(Integer contactId) {
        Optional<Contact> contact = contactRepository.findById(contactId);
        contact.get().setStatus(Contact.ContactStatus.CLOSED);
        contactRepository.save(contact.get());
    }

    public List<ContactDTO> findByUserId(Integer userId) {
        List<Contact> contacts = contactRepository.findByUserId(userId);
        return contacts.stream()
                .map(contact -> ContactDTO.builder()
                        .id(contact.getId())
                        .userId(contact.getUserId())
                        .title(contact.getTitle())
                        .content(contact.getContent())
                        .status(contact.getStatus())
                        .status(contact.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
