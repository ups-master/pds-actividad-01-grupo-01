package ec.edu.ups.pds.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ActivoFijoTest {
    @Test
    void elConstructorDelProductoExigeLosTresDatosObligatorios() {
        assertThrows(
                NullPointerException.class,
                () -> new ActivoFijo(null, "Mesa", BigDecimal.TEN, null, null, null, null, null, null, null, null, null, null));
        assertThrows(
                NullPointerException.class,
                () -> new ActivoFijo("AF-1", null, BigDecimal.TEN, null, null, null, null, null, null, null, null, null, null));
        assertThrows(
                NullPointerException.class,
                () -> new ActivoFijo("AF-1", "Mesa", null, null, null, null, null, null, null, null, null, null, null));
    }

    @Test
    void losOpcionalesAusentesSeExponenComoOptionalVacio() {
        ActivoFijo activo = new ActivoFijoBuilder("AF-1", "Mesa", BigDecimal.TEN).construir();

        assertTrue(activo.getMarca().isEmpty());
        assertTrue(activo.getModelo().isEmpty());
        assertTrue(activo.getNumeroSerie().isEmpty());
        assertTrue(activo.getUbicacion().isEmpty());
        assertTrue(activo.getCustodio().isEmpty());
        assertTrue(activo.getFechaAdquisicion().isEmpty());
        assertTrue(activo.getGarantiaMeses().isEmpty());
        assertTrue(activo.getProveedor().isEmpty());
        assertTrue(activo.getCaracteristicasTecnicas().isEmpty());
        assertTrue(activo.getObservaciones().isEmpty());
    }

    @Test
    void toStringMuestraLosDatosObligatoriosYLosOpcionalesConfigurados() {
        String texto =
                new ActivoFijoBuilder("AF-1", "Mesa", new BigDecimal("99.90"))
                        .conMarca("Ikea")
                        .construir()
                        .toString();

        assertTrue(texto.contains("codigoInstitucional='AF-1'"));
        assertTrue(texto.contains("precioAdquisicion=99.90"));
        assertTrue(texto.contains("marca='Ikea'"));
        assertEquals(1, texto.split("modelo='null'", -1).length - 1);
    }
}
