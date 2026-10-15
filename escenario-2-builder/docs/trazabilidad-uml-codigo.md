# Trazabilidad UML–código del escenario 2

| Rol Builder | Elemento UML | Implementación Java | Verificación |
|---|---|---|---|
| Product | `ActivoFijo` | Clase final e inmutable con tres datos obligatorios y diez opcionales | `ActivoFijoBuilderTest` y reflexión |
| Builder | `ConstructorActivoFijo` | Interfaz con operaciones fluidas y `construir()` | `CorrespondenciaUmlCodigoTest` |
| ConcreteBuilder | `ActivoFijoBuilder` | Valida los datos, mantiene el estado parcial y crea instantáneas | Pruebas de validación e independencia |
| Director | `DirectorInventario` | Encapsula dos secuencias de construcción frecuentes | `DirectorInventarioTest` |
| Client | `Main` | Configura el builder y solicita el producto al director | Compilación y ejecución del ejemplo |

## Reglas del escenario

- `codigoInstitucional`, `nombreDescripcion` y `precioAdquisicion` son obligatorios.
- El precio debe ser mayor que cero.
- Los textos opcionales, cuando se proporcionan, no pueden estar vacíos.
- La garantía no puede ser negativa.
- Cada llamada a `construir()` produce un producto inmutable independiente.
- Los datos opcionales ausentes se exponen mediante `Optional`.

La prueba `CorrespondenciaUmlCodigoTest` comprueba automáticamente los participantes, relaciones,
campos, modificadores, constructor obligatorio y firmas definidos en `builderE2.puml`.
