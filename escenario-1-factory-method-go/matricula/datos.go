package matricula

// DatosVehiculoDTO es un DTO (Data Transfer Object): un struct sin lógica
// que solo transporta los datos de entrada del cliente hasta las fábricas.
// No es un Vehiculo; la fábrica lo valida y a partir de él construye el
// ConcreteProduct.
//
// Contiene la información que proporciona el cliente. Tipo decide,
// en tiempo de ejecución, qué fábrica se utiliza; los campos específicos
// solo los lee la fábrica que los necesita.
type DatosVehiculoDTO struct {
	Tipo            string
	Placa           string
	Marca           string
	Modelo          string
	AnioFabricacion int
	Avaluo          float64

	// Camioneta
	CapacidadPasajeros int
	UsoComercial       bool

	// Camión
	Tonelaje   float64
	NumeroEjes int
}
