package ec.edu.ups.pds.factorymethod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Producto abstracto del patron Factory Method. */
public abstract class Vehiculo {
    private static final BigDecimal DESCUENTO_ANUAL = new BigDecimal("0.02");
    private static final BigDecimal DESCUENTO_MAXIMO = new BigDecimal("0.30");

    protected final DatosVehiculo datos;

    public Vehiculo(DatosVehiculo datos) {
        this.datos = Objects.requireNonNull(datos, "Los datos del vehiculo son obligatorios");
    }

    public abstract BigDecimal calcularMatricula(int anioActual);

    public DatosVehiculo getDatos() {
        return datos;
    }

    /**
     * Calcula un porcentaje del avaluo y aplica 2 % de descuento por cada anio de antiguedad,
     * con un descuento maximo de 30 %.
     */
    protected BigDecimal calcularBaseAjustada(BigDecimal porcentaje, int anioActual) {
        int antiguedad = calcularAntiguedad(anioActual);
        BigDecimal descuento = DESCUENTO_ANUAL
                .multiply(BigDecimal.valueOf(antiguedad))
                .min(DESCUENTO_MAXIMO);
        BigDecimal factorAntiguedad = BigDecimal.ONE.subtract(descuento);
        return datos.getAvaluo().multiply(porcentaje).multiply(factorAntiguedad);
    }

    protected final int calcularAntiguedad(int anioActual) {
        if (anioActual < datos.getAnioFabricacion()) {
            throw new IllegalArgumentException(
                    "El anio actual no puede ser anterior al anio de fabricacion");
        }
        return anioActual - datos.getAnioFabricacion();
    }

    protected static BigDecimal dinero(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }
}
