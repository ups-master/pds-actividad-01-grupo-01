package matricula

import "math"

const (
	tasaAuto            = 0.015 // 1.5 % del avalúo
	descuentoAnualAuto  = 0.02  // 2 % menos por cada año de antigüedad
	descuentoMaximoAuto = 0.30  // tope del descuento por antigüedad
)

// Auto es un ConcreteProduct.
//
// ConcreteProduct: implementación concreta de la interfaz Product (Vehiculo).
// Es el objeto que finalmente se crea; cada uno aporta su propia lógica
// (aquí, CalcularMatricula) pero el cliente lo usa solo a través de Vehiculo.
//
// La matrícula de un Auto depende del avalúo y la antigüedad.
type Auto struct {
	VehiculoBase
}

// CalcularMatricula aplica la tasa sobre el avalúo con descuento por antigüedad.
func (a Auto) CalcularMatricula(anioActual int) float64 {
	descuento := math.Min(float64(a.Antiguedad(anioActual))*descuentoAnualAuto, descuentoMaximoAuto)
	return a.avaluo * tasaAuto * (1 - descuento)
}

// AutoFactory es un ConcreteCreator.
//
// ConcreteCreator: implementación concreta de la interfaz Creator
// (VehiculoFactory). Sobrescribe el Factory Method (CrearVehiculo) y es quien
// decide QUÉ clase concreta instanciar: AutoFactory siempre crea un Auto.
type AutoFactory struct{}

// CrearVehiculo implementa el Factory Method para Auto.
func (AutoFactory) CrearVehiculo(datos DatosVehiculoDTO) (Vehiculo, error) {
	base, err := nuevoVehiculoBase(datos)
	if err != nil {
		return nil, err
	}
	return Auto{VehiculoBase: base}, nil
}
