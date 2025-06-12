package com.ecomarket.inventarios.duoc.inventarios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.ecomarket.inventarios.duoc.inventarios.DTO.InventarioResponseDTO;
import com.ecomarket.inventarios.duoc.inventarios.DTO.ProductoDTO;
import com.ecomarket.inventarios.duoc.inventarios.DTO.TiendaDTO;
import com.ecomarket.inventarios.duoc.inventarios.model.Inventario;
import com.ecomarket.inventarios.duoc.inventarios.model.MovimientoInventario;
import com.ecomarket.inventarios.duoc.inventarios.repository.InventarioRepository;
import com.ecomarket.inventarios.duoc.inventarios.repository.MovimientoInventarioRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InventarioService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioService.class);

    @Autowired
    private InventarioRepository inventarioRepo;

    @Autowired
    private MovimientoInventarioRepository movimientoRepo;

    @Autowired
    private RestTemplate restTemplate;

    // Inyectar la API Key desde propiedades
    @Value("${external.api.key}")
    private String apiKey;

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
        Inventario inventario = inventarioRepo.findById(inventarioId).orElseThrow(() -> {
            logger.error("Inventario no encontrado con ID: {}", inventarioId);
            return new IllegalArgumentException("Inventario no encontrado");
        });

        if ("entrada".equalsIgnoreCase(movimiento.getTipo())) {
            inventario.setCantidad(inventario.getCantidad() + movimiento.getCantidad());
        } else if ("salida".equalsIgnoreCase(movimiento.getTipo())) {
            int nuevaCantidad = inventario.getCantidad() - movimiento.getCantidad();
            if (nuevaCantidad < 0) {
                logger.warn("No se puede realizar salida, cantidad insuficiente. Inventario ID: {}", inventarioId);
                throw new IllegalArgumentException("Cantidad insuficiente en inventario");
            }
            inventario.setCantidad(nuevaCantidad);
        } else {
            logger.warn("Tipo de movimiento inválido: {}", movimiento.getTipo());
            throw new IllegalArgumentException("Tipo de movimiento inválido");
        }

        inventarioRepo.save(inventario);
        movimiento.setInventario(inventario);
        return movimientoRepo.save(movimiento);
    }

    public InventarioResponseDTO getInventarioConDetalles(Long inventarioId) {
        Inventario inventario = inventarioRepo.findById(inventarioId).orElse(null);
        if (inventario == null) {
            logger.warn("Inventario no encontrado con ID: {}", inventarioId);
            return null;
        }

        ProductoDTO producto = getProductoById(inventario.getProductoId());
        TiendaDTO tienda = getTiendaById(inventario.getTiendaId());

        return new InventarioResponseDTO(
                inventario.getInventarioId(),
                inventario.getProductoId(),
                producto != null ? producto.getNombre() : "Desconocido",
                inventario.getTiendaId(),
                tienda != null ? tienda.getNombre() : "Desconocido",
                inventario.getCantidad(),
                inventario.getStockMinimo());
    }

    private HttpEntity<String> createHttpEntityWithApiKey() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        return new HttpEntity<>(headers);
    }

    public TiendaDTO getTiendaById(Long tiendaId) {
        String url = "http://localhost:8084/api/v1/tiendas/" + tiendaId;
        try {
            ResponseEntity<TiendaDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createHttpEntityWithApiKey(),
                    TiendaDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Error HTTP al obtener tienda con ID {}: {} - {}", tiendaId, e.getStatusCode(), e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            logger.error("Error inesperado al obtener tienda con ID {}: {}", tiendaId, e.getMessage());
            return null;
        }
    }

    public ProductoDTO getProductoById(Long productoId) {
        String url = "http://localhost:8081/api/v1/productos/" + productoId;
        try {
            ResponseEntity<ProductoDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createHttpEntityWithApiKey(),
                    ProductoDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Error HTTP al obtener producto con ID {}: {} - {}", productoId, e.getStatusCode(), e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            logger.error("Error inesperado al obtener producto con ID {}: {}", productoId, e.getMessage());
            return null;
        }
    }
}
