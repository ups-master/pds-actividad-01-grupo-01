package ec.edu.utpl.pds.builder;

import java.time.LocalDate;

/** Builder del dominio de activos fijos. */
public interface ConstructorActivoFijo {
    ConstructorActivoFijo conMarca(String marca);

    ConstructorActivoFijo conModelo(String modelo);

    ConstructorActivoFijo conNumeroSerie(String numeroSerie);

    ConstructorActivoFijo conUbicacion(String ubicacion);

    ConstructorActivoFijo conCustodio(String custodio);

    ConstructorActivoFijo conFechaAdquisicion(LocalDate fechaAdquisicion);

    ConstructorActivoFijo conGarantiaMeses(int garantiaMeses);

    ConstructorActivoFijo conProveedor(String proveedor);

    ConstructorActivoFijo conCaracteristicasTecnicas(String caracteristicasTecnicas);

    ConstructorActivoFijo conObservaciones(String observaciones);

    ActivoFijo construir();
}
