package com.ecomarket.inventarios.duoc.inventarios.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventario", uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "tienda_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventarioId;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;
    @Column(name = "tienda_id", nullable = false)
    private Long tiendaId;
    @Column(name = "cantidad", nullable = true)
    private Integer cantidad;
    @Column(name = "stockminimo", nullable = false)
    private Integer stockMinimo;
}