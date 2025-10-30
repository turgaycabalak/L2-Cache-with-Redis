package com.cache.supercache.controller;

import com.cache.supercache.dto.EmailResponse;
import com.cache.supercache.entity.EmailEntity;
import com.cache.supercache.repository.EmailRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class EmailController {
  private final EmailRepository emailRepository;


  @GetMapping("/{id}")
  public EmailResponse getEmail(@PathVariable Long id) {
    EmailEntity emailEntity = emailRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Email not found with id " + id));

    return EmailResponse.builder()
        .id(emailEntity.getId())
        .createdAt(emailEntity.getCreatedAt())
        .updatedAt(emailEntity.getUpdatedAt())
        .emailAddress(emailEntity.getEmailAddress())
        .description(emailEntity.getDescription())
        .build();
  }
}
