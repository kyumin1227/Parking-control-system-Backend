package com.example.parking_control_system.entity;

import com.example.parking_control_system.type.Role;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Member {
    @Id
    private String memberId;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String password;
    private String email;

}
