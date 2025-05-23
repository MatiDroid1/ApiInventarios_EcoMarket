package com.ecomarket.inventarios.duoc.inventarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecomarket.inventarios.duoc.inventarios.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
}
