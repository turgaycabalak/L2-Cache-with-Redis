package com.cache.supercache.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record EmailResponse(
    Long id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String emailAddress,
    String description
) {
}
