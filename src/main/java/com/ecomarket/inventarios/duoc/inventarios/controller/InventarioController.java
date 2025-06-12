package com.ecomarket.inventarios.duoc.inventarios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecomarket.inventarios.duoc.inventarios.DTO.InventarioResponseDTO;
import com.ecomarket.inventarios.duoc.inventarios.model.Inventario;
import com.ecomarket.inventarios.duoc.inventarios.model.MovimientoInventario;
import com.ecomarket.inventarios.duoc.inventarios.service.InventarioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public List<Inventario> getAllInventario() {
        return inventarioService.getAllInventario();
    }

    @GetMapping("/{id}")
    public Inventario getInventarioById(@PathVariable Long id) {
        return inventarioService.getInventarioById(id);
    }

    @PostMapping
    public Inventario crearInventario(@RequestBody Inventario inventario) {
        return inventarioService.saveInventario(inventario);
    }

    @PostMapping("/{id}/movimiento")
    public MovimientoInventario registrarMovimiento(@PathVariable Long id,
            @RequestBody MovimientoInventario movimiento) {
        return inventarioService.registrarMovimiento(id, movimiento);
    }

    @GetMapping("/detallado/{id}")
    public ResponseEntity<InventarioResponseDTO> getInventarioDetallado(@PathVariable Long id) {
        InventarioResponseDTO response = inventarioService.getInventarioConDetalles(id);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

}
