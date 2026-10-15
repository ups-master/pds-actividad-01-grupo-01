package ec.edu.ups.pds.factorymethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculoMatriculaTest {
    private DatosVehiculo datos;

    @BeforeEach
    void prepararDatos() {
        datos =
                new DatosVehiculo(
                        "ABC-1234", "Toyota", "Demo", 2020, new BigDecimal("20000.00"));
    }

    @Test
    void autoCalculaPorAvaluoYAntiguedad() {
        Vehiculo auto = new Auto(datos);

        assertEquals(new BigDecimal("360.00"), auto.calcularMatricula(2025));
    }

    @Test
    void camionetaAgregaRecargoPorCapacidadDeCarga() {
        Vehiculo camioneta = new Camioneta(datos, 1000.0);

        assertEquals(new BigDecimal("406.00"), camioneta.calcularMatricula(2025));
    }

    @Test
    void camionAgregaRecargoPorTonelaje() {
        Vehiculo camion = new Camion(datos, 10.0);

        assertEquals(new BigDecimal("700.00"), camion.calcularMatricula(2025));
    }

    @Test
    void descuentoPorAntiguedadSeLimitaAlTreintaPorCiento() {
        DatosVehiculo antiguo =
                new DatosVehiculo(
                        "AAA-0001", "Ford", "Clasico", 1990, new BigDecimal("10000.00"));

        assertEquals(new BigDecimal("140.00"), new Auto(antiguo).calcularMatricula(2025));
    }

    @Test
    void rechazaAnioActualAnteriorALaFabricacion() {
        Vehiculo auto = new Auto(datos);

        assertThrows(IllegalArgumentException.class, () -> auto.calcularMatricula(2019));
    }

    @Test
    void rechazaDatosYCapacidadesInvalidas() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DatosVehiculo(" ", "Toyota", "Demo", 2020, BigDecimal.TEN));
        assertThrows(IllegalArgumentException.class, () -> new Camioneta(datos, 0));
        assertThrows(IllegalArgumentException.class, () -> new Camion(datos, Double.NaN));
    }

    @Test
    void losTiposConMasCapacidadProducenMatriculasMayoresEnEsteEjemplo() {
        BigDecimal auto = new Auto(datos).calcularMatricula(2025);
        BigDecimal camioneta = new Camioneta(datos, 1000).calcularMatricula(2025);
        BigDecimal camion = new Camion(datos, 10).calcularMatricula(2025);

        assertTrue(camioneta.compareTo(auto) > 0);
        assertTrue(camion.compareTo(camioneta) > 0);
    }
}
