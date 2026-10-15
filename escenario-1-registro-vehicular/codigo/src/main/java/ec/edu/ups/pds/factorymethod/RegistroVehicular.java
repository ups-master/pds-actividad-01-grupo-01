package ec.edu.ups.pds.factorymethod;

import java.math.BigDecimal;

/** Creador abstracto del patron Factory Method. */
public abstract class RegistroVehicular {
    public abstract Vehiculo crearVehiculo(DatosVehiculo datos);

    /** Operacion de negocio que utiliza el producto sin conocer su clase concreta. */
    public BigDecimal calcularMatricula(DatosVehiculo datos, int anioActual) {
        Vehiculo vehiculo = crearVehiculo(datos);
        return vehiculo.calcularMatricula(anioActual);
    }
}
