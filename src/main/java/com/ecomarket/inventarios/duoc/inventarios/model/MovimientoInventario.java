package com.ecomarket.inventarios.duoc.inventarios.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movimientoId;

    @ManyToOne
    @JoinColumn(name = "inventario_id", nullable = false)
    private Inventario inventario;

    @Column(nullable = false)
    private String tipo; // entrada, salida, ajuste

    @Column(nullable = false)
    private Integer cantidad;

    private LocalDateTime fecha = LocalDateTime.now();
    private String descripcion;
}

