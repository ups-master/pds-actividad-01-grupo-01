package matricula

import "errors"

const (
	tasaCamion             = 0.01 // 1 % del avalúo
	valorPorToneladaCamion = 15.0 // valor fijo por tonelada de carga
	valorPorEjeCamion      = 20.0 // valor fijo por eje
)

// Camion es un ConcreteProduct (implementación concreta de Vehiculo,
// ver Auto): además de los datos comunes incorpora tonelaje y número de ejes.
type Camion struct {
	VehiculoBase
	tonelaje   float64
	numeroEjes int
}

// CalcularMatricula suma la tasa sobre el avalúo, el tonelaje y los ejes.
func (c Camion) CalcularMatricula(anioActual int) float64 {
	return c.avaluo*tasaCamion +
		c.tonelaje*valorPorToneladaCamion +
		float64(c.numeroEjes)*valorPorEjeCamion
}

// CamionFactory es un ConcreteCreator (implementación concreta de
// VehiculoFactory, ver AutoFactory): decide crear un Camion.
type CamionFactory struct{}

// CrearVehiculo implementa el Factory Method para Camion.
func (CamionFactory) CrearVehiculo(datos DatosVehiculoDTO) (Vehiculo, error) {
	base, err := nuevoVehiculoBase(datos)
	if err != nil {
		return nil, err
	}
	if datos.Tonelaje <= 0 {
		return nil, errors.New("el tonelaje debe ser mayor a cero")
	}
	if datos.NumeroEjes < 2 {
		return nil, errors.New("un camión debe tener al menos 2 ejes")
	}
	return Camion{
		VehiculoBase: base,
		tonelaje:     datos.Tonelaje,
		numeroEjes:   datos.NumeroEjes,
	}, nil
}
