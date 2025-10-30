package com.cache.supercache.dto;

import lombok.Builder;

@Builder
public record LookupResponse(
    Long id,
    String name,
    String code
) {
}
