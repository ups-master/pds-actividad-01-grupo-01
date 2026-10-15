package ec.edu.ups.pds.factorymethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {
    private final PrintStream salidaOriginal = System.out;
    private final PrintStream errorOriginal = System.err;
    private ByteArrayOutputStream salida;
    private ByteArrayOutputStream error;

    @BeforeEach
    void capturarSalidas() {
        salida = new ByteArrayOutputStream();
        error = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salida, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(error, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restaurarSalidas() {
        System.setOut(salidaOriginal);
        System.setErr(errorOriginal);
    }

    private String salida() {
        return salida.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    private String error() {
        return error.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    @Test
    void autoImprimeLaMatriculaCalculada() {
        Main.main(new String[] {"auto", "ABC-1234", "Toyota", "Corolla", "2020", "20000", "0", "2026"});

        assertEquals("Tipo: auto | Placa: ABC-1234 | Matricula: $352.00\n", salida());
    }

    @Test
    void camionetaYCamionUsanLaCapacidadRecibida() {
        Main.main(new String[] {"camioneta", "ABC-1234", "Toyota", "Hilux", "2020", "20000", "1000", "2026"});
        Main.main(new String[] {"camion", "ABC-1234", "Hino", "Serie500", "2020", "20000", "10", "2026"});

        assertEquals(
                "Tipo: camioneta | Placa: ABC-1234 | Matricula: $397.20\n"
                        + "Tipo: camion | Placa: ABC-1234 | Matricula: $690.00\n",
                salida());
    }

    @Test
    void elTipoNoDistingueMayusculas() {
        Main.main(new String[] {"AUTO", "ABC-1234", "Toyota", "Corolla", "2020", "20000", "0", "2026"});

        assertTrue(salida().startsWith("Tipo: auto |"));
    }

    @Test
    void sinArgumentosMuestraSoloLaAyuda() {
        Main.main(new String[] {});

        assertTrue(salida().startsWith("Uso: auto|camioneta|camion"));
        assertEquals("", error());
    }

    @Test
    void tipoNoSoportadoInformaElErrorYMuestraLaAyuda() {
        Main.main(new String[] {"moto"});

        assertEquals("Datos invalidos: Tipo no soportado: moto\n", error());
        assertTrue(salida().startsWith("Uso: auto|camioneta|camion"));
    }

    @Test
    void anioDeFabricacionInvalidoInformaElError() {
        Main.main(new String[] {"auto", "ABC-1234", "Toyota", "Corolla", "1800", "20000", "0", "2026"});

        assertEquals(
                "Datos invalidos: El anio de fabricacion no puede ser anterior a 1886\n", error());
    }
}
