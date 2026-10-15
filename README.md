# Actividad 01 - Patrones creacionales

**UNIVERSIDAD POLITÉCNICA SALESIANA - ECUADOR · Posgrados | Maestría en Software**

## Datos generales de la actividad

| | |
|---|---|
| **Asignatura** | Patrones de Diseño de Software |
| **Actividad de aprendizaje #** | 01 - Patrones creacionales |
| **Docente** | Mauricio Ortiz Ochoa |
| **Fecha** | 2026/10/15 |
| **Grupo base** | Grupo 01 |
| **Resultado de aprendizaje** | RA1. Selecciona y aplica patrones creacionales para resolver problemas de creación de objetos, revisando críticamente las alternativas elaboradas con apoyo de inteligencia artificial. |

### Integrantes

| N.º | Integrante (Apellidos Nombres) |
|---|---|
| 1 | Aliatis Ramirez Enzo Paolo |
| 2 | Cadme Vázquez Mauro Emiliano |
| 3 | Murillo Valarezo Jordan Fernando |
| 4 | Suárez Pesantez Galo Fabian |

### Entregables

- Archivo en PDF con el nombre `PDS_Actividad_01_Grupo01.pdf`.
- Aplicación de software almacenada en un repositorio de código (este repositorio).
- La actividad se desarrolla en grupo; la carga al AVAC es individual.

### Tareas específicas

1. Participar en la clase de la Unidad 01, secciones 01, 02 y 03.
2. Leer detenidamente el material de apoyo relacionado.
3. Crear un repositorio de código para almacenar la aplicación solicitada.
4. Seguir las instrucciones del ejercicio.

### Requerimientos generales para ambos escenarios

El enunciado completo de los dos escenarios y la plantilla original de la actividad están en [`docs/enunciado-actividad-01.md`](docs/enunciado-actividad-01.md).

Para cada escenario, el grupo debe:

1. Identificar el problema de creación de objetos.
2. Seleccionar y justificar el patrón creacional correspondiente.
3. Elaborar el diagrama de clases UML.
4. Identificar los participantes del patrón.
5. Implementar la solución en un lenguaje de programación.
6. Almacenar el código en el repositorio del grupo.
7. Documentar el uso crítico de IA: prompt principal, una recomendación aceptada, una recomendación modificada o rechazada, y la justificación de ambas decisiones.

Además, el informe incluye unas conclusiones de la actividad (200 palabras), que están en la sección [Conclusiones de la actividad](#conclusiones-de-la-actividad) de este archivo.

### Rúbrica de evaluación

Cada criterio se califica de 0 (no presenta) a 3 (nivel alto):

- Selección, justificación y diagrama UML de Factory Method.
- Implementación funcional y correspondencia UML-código de Factory Method.
- Selección, justificación y diagrama UML de Builder.
- Implementación funcional y correspondencia UML-código de Builder.
- Uso crítico de IA, justificación y conclusiones.

Material de apoyo: `Unidad_01_01_PDS.pdf`, `Unidad_01_02_PDS.pdf` y `Unidad_01_03_PDS.pdf`.

## Escenarios

| Escenario | Problema | Patrón | Informe | Diagrama final |
|---|---|---|---|---|
| 1. Registro vehicular | Crear el tipo de vehículo (Auto, Camioneta, Camión) elegido en tiempo de ejecución y calcular su matrícula sin depender de clases concretas | Factory Method | [`escenario-1-registro-vehicular`](escenario-1-registro-vehicular/README.md) | [`uml/diagrama-clases-mejorado.png`](escenario-1-registro-vehicular/uml/diagrama-clases-mejorado.png) |
| 2. Activos fijos | Construir paso a paso activos con tres datos obligatorios y diez opcionales | Builder | [`escenario-2-registro-activos`](escenario-2-registro-activos/README.md) | [`uml/diagrama-clases-mejorado.png`](escenario-2-registro-activos/uml/diagrama-clases-mejorado.png) |

Cada informe contiene el problema de creación de objetos, la justificación, los participantes del
patrón, los diagramas, el enlace al código y el uso crítico de IA.

## Probar el proyecto

Requisitos: JDK 21 o superior y Maven 3.9 o posterior.

```text
mvn test
```

Desde la raíz ejecuta los dos escenarios y debe dar 14 pruebas por escenario, sin fallos. Cada
escenario también puede probarse y ejecutarse por separado desde su carpeta `codigo/`; las
instrucciones están en su manual de uso:

- [Manual del escenario 1](escenario-1-registro-vehicular/codigo/README.md)
- [Manual del escenario 2](escenario-2-registro-activos/codigo/README.md)

## Conclusiones de la actividad

Factory Method encajó en el escenario 1 porque el tipo de vehículo solo se conoce en ejecución. Los creadores concretos instancian cada vehículo y la operación principal usa únicamente `RegistroVehicular` y `Vehiculo`, así que agregar una motocicleta pide una subclase, su creador y una opción en `Main`, sin modificar lo demás. En el escenario 2, Builder nos permitió armar activos fijos paso a paso: se parte de los tres datos obligatorios y se suman solo los opcionales que apliquen, sin sobrecargas ni valores vacíos. El producto es inmutable y el Director reúne dos secuencias que se repiten.

En ambos escenarios una prueba compara el código con el diagrama mejorado, y las 14 pruebas de cada uno pasan al ejecutar `mvn test` desde la raíz del repositorio.

Usamos la IA para revisar los diseños y contrastamos cada recomendación con el UML y el código. En el escenario 1 incorporamos el constructor y las consultas de `DatosVehiculo` y rechazamos Strategy, porque el cálculo depende solo del tipo de vehículo. En el escenario 2 mantuvimos y documentamos las validaciones que ya existían, y descartamos eliminar el Director y la interfaz, porque ya hay dos secuencias reutilizables.

## Estructura del repositorio

```text
.
├── README.md                              Este archivo
├── pom.xml                                POM padre: Java 21 y JUnit para los dos módulos
├── docs/
│   └── enunciado-actividad-01.md          Enunciado y plantilla original de la actividad
├── escenario-1-registro-vehicular/
│   ├── README.md                          Informe del escenario 1
│   ├── uml/                               Diagramas PlantUML e imágenes
│   ├── codigo/                            Proyecto Maven y manual de uso
│   └── docs/                              Trazabilidad UML-código
└── escenario-2-registro-activos/
    ├── README.md                          Informe del escenario 2
    ├── uml/                               Diagramas PlantUML e imágenes
    ├── codigo/                            Proyecto Maven y manual de uso
    └── docs/                              Trazabilidad UML-código
```
