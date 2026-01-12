package org.example.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.Adestrador;
import org.example.model.Pokemon;
import org.example.repository.AdestradorRepository;
import org.example.repository.PokemonRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class PokemonService {
    private final PokemonRepository pokemonRepo;
    private final AdestradorRepository adestradorRepo;

    public PokemonService(PokemonRepository pokemonRepo, AdestradorRepository adestradorRepo) {
        this.pokemonRepo = pokemonRepo;
        this.adestradorRepo = adestradorRepo;
    }

    public void crearPokemon(Pokemon pokemon) {
        pokemonRepo.save(pokemon);
    }

    public Pokemon buscarPokemon(String id) {
        return pokemonRepo.findById(id).orElse(null);
    }

    public List<Pokemon> buscarPokemons() {
        return pokemonRepo.findAll();
    }

    public Adestrador buscarAdestradorDePokemon(String idPokemon) {
        Pokemon pokemon = buscarPokemon(idPokemon);
        if (pokemon == null) return null;
        Adestrador adestrador = adestradorRepo.findById(String.valueOf(pokemon.getAdestradorId())).orElse(null);

        return adestrador;
    }

    public List <Pokemon> importarDesdeJSON(String ruta) throws IOException {
        try(Reader reader = new InputStreamReader(new ClassPathResource(ruta).getInputStream());){
            Type listType = new TypeToken<ArrayList<Pokemon>>() {}.getType();
            List<Pokemon> list_pokemon = new Gson().fromJson(reader, listType);

            return pokemonRepo.saveAll(list_pokemon);
        }
    }

}
