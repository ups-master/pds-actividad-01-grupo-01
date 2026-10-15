# Trazabilidad UML–código del escenario 2

El escenario 2 tiene dos versiones del diagrama de clases, ambas en la carpeta `uml/`. El código
vive en `codigo/`:

| Versión | Fuente | Imagen | Uso |
|---|---|---|---|
| Original | `uml/builderE2.puml` | `uml/diagrama-clases-original.png` | Histórico, previo a la mejora; ninguna prueba lo lee |
| Mejorada | `uml/builderE2_mejorado.puml` | `uml/diagrama-clases-mejorado.png` | Versión final y referencia de `CorrespondenciaUmlCodigoTest` |

La versión mejorada mantiene los mismos cinco participantes. Solo difiere de la original en la
relación Director–Builder (dependencia «usa por parámetro» en lugar de asociación «dirige»), en el
constructor de paquete de `ActivoFijo`, en la nota de validaciones y en la leyenda de elementos
omitidos. Se retiró además la dependencia `DirectorInventario ..> ActivoFijo : entrega`, que el
tipo de retorno de los métodos del Director ya expresa.

| Rol Builder | Elemento UML | Implementación Java | Verificación |
|---|---|---|---|
| Product | `ActivoFijo` | Clase final e inmutable con tres datos obligatorios y diez opcionales | `ActivoFijoBuilderTest`, `ActivoFijoTest` y reflexión |
| Builder | `ConstructorActivoFijo` | Interfaz con operaciones fluidas y `construir()` | `CorrespondenciaUmlCodigoTest` |
| ConcreteBuilder | `ActivoFijoBuilder` | Valida los datos, mantiene el estado parcial y crea instantáneas | Pruebas de validación e independencia |
| Director | `DirectorInventario` | Encapsula dos secuencias de construcción frecuentes | `DirectorInventarioTest` |
| Client | `Main` | Configura el builder y solicita el producto al director | `MainTest` |
| Product (constructor) | `~ActivoFijo(...)` (solo mejorado) | Constructor con visibilidad de paquete que recibe los 13 atributos | `CorrespondenciaUmlCodigoTest` |

## Reglas del escenario

- `codigoInstitucional`, `nombreDescripcion` y `precioAdquisicion` son obligatorios.
- El precio debe ser mayor que cero.
- Los textos opcionales, cuando se proporcionan, no pueden estar vacíos.
- La garantía no puede ser negativa.
- Cada llamada a `construir()` produce un producto inmutable independiente.
- Los datos opcionales ausentes se exponen mediante `Optional`.

La prueba `CorrespondenciaUmlCodigoTest` comprueba automáticamente los participantes, relaciones,
campos, modificadores, constructor obligatorio y firmas definidos en `uml/builderE2_mejorado.puml`.

## Extensiones deliberadas

Ambos UML omiten detalles auxiliares para conservar claridad. El código agrega los getters de
`ActivoFijo` (los opcionales devuelven `Optional`), `toString()` y las operaciones de configuración
repetidas en `ActivoFijoBuilder`. Estas extensiones no cambian los participantes ni las relaciones
del patrón; la leyenda del diagrama mejorado las declara como omitidas.

## Ejecución de la verificación

```text
# Desde escenario-2-registro-activos/codigo
mvn test
```

La prueba lee `../uml/builderE2_mejorado.puml`, por lo que debe ejecutarse desde `codigo/`.
