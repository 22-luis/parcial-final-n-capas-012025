package com.uca.parcialfinalncapas.dto.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
