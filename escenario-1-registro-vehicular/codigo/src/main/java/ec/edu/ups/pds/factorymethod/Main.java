package ec.edu.ups.pds.factorymethod;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Locale;

/** Ejemplo de seleccion de la fabrica durante la ejecucion. */
public final class Main {
    private Main() {}

    public static void main(String[] args) {
        if (args.length == 0) {
            mostrarUso();
            return;
        }

        try {
            String tipo = args[0].toLowerCase(Locale.ROOT);
            DatosVehiculo datos = new DatosVehiculo(
                    argumento(args, 1, "ABC-1234"),
                    argumento(args, 2, "Marca demo"),
                    argumento(args, 3, "Modelo demo"),
                    entero(args, 4, 2020),
                    decimal(args, 5, "20000.00"));

            RegistroVehicular registro = seleccionarRegistro(tipo, args);
            int anioActual = entero(args, 7, Year.now().getValue());
            BigDecimal matricula = registro.calcularMatricula(datos, anioActual);

            System.out.printf(
                    "Tipo: %s | Placa: %s | Matricula: $%s%n",
                    tipo, datos.getPlaca(), matricula.toPlainString());
        } catch (IllegalArgumentException ex) {
            System.err.println("Datos invalidos: " + ex.getMessage());
            mostrarUso();
        }
    }

    private static RegistroVehicular seleccionarRegistro(String tipo, String[] args) {
        return switch (tipo) {
            case "auto" -> new RegistroAuto();
            case "camioneta" -> new RegistroCamioneta(numero(args, 6, 1000.0));
            case "camion" -> new RegistroCamion(numero(args, 6, 8.0));
            default -> throw new IllegalArgumentException("Tipo no soportado: " + tipo);
        };
    }

    private static String argumento(String[] args, int posicion, String valorPredeterminado) {
        return args.length > posicion ? args[posicion] : valorPredeterminado;
    }

    private static int entero(String[] args, int posicion, int valorPredeterminado) {
        return args.length > posicion ? Integer.parseInt(args[posicion]) : valorPredeterminado;
    }

    private static double numero(String[] args, int posicion, double valorPredeterminado) {
        return args.length > posicion ? Double.parseDouble(args[posicion]) : valorPredeterminado;
    }

    private static BigDecimal decimal(String[] args, int posicion, String valorPredeterminado) {
        return new BigDecimal(argumento(args, posicion, valorPredeterminado));
    }

    private static void mostrarUso() {
        System.out.println(
                "Uso: auto|camioneta|camion [placa marca modelo anio avaluo capacidad anioActual]");
        System.out.println("capacidad representa kg para camioneta y toneladas para camion.");
    }
}
