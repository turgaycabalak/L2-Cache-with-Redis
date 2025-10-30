package com.cache.supercache.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record AddressResponse(
    Long id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LookupResponse addressType,
    String address
) {
}
