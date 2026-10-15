package ec.edu.ups.pds.factorymethod;

import java.math.BigDecimal;

/** Producto concreto para camiones. */
public class Camion extends Vehiculo {
    private static final BigDecimal PORCENTAJE_AVALUO = new BigDecimal("0.025");
    private static final BigDecimal VALOR_POR_TONELADA = new BigDecimal("25.00");

    private final double tonelaje;

    public Camion(DatosVehiculo datos, double tonelaje) {
        super(datos);
        if (!Double.isFinite(tonelaje) || tonelaje <= 0) {
            throw new IllegalArgumentException("El tonelaje debe ser un numero positivo");
        }
        this.tonelaje = tonelaje;
    }

    @Override
    public BigDecimal calcularMatricula(int anioActual) {
        BigDecimal recargoTonelaje =
                BigDecimal.valueOf(tonelaje).multiply(VALOR_POR_TONELADA);
        return dinero(calcularBaseAjustada(PORCENTAJE_AVALUO, anioActual).add(recargoTonelaje));
    }

    public double getTonelaje() {
        return tonelaje;
    }
}
