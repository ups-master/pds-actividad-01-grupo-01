package ec.edu.ups.pds.builder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void imprimeElActivoConstruidoConElBuilderYElDirector() {
        PrintStream original = System.out;
        ByteArrayOutputStream captura = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captura, true, StandardCharsets.UTF_8));
        try {
            Main.main(new String[] {});
        } finally {
            System.setOut(original);
        }

        String salida = captura.toString(StandardCharsets.UTF_8);
        assertTrue(salida.contains("codigoInstitucional='UPS-TI-0001'"));
        assertTrue(salida.contains("marca='Dell'"));
        assertTrue(salida.contains("garantiaMeses=36"));
        assertTrue(salida.contains("ubicacion='Laboratorio de Software'"));
        assertTrue(salida.contains("custodio='Ana Pérez'"));
        assertTrue(salida.contains("observaciones='null'"));
    }
}
