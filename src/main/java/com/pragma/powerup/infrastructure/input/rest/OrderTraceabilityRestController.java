package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.OrderTraceabilityRequestDto;
import com.pragma.powerup.application.dto.response.OrderTraceabilityResponseDto;
import com.pragma.powerup.application.handler.IOrderTraceabilityHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/traceability")
@RequiredArgsConstructor
@Tag(name = "Trazabilidad de Pedidos", description = "Endpoints para registro y consulta del historial de cambios de estado de pedidos en MongoDB")
public class OrderTraceabilityRestController {

    private final IOrderTraceabilityHandler traceabilityHandler;

    @Operation(summary = "Registrar cambio de estado de un pedido",
            description = "Crea un registro de auditoría/trazabilidad en MongoDB para cada cambio de estado de un pedido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Log de trazabilidad registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderTraceabilityResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o campos obligatorios faltantes", content = @Content)
    })
    @PostMapping
    public ResponseEntity<OrderTraceabilityResponseDto> createLog(@Valid @RequestBody OrderTraceabilityRequestDto requestDto) {
        OrderTraceabilityResponseDto response = traceabilityHandler.createLog(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Consultar historial de logs por ID de pedido",
            description = "Permite consultar los cambios de estado de un pedido. Cada cliente solo puede consultar los de sus propios pedidos.",
            security = {@SecurityRequirement(name = "bearer-jwt")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de logs obtenido exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderTraceabilityResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "No autenticado o token JWT inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: el pedido no pertenece al cliente autenticado", content = @Content)
    })
    @GetMapping(value = {"/order/{orderId}", "/{orderId}"})
    @PreAuthorize("hasAnyRole('CLIENTE', 'CUSTOMER', 'ADMIN', 'EMPLEADO', 'EMPLOYEE')")
    public ResponseEntity<List<OrderTraceabilityResponseDto>> getLogsByOrderId(@PathVariable("orderId") Long orderId) {
        List<OrderTraceabilityResponseDto> logs = traceabilityHandler.getLogsByOrderId(orderId);
        return ResponseEntity.ok(logs);
    }

    @Operation(summary = "Consultar historial de pedidos del cliente autenticado",
            description = "Permite a un Cliente obtener todo el historial de trazabilidad de sus propios pedidos.",
            security = {@SecurityRequirement(name = "bearer-jwt")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de logs del cliente obtenido exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderTraceabilityResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "No autenticado o token JWT inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: solo clientes pueden consultar su propio historial", content = @Content)
    })
    @GetMapping("/customer")
    @PreAuthorize("hasAnyRole('CLIENTE', 'CUSTOMER')")
    public ResponseEntity<List<OrderTraceabilityResponseDto>> getLogsForCurrentCustomer() {
        List<OrderTraceabilityResponseDto> logs = traceabilityHandler.getLogsForCurrentCustomer();
        return ResponseEntity.ok(logs);
    }
}
