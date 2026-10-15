# Escenario 2 - Registro de activos fijos con Builder

Implementación Maven con Java 21 del patrón Builder para construir activos fijos progresivamente,
sin recurrir a un constructor extenso.

## Participantes

- `ActivoFijo`: producto final inmutable.
- `ConstructorActivoFijo`: interfaz Builder.
- `ActivoFijoBuilder`: Concrete Builder; exige los datos obligatorios y configura los opcionales.
- `DirectorInventario`: ejecuta secuencias reutilizables de construcción.
- `Main`: cliente de ejemplo.

## Datos del activo

Los datos obligatorios son código institucional, nombre o descripción y precio de adquisición.
Los datos opcionales son marca, modelo, número de serie, ubicación, custodio, fecha de adquisición,
garantía, proveedor, características técnicas y observaciones.

## Compilar y probar

Desde este módulo:

```text
mvn clean test
```

Desde la raíz del proyecto:

```text
mvn -pl escenario-2-builder test
```

## Ejecutar el ejemplo

```text
java -cp target/classes ec.edu.utpl.pds.builder.Main
```

El diseño está en `builderE2.puml` y la matriz detallada se encuentra en
`docs/trazabilidad-uml-codigo.md`.
