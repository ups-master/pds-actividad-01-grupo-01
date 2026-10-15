# Verificacion de correspondencia UML codigo

El escenario 1 tiene dos versiones del diagrama de clases, ambas en la carpeta `uml/`. El codigo
vive en `codigo/`:

| Version | Fuente | Imagen | Uso |
|---|---|---|---|
| Original | `uml/factoryMethodE1.puml` | `uml/diagrama-clases-original.png` | Historico, previo a la mejora; ninguna prueba lo lee |
| Mejorada | `uml/factoryMethodE1_mejorado.puml` | `uml/diagrama-clases-mejorado.png` | Version final y referencia de `CorrespondenciaUmlCodigoTest` |

La version mejorada es la que refleja el codigo completo. Solo difiere de la original en
`DatosVehiculo` (constructor y consultas) y en la relacion `Vehiculo`-`DatosVehiculo`. La
implementacion mantiene los participantes, relaciones, atributos y operaciones mostrados en ambos
diagramas.

| Elemento UML | Implementacion | Verificacion |
|---|---|---|
| `DatosVehiculo` | Clase con `placa`, `marca`, `modelo`, `anioFabricacion` y `avaluo` privados y finales | Reflexion en `CorrespondenciaUmlCodigoTest` |
| `DatosVehiculo(...)` y `getPlaca()`, `getMarca()`, `getModelo()`, `getAnioFabricacion()`, `getAvaluo()` (solo mejorado) | Constructor publico de cinco parametros y una consulta por atributo, sin setters | Uso en `CalculoMatriculaTest` y `FactoryMethodTest` |
| `Vehiculo "1" *-- "1" DatosVehiculo : contiene` (solo mejorado) | Campo `datos` obligatorio en `Vehiculo`, inicializado en el constructor | Inspeccion de codigo |
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

Ambos UML omiten detalles auxiliares para conservar claridad. El codigo agrega `getDatos()` en
`Vehiculo`, getters para las propiedades especificas de cada tipo, validacion de entrada y metodos
protegidos de calculo en `Vehiculo`. Estas extensiones no cambian los participantes ni las
relaciones del patron. El constructor y los getters de `DatosVehiculo` ya no son una extension: la
version mejorada del diagrama los incluye.

## Decision sobre la composicion

La version mejorada modela `DatosVehiculo` como parte de `Vehiculo` (composicion 1 a 1) porque los
datos son inmutables y describen a un unico vehiculo. En el codigo, `Main` crea los datos antes que
el vehiculo y `RegistroVehicular.calcularMatricula` crea un `Vehiculo` nuevo en cada llamada con la
misma instancia de `DatosVehiculo`. Por eso la composicion expresa la relacion conceptual y no
una restriccion de ciclo de vida que el codigo imponga.

## Ejecucion de la verificacion

```text
# Desde escenario-1-registro-vehicular/codigo
mvn test
```

La prueba `CorrespondenciaUmlCodigoTest` verifica el codigo contra el diagrama mejorado
`../uml/factoryMethodE1_mejorado.puml`: falla si falta cualquiera de las diez clases, y comprueba
que `DatosVehiculo` tenga el constructor y los cinco getters del UML, ningun setter y la composicion
`Vehiculo "1" *-- "1" DatosVehiculo : contiene`. Resultado de la ultima ejecucion: 14 pruebas, 0 fallos.
