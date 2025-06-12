package com.ecomarket.inventarios.duoc.inventarios.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventarioResponseDTO {
    private Long inventarioId;
    private Long productoId;
    private String nombreProducto;
    private Long tiendaId;
    private String nombreTienda;
    private Integer cantidad;
    private Integer stockMinimo;
}

