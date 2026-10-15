// Package matricula implementa el registro vehicular y el cálculo de
// matrícula aplicando el patrón Factory Method.
package matricula

import (
	"errors"
	"strings"
)

// Vehiculo es el Product del patrón: el contrato común que el cliente
// utiliza sin conocer el tipo concreto.
type Vehiculo interface {
	Placa() string
	CalcularMatricula(anioActual int) float64
}

// VehiculoBase agrupa los datos comunes. Los vehículos concretos la
// embeben (composición) y heredan sus métodos por promoción.
type VehiculoBase struct {
	placa           string
	marca           string
	modelo          string
	anioFabricacion int
	avaluo          float64
}

// Placa devuelve la placa del vehículo.
func (b VehiculoBase) Placa() string {
	return b.placa
}

// Antiguedad devuelve los años transcurridos desde la fabricación.
func (b VehiculoBase) Antiguedad(anioActual int) int {
	if anioActual < b.anioFabricacion {
		return 0
	}
	return anioActual - b.anioFabricacion
}

// nuevoVehiculoBase valida y copia los datos comunes enviados por el cliente.
func nuevoVehiculoBase(datos DatosVehiculoDTO) (VehiculoBase, error) {
	if strings.TrimSpace(datos.Placa) == "" {
		return VehiculoBase{}, errors.New("la placa es obligatoria")
	}
	if datos.AnioFabricacion <= 0 {
		return VehiculoBase{}, errors.New("el año de fabricación es inválido")
	}
	if datos.Avaluo <= 0 {
		return VehiculoBase{}, errors.New("el avalúo debe ser mayor a cero")
	}
	return VehiculoBase{
		placa:           datos.Placa,
		marca:           datos.Marca,
		modelo:          datos.Modelo,
		anioFabricacion: datos.AnioFabricacion,
		avaluo:          datos.Avaluo,
	}, nil
}
