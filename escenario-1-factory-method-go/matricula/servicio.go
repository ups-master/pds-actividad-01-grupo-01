package matricula

import (
	"fmt"
	"strings"
)

// VehiculoFactory es el Creator del patrón.
//
// Creator: declara el Factory Method (CrearVehiculo), que devuelve un objeto
// del tipo abstracto Vehiculo. No decide qué clase concreta se crea; esa
// decisión se delega a cada ConcreteCreator (AutoFactory, CamionetaFactory,
// CamionFactory).
//
// ¿Quién decide qué crear? La decisión ocurre en dos niveles:
//  1. ServicioMatricula elige en ejecución QUÉ fábrica usar, buscando
//     datos.Tipo en el registro (ver ProcesarMatricula).
//  2. La fábrica elegida (ConcreteCreator) decide QUÉ struct instanciar.
//
// Así ninguno de los dos depende de Auto, Camioneta o Camion.
type VehiculoFactory interface {
	CrearVehiculo(datos DatosVehiculoDTO) (Vehiculo, error)
}

// ServicioMatricula es el cliente: solo conoce VehiculoFactory y Vehiculo.
// Las fábricas se registran desde fuera, así que agregar un tipo nuevo
// no requiere modificar este código.
type ServicioMatricula struct {
	fabricas map[string]VehiculoFactory
}

// NuevoServicioMatricula crea un servicio sin fábricas registradas.
func NuevoServicioMatricula() *ServicioMatricula {
	return &ServicioMatricula{fabricas: make(map[string]VehiculoFactory)}
}

// Registrar asocia un tipo de vehículo con la fábrica que lo crea.
func (s *ServicioMatricula) Registrar(tipo string, fabrica VehiculoFactory) {
	s.fabricas[normalizar(tipo)] = fabrica
}

// ProcesarMatricula selecciona la fábrica según datos.Tipo, crea el
// vehículo y calcula su matrícula sin depender del tipo concreto.
func (s *ServicioMatricula) ProcesarMatricula(datos DatosVehiculoDTO, anioActual int) (float64, error) {
	// Nivel 1 de la decisión: se elige la fábrica según el tipo pedido.
	fabrica, ok := s.fabricas[normalizar(datos.Tipo)]
	if !ok {
		return 0, fmt.Errorf("tipo de vehículo no soportado: %q", datos.Tipo)
	}
	// Nivel 2: la fábrica (ConcreteCreator) crea el ConcreteProduct.
	vehiculo, err := fabrica.CrearVehiculo(datos)
	if err != nil {
		return 0, fmt.Errorf("no se pudo registrar %q: %w", datos.Placa, err)
	}
	return vehiculo.CalcularMatricula(anioActual), nil
}

func normalizar(tipo string) string {
	return strings.ToLower(strings.TrimSpace(tipo))
}
