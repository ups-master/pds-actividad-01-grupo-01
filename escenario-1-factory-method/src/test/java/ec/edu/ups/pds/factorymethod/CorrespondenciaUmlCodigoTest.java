package ec.edu.ups.pds.factorymethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class CorrespondenciaUmlCodigoTest {
    @Test
    void contieneTodasLasClasesDeclaradasEnElUml() throws Exception {
        String uml = Files.readString(Path.of("factoryMethodE1.puml"));
        List<Class<?>> clases =
                List.of(
                        DatosVehiculo.class,
                        Vehiculo.class,
                        Auto.class,
                        Camioneta.class,
                        Camion.class,
                        RegistroVehicular.class,
                        RegistroAuto.class,
                        RegistroCamioneta.class,
                        RegistroCamion.class,
                        Main.class);

        for (Class<?> clase : clases) {
            assertTrue(uml.contains("class " + clase.getSimpleName()), clase.getSimpleName());
        }
    }

    @Test
    void respetaLaJerarquiaDeProductosYCreadores() {
        assertTrue(Modifier.isAbstract(Vehiculo.class.getModifiers()));
        assertEquals(Vehiculo.class, Auto.class.getSuperclass());
        assertEquals(Vehiculo.class, Camioneta.class.getSuperclass());
        assertEquals(Vehiculo.class, Camion.class.getSuperclass());

        assertTrue(Modifier.isAbstract(RegistroVehicular.class.getModifiers()));
        assertEquals(RegistroVehicular.class, RegistroAuto.class.getSuperclass());
        assertEquals(RegistroVehicular.class, RegistroCamioneta.class.getSuperclass());
        assertEquals(RegistroVehicular.class, RegistroCamion.class.getSuperclass());
    }

    @Test
    void respetaAtributosYVisibilidadDelUml() throws Exception {
        assertCampo(DatosVehiculo.class, "placa", String.class, Modifier.PRIVATE);
        assertCampo(DatosVehiculo.class, "marca", String.class, Modifier.PRIVATE);
        assertCampo(DatosVehiculo.class, "modelo", String.class, Modifier.PRIVATE);
        assertCampo(DatosVehiculo.class, "anioFabricacion", int.class, Modifier.PRIVATE);
        assertCampo(DatosVehiculo.class, "avaluo", BigDecimal.class, Modifier.PRIVATE);
        assertCampo(Vehiculo.class, "datos", DatosVehiculo.class, Modifier.PROTECTED);
        assertCampo(Camioneta.class, "capacidadCargaKg", double.class, Modifier.PRIVATE);
        assertCampo(Camion.class, "tonelaje", double.class, Modifier.PRIVATE);
        assertCampo(RegistroCamioneta.class, "capacidadCargaKg", double.class, Modifier.PRIVATE);
        assertCampo(RegistroCamion.class, "tonelaje", double.class, Modifier.PRIVATE);
    }

    @Test
    void respetaConstructoresYOperacionesDelUml() throws Exception {
        assertConstructorPublico(Vehiculo.class, DatosVehiculo.class);
        assertConstructorPublico(Auto.class, DatosVehiculo.class);
        assertConstructorPublico(Camioneta.class, DatosVehiculo.class, double.class);
        assertConstructorPublico(Camion.class, DatosVehiculo.class, double.class);
        assertConstructorPublico(RegistroCamioneta.class, double.class);
        assertConstructorPublico(RegistroCamion.class, double.class);

        Method calculoVehiculo =
                Vehiculo.class.getDeclaredMethod("calcularMatricula", int.class);
        assertEquals(BigDecimal.class, calculoVehiculo.getReturnType());
        assertTrue(Modifier.isPublic(calculoVehiculo.getModifiers()));
        assertTrue(Modifier.isAbstract(calculoVehiculo.getModifiers()));

        Method fabrica =
                RegistroVehicular.class.getDeclaredMethod(
                        "crearVehiculo", DatosVehiculo.class);
        assertEquals(Vehiculo.class, fabrica.getReturnType());
        assertTrue(Modifier.isPublic(fabrica.getModifiers()));
        assertTrue(Modifier.isAbstract(fabrica.getModifiers()));

        Method operacion =
                RegistroVehicular.class.getDeclaredMethod(
                        "calcularMatricula", DatosVehiculo.class, int.class);
        assertEquals(BigDecimal.class, operacion.getReturnType());
        assertTrue(Modifier.isPublic(operacion.getModifiers()));
        assertFalse(Modifier.isAbstract(operacion.getModifiers()));

        Method main = Main.class.getDeclaredMethod("main", String[].class);
        assertTrue(Modifier.isPublic(main.getModifiers()));
        assertTrue(Modifier.isStatic(main.getModifiers()));
    }

    private static void assertCampo(
            Class<?> tipo, String nombre, Class<?> tipoCampo, int modificadorEsperado)
            throws Exception {
        Field campo = tipo.getDeclaredField(nombre);
        assertEquals(tipoCampo, campo.getType());
        assertTrue((campo.getModifiers() & modificadorEsperado) != 0);
    }

    private static void assertConstructorPublico(Class<?> tipo, Class<?>... parametros)
            throws Exception {
        Constructor<?> constructor = tipo.getDeclaredConstructor(parametros);
        assertTrue(Modifier.isPublic(constructor.getModifiers()));
    }
}
