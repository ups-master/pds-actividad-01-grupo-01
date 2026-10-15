# Escenario 1 — Registro vehicular con Factory Method (Go)

Aplicación que registra vehículos (Auto, Camioneta, Camión) y calcula el valor de su matrícula. El tipo de vehículo se decide en tiempo de ejecución y su creación se delega a fábricas mediante el patrón **Factory Method**.

## Requisitos

- [Go](https://go.dev/dl/) 1.25 o superior

```bash
go version
```

## Ejecutar el programa

Desde la carpeta `escenario-1-factory-method-go/`:

```bash
go run .
```

Salida esperada:

```
auto       PBA-1234  matrícula: $151.20
camioneta  GSD-5678  matrícula: $906.25
camion     ABC-9012  matrícula: $840.00
```

Para generar un ejecutable:

```bash
go build -o matricula .
./matricula
```

## Ejecutar las pruebas

```bash
go test ./...          # resumen
go test ./... -v       # detalle de cada caso
```

`TestNuevaVarianteSinModificarElServicio` agrega una `Motocicleta` sin modificar `ServicioMatricula` y comprueba así el principio Abierto/Cerrado.

## Ver el diagrama UML

El diagrama está en `docs/factoryMethodE1.puml`. En VS Code, con la extensión **PlantUML** (`jebbs.plantuml`), Java y Graphviz instalados, abre el archivo y ejecuta `PlantUML: Preview Current Diagram` desde la paleta de comandos (`Ctrl+Shift+P`).

## Estructura

```
escenario-1-factory-method-go/
├── docs/factoryMethodE1.puml  diagrama UML
├── go.mod
├── main.go                 registra las fábricas y procesa solicitudes de ejemplo
└── matricula/
    ├── vehiculo.go         Vehiculo (Product) y VehiculoBase (datos comunes)
    ├── datos.go            DatosVehiculoDTO (información del cliente)
    ├── auto.go             Auto (ConcreteProduct) y AutoFactory (ConcreteCreator)
    ├── camioneta.go        Camioneta y CamionetaFactory
    ├── camion.go           Camion y CamionFactory
    ├── servicio.go         VehiculoFactory (Creator) y ServicioMatricula (cliente)
    └── matricula_test.go   pruebas
```

## Roles del patrón

| Rol GoF | Elemento en Go |
|---|---|
| Product | interfaz `Vehiculo` |
| ConcreteProduct | `Auto`, `Camioneta`, `Camion` (embeben `VehiculoBase` por composición) |
| Creator | interfaz `VehiculoFactory` con el Factory Method `CrearVehiculo` |
| ConcreteCreator | `AutoFactory`, `CamionetaFactory`, `CamionFactory` |
| Cliente | `ServicioMatricula`: elige la fábrica según `DatosVehiculoDTO.Tipo` |

## Agregar un nuevo tipo de vehículo

1. Crear `matricula/motocicleta.go` con el struct `Motocicleta` (embebiendo `VehiculoBase`) y su método `CalcularMatricula`.
2. Crear en el mismo archivo `MotocicletaFactory` con el método `CrearVehiculo`.
3. Registrarla en `main.go`:

   ```go
   servicio.Registrar("motocicleta", matricula.MotocicletaFactory{})
   ```

No hace falta modificar `ServicioMatricula` ni los vehículos existentes.
