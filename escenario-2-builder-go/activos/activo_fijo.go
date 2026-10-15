// Package activos implementa el registro de activos fijos aplicando el
// patrón Builder.
package activos

import (
	"fmt"
	"maps"
	"slices"
	"strings"
	"time"
)

// ActivoFijo es el Product del patrón.
//
// Product: el objeto complejo que se construye paso a paso. Sus campos son
// privados y solo expone getters, así que una vez construido no puede
// modificarse: la única forma de crearlo es a través del Builder.
type ActivoFijo struct {
	// Obligatorios
	codigo string
	nombre string
	precio float64

	// Opcionales
	marca            string
	modelo           string
	numeroSerie      string
	ubicacion        string
	custodio         string
	fechaAdquisicion time.Time
	garantiaMeses    int
	proveedor        string
	observaciones    string

	// Ficha técnica: datos propios de cada tipo de activo
	// (procesador, material, dimensiones...).
	caracteristicasTecnicas map[string]string
}

func (a ActivoFijo) Codigo() string              { return a.codigo }
func (a ActivoFijo) Nombre() string              { return a.nombre }
func (a ActivoFijo) Precio() float64             { return a.precio }
func (a ActivoFijo) Marca() string               { return a.marca }
func (a ActivoFijo) Modelo() string              { return a.modelo }
func (a ActivoFijo) NumeroSerie() string         { return a.numeroSerie }
func (a ActivoFijo) Ubicacion() string           { return a.ubicacion }
func (a ActivoFijo) Custodio() string            { return a.custodio }
func (a ActivoFijo) FechaAdquisicion() time.Time { return a.fechaAdquisicion }
func (a ActivoFijo) GarantiaMeses() int          { return a.garantiaMeses }
func (a ActivoFijo) Proveedor() string           { return a.proveedor }
func (a ActivoFijo) Observaciones() string       { return a.observaciones }

// Caracteristica devuelve un dato de la ficha técnica e indica si existe.
func (a ActivoFijo) Caracteristica(nombre string) (string, bool) {
	valor, ok := a.caracteristicasTecnicas[nombre]
	return valor, ok
}

// CaracteristicasTecnicas devuelve una copia de la ficha técnica, para que
// quien la reciba no pueda modificar el activo.
func (a ActivoFijo) CaracteristicasTecnicas() map[string]string {
	return maps.Clone(a.caracteristicasTecnicas)
}

// String muestra el activo incluyendo solo los datos que tiene.
func (a ActivoFijo) String() string {
	var sb strings.Builder
	fmt.Fprintf(&sb, "[%s] %s - $%.2f\n", a.codigo, a.nombre, a.precio)

	agregar := func(etiqueta, valor string) {
		if valor != "" {
			fmt.Fprintf(&sb, "  %-18s %s\n", etiqueta+":", valor)
		}
	}
	agregar("Marca", a.marca)
	agregar("Modelo", a.modelo)
	agregar("Número de serie", a.numeroSerie)
	agregar("Ubicación", a.ubicacion)
	agregar("Custodio", a.custodio)
	if !a.fechaAdquisicion.IsZero() {
		agregar("Fecha adquisición", a.fechaAdquisicion.Format("2006-01-02"))
	}
	if a.garantiaMeses > 0 {
		agregar("Garantía", fmt.Sprintf("%d meses", a.garantiaMeses))
	}
	agregar("Proveedor", a.proveedor)

	if len(a.caracteristicasTecnicas) > 0 {
		sb.WriteString("  Ficha técnica:\n")
		for _, nombre := range slices.Sorted(maps.Keys(a.caracteristicasTecnicas)) {
			fmt.Fprintf(&sb, "    - %s: %s\n", nombre, a.caracteristicasTecnicas[nombre])
		}
	}
	agregar("Observaciones", a.observaciones)
	return sb.String()
}
