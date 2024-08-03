package com.major.majorproject.DTO;

import com.major.majorproject.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private int userId;
    private String userName;
    private String email;
    private String phone;
    private Role role;
}
