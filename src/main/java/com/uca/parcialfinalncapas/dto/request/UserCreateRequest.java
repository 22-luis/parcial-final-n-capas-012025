package com.uca.parcialfinalncapas.dto.request;

import com.uca.parcialfinalncapas.entities.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateRequest {
    private String nombre;
    private String correo;
    private String password;
    private Role nombreRol;
}
