package ec.edu.ups.pds.factorymethod;

import java.math.BigDecimal;
import java.util.Objects;

/** Datos generales que comparten todos los vehiculos registrados. */
public class DatosVehiculo {
    private final String placa;
    private final String marca;
    private final String modelo;
    private final int anioFabricacion;
    private final BigDecimal avaluo;

    public DatosVehiculo(
            String placa, String marca, String modelo, int anioFabricacion, BigDecimal avaluo) {
        this.placa = textoObligatorio(placa, "placa");
        this.marca = textoObligatorio(marca, "marca");
        this.modelo = textoObligatorio(modelo, "modelo");
        if (anioFabricacion < 1886) {
            throw new IllegalArgumentException("El anio de fabricacion no puede ser anterior a 1886");
        }
        this.anioFabricacion = anioFabricacion;
        this.avaluo = Objects.requireNonNull(avaluo, "El avaluo es obligatorio");
        if (avaluo.signum() < 0) {
            throw new IllegalArgumentException("El avaluo no puede ser negativo");
        }
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnioFabricacion() {
        return anioFabricacion;
    }

    public BigDecimal getAvaluo() {
        return avaluo;
    }

    private static String textoObligatorio(String valor, String nombre) {
        Objects.requireNonNull(valor, "El campo " + nombre + " es obligatorio");
        if (valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + nombre + " no puede estar vacio");
        }
        return valor.strip();
    }
}
