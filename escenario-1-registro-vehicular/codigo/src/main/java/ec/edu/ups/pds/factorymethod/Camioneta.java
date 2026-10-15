package ec.edu.ups.pds.factorymethod;

import java.math.BigDecimal;

/** Producto concreto para camionetas. */
public class Camioneta extends Vehiculo {
    private static final BigDecimal PORCENTAJE_AVALUO = new BigDecimal("0.022");
    private static final BigDecimal VALOR_POR_KILOGRAMO = new BigDecimal("0.01");

    private final double capacidadCargaKg;

    public Camioneta(DatosVehiculo datos, double capacidadCargaKg) {
        super(datos);
        if (!Double.isFinite(capacidadCargaKg) || capacidadCargaKg <= 0) {
            throw new IllegalArgumentException("La capacidad de carga debe ser un numero positivo");
        }
        this.capacidadCargaKg = capacidadCargaKg;
    }

    @Override
    public BigDecimal calcularMatricula(int anioActual) {
        BigDecimal recargoCarga =
                BigDecimal.valueOf(capacidadCargaKg).multiply(VALOR_POR_KILOGRAMO);
        return dinero(calcularBaseAjustada(PORCENTAJE_AVALUO, anioActual).add(recargoCarga));
    }

    public double getCapacidadCargaKg() {
        return capacidadCargaKg;
    }
}
