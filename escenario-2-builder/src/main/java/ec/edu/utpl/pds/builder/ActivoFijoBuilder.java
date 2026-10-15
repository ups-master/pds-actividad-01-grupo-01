package ec.edu.utpl.pds.builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** Constructor concreto que conserva el estado parcial hasta crear un producto inmutable. */
public final class ActivoFijoBuilder implements ConstructorActivoFijo {
    private final String codigoInstitucional;
    private final String nombreDescripcion;
    private final BigDecimal precioAdquisicion;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String ubicacion;
    private String custodio;
    private LocalDate fechaAdquisicion;
    private Integer garantiaMeses;
    private String proveedor;
    private String caracteristicasTecnicas;
    private String observaciones;

    public ActivoFijoBuilder(
            String codigoInstitucional,
            String nombreDescripcion,
            BigDecimal precioAdquisicion) {
        this.codigoInstitucional = textoObligatorio(codigoInstitucional, "codigoInstitucional");
        this.nombreDescripcion = textoObligatorio(nombreDescripcion, "nombreDescripcion");
        this.precioAdquisicion = precioValido(precioAdquisicion);
    }

    @Override
    public ConstructorActivoFijo conMarca(String marca) {
        this.marca = textoOpcionalValido(marca, "marca");
        return this;
    }

    @Override
    public ConstructorActivoFijo conModelo(String modelo) {
        this.modelo = textoOpcionalValido(modelo, "modelo");
        return this;
    }

    @Override
    public ConstructorActivoFijo conNumeroSerie(String numeroSerie) {
        this.numeroSerie = textoOpcionalValido(numeroSerie, "numeroSerie");
        return this;
    }

    @Override
    public ConstructorActivoFijo conUbicacion(String ubicacion) {
        this.ubicacion = textoOpcionalValido(ubicacion, "ubicacion");
        return this;
    }

    @Override
    public ConstructorActivoFijo conCustodio(String custodio) {
        this.custodio = textoOpcionalValido(custodio, "custodio");
        return this;
    }

    @Override
    public ConstructorActivoFijo conFechaAdquisicion(LocalDate fechaAdquisicion) {
        this.fechaAdquisicion = Objects.requireNonNull(fechaAdquisicion, "fechaAdquisicion");
        return this;
    }

    @Override
    public ConstructorActivoFijo conGarantiaMeses(int garantiaMeses) {
        if (garantiaMeses < 0) {
            throw new IllegalArgumentException("garantiaMeses no puede ser negativa");
        }
        this.garantiaMeses = garantiaMeses;
        return this;
    }

    @Override
    public ConstructorActivoFijo conProveedor(String proveedor) {
        this.proveedor = textoOpcionalValido(proveedor, "proveedor");
        return this;
    }

    @Override
    public ConstructorActivoFijo conCaracteristicasTecnicas(String caracteristicasTecnicas) {
        this.caracteristicasTecnicas =
                textoOpcionalValido(caracteristicasTecnicas, "caracteristicasTecnicas");
        return this;
    }

    @Override
    public ConstructorActivoFijo conObservaciones(String observaciones) {
        this.observaciones = textoOpcionalValido(observaciones, "observaciones");
        return this;
    }

    @Override
    public ActivoFijo construir() {
        return new ActivoFijo(
                codigoInstitucional,
                nombreDescripcion,
                precioAdquisicion,
                marca,
                modelo,
                numeroSerie,
                ubicacion,
                custodio,
                fechaAdquisicion,
                garantiaMeses,
                proveedor,
                caracteristicasTecnicas,
                observaciones);
    }

    private static String textoObligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio");
        }
        return valor.trim();
    }

    private static String textoOpcionalValido(String valor, String campo) {
        return textoObligatorio(valor, campo);
    }

    private static BigDecimal precioValido(BigDecimal precio) {
        Objects.requireNonNull(precio, "precioAdquisicion");
        if (precio.signum() <= 0) {
            throw new IllegalArgumentException("precioAdquisicion debe ser mayor que cero");
        }
        return precio;
    }
}
