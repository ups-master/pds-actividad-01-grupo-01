package ec.edu.ups.pds.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ActivoFijoBuilderTest {
    @Test
    void construyeActivoConSoloLosDatosObligatorios() {
        ActivoFijo activo =
                new ActivoFijoBuilder(
                                "UPS-MOB-001",
                                "Escritorio administrativo",
                                new BigDecimal("425.50"))
                        .construir();

        assertEquals("UPS-MOB-001", activo.getCodigoInstitucional());
        assertEquals("Escritorio administrativo", activo.getNombreDescripcion());
        assertEquals(new BigDecimal("425.50"), activo.getPrecioAdquisicion());
        assertFalse(activo.getMarca().isPresent());
        assertFalse(activo.getUbicacion().isPresent());
        assertFalse(activo.getGarantiaMeses().isPresent());
    }

    @Test
    void construyeActivoProgresivamenteConTodosLosDatosOpcionales() {
        LocalDate fecha = LocalDate.of(2026, 9, 15);

        ActivoFijo activo =
                new ActivoFijoBuilder(
                                "UPS-TI-002",
                                "Servidor de aplicaciones",
                                new BigDecimal("8500.00"))
                        .conMarca("Dell")
                        .conModelo("PowerEdge R760")
                        .conNumeroSerie("SRV-760-01")
                        .conUbicacion("Centro de datos")
                        .conCustodio("Dirección de TI")
                        .conFechaAdquisicion(fecha)
                        .conGarantiaMeses(60)
                        .conProveedor("Infraestructura Digital")
                        .conCaracteristicasTecnicas("128 GB RAM, 4 TB SSD")
                        .conObservaciones("Activo crítico")
                        .construir();

        assertEquals("Dell", activo.getMarca().orElseThrow());
        assertEquals("PowerEdge R760", activo.getModelo().orElseThrow());
        assertEquals("SRV-760-01", activo.getNumeroSerie().orElseThrow());
        assertEquals("Centro de datos", activo.getUbicacion().orElseThrow());
        assertEquals("Dirección de TI", activo.getCustodio().orElseThrow());
        assertEquals(fecha, activo.getFechaAdquisicion().orElseThrow());
        assertEquals(60, activo.getGarantiaMeses().orElseThrow());
        assertEquals("Infraestructura Digital", activo.getProveedor().orElseThrow());
        assertEquals(
                "128 GB RAM, 4 TB SSD",
                activo.getCaracteristicasTecnicas().orElseThrow());
        assertEquals("Activo crítico", activo.getObservaciones().orElseThrow());
    }

    @Test
    void rechazaDatosObligatoriosInvalidos() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ActivoFijoBuilder(" ", "Escritorio", BigDecimal.ONE));
        assertThrows(
                IllegalArgumentException.class,
                () -> new ActivoFijoBuilder("AF-1", "", BigDecimal.ONE));
        assertThrows(
                NullPointerException.class,
                () -> new ActivoFijoBuilder("AF-1", "Escritorio", null));
        assertThrows(
                IllegalArgumentException.class,
                () -> new ActivoFijoBuilder("AF-1", "Escritorio", BigDecimal.ZERO));
    }

    @Test
    void rechazaDatosOpcionalesInvalidos() {
        ConstructorActivoFijo builder =
                new ActivoFijoBuilder("AF-1", "Escritorio", BigDecimal.ONE);

        assertThrows(IllegalArgumentException.class, () -> builder.conMarca(" "));
        assertThrows(NullPointerException.class, () -> builder.conFechaAdquisicion(null));
        assertThrows(IllegalArgumentException.class, () -> builder.conGarantiaMeses(-1));
    }

    @Test
    void cadaConstruccionProduceUnaInstantaneaInmutable() {
        ConstructorActivoFijo builder =
                new ActivoFijoBuilder("AF-1", "Portátil", new BigDecimal("1200"))
                        .conMarca("Dell");
        ActivoFijo primero = builder.construir();

        ActivoFijo segundo = builder.conMarca("Lenovo").construir();

        assertEquals("Dell", primero.getMarca().orElseThrow());
        assertEquals("Lenovo", segundo.getMarca().orElseThrow());
    }

    @Test
    void aceptaGarantiaDeCeroMeses() {
        ActivoFijo activo =
                new ActivoFijoBuilder("AF-1", "Mesa", BigDecimal.TEN).conGarantiaMeses(0).construir();

        assertEquals(0, activo.getGarantiaMeses().orElseThrow());
    }

    @Test
    void recortaLosEspaciosDeLosTextos() {
        ActivoFijo activo =
                new ActivoFijoBuilder("  AF-1  ", " Mesa de reuniones ", BigDecimal.TEN)
                        .conMarca("  Ikea ")
                        .construir();

        assertEquals("AF-1", activo.getCodigoInstitucional());
        assertEquals("Mesa de reuniones", activo.getNombreDescripcion());
        assertEquals("Ikea", activo.getMarca().orElseThrow());
    }

    @Test
    void rechazaPrecioNegativo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ActivoFijoBuilder("AF-1", "Mesa", new BigDecimal("-1")));
    }

    @Test
    void rechazaTextosOpcionalesNulosOEnBlanco() {
        ConstructorActivoFijo builder = new ActivoFijoBuilder("AF-1", "Mesa", BigDecimal.TEN);

        assertThrows(IllegalArgumentException.class, () -> builder.conModelo(null));
        assertThrows(IllegalArgumentException.class, () -> builder.conNumeroSerie(""));
        assertThrows(IllegalArgumentException.class, () -> builder.conUbicacion("  "));
        assertThrows(IllegalArgumentException.class, () -> builder.conCustodio(null));
        assertThrows(IllegalArgumentException.class, () -> builder.conProveedor(" "));
        assertThrows(IllegalArgumentException.class, () -> builder.conCaracteristicasTecnicas(""));
        assertThrows(IllegalArgumentException.class, () -> builder.conObservaciones(null));
    }

    @Test
    void unaValidacionFallidaNoAlteraElEstadoDelBuilder() {
        ConstructorActivoFijo builder =
                new ActivoFijoBuilder("AF-1", "Mesa", BigDecimal.TEN).conMarca("Ikea");

        assertThrows(IllegalArgumentException.class, () -> builder.conMarca(" "));

        assertEquals("Ikea", builder.construir().getMarca().orElseThrow());
        assertTrue(builder.construir().getModelo().isEmpty());
    }
}
