package ec.edu.ups.pds.factorymethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FactoryMethodTest {
    private DatosVehiculo datos;

    @BeforeEach
    void prepararDatos() {
        datos =
                new DatosVehiculo(
                        "ABC-1234", "Toyota", "Demo", 2020, new BigDecimal("20000.00"));
    }

    @Test
    void cadaCreadorInstanciaElProductoEsperado() {
        assertInstanceOf(Auto.class, new RegistroAuto().crearVehiculo(datos));
        assertInstanceOf(Camioneta.class, new RegistroCamioneta(1000).crearVehiculo(datos));
        assertInstanceOf(Camion.class, new RegistroCamion(10).crearVehiculo(datos));
    }

    @Test
    void operacionDelCreadorTrabajaContraElContratoVehiculo() {
        RegistroVehicular registro = new RegistroCamioneta(1000);

        assertEquals(new BigDecimal("406.00"), registro.calcularMatricula(datos, 2025));
    }
}
