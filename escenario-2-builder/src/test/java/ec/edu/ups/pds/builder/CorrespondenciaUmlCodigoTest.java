package ec.edu.ups.pds.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class CorrespondenciaUmlCodigoTest {
    @Test
    void umlDeclaraTodosLosParticipantesBuilder() throws Exception {
        String uml = Files.readString(Path.of("builderE2.puml"));

        for (String nombre :
                List.of(
                        "ConstructorActivoFijo",
                        "ActivoFijoBuilder",
                        "ActivoFijo",
                        "DirectorInventario",
                        "Main")) {
            assertTrue(uml.contains(nombre), () -> "Falta en UML: " + nombre);
        }
        assertTrue(uml.contains("ConstructorActivoFijo <|.. ActivoFijoBuilder"));
        assertTrue(uml.contains("ActivoFijoBuilder ..> ActivoFijo"));
        assertTrue(uml.contains("DirectorInventario --> ConstructorActivoFijo"));
    }

    @Test
    void codigoRespetaLosRolesPrincipalesDelPatron() {
        assertTrue(ConstructorActivoFijo.class.isInterface());
        assertTrue(ConstructorActivoFijo.class.isAssignableFrom(ActivoFijoBuilder.class));
        assertTrue(Modifier.isFinal(ActivoFijo.class.getModifiers()));
        assertTrue(Modifier.isFinal(ActivoFijoBuilder.class.getModifiers()));
        assertTrue(Modifier.isFinal(DirectorInventario.class.getModifiers()));
    }

    @Test
    void productoEsInmutableYContieneTodosLosDatosDelEscenario() throws Exception {
        for (String campo :
                List.of(
                        "codigoInstitucional",
                        "nombreDescripcion",
                        "precioAdquisicion",
                        "marca",
                        "modelo",
                        "numeroSerie",
                        "ubicacion",
                        "custodio",
                        "fechaAdquisicion",
                        "garantiaMeses",
                        "proveedor",
                        "caracteristicasTecnicas",
                        "observaciones")) {
            Field field = ActivoFijo.class.getDeclaredField(campo);
            assertTrue(Modifier.isPrivate(field.getModifiers()), campo);
            assertTrue(Modifier.isFinal(field.getModifiers()), campo);
        }
    }

    @Test
    void constructorConcretoExigeLosTresDatosObligatorios() throws Exception {
        assertTrue(
                Modifier.isPublic(
                        ActivoFijoBuilder.class
                                .getDeclaredConstructor(
                                        String.class, String.class, BigDecimal.class)
                                .getModifiers()));
    }

    @Test
    void interfazDefineLasOperacionesFluidasYLaConstruccion() throws Exception {
        assertMetodoFluido("conMarca", String.class);
        assertMetodoFluido("conModelo", String.class);
        assertMetodoFluido("conNumeroSerie", String.class);
        assertMetodoFluido("conUbicacion", String.class);
        assertMetodoFluido("conCustodio", String.class);
        assertMetodoFluido("conFechaAdquisicion", LocalDate.class);
        assertMetodoFluido("conGarantiaMeses", int.class);
        assertMetodoFluido("conProveedor", String.class);
        assertMetodoFluido("conCaracteristicasTecnicas", String.class);
        assertMetodoFluido("conObservaciones", String.class);

        assertEquals(
                ActivoFijo.class,
                ConstructorActivoFijo.class.getDeclaredMethod("construir").getReturnType());
    }

    @Test
    void directorDependeDeLaAbstraccionBuilder() throws Exception {
        Method asignado =
                DirectorInventario.class.getDeclaredMethod(
                        "construirActivoAsignado",
                        ConstructorActivoFijo.class,
                        String.class,
                        String.class);
        Method equipo =
                DirectorInventario.class.getDeclaredMethod(
                        "construirEquipoConGarantia",
                        ConstructorActivoFijo.class,
                        String.class,
                        String.class,
                        String.class,
                        String.class,
                        int.class);

        assertEquals(ActivoFijo.class, asignado.getReturnType());
        assertEquals(ActivoFijo.class, equipo.getReturnType());
    }

    private static void assertMetodoFluido(String nombre, Class<?> parametro) throws Exception {
        assertEquals(
                ConstructorActivoFijo.class,
                ConstructorActivoFijo.class
                        .getDeclaredMethod(nombre, parametro)
                        .getReturnType());
    }
}
