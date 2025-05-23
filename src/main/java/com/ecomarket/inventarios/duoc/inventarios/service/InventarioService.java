package com.ecomarket.inventarios.duoc.inventarios.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomarket.inventarios.duoc.inventarios.model.Inventario;
import com.ecomarket.inventarios.duoc.inventarios.model.MovimientoInventario;
import com.ecomarket.inventarios.duoc.inventarios.repository.InventarioRepository;
import com.ecomarket.inventarios.duoc.inventarios.repository.MovimientoInventarioRepository;

import java.util.List;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepo;

    @Autowired
    private MovimientoInventarioRepository movimientoRepo;

    public List<Inventario> getAllInventario() {
        return inventarioRepo.findAll();
    }

    public Inventario getInventarioById(Long id) {
        return inventarioRepo.findById(id).orElse(null);
    }

    public Inventario saveInventario(Inventario inventario) {
        return inventarioRepo.save(inventario);
    }

    public MovimientoInventario registrarMovimiento(Long inventarioId, MovimientoInventario movimiento) {
        Inventario inventario = inventarioRepo.findById(inventarioId).orElseThrow();

        if (movimiento.getTipo().equals("entrada")) {
            inventario.setCantidad(inventario.getCantidad() + movimiento.getCantidad());
        } else if (movimiento.getTipo().equals("salida")) {
            inventario.setCantidad(inventario.getCantidad() - movimiento.getCantidad());
        }

        inventarioRepo.save(inventario);
        movimiento.setInventario(inventario);
        return movimientoRepo.save(movimiento);
    }
}
