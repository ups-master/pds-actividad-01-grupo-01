package main

import (
	"fmt"
	"time"

	"escenario2/activos"
)

func main() {
	director := activos.DirectorInventario{}

	// 1. Computador: receta del Director + pasos extra del cliente.
	b := activos.NuevoActivoFijoBuilder("AF001", "Computador portátil", 1200)
	director.ConstruirComputador(b, "Dell", "Latitude 5440", "SN-48213", "Intel Core i7", 36)
	computador, err := b.
		ConCaracteristica("memoria RAM", "16 GB").
		ConProveedor("Tecnología Andina").
		ConFechaAdquisicion(time.Date(2026, 10, 12, 0, 0, 0, 0, time.Local)).
		Build()
	mostrar(computador, err)

	// 2. Mesa: solo la receta del Director.
	b = activos.NuevoActivoFijoBuilder("AF002", "Mesa de reuniones", 350)
	director.ConstruirMesa(b, "Madera de roble", "200 x 90 x 75 cm", "Sala de juntas, Cuenca", "Ana Pérez")
	mesa, err := b.Build()
	mostrar(mesa, err)

	// 3. Monitor: receta del Director + una observación.
	b = activos.NuevoActivoFijoBuilder("AF003", "Monitor 27 pulgadas", 280)
	director.ConstruirMonitor(b, "LG", "27GP850", "2560 x 1440")
	monitor, err := b.ConObservaciones("Pantalla con un píxel muerto").Build()
	mostrar(monitor, err)

	// 4. Activo inválido: la receta se aplica, pero faltan datos
	// obligatorios y Build() lo rechaza.
	b = activos.NuevoActivoFijoBuilder("", "Computador de escritorio", 0)
	director.ConstruirComputador(b, "HP", "ProDesk 400", "SN-90311", "Intel Core i5", 12)
	_, err = b.Build()
	mostrar(activos.ActivoFijo{}, err)
}

func mostrar(activo activos.ActivoFijo, err error) {
	if err != nil {
		fmt.Printf("No se pudo registrar el activo:\n%v\n\n", err)
		return
	}
	fmt.Println(activo)
}
