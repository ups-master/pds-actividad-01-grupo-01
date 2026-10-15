package ec.edu.ups.pds.builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/** Producto inmutable construido por {@link ConstructorActivoFijo}. */
public final class ActivoFijo {
    private final String codigoInstitucional;
    private final String nombreDescripcion;
    private final BigDecimal precioAdquisicion;
    private final String marca;
    private final String modelo;
    private final String numeroSerie;
    private final String ubicacion;
    private final String custodio;
    private final LocalDate fechaAdquisicion;
    private final Integer garantiaMeses;
    private final String proveedor;
    private final String caracteristicasTecnicas;
    private final String observaciones;

    ActivoFijo(
            String codigoInstitucional,
            String nombreDescripcion,
            BigDecimal precioAdquisicion,
            String marca,
            String modelo,
            String numeroSerie,
            String ubicacion,
            String custodio,
            LocalDate fechaAdquisicion,
            Integer garantiaMeses,
            String proveedor,
            String caracteristicasTecnicas,
            String observaciones) {
        this.codigoInstitucional = Objects.requireNonNull(codigoInstitucional);
        this.nombreDescripcion = Objects.requireNonNull(nombreDescripcion);
        this.precioAdquisicion = Objects.requireNonNull(precioAdquisicion);
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.ubicacion = ubicacion;
        this.custodio = custodio;
        this.fechaAdquisicion = fechaAdquisicion;
        this.garantiaMeses = garantiaMeses;
        this.proveedor = proveedor;
        this.caracteristicasTecnicas = caracteristicasTecnicas;
        this.observaciones = observaciones;
    }

    public String getCodigoInstitucional() {
        return codigoInstitucional;
    }

    public String getNombreDescripcion() {
        return nombreDescripcion;
    }

    public BigDecimal getPrecioAdquisicion() {
        return precioAdquisicion;
    }

    public Optional<String> getMarca() {
        return Optional.ofNullable(marca);
    }

    public Optional<String> getModelo() {
        return Optional.ofNullable(modelo);
    }

    public Optional<String> getNumeroSerie() {
        return Optional.ofNullable(numeroSerie);
    }

    public Optional<String> getUbicacion() {
        return Optional.ofNullable(ubicacion);
    }

    public Optional<String> getCustodio() {
        return Optional.ofNullable(custodio);
    }

    public Optional<LocalDate> getFechaAdquisicion() {
        return Optional.ofNullable(fechaAdquisicion);
    }

    public Optional<Integer> getGarantiaMeses() {
        return Optional.ofNullable(garantiaMeses);
    }

    public Optional<String> getProveedor() {
        return Optional.ofNullable(proveedor);
    }

    public Optional<String> getCaracteristicasTecnicas() {
        return Optional.ofNullable(caracteristicasTecnicas);
    }

    public Optional<String> getObservaciones() {
        return Optional.ofNullable(observaciones);
    }

    @Override
    public String toString() {
        return "ActivoFijo{" + "codigoInstitucional='" + codigoInstitucional + '\''
                + ", nombreDescripcion='" + nombreDescripcion + '\''
                + ", precioAdquisicion=" + precioAdquisicion
                + ", marca='" + marca + '\''
                + ", modelo='" + modelo + '\''
                + ", numeroSerie='" + numeroSerie + '\''
                + ", ubicacion='" + ubicacion + '\''
                + ", custodio='" + custodio + '\''
                + ", fechaAdquisicion=" + fechaAdquisicion
                + ", garantiaMeses=" + garantiaMeses
                + ", proveedor='" + proveedor + '\''
                + ", caracteristicasTecnicas='" + caracteristicasTecnicas + '\''
                + ", observaciones='" + observaciones + '\''
                + '}';
    }
}
