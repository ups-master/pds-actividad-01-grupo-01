package ec.edu.ups.pds.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class DirectorInventarioTest {
    private final DirectorInventario director = new DirectorInventario();

    @Test
    void construyeActivoAsignadoUsandoLaAbstraccionBuilder() {
        ConstructorActivoFijo builder =
                new ActivoFijoBuilder(
                        "UPS-MOB-010", "Silla ergonómica", new BigDecimal("210.00"));

        ActivoFijo activo =
                director.construirActivoAsignado(builder, "Edificio 3", "María Torres");

        assertEquals("Edificio 3", activo.getUbicacion().orElseThrow());
        assertEquals("María Torres", activo.getCustodio().orElseThrow());
    }

    @Test
    void construyeEquipoConGarantiaMedianteUnaSecuenciaReutilizable() {
        ConstructorActivoFijo builder =
                new ActivoFijoBuilder(
                        "UPS-TI-020", "Estación de trabajo", new BigDecimal("2400.00"));

        ActivoFijo activo =
                director.construirEquipoConGarantia(
                        builder, "Lenovo", "ThinkStation", "WS-020", "CompuEmpresa", 36);

        assertEquals("Lenovo", activo.getMarca().orElseThrow());
        assertEquals("ThinkStation", activo.getModelo().orElseThrow());
        assertEquals("WS-020", activo.getNumeroSerie().orElseThrow());
        assertEquals("CompuEmpresa", activo.getProveedor().orElseThrow());
        assertEquals(36, activo.getGarantiaMeses().orElseThrow());
    }
}
