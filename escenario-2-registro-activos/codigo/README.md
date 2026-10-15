# Manual de uso - Escenario 2 (Registro de activos fijos con Builder)

Este manual explica cómo compilar, probar y ejecutar el código del escenario 2. Todos los comandos
se lanzan desde esta carpeta (`escenario-2-registro-activos/codigo`).

## 1. Requisitos

| Herramienta | Versión | Para qué se usa |
|---|---|---|
| JDK | 21 o superior | Compilar y ejecutar |
| Maven | 3.9 o posterior | Ejecutar las pruebas con `mvn test` |

## 2. Estructura del código

```text
codigo/
├── pom.xml
└── src/
    ├── main/java/ec/edu/ups/pds/builder/
    │   ├── ActivoFijo.java             Product: activo inmutable
    │   ├── ConstructorActivoFijo.java  Builder: operaciones fluidas y construir()
    │   ├── ActivoFijoBuilder.java      ConcreteBuilder: valida y arma el activo
    │   ├── DirectorInventario.java     Director: secuencias de construcción reutilizables
    │   └── Main.java                   Client: ejemplo de uso
    └── test/java/ec/edu/ups/pds/builder/
        ├── ActivoFijoBuilderTest.java
        ├── DirectorInventarioTest.java
        └── CorrespondenciaUmlCodigoTest.java
```

El diseño UML está en [`../uml`](../uml) y la correspondencia con el código en
[`../docs/trazabilidad-uml-codigo.md`](../docs/trazabilidad-uml-codigo.md).

## 3. Compilar y ejecutar

### Opción A: solo con el JDK (sin Maven)

```text
mkdir -p target/classes
javac -encoding UTF-8 -d target/classes $(find src/main -name '*.java')
java -cp target/classes ec.edu.ups.pds.builder.Main
```

### Opción B: con Maven

```text
mvn clean compile
java -cp target/classes ec.edu.ups.pds.builder.Main
```

El `pom.xml` de este módulo hereda del POM padre de la raíz del repositorio (`../../pom.xml`), que fija
Java 21 y la versión de JUnit. Desde la raíz, `mvn test` compila y prueba los dos escenarios.

## 4. Datos del activo

- **Obligatorios:** código institucional, nombre o descripción y precio de adquisición (se pasan al
  constructor de `ActivoFijoBuilder`).
- **Opcionales:** marca, modelo, número de serie, ubicación, custodio, fecha de adquisición, garantía,
  proveedor, características técnicas y observaciones (operaciones `con...` del builder).

Reglas: el precio debe ser mayor que cero; los textos opcionales no pueden estar vacíos; la garantía
no puede ser negativa; cada `construir()` produce un producto inmutable independiente; los datos
ausentes se exponen con `Optional`.

## 5. Pruebas

```text
mvn test
```

También puede lanzarse desde la raíz del repositorio, donde ejecuta los dos escenarios.

Resultado esperado al verificar este escenario: **14 pruebas, 0 fallos**.

| Clase de prueba | Qué comprueba |
|---|---|
| `ActivoFijoBuilderTest` | Validaciones, datos opcionales e independencia entre productos construidos |
| `DirectorInventarioTest` | Las secuencias de construcción del director |
| `CorrespondenciaUmlCodigoTest` | Que el diagrama mejorado `../uml/builderE2_mejorado.puml` coincide con el código: participantes, relaciones, campos, modificadores, constructor, firmas y visibilidad de paquete del constructor de `ActivoFijo` |

`CorrespondenciaUmlCodigoTest` lee el `.puml` con la ruta relativa `../uml/builderE2_mejorado.puml`, por lo que
las pruebas deben ejecutarse desde esta carpeta.

## 6. Cómo agregar un dato opcional o una secuencia nueva

- **Dato opcional:** añadir el campo en `ActivoFijo`, la operación `con...` en `ConstructorActivoFijo` y
  `ActivoFijoBuilder`, y reflejarlo en `../uml/builderE2_mejorado.puml`.
- **Secuencia reutilizable:** añadir un método en `DirectorInventario` que reciba un
  `ConstructorActivoFijo` y encadene las operaciones necesarias.
