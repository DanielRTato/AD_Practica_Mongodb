package org.example.controller;

import org.example.model.Adestrador;
import org.example.service.AdestradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RestAdestrador.MAPPING)
public class RestAdestrador {

    public static final String MAPPING = "/mongodb/adestrador";

    @Autowired
    private AdestradorService adestradorService;

    @PostMapping("gardar")
    public ResponseEntity<Adestrador> gardar(@RequestBody Adestrador adestrador) {
        adestradorService.crearAdestrador(adestrador);
        return ResponseEntity.ok(adestrador);
    }

    @GetMapping("/listarTodos")
    public ResponseEntity<List<Adestrador>> listarColeccion() {
        List<Adestrador> adestradors = adestradorService.buscarAdestradores();
        return ResponseEntity.ok(adestradors);
    }



}
