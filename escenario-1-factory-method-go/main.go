package main

import (
	"fmt"

	"escenario1/matricula"
)

func main() {
	// Único lugar donde se conocen los tipos concretos.
	servicio := matricula.NuevoServicioMatricula()
	servicio.Registrar("auto", matricula.AutoFactory{})
	servicio.Registrar("camioneta", matricula.CamionetaFactory{})
	servicio.Registrar("camion", matricula.CamionFactory{})

	// Datos que llegarían del cliente; el tipo se decide en ejecución.
	solicitudes := []matricula.DatosVehiculoDTO{
		{Tipo: "auto", Placa: "PBA-1234", Marca: "Chevrolet", Modelo: "Aveo", AnioFabricacion: 2018, Avaluo: 12000},
		{Tipo: "camioneta", Placa: "GSD-5678", Marca: "Toyota", Modelo: "Hilux", AnioFabricacion: 2021, Avaluo: 35000, CapacidadPasajeros: 5, UsoComercial: true},
		{Tipo: "camion", Placa: "ABC-9012", Marca: "Hino", Modelo: "500", AnioFabricacion: 2015, Avaluo: 60000, Tonelaje: 12, NumeroEjes: 3},
	}

	const anioActual = 2026
	for _, datos := range solicitudes {
		valor, err := servicio.ProcesarMatricula(datos, anioActual)
		if err != nil {
			fmt.Printf("%-10s %-9s error: %v\n", datos.Tipo, datos.Placa, err)
			continue
		}
		fmt.Printf("%-10s %-9s matrícula: $%.2f\n", datos.Tipo, datos.Placa, valor)
	}
}
