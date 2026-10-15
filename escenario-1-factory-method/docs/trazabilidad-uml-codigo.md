# Verificacion de correspondencia UML codigo

El diagrama `factoryMethodE1.puml` es la referencia estructural del escenario 1. La implementacion
mantiene los participantes, relaciones, atributos y operaciones mostrados en el diagrama.

| Elemento UML | Implementacion | Verificacion |
|---|---|---|
| `DatosVehiculo` | Clase con `placa`, `marca`, `modelo`, `anioFabricacion` y `avaluo` privados | Reflexion en `CorrespondenciaUmlCodigoTest` |
| `Vehiculo` | Clase abstracta con `datos` protegido y `calcularMatricula(int)` abstracto | Reflexion y pruebas de calculo |
| `Auto`, `Camioneta`, `Camion` | Productos concretos que extienden `Vehiculo` | Reflexion y `CalculoMatriculaTest` |
| `RegistroVehicular` | Creador abstracto con factory method `crearVehiculo` y operacion concreta `calcularMatricula` | Reflexion y `FactoryMethodTest` |
| `RegistroAuto`, `RegistroCamioneta`, `RegistroCamion` | Creadores concretos que instancian el producto correspondiente | `FactoryMethodTest` |
| `Main` | Selecciona el creador en tiempo de ejecucion y trabaja mediante `RegistroVehicular` | Inspeccion de firma y ejecucion de ejemplo |

## Reglas de calculo implementadas

- Auto: 2 % del avaluo ajustado por antiguedad.
- Camioneta: 2,2 % del avaluo ajustado por antiguedad, mas USD 0,01 por kilogramo de carga.
- Camion: 2,5 % del avaluo ajustado por antiguedad, mas USD 25 por tonelada.
- El ajuste descuenta 2 % por cada anio de antiguedad, con un maximo de 30 %.
- Los resultados monetarios se redondean a dos decimales con `HALF_UP`.

Las reglas son decisiones de implementacion porque el enunciado describe los factores que debe
considerar cada tipo, pero no establece porcentajes o tarifas oficiales.

## Extensiones deliberadas

El UML omite detalles auxiliares para conservar claridad. El codigo agrega un constructor y getters
en `DatosVehiculo`, getters para las propiedades especificas, validacion de entrada y metodos
protegidos de calculo en `Vehiculo`. Estas extensiones no cambian los participantes ni las relaciones
del patron.

## Ejecucion de la verificacion

```text
mvn test
```

La prueba `CorrespondenciaUmlCodigoTest` tambien lee el archivo PUML y falla si falta cualquiera de
las diez clases representadas en el diagrama.
