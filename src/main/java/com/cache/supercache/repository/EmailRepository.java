package com.cache.supercache.repository;

import com.cache.supercache.entity.EmailEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
}
