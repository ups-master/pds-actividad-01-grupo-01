package matricula

import "errors"

const (
	tasaCamioneta             = 0.02 // 2 % del avalúo
	valorPorPasajeroCamioneta = 5.0  // valor fijo por cada pasajero
	recargoComercialCamioneta = 0.25 // 25 % adicional si es de uso comercial
)

// Camioneta es un ConcreteProduct (implementación concreta de Vehiculo,
// ver Auto): además de los datos comunes incorpora su capacidad y su uso.
type Camioneta struct {
	VehiculoBase
	capacidadPasajeros int
	usoComercial       bool
}

// CalcularMatricula suma la tasa sobre el avalúo y un valor por pasajero,
// con recargo si la camioneta es de uso comercial.
func (c Camioneta) CalcularMatricula(anioActual int) float64 {
	valor := c.avaluo*tasaCamioneta + float64(c.capacidadPasajeros)*valorPorPasajeroCamioneta
	if c.usoComercial {
		valor *= 1 + recargoComercialCamioneta
	}
	return valor
}

// CamionetaFactory es un ConcreteCreator (implementación concreta de
// VehiculoFactory, ver AutoFactory): decide crear una Camioneta.
type CamionetaFactory struct{}

// CrearVehiculo implementa el Factory Method para Camioneta.
func (CamionetaFactory) CrearVehiculo(datos DatosVehiculoDTO) (Vehiculo, error) {
	base, err := nuevoVehiculoBase(datos)
	if err != nil {
		return nil, err
	}
	if datos.CapacidadPasajeros <= 0 {
		return nil, errors.New("la capacidad de pasajeros debe ser mayor a cero")
	}
	return Camioneta{
		VehiculoBase:       base,
		capacidadPasajeros: datos.CapacidadPasajeros,
		usoComercial:       datos.UsoComercial,
	}, nil
}
