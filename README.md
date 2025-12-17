# 🎮 API REST de Pokémon con Spring Boot y MongoDB

## 📚 Guía Completa para Alumnos de 2º DAM

Este proyecto es una **API REST** que gestiona **Adestradores** (entrenadores) y **Pokémon** usando **Spring Boot** como framework backend y **MongoDB** como base de datos NoSQL.

---

## 📁 Estructura del Proyecto

```
AD_Practica1_Mongodb/
├── pom.xml                          # Configuración de Maven y dependencias
├── src/
│   └── main/
│       ├── java/org/example/
│       │   ├── Main.java            # Punto de entrada de la aplicación
│       │   ├── config/              # Configuraciones
│       │   │   ├── MongoConfig.java
│       │   │   └── OpenApiConfig.java
│       │   ├── controller/          # Controladores REST (endpoints)
│       │   │   ├── RestAdestrador.java
│       │   │   └── RestPokemon.java
│       │   ├── model/               # Modelos/Entidades
│       │   │   ├── Adestrador.java
│       │   │   └── Pokemon.java
│       │   ├── repository/          # Repositorios (acceso a BD)
│       │   │   ├── AdestradorRepository.java
│       │   │   └── PokemonRepository.java
│       │   └── service/             # Servicios (lógica de negocio)
│       │       ├── AdestradorService.java
│       │       └── PokemonService.java
│       └── resources/
│           └── application.properties  # Configuración de la aplicación
```

---

## 🏗️ Arquitectura por Capas

Spring Boot sigue una arquitectura de **3 capas**. Es importante entender cada una:

```
┌─────────────────────────────────────────────────────────────┐
│                    CLIENTE (Postman, Navegador)             │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  CONTROLLER (RestAdestrador, RestPokemon)                   │
│  - Recibe peticiones HTTP (GET, POST, PUT, DELETE)          │
│  - Valida datos de entrada                                  │
│  - Devuelve respuestas HTTP                                 │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  SERVICE (AdestradorService, PokemonService)                │
│  - Contiene la lógica de negocio                            │
│  - Procesa los datos                                        │
│  - Coordina operaciones complejas                           │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  REPOSITORY (AdestradorRepository, PokemonRepository)       │
│  - Acceso directo a la base de datos                        │
│  - Operaciones CRUD automáticas                             │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    MONGODB (Base de Datos)                  │
└─────────────────────────────────────────────────────────────┘
```

---

## 📝 Explicación de Cada Archivo

### 1️⃣ Main.java - Punto de Entrada

```java
@SpringBootApplication
@ComponentScan({"org.example"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
```

**¿Qué hace?**
- `@SpringBootApplication`: Anotación que combina 3 anotaciones:
  - `@Configuration`: Indica que es una clase de configuración
  - `@EnableAutoConfiguration`: Configura automáticamente Spring Boot
  - `@ComponentScan`: Busca componentes (@Service, @Controller, @Repository) en el paquete
- `SpringApplication.run()`: Arranca la aplicación Spring Boot

---

### 2️⃣ Model - Entidades (Adestrador.java y Pokemon.java)

Los **modelos** representan las tablas/colecciones en la base de datos.

#### Adestrador.java
```java
@Document(collection = "adestradores")  // Nombre de la colección en MongoDB
public class Adestrador {
    private String id;      // MongoDB genera automáticamente el _id
    private String nome;
    private int idade;
    private String cidade;
    
    // Getters y Setters...
}
```

#### Pokemon.java
```java
@Document(collection = "pokemons")
public class Pokemon {
    @Id
    private String id;
    private String nome;
    private List<String> tipo;        // Ej: ["Fuego", "Volador"]
    private int nivel;
    private List<String> habilidades; // Ej: ["Lanzallamas", "Vuelo"]
    private Adestrador adestradorId;  // Referencia al entrenador
    
    // Getters y Setters...
}
```

**Anotaciones importantes:**
- `@Document(collection = "nombre")`: Define el nombre de la colección en MongoDB
- `@Id`: Marca el campo como identificador único

---

### 3️⃣ Repository - Acceso a Base de Datos

Los repositorios heredan de `MongoRepository` y proporcionan operaciones CRUD automáticas.

```java
public interface AdestradorRepository extends MongoRepository<Adestrador, String> {
    // ¡No necesitas escribir código! Spring genera todo automáticamente
}
```

**Métodos disponibles automáticamente:**
| Método | Descripción |
|--------|-------------|
| `save(entidad)` | Guarda o actualiza una entidad |
| `findById(id)` | Busca por ID |
| `findAll()` | Obtiene todos los registros |
| `deleteById(id)` | Elimina por ID |
| `count()` | Cuenta registros |
| `existsById(id)` | Comprueba si existe |

---

### 4️⃣ Service - Lógica de Negocio

Los servicios contienen la **lógica de negocio** y actúan como intermediarios.

```java
@Service  // Indica que es un servicio de Spring
public class AdestradorService {

    private final AdestradorRepository adestradorRepo;

    // Inyección de dependencias por constructor (recomendado)
    public AdestradorService(AdestradorRepository adestradorRepo) {
        this.adestradorRepo = adestradorRepo;
    }

    public void crearAdestrador(Adestrador adestrador) {
        adestradorRepo.save(adestrador);  // Guarda en MongoDB
    }

    public Adestrador buscarAdestrador(String id) {
        return adestradorRepo.findById(id).orElse(null);
    }

    public List<Adestrador> buscarAdestradores() {
        return adestradorRepo.findAll();
    }
}
```

**¿Por qué usar @Service?**
- Spring detecta esta clase y la convierte en un **Bean** (objeto gestionado por Spring)
- Permite usar **inyección de dependencias**
- Separa la lógica de negocio del controlador

---

### 5️⃣ Controller - Endpoints REST

Los controladores definen los **endpoints** (URLs) de la API.

```java
@RestController                              // Indica que devuelve JSON
@RequestMapping(RestAdestrador.MAPPING)      // URL base: /mongodb/adestrador
public class RestAdestrador {

    public static final String MAPPING = "/mongodb/adestrador";

    @Autowired  // Inyección de dependencias
    private AdestradorService adestradorService;

    // POST /mongodb/adestrador/gardar
    @PostMapping("gardar")
    public ResponseEntity<Adestrador> gardar(@RequestBody Adestrador adestrador) {
        adestradorService.crearAdestrador(adestrador);
        return ResponseEntity.ok(adestrador);
    }

    // GET /mongodb/adestrador/listarTodos
    @GetMapping("/listarTodos")
    public ResponseEntity<List<Adestrador>> listarColeccion() {
        List<Adestrador> adestradors = adestradorService.buscarAdestradores();
        return ResponseEntity.ok(adestradors);
    }
}
```

**Anotaciones HTTP:**
| Anotación | Método HTTP | Uso |
|-----------|-------------|-----|
| `@GetMapping` | GET | Obtener datos |
| `@PostMapping` | POST | Crear nuevos datos |
| `@PutMapping` | PUT | Actualizar datos |
| `@DeleteMapping` | DELETE | Eliminar datos |

**Otras anotaciones:**
- `@RequestBody`: El cuerpo de la petición se convierte a objeto Java
- `@PathVariable`: Obtiene variables de la URL (ej: `/pokemon/{id}`)
- `@RequestParam`: Obtiene parámetros de query (ej: `?nombre=Pikachu`)

---

### 6️⃣ application.properties - Configuración

```properties
# Nombre de la aplicación
spring.application.nome=MongonatorProfeReferenciado

# Conexión a MongoDB (IP:puerto/nombre_base_datos)
spring.data.mongodb.uri=mongodb://10.0.9.100/probas

# Swagger UI habilitado
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui/index.html

# Puerto del servidor
server.port=8080
```

---

## 🚀 Cómo Usar la API

### Arrancar la aplicación
```bash
mvn spring-boot:run
```

### Acceder a Swagger UI (documentación interactiva)
```
http://localhost:8080/swagger-ui/index.html
```

---

## 📮 Endpoints Disponibles

### Adestradores

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/mongodb/adestrador/gardar` | Crear un adestrador |
| GET | `/mongodb/adestrador/listarTodos` | Listar todos los adestradores |

### Pokémon

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/mongodb/pokemon/guardar` | Crear un pokémon |
| GET | `/mongodb/pokemon/listarTodos` | Listar todos los pokémon |
| GET | `/mongodb/pokemon/getAdestradorDePokemon/{id}` | Obtener el adestrador de un pokémon |

### 📥 Importación desde JSON

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/mongodb/import/adestradores/upload` | Importar adestradores subiendo archivo JSON |
| POST | `/mongodb/import/pokemons/upload` | Importar pokémon subiendo archivo JSON |
| POST | `/mongodb/import/adestradores/ruta?path=` | Importar adestradores desde ruta del sistema |
| POST | `/mongodb/import/pokemons/ruta?path=` | Importar pokémon desde ruta del sistema |
| POST | `/mongodb/import/adestradores/resources?filename=` | Importar adestradores desde resources |
| POST | `/mongodb/import/pokemons/resources?filename=` | Importar pokémon desde resources |

---

## 📬 Ejemplos con cURL o Postman

### Crear un Adestrador (POST)

**URL:** `POST http://localhost:8080/mongodb/adestrador/gardar`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
    "nome": "Ash Ketchum",
    "idade": 10,
    "cidade": "Pueblo Paleta"
}
```

**Respuesta esperada:**
```json
{
    "id": "6761234567890abcdef12345",
    "nome": "Ash Ketchum",
    "idade": 10,
    "cidade": "Pueblo Paleta"
}
```

---

### Listar Todos los Adestradores (GET)

**URL:** `GET http://localhost:8080/mongodb/adestrador/listarTodos`

**Respuesta:**
```json
[
    {
        "id": "6761234567890abcdef12345",
        "nome": "Ash Ketchum",
        "idade": 10,
        "cidade": "Pueblo Paleta"
    },
    {
        "id": "6761234567890abcdef12346",
        "nome": "Misty",
        "idade": 12,
        "cidade": "Ciudad Celeste"
    }
]
```

---

### Crear un Pokémon (POST)

**URL:** `POST http://localhost:8080/mongodb/pokemon/guardar`

**Body:**
```json
{
    "nome": "Pikachu",
    "tipo": ["Eléctrico"],
    "nivel": 25,
    "habilidades": ["Impactrueno", "Rayo", "Ataque Rápido"],
    "adestradorId": {
        "id": "6761234567890abcdef12345"
    }
}
```

---

## 📥 Importación de Datos desde JSON

El proyecto incluye funcionalidad para **importar datos masivamente** desde archivos JSON. Hay 3 formas de hacerlo:

### 🔹 Opción 1: Subir un archivo JSON (Upload)

Ideal para subir archivos desde Postman o un formulario web.

**URL:** `POST http://localhost:8080/mongodb/import/adestradores/upload`

**En Postman:**
1. Selecciona método `POST`
2. Ve a la pestaña `Body`
3. Selecciona `form-data`
4. Añade una key llamada `file` de tipo `File`
5. Selecciona tu archivo JSON

**Con cURL:**
```bash
curl -X POST http://localhost:8080/mongodb/import/adestradores/upload \
  -F "file=@/home/dam/mis_adestradores.json"
```

---

### 🔹 Opción 2: Importar desde ruta del sistema

Útil cuando el archivo ya está en el servidor.

**URL:** `POST http://localhost:8080/mongodb/import/adestradores/ruta?path=/home/dam/datos.json`

**Con cURL:**
```bash
curl -X POST "http://localhost:8080/mongodb/import/adestradores/ruta?path=/home/dam/adestradores.json"
```

---

### 🔹 Opción 3: Importar desde carpeta resources

El proyecto incluye archivos de ejemplo en `src/main/resources/`:
- `adestradores.json` - 4 entrenadores de ejemplo
- `pokemons.json` - 5 pokémon de ejemplo

**URL:** `POST http://localhost:8080/mongodb/import/adestradores/resources?filename=adestradores.json`

**Con cURL:**
```bash
# Importar adestradores de ejemplo
curl -X POST "http://localhost:8080/mongodb/import/adestradores/resources?filename=adestradores.json"

# Importar pokémon de ejemplo
curl -X POST "http://localhost:8080/mongodb/import/pokemons/resources?filename=pokemons.json"
```

---

### 📄 Formato del archivo JSON

#### Para Adestradores (`adestradores.json`):
```json
[
    {
        "nome": "Ash Ketchum",
        "idade": 10,
        "cidade": "Pueblo Paleta"
    },
    {
        "nome": "Misty",
        "idade": 12,
        "cidade": "Ciudad Celeste"
    }
]
```

#### Para Pokémon (`pokemons.json`):
```json
[
    {
        "nome": "Pikachu",
        "tipo": ["Eléctrico"],
        "nivel": 25,
        "habilidades": ["Impactrueno", "Rayo", "Ataque Rápido"]
    },
    {
        "nome": "Charizard",
        "tipo": ["Fuego", "Volador"],
        "nivel": 50,
        "habilidades": ["Lanzallamas", "Vuelo", "Garra Dragón"]
    }
]
```

---

### 📝 Respuesta de la importación

Cuando importas datos, recibes una respuesta como esta:

```json
{
    "mensaje": "Adestradores importados correctamente",
    "cantidad": 4,
    "adestradores": [
        {
            "id": "6761234567890abcdef12345",
            "nome": "Ash Ketchum",
            "idade": 10,
            "cidade": "Pueblo Paleta"
        },
        ...
    ]
}
```

---

### 🔍 Cómo funciona el ImportService

```java
@Service
public class ImportService {

    // ObjectMapper es de Jackson - convierte JSON ↔ Objetos Java
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Método para importar desde archivo subido
    public List<Adestrador> importarAdestradoresDesdeArchivo(MultipartFile file) throws IOException {
        // 1. Lee el JSON del archivo y lo convierte a Lista de Adestrador
        List<Adestrador> adestradores = objectMapper.readValue(
                file.getInputStream(),
                new TypeReference<List<Adestrador>>() {}
        );
        
        // 2. Guarda todos en MongoDB de una vez
        return adestradorRepository.saveAll(adestradores);
    }
}
```

**Clases clave:**
- `ObjectMapper`: Convierte JSON a objetos Java y viceversa (de la librería Jackson)
- `TypeReference<List<T>>`: Indica el tipo de dato esperado (lista de objetos)
- `MultipartFile`: Representa un archivo subido en una petición HTTP

---

## 🔧 Anotaciones Importantes de Spring

| Anotación | Descripción |
|-----------|-------------|
| `@SpringBootApplication` | Marca la clase principal de Spring Boot |
| `@RestController` | Controlador REST que devuelve JSON |
| `@Service` | Marca una clase como servicio (lógica de negocio) |
| `@Repository` | Marca una interfaz como repositorio (acceso a BD) |
| `@Autowired` | Inyecta automáticamente una dependencia |
| `@RequestMapping` | Define la URL base de un controlador |
| `@GetMapping` | Endpoint para peticiones GET |
| `@PostMapping` | Endpoint para peticiones POST |
| `@RequestBody` | Convierte el JSON del body a objeto Java |
| `@PathVariable` | Obtiene variables de la URL |
| `@Document` | Marca una clase como documento de MongoDB |
| `@Id` | Marca el campo como identificador único |

---

## 🔄 Flujo de una Petición

Cuando haces una petición POST para crear un adestrador:

```
1. Cliente envía: POST /mongodb/adestrador/gardar con JSON en el body
                              │
                              ▼
2. RestAdestrador.gardar() recibe la petición
   - @RequestBody convierte JSON → objeto Adestrador
                              │
                              ▼
3. AdestradorService.crearAdestrador(adestrador)
   - Aquí iría validación, lógica de negocio, etc.
                              │
                              ▼
4. AdestradorRepository.save(adestrador)
   - Spring Data MongoDB guarda en la BD
                              │
                              ▼
5. MongoDB almacena el documento en la colección "adestradores"
                              │
                              ▼
6. Se devuelve ResponseEntity.ok(adestrador) → JSON al cliente
```

---

## ⚠️ Solución al Error Común

Si ves este error:
```
No qualifying bean of type 'org.example.service.AdestradorService' available
```

**Significa que:**
1. Falta la anotación `@Service` en la clase del servicio
2. El servicio no está en un paquete que Spring escanea
3. Necesitas recompilar el proyecto

**Solución:**
```bash
mvn clean compile
```

---

## 📚 Recursos para Aprender Más

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb)
- [Baeldung - Spring Boot](https://www.baeldung.com/spring-boot)

---

## 🎯 Ejercicios Propuestos

1. **Añadir endpoint DELETE** para eliminar un adestrador por ID
2. **Añadir endpoint PUT** para actualizar un pokémon
3. **Crear un endpoint** que liste todos los pokémon de un adestrador
4. **Añadir validaciones** (ej: el nombre no puede estar vacío)

---

*Creado para alumnos de 2º DAM - Acceso a Datos y Programación de Servicios y Procesos*
