package com.cache.supercache.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record CustomerResponse(
    Long id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String firstName,
    String lastName,
    EmailResponse email,
    List<AddressResponse> addresses,
    List<LookupResponse> interests
) {
}
