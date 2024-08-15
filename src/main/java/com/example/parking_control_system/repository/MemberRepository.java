package com.example.parking_control_system.repository;

import com.example.parking_control_system.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
