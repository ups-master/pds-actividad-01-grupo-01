package ec.edu.ups.pds.factorymethod;

import java.math.BigDecimal;

/** Producto concreto para automoviles. */
public class Auto extends Vehiculo {
    private static final BigDecimal PORCENTAJE_AVALUO = new BigDecimal("0.020");

    public Auto(DatosVehiculo datos) {
        super(datos);
    }

    @Override
    public BigDecimal calcularMatricula(int anioActual) {
        return dinero(calcularBaseAjustada(PORCENTAJE_AVALUO, anioActual));
    }
}
