package activos

// DirectorInventario es el Director del patrón.
//
// Director: define recetas reutilizables, es decir, QUÉ pasos aplicar y en
// qué orden para cada tipo de activo. No sabe CÓMO se ejecuta cada paso: solo
// conoce la interfaz ConstructorActivoFijo, nunca ActivoFijoBuilder ni los
// campos de ActivoFijo.
//
// El Director no llama a Build(): el cliente obtiene el resultado del
// builder y, antes de hacerlo, puede agregar pasos extra a la receta.
type DirectorInventario struct{}

// ConstruirComputador aplica los datos que requiere un computador: marca,
// modelo, número de serie, procesador y garantía.
func (DirectorInventario) ConstruirComputador(b ConstructorActivoFijo, marca, modelo, numeroSerie, procesador string, garantiaMeses int) {
	b.ConMarca(marca).
		ConModelo(modelo).
		ConNumeroSerie(numeroSerie).
		ConCaracteristica("procesador", procesador).
		ConGarantia(garantiaMeses)
}

// ConstruirMesa aplica los datos que requiere una mesa: material,
// dimensiones, ubicación y custodio.
func (DirectorInventario) ConstruirMesa(b ConstructorActivoFijo, material, dimensiones, ubicacion, custodio string) {
	b.ConCaracteristica("material", material).
		ConCaracteristica("dimensiones", dimensiones).
		ConUbicacion(ubicacion).
		ConCustodio(custodio)
}

// ConstruirMonitor aplica los datos que requiere un monitor: marca, modelo
// y resolución.
func (DirectorInventario) ConstruirMonitor(b ConstructorActivoFijo, marca, modelo, resolucion string) {
	b.ConMarca(marca).
		ConModelo(modelo).
		ConCaracteristica("resolución", resolucion)
}
