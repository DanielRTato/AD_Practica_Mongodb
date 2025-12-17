package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Adestrador;
import org.example.model.Pokemon;
import org.example.repository.AdestradorRepository;
import org.example.repository.PokemonRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Servicio para importar datos desde archivos JSON.
 * Permite cargar Adestradores y Pokémon desde ficheros externos.
 */
@Service
public class ImportService {

    private final AdestradorRepository adestradorRepository;
    private final PokemonRepository pokemonRepository;
    private final ObjectMapper objectMapper;

    public ImportService(AdestradorRepository adestradorRepository,
                         PokemonRepository pokemonRepository) {
        this.adestradorRepository = adestradorRepository;
        this.pokemonRepository = pokemonRepository;
        this.objectMapper = new ObjectMapper();
    }

    // =====================================================
    // IMPORTAR DESDE ARCHIVO SUBIDO (MultipartFile)
    // =====================================================

    /**
     * Importa una lista de Adestradores desde un archivo JSON subido.
     * @param file Archivo JSON con array de adestradores
     * @return Lista de adestradores importados
     */
    public List<Adestrador> importarAdestradoresDesdeArchivo(MultipartFile file) throws IOException {
        // Leer el JSON y convertirlo a lista de Adestrador
        List<Adestrador> adestradores = objectMapper.readValue(
                file.getInputStream(),
                new TypeReference<List<Adestrador>>() {}
        );

        // Guardar todos en la base de datos
        return adestradorRepository.saveAll(adestradores);
    }

    /**
     * Importa una lista de Pokémon desde un archivo JSON subido.
     * @param file Archivo JSON con array de pokémon
     * @return Lista de pokémon importados
     */
    public List<Pokemon> importarPokemonsDesdeArchivo(MultipartFile file) throws IOException {
        List<Pokemon> pokemons = objectMapper.readValue(
                file.getInputStream(),
                new TypeReference<List<Pokemon>>() {}
        );

        return pokemonRepository.saveAll(pokemons);
    }

    // =====================================================
    // IMPORTAR DESDE RUTA DE ARCHIVO LOCAL
    // =====================================================

    /**
     * Importa Adestradores desde un archivo JSON en el sistema de archivos.
     * @param rutaArchivo Ruta completa al archivo JSON
     * @return Lista de adestradores importados
     */
    public List<Adestrador> importarAdestradoresDesdeRuta(String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);

        List<Adestrador> adestradores = objectMapper.readValue(
                archivo,
                new TypeReference<List<Adestrador>>() {}
        );

        return adestradorRepository.saveAll(adestradores);
    }

    /**
     * Importa Pokémon desde un archivo JSON en el sistema de archivos.
     * @param rutaArchivo Ruta completa al archivo JSON
     * @return Lista de pokémon importados
     */
    public List<Pokemon> importarPokemonsDesdeRuta(String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);

        List<Pokemon> pokemons = objectMapper.readValue(
                archivo,
                new TypeReference<List<Pokemon>>() {}
        );

        return pokemonRepository.saveAll(pokemons);
    }

    // =====================================================
    // IMPORTAR DESDE RESOURCES (dentro del proyecto)
    // =====================================================

    /**
     * Importa Adestradores desde un archivo JSON en la carpeta resources.
     * @param nombreArchivo Nombre del archivo (ej: "adestradores.json")
     * @return Lista de adestradores importados
     */
    public List<Adestrador> importarAdestradoresDesdeResources(String nombreArchivo) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(nombreArchivo);

        if (inputStream == null) {
            throw new IOException("No se encontró el archivo: " + nombreArchivo + " en resources");
        }

        List<Adestrador> adestradores = objectMapper.readValue(
                inputStream,
                new TypeReference<List<Adestrador>>() {}
        );

        return adestradorRepository.saveAll(adestradores);
    }

    /**
     * Importa Pokémon desde un archivo JSON en la carpeta resources.
     * @param nombreArchivo Nombre del archivo (ej: "pokemons.json")
     * @return Lista de pokémon importados
     */
    public List<Pokemon> importarPokemonsDesdeResources(String nombreArchivo) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(nombreArchivo);

        if (inputStream == null) {
            throw new IOException("No se encontró el archivo: " + nombreArchivo + " en resources");
        }

        List<Pokemon> pokemons = objectMapper.readValue(
                inputStream,
                new TypeReference<List<Pokemon>>() {}
        );

        return pokemonRepository.saveAll(pokemons);
    }
}

