package ec.edu.ups.pds.factorymethod;

/** Creador concreto de camiones. */
public class RegistroCamion extends RegistroVehicular {
    private final double tonelaje;

    public RegistroCamion(double tonelaje) {
        if (!Double.isFinite(tonelaje) || tonelaje <= 0) {
            throw new IllegalArgumentException("El tonelaje debe ser un numero positivo");
        }
        this.tonelaje = tonelaje;
    }

    @Override
    public Vehiculo crearVehiculo(DatosVehiculo datos) {
        return new Camion(datos, tonelaje);
    }
}
