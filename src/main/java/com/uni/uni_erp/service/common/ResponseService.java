package com.uni.uni_erp.service.common;

import com.uni.uni_erp.domain.entity.Response;
import com.uni.uni_erp.dto.ResponseDTO;
import com.uni.uni_erp.repository.common.ResponseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository responseRepository;

    @Transactional
    public void save(ResponseDTO responseDTO) {
        Response response = Response.builder()
                .author (responseDTO.getAuthor())
                .contactId(responseDTO.getContactId())
                .content(responseDTO.getContent())
                .build();
        responseRepository.save(response);
    }

    public ResponseDTO findByContactId(Integer contactId) {
        Response response = responseRepository.findByContactId(contactId);
        if (response == null) {
            return null;
        }
        return ResponseDTO.builder().author(response.getAuthor()).contactId(response.getContactId()).content(response.getContent()).build();
    }
}
