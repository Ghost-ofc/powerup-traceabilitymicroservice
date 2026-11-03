package com.pragma.powerup.infrastructure.out.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserClientDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String rolNombre;
}
