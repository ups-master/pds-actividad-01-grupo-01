package ec.edu.ups.pds.factorymethod;

/** Creador concreto de automoviles. */
public class RegistroAuto extends RegistroVehicular {
    @Override
    public Vehiculo crearVehiculo(DatosVehiculo datos) {
        return new Auto(datos);
    }
}
