package com.vaguinhasdev.jobs.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface SpringDataJobRepository extends JpaRepository<JpaJobEntity, UUID> {
}
