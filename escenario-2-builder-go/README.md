# Escenario 2 — Registro de activos fijos con Builder (Go)

Módulo que registra activos fijos (computadores, monitores, muebles, etc.). Cada activo exige tres datos obligatorios y admite cualquier combinación de datos opcionales. El patrón **Builder** permite construirlo paso a paso, de forma legible y sin constructores con muchos parámetros.

## Requisitos

- [Go](https://go.dev/dl/) 1.25 o superior

```bash
go version
```

## Ejecutar el programa

Desde la carpeta `escenario-2-builder-go/`:

```bash
go run .
```

Salida esperada:

```
[AF001] Computador portátil - $1200.00
  Marca:             Dell
  Modelo:            Latitude 5440
  Número de serie:   SN-48213
  Fecha adquisición: 2026-10-12
  Garantía:          36 meses
  Proveedor:         Tecnología Andina
  Ficha técnica:
    - memoria RAM: 16 GB
    - procesador: Intel Core i7

[AF002] Mesa de reuniones - $350.00
  Ubicación:         Sala de juntas, Cuenca
  Custodio:          Ana Pérez
  Ficha técnica:
    - dimensiones: 200 x 90 x 75 cm
    - material: Madera de roble

[AF003] Monitor 27 pulgadas - $280.00
  Marca:             LG
  Modelo:            27GP850
  Ficha técnica:
    - resolución: 2560 x 1440
  Observaciones:     Pantalla con un píxel muerto

No se pudo registrar el activo:
el código institucional es obligatorio
el precio de adquisición debe ser mayor a cero
```

El último activo falla a propósito: se aplica la receta de computador, pero le faltan datos obligatorios y `Build()` lo rechaza.

Para generar un ejecutable:

```bash
go build -o activos-fijos .
./activos-fijos
```

## Ejecutar las pruebas

```bash
go test ./...          # resumen
go test ./... -v       # detalle de cada caso
```

`TestDirectorSoloConoceLaInterfaz` usa un builder espía (una implementación falsa de `ConstructorActivoFijo`) y comprueba que el Director funciona con cualquier implementación de la interfaz, sin depender de `ActivoFijoBuilder`.

## Ver el diagrama UML

El diagrama está en `docs/builderE2.puml`. En VS Code, con la extensión **PlantUML** (`jebbs.plantuml`), Java y Graphviz instalados, abre el archivo y ejecuta `PlantUML: Preview Current Diagram` desde la paleta de comandos (`Ctrl+Shift+P`).

## Estructura

```
escenario-2-builder-go/
├── docs/builderE2.puml     diagrama UML
├── go.mod
├── main.go                 construye activos de ejemplo a través del Director
└── activos/
    ├── activo_fijo.go      ActivoFijo (Product)
    ├── builder.go          ConstructorActivoFijo (Builder) y ActivoFijoBuilder (ConcreteBuilder)
    ├── director.go         DirectorInventario (Director)
    └── activos_test.go     pruebas
```

## Roles del patrón

| Rol GoF | Elemento en Go |
|---|---|
| Product | `ActivoFijo`: campos privados y solo getters (inmutable una vez construido) |
| Builder | interfaz `ConstructorActivoFijo`: pasos `ConXxx()` y `Build()` |
| ConcreteBuilder | `ActivoFijoBuilder`: sabe **cómo** ejecutar cada paso y valida en `Build()` |
| Director | `DirectorInventario`: sabe **qué** pasos aplicar para un computador, una mesa o un monitor |
| Cliente | `main` |

## Cómo construir un activo

Toda construcción pasa por una receta del Director:

1. Crear el builder con los datos obligatorios.
2. Aplicar la receta del Director según el tipo de activo.
3. Opcionalmente, agregar pasos extra.
4. Obtener el activo con `Build()`.

```go
director := activos.DirectorInventario{}

b := activos.NuevoActivoFijoBuilder("AF001", "Computador portátil", 1200)
director.ConstruirComputador(b, "Dell", "Latitude 5440", "SN-48213", "Intel Core i7", 36)
computador, err := b.ConObservaciones("Asignado a TI").Build()
```

Para registrar un tipo de activo nuevo, se agrega una receta en `DirectorInventario` (por ejemplo `ConstruirImpresora`).

Los datos propios de cada tipo de activo (procesador, material, dimensiones…) se guardan en la **ficha técnica** con `ConCaracteristica(nombre, valor)`. Un activo nuevo, como una impresora, no requiere modificar `ActivoFijo`.
