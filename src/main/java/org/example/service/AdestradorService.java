package org.example.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.Adestrador;
import org.example.model.Pokemon;
import org.example.repository.AdestradorRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
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

    public List <Adestrador> importarDesdeJSON(String ruta) throws IOException {
        try(Reader reader = new InputStreamReader(new ClassPathResource(ruta).getInputStream());){
            Type listType = new TypeToken<ArrayList<Adestrador>>() {}.getType();
            List<Adestrador> list_adestrador = new Gson().fromJson(reader, listType);

            return adestradorRepo.saveAll(list_adestrador);
        }
    }
}
