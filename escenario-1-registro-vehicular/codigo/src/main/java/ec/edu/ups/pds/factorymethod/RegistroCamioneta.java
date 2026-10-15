package ec.edu.ups.pds.factorymethod;

/** Creador concreto de camionetas. */
public class RegistroCamioneta extends RegistroVehicular {
    private final double capacidadCargaKg;

    public RegistroCamioneta(double capacidadCargaKg) {
        if (!Double.isFinite(capacidadCargaKg) || capacidadCargaKg <= 0) {
            throw new IllegalArgumentException("La capacidad de carga debe ser un numero positivo");
        }
        this.capacidadCargaKg = capacidadCargaKg;
    }

    @Override
    public Vehiculo crearVehiculo(DatosVehiculo datos) {
        return new Camioneta(datos, capacidadCargaKg);
    }
}
