package ec.edu.utpl.pds.builder;

import java.util.Objects;

/** Director con secuencias frecuentes para registrar activos institucionales. */
public final class DirectorInventario {
    public ActivoFijo construirActivoAsignado(
            ConstructorActivoFijo builder, String ubicacion, String custodio) {
        Objects.requireNonNull(builder, "builder");
        return builder.conUbicacion(ubicacion).conCustodio(custodio).construir();
    }

    public ActivoFijo construirEquipoConGarantia(
            ConstructorActivoFijo builder,
            String marca,
            String modelo,
            String numeroSerie,
            String proveedor,
            int garantiaMeses) {
        Objects.requireNonNull(builder, "builder");
        return builder.conMarca(marca)
                .conModelo(modelo)
                .conNumeroSerie(numeroSerie)
                .conProveedor(proveedor)
                .conGarantiaMeses(garantiaMeses)
                .construir();
    }
}
