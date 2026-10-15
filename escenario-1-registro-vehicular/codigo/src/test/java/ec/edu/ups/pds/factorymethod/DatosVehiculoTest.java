package ec.edu.ups.pds.factorymethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class DatosVehiculoTest {
    @Test
    void conservaLosDatosYRecortaLosTextos() {
        DatosVehiculo datos =
                new DatosVehiculo(" ABC-1234 ", " Toyota ", " Hilux ", 2020, new BigDecimal("20000"));

        assertEquals("ABC-1234", datos.getPlaca());
        assertEquals("Toyota", datos.getMarca());
        assertEquals("Hilux", datos.getModelo());
        assertEquals(2020, datos.getAnioFabricacion());
        assertEquals(new BigDecimal("20000"), datos.getAvaluo());
    }

    @Test
    void rechazaTextosNulosOEnBlanco() {
        BigDecimal avaluo = BigDecimal.TEN;

        assertThrows(NullPointerException.class, () -> new DatosVehiculo(null, "M", "X", 2020, avaluo));
        assertThrows(IllegalArgumentException.class, () -> new DatosVehiculo("A-1", " ", "X", 2020, avaluo));
        assertThrows(IllegalArgumentException.class, () -> new DatosVehiculo("A-1", "M", "", 2020, avaluo));
    }

    @Test
    void rechazaAvaluoNuloONegativoYAceptaCero() {
        assertThrows(NullPointerException.class, () -> new DatosVehiculo("A-1", "M", "X", 2020, null));
        assertThrows(
                IllegalArgumentException.class,
                () -> new DatosVehiculo("A-1", "M", "X", 2020, new BigDecimal("-1")));
        assertEquals(
                BigDecimal.ZERO, new DatosVehiculo("A-1", "M", "X", 2020, BigDecimal.ZERO).getAvaluo());
    }

    @Test
    void aceptaElAnioMinimoYRechazaUnoAnterior() {
        assertEquals(1886, new DatosVehiculo("A-1", "M", "X", 1886, BigDecimal.TEN).getAnioFabricacion());
        assertThrows(
                IllegalArgumentException.class,
                () -> new DatosVehiculo("A-1", "M", "X", 1885, BigDecimal.TEN));
    }
}
