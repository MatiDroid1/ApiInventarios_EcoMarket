package com.ecomarket.inventarios.duoc.inventarios.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ecomarket.inventarios.duoc.inventarios.model.MovimientoInventario;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
}
