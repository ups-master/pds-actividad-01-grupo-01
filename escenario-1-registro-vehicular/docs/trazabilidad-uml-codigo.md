# Trazabilidad UML–código del escenario 1

El escenario 1 tiene dos versiones del diagrama de clases, ambas en la carpeta `uml/`. El código
vive en `codigo/`:

| Versión | Fuente | Imagen | Uso |
|---|---|---|---|
| Original | `uml/factoryMethodE1.puml` | `uml/diagrama-clases-original.png` | Histórico, previo a la mejora; ninguna prueba lo lee |
| Mejorada | `uml/factoryMethodE1_mejorado.puml` | `uml/diagrama-clases-mejorado.png` | Versión final y referencia de `CorrespondenciaUmlCodigoTest` |

La versión mejorada es la que refleja el código completo. Solo difiere de la original en
`DatosVehiculo` (constructor y consultas) y en la relación `Vehiculo`-`DatosVehiculo`. La
implementación mantiene los participantes, relaciones, atributos y operaciones mostrados en ambos
diagramas.

| Elemento UML | Implementación | Verificación |
|---|---|---|
| `DatosVehiculo` | Clase con `placa`, `marca`, `modelo`, `anioFabricacion` y `avaluo` privados y finales | Reflexión en `CorrespondenciaUmlCodigoTest` |
| `DatosVehiculo(...)` y `getPlaca()`, `getMarca()`, `getModelo()`, `getAnioFabricacion()`, `getAvaluo()` (solo mejorado) | Constructor público de cinco parámetros y una consulta por atributo, sin setters | Uso en `CalculoMatriculaTest` y `FactoryMethodTest` |
| `Vehiculo "1" *-- "1" DatosVehiculo : contiene` (solo mejorado) | Campo `datos` obligatorio en `Vehiculo`, inicializado en el constructor | Inspección de código |
| `Vehiculo` | Clase abstracta con `datos` protegido y `calcularMatricula(int)` abstracto | Reflexión y pruebas de cálculo |
| `Auto`, `Camioneta`, `Camion` | Productos concretos que extienden `Vehiculo` | Reflexión y `CalculoMatriculaTest` |
| `RegistroVehicular` | Creador abstracto con factory method `crearVehiculo` y operación concreta `calcularMatricula` | Reflexión y `FactoryMethodTest` |
| `RegistroAuto`, `RegistroCamioneta`, `RegistroCamion` | Creadores concretos que instancian el producto correspondiente | `FactoryMethodTest` |
| `Main` | Selecciona el creador en tiempo de ejecución y trabaja mediante `RegistroVehicular` | Inspección de firma y ejecución de ejemplo |

## Reglas de cálculo implementadas

- Auto: 2 % del avalúo ajustado por antigüedad.
- Camioneta: 2,2 % del avalúo ajustado por antigüedad, más USD 0,01 por kilogramo de carga.
- Camión: 2,5 % del avalúo ajustado por antigüedad, más USD 25 por tonelada.
- El ajuste descuenta 2 % por cada año de antigüedad, con un máximo de 30 %.
- Los resultados monetarios se redondean a dos decimales con `HALF_UP`.

Las reglas son decisiones de implementación porque el enunciado describe los factores que debe
considerar cada tipo, pero no establece porcentajes o tarifas oficiales.

## Extensiones deliberadas

Ambos UML omiten detalles auxiliares para conservar claridad. El código agrega `getDatos()` en
`Vehiculo`, getters para las propiedades específicas de cada tipo, validación de entrada y métodos
protegidos de cálculo en `Vehiculo`. Estas extensiones no cambian los participantes ni las
relaciones del patrón. El constructor y los getters de `DatosVehiculo` ya no son una extensión: la
versión mejorada del diagrama los incluye.

## Decisión sobre la composición

La versión mejorada modela `DatosVehiculo` como parte de `Vehiculo` (composición 1 a 1) porque los
datos son inmutables y describen a un único vehículo. En el código, `Main` crea los datos antes que
el vehículo y `RegistroVehicular.calcularMatricula` crea un `Vehiculo` nuevo en cada llamada con la
misma instancia de `DatosVehiculo`. Por eso la composición expresa la relación conceptual y no
una restricción de ciclo de vida que el código imponga.

## Ejecución de la verificación

```text
# Desde escenario-1-registro-vehicular/codigo
mvn test
```

La prueba `CorrespondenciaUmlCodigoTest` verifica el código contra el diagrama mejorado
`../uml/factoryMethodE1_mejorado.puml`: falla si falta cualquiera de las diez clases, y comprueba
que `DatosVehiculo` tenga el constructor y los cinco getters del UML, ningún setter y la composición
`Vehiculo "1" *-- "1" DatosVehiculo : contiene`. Resultado de la última ejecución: 14 pruebas, 0 fallos.
