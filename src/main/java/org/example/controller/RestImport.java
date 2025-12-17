package org.example.controller;

import org.example.model.Adestrador;
import org.example.model.Pokemon;
import org.example.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para importar datos desde archivos JSON.
 * Permite subir archivos o importar desde rutas del sistema.
 */
@RestController
@RequestMapping(RestImport.MAPPING)
public class RestImport {

    public static final String MAPPING = "/mongodb/import";

    @Autowired
    private ImportService importService;

    // =====================================================
    // IMPORTAR SUBIENDO UN ARCHIVO (MultipartFile)
    // =====================================================

    /**
     * Importa adestradores desde un archivo JSON subido.
     * POST /mongodb/import/adestradores/upload
     *
     * @param file Archivo JSON con formato: [{"nome": "Ash", "idade": 10, "cidade": "Pueblo Paleta"}, ...]
     */
    @PostMapping("/adestradores/upload")
    public ResponseEntity<?> importarAdestradoresUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Validar que el archivo no esté vacío
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("El archivo está vacío");
            }

            // Validar que sea un archivo JSON
            String contentType = file.getContentType();
            if (contentType == null || !contentType.equals("application/json")) {
                return ResponseEntity.badRequest().body("El archivo debe ser JSON");
            }

            List<Adestrador> importados = importService.importarAdestradoresDesdeArchivo(file);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Adestradores importados correctamente");
            respuesta.put("cantidad", importados.size());
            respuesta.put("adestradores", importados);

            return ResponseEntity.ok(respuesta);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el archivo: " + e.getMessage());
        }
    }

    /**
     * Importa pokémon desde un archivo JSON subido.
     * POST /mongodb/import/pokemons/upload
     */
    @PostMapping("/pokemons/upload")
    public ResponseEntity<?> importarPokemonsUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("El archivo está vacío");
            }

            List<Pokemon> importados = importService.importarPokemonsDesdeArchivo(file);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Pokémon importados correctamente");
            respuesta.put("cantidad", importados.size());
            respuesta.put("pokemons", importados);

            return ResponseEntity.ok(respuesta);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el archivo: " + e.getMessage());
        }
    }

    // =====================================================
    // IMPORTAR DESDE RUTA DEL SISTEMA DE ARCHIVOS
    // =====================================================

    /**
     * Importa adestradores desde una ruta del sistema.
     * POST /mongodb/import/adestradores/ruta?path=/home/usuario/datos.json
     */
    @PostMapping("/adestradores/ruta")
    public ResponseEntity<?> importarAdestradoresDesdeRuta(@RequestParam("path") String rutaArchivo) {
        try {
            List<Adestrador> importados = importService.importarAdestradoresDesdeRuta(rutaArchivo);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Adestradores importados desde: " + rutaArchivo);
            respuesta.put("cantidad", importados.size());
            respuesta.put("adestradores", importados);

            return ResponseEntity.ok(respuesta);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al leer el archivo: " + e.getMessage());
        }
    }

    /**
     * Importa pokémon desde una ruta del sistema.
     * POST /mongodb/import/pokemons/ruta?path=/home/usuario/pokemons.json
     */
    @PostMapping("/pokemons/ruta")
    public ResponseEntity<?> importarPokemonsDesdeRuta(@RequestParam("path") String rutaArchivo) {
        try {
            List<Pokemon> importados = importService.importarPokemonsDesdeRuta(rutaArchivo);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Pokémon importados desde: " + rutaArchivo);
            respuesta.put("cantidad", importados.size());
            respuesta.put("pokemons", importados);

            return ResponseEntity.ok(respuesta);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al leer el archivo: " + e.getMessage());
        }
    }

    // =====================================================
    // IMPORTAR DESDE RESOURCES (dentro del proyecto)
    // =====================================================

    /**
     * Importa adestradores desde un archivo en la carpeta resources.
     * POST /mongodb/import/adestradores/resources?filename=adestradores.json
     */
    @PostMapping("/adestradores/resources")
    public ResponseEntity<?> importarAdestradoresDesdeResources(@RequestParam("filename") String nombreArchivo) {
        try {
            List<Adestrador> importados = importService.importarAdestradoresDesdeResources(nombreArchivo);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Adestradores importados desde resources: " + nombreArchivo);
            respuesta.put("cantidad", importados.size());
            respuesta.put("adestradores", importados);

            return ResponseEntity.ok(respuesta);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Importa pokémon desde un archivo en la carpeta resources.
     * POST /mongodb/import/pokemons/resources?filename=pokemons.json
     */
    @PostMapping("/pokemons/resources")
    public ResponseEntity<?> importarPokemonsDesdeResources(@RequestParam("filename") String nombreArchivo) {
        try {
            List<Pokemon> importados = importService.importarPokemonsDesdeResources(nombreArchivo);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Pokémon importados desde resources: " + nombreArchivo);
            respuesta.put("cantidad", importados.size());
            respuesta.put("pokemons", importados);

            return ResponseEntity.ok(respuesta);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }
}

