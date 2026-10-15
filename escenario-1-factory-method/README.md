# Escenario 1 - Registro vehicular con Factory Method

Proyecto Maven con Java 21 para el escenario 1 de la actividad de patrones creacionales. La
creacion de cada vehiculo se delega a un creador concreto; la operacion principal depende de los
contratos `RegistroVehicular` y `Vehiculo`, no de una clase concreta.

## Requisitos

- JDK 21
- Maven 3.9 o posterior

## Compilar y probar

```text
mvn clean test

# Desde la raíz del repositorio:
mvn -pl escenario-1-factory-method test
```

Las pruebas cubren las reglas de matricula, las fabricas concretas, las validaciones y la
correspondencia entre `factoryMethodE1.puml` y el codigo Java.

## Ejecutar el ejemplo

Tras compilar:

```text
java -cp target/classes ec.edu.ups.pds.factorymethod.Main auto ABC-1234 Toyota Corolla 2020 20000 0 2026
java -cp target/classes ec.edu.ups.pds.factorymethod.Main camioneta ABC-1234 Toyota Hilux 2020 20000 1000 2026
java -cp target/classes ec.edu.ups.pds.factorymethod.Main camion ABC-1234 Hino Serie500 2020 20000 10 2026
```

La documentacion detallada de las reglas y de la trazabilidad se encuentra en
`docs/trazabilidad-uml-codigo.md`.
