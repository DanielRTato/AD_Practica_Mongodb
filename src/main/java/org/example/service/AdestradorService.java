package org.example.service;

import org.example.model.Adestrador;
import org.example.repository.AdestradorRepository;

import java.util.List;

public class AdestradorService {

    private final AdestradorRepository adestradorRepo;

    public AdestradorService(AdestradorRepository adestradorRepo) {
        this.adestradorRepo = adestradorRepo;
    }

    public void crearAdestrador(Adestrador adestrador) {
        adestradorRepo.save(adestrador);
    }

    public Adestrador buscarAdestrador(String id) {
        return adestradorRepo.findById(id).orElse(null);
    }

    public List<Adestrador> buscarAdestradores() {
        return adestradorRepo.findAll();
    }
}
