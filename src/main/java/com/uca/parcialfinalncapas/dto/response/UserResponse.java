package com.uca.parcialfinalncapas.dto.response;

import com.uca.parcialfinalncapas.entities.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long idUsuario;
    private String nombre;
    private String correo;
    private Role nombreRol;
}
