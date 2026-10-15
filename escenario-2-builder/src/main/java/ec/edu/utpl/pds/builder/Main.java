package ec.edu.utpl.pds.builder;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Cliente de ejemplo del patrón Builder. */
public final class Main {
    private Main() {}

    public static void main(String[] args) {
        ConstructorActivoFijo builder =
                new ActivoFijoBuilder(
                                "UTPL-TI-0001",
                                "Computador portátil para desarrollo",
                                new BigDecimal("1450.00"))
                        .conMarca("Dell")
                        .conModelo("Latitude")
                        .conNumeroSerie("SN-2026-001")
                        .conFechaAdquisicion(LocalDate.of(2026, 10, 12))
                        .conGarantiaMeses(36)
                        .conProveedor("Tecnología Andina");

        ActivoFijo activo =
                new DirectorInventario()
                        .construirActivoAsignado(
                                builder, "Laboratorio de Software", "Ana Pérez");

        System.out.println(activo);
    }
}
