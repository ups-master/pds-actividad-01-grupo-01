package ec.edu.ups.pds.factorymethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/**
 * Comprueba la afirmación del informe: agregar un tipo de vehículo nuevo solo exige una subclase de
 * {@link Vehiculo} y su creador. Aquí se define una motocicleta sin modificar ninguna clase existente.
 */
class ExtensibilidadFactoryMethodTest {
    private static final class Motocicleta extends Vehiculo {
        Motocicleta(DatosVehiculo datos) {
            super(datos);
        }

        @Override
        public BigDecimal calcularMatricula(int anioActual) {
            return dinero(calcularBaseAjustada(new BigDecimal("0.01"), anioActual));
        }
    }

    private static final class RegistroMotocicleta extends RegistroVehicular {
        @Override
        public Vehiculo crearVehiculo(DatosVehiculo datos) {
            return new Motocicleta(datos);
        }
    }

    @Test
    void unTipoNuevoFuncionaConLaOperacionDelCreadorSinModificarlo() {
        DatosVehiculo datos =
                new DatosVehiculo("MOT-0001", "Yamaha", "R3", 2020, new BigDecimal("10000.00"));
        RegistroVehicular registro = new RegistroMotocicleta();

        assertEquals(new BigDecimal("88.00"), registro.calcularMatricula(datos, 2026));
    }

    @Test
    void cadaLlamadaDelCreadorProduceUnVehiculoNuevo() {
        DatosVehiculo datos = new DatosVehiculo("A-1", "M", "X", 2020, BigDecimal.TEN);
        RegistroVehicular registro = new RegistroAuto();

        assertNotSame(registro.crearVehiculo(datos), registro.crearVehiculo(datos));
    }

    @Test
    void losCreadoresConDatoPropioRechazanCapacidadesInvalidas() {
        assertThrows(IllegalArgumentException.class, () -> new RegistroCamioneta(0));
        assertThrows(IllegalArgumentException.class, () -> new RegistroCamioneta(-5));
        assertThrows(IllegalArgumentException.class, () -> new RegistroCamion(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new RegistroCamion(Double.NaN));
    }

    @Test
    void unVehiculoExigeDatos() {
        assertThrows(NullPointerException.class, () -> new Auto(null));
    }
}
