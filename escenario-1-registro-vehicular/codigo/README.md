# Manual de uso - Escenario 1 (Registro vehicular con Factory Method)

Este manual explica cómo compilar, probar y ejecutar el código del escenario 1, y cómo extenderlo con
un nuevo tipo de vehículo. Todos los comandos se lanzan desde esta carpeta (`escenario-1-registro-vehicular/codigo`).

## 1. Requisitos

| Herramienta | Versión | Para qué se usa |
|---|---|---|
| JDK | 21 o superior | Compilar y ejecutar |
| Maven | 3.9 o posterior | Ejecutar las pruebas con `mvn test` |

Comprobación:

```text
java -version
mvn -v
```

## 2. Estructura del código

```text
codigo/
├── pom.xml
└── src/
    ├── main/java/ec/edu/ups/pds/factorymethod/
    │   ├── Vehiculo.java              Product: clase abstracta con calcularMatricula
    │   ├── Auto.java                  Producto concreto
    │   ├── Camioneta.java             Producto concreto (capacidad de carga en kg)
    │   ├── Camion.java                Producto concreto (tonelaje)
    │   ├── DatosVehiculo.java         Datos comunes e inmutables del vehículo
    │   ├── RegistroVehicular.java     Creator: declara crearVehiculo
    │   ├── RegistroAuto.java          Creador concreto
    │   ├── RegistroCamioneta.java     Creador concreto
    │   ├── RegistroCamion.java        Creador concreto
    │   └── Main.java                  Punto de entrada; único lugar que elige el creador
    └── test/java/ec/edu/ups/pds/factorymethod/
        ├── CalculoMatriculaTest.java
        ├── FactoryMethodTest.java
        └── CorrespondenciaUmlCodigoTest.java
```

El diseño UML está en [`../uml`](../uml) y la correspondencia con el código en
[`../docs/trazabilidad-uml-codigo.md`](../docs/trazabilidad-uml-codigo.md).

## 3. Compilar y ejecutar

### Opción A: solo con el JDK (sin Maven)

```text
mkdir -p target/classes
javac -encoding UTF-8 -d target/classes $(find src/main -name '*.java')
java -cp target/classes ec.edu.ups.pds.factorymethod.Main auto ABC-1234 Toyota Corolla 2020 20000 0 2026
```

### Opción B: con Maven

```text
mvn clean compile
java -cp target/classes ec.edu.ups.pds.factorymethod.Main auto ABC-1234 Toyota Corolla 2020 20000 0 2026
```

> **Nota sobre Maven:** el `pom.xml` de este módulo declara un POM padre en `../../pom.xml`
> (`patrones-creacionales-actividad-01`). Ese archivo debe existir en la raíz del repositorio para que
> `mvn` funcione. Si no existe, usa la Opción A para compilar y ejecutar.

## 4. Uso del programa

### Sintaxis

```text
Main <tipo> [placa] [marca] [modelo] [anioFabricacion] [avaluo] [capacidad] [anioActual]
```

| Posición | Argumento | Obligatorio | Valor por defecto | Descripción |
|---|---|---|---|---|
| 0 | `tipo` | Sí | | `auto`, `camioneta` o `camion` (sin distinguir mayúsculas) |
| 1 | `placa` | No | `ABC-1234` | Texto no vacío |
| 2 | `marca` | No | `Marca demo` | Texto no vacío |
| 3 | `modelo` | No | `Modelo demo` | Texto no vacío |
| 4 | `anioFabricacion` | No | `2020` | Entero, no anterior a 1886 |
| 5 | `avaluo` | No | `20000.00` | Decimal, no negativo |
| 6 | `capacidad` | No | `1000` (camioneta), `8` (camión) | Kilogramos para camioneta, toneladas para camión; debe ser positivo. Lo ignora `auto` |
| 7 | `anioActual` | No | Año del sistema | Entero, no anterior al año de fabricación |

Los argumentos son posicionales: para indicar `anioActual` hay que escribir también los anteriores.
Sin argumentos, el programa muestra la ayuda.

### Ejemplos y resultados

```text
$ java -cp target/classes ec.edu.ups.pds.factorymethod.Main auto ABC-1234 Toyota Corolla 2020 20000 0 2026
Tipo: auto | Placa: ABC-1234 | Matricula: $352.00

$ java -cp target/classes ec.edu.ups.pds.factorymethod.Main camioneta ABC-1234 Toyota Hilux 2020 20000 1000 2026
Tipo: camioneta | Placa: ABC-1234 | Matricula: $397.20

$ java -cp target/classes ec.edu.ups.pds.factorymethod.Main camion ABC-1234 Hino Serie500 2020 20000 10 2026
Tipo: camion | Placa: ABC-1234 | Matricula: $690.00
```

### Errores frecuentes

| Entrada | Salida |
|---|---|
| `Main moto` | `Datos invalidos: Tipo no soportado: moto` y la ayuda |
| Año de fabricación `1800` | `Datos invalidos: El anio de fabricacion no puede ser anterior a 1886` |
| Sin argumentos | Solo la ayuda de uso |

## 5. Reglas de cálculo de la matrícula

- **Auto:** 2 % del avalúo ajustado por antigüedad.
- **Camioneta:** 2,2 % del avalúo ajustado por antigüedad, más USD 0,01 por kilogramo de carga.
- **Camión:** 2,5 % del avalúo ajustado por antigüedad, más USD 25 por tonelada.
- **Ajuste por antigüedad:** descuento de 2 % por cada año, con un máximo de 30 %.
- Los montos se redondean a dos decimales (`HALF_UP`).

Estas reglas son decisiones de implementación: el enunciado no fija porcentajes ni tarifas oficiales.

Ejemplo del auto: `20000 × 0,02 = 400`; antigüedad `2026 − 2020 = 6` años, descuento `12 %`;
`400 × 0,88 = 352,00`.

## 6. Pruebas

```text
mvn test
```

Resultado esperado al verificar este escenario: **14 pruebas, 0 fallos**.

| Clase de prueba | Qué comprueba |
|---|---|
| `CalculoMatriculaTest` | Reglas de cálculo y validaciones de entrada |
| `FactoryMethodTest` | Que cada creador concreto instancia el producto correcto y que el cálculo se hace mediante `Vehiculo` |
| `CorrespondenciaUmlCodigoTest` | Que el diagrama mejorado `../uml/factoryMethodE1_mejorado.puml` coincide con el código: clases, jerarquías, atributos, visibilidad, constructores, operaciones, getters de `DatosVehiculo`, ausencia de setters y composición `contiene` |

`CorrespondenciaUmlCodigoTest` lee los archivos `.puml` con rutas relativas (`../uml/...`), por lo que
las pruebas deben ejecutarse desde esta carpeta.

## 7. Cómo agregar un nuevo tipo de vehículo

Ejemplo con una motocicleta. No se modifica ninguna clase existente salvo `Main`.

1. Crear el producto `Motocicleta extends Vehiculo` e implementar `calcularMatricula(int anioActual)`.
2. Crear el creador `RegistroMotocicleta extends RegistroVehicular` e implementar
   `crearVehiculo(DatosVehiculo datos)`, devolviendo una `Motocicleta`. Si necesita un dato propio
   (por ejemplo la cilindrada), recíbelo en el constructor del creador, como hacen
   `RegistroCamioneta` y `RegistroCamion`.
3. Añadir la opción en `Main.seleccionarRegistro`: `case "motocicleta" -> new RegistroMotocicleta(...)`.
4. Actualizar los diagramas en `../uml` y agregar pruebas.

`RegistroVehicular.calcularMatricula` y el resto de clases no cambian, porque trabajan a través de
`Vehiculo` y `RegistroVehicular`.

## 8. Notas de diseño

- `RegistroCamioneta` y `RegistroCamion` guardan datos de un vehículo concreto: crea una instancia
  nueva por cada registro y no las compartas ni las reutilices.
- `DatosVehiculo` es inmutable (campos `final`, sin setters) y valida sus datos al construirse.
- El año actual se recibe como parámetro para que el cálculo sea reproducible en las pruebas.
