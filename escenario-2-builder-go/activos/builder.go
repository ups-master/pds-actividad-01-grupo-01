package activos

import (
	"errors"
	"maps"
	"strings"
	"time"
)

// ConstructorActivoFijo es el Builder del patrón.
//
// Builder: declara los pasos para construir el producto. Cada paso agrega un
// dato opcional con un nombre explícito (ConMarca, ConUbicacion...) y
// devuelve el mismo builder, lo que permite encadenar las llamadas. Build()
// es el paso final que entrega el producto terminado.
//
// El Director solo conoce esta interfaz, no la implementación concreta.
type ConstructorActivoFijo interface {
	ConMarca(marca string) ConstructorActivoFijo
	ConModelo(modelo string) ConstructorActivoFijo
	ConNumeroSerie(numeroSerie string) ConstructorActivoFijo
	ConUbicacion(ubicacion string) ConstructorActivoFijo
	ConCustodio(custodio string) ConstructorActivoFijo
	ConFechaAdquisicion(fecha time.Time) ConstructorActivoFijo
	ConGarantia(meses int) ConstructorActivoFijo
	ConProveedor(proveedor string) ConstructorActivoFijo
	ConCaracteristica(nombre, valor string) ConstructorActivoFijo
	ConObservaciones(observaciones string) ConstructorActivoFijo
	Build() (ActivoFijo, error)
}

// ActivoFijoBuilder es el ConcreteBuilder del patrón.
//
// ConcreteBuilder: implementa los pasos del Builder y sabe CÓMO se ejecuta
// cada uno: dónde se guarda el dato, cómo se arma la ficha técnica y qué se
// valida. Contiene el ActivoFijo en construcción (composición) y lo entrega
// terminado con Build().
type ActivoFijoBuilder struct {
	activo ActivoFijo
}

// NuevoActivoFijoBuilder crea el builder exigiendo primero los datos
// obligatorios; los opcionales se agregan después con los pasos ConXxx().
func NuevoActivoFijoBuilder(codigo, nombre string, precio float64) *ActivoFijoBuilder {
	return &ActivoFijoBuilder{activo: ActivoFijo{
		codigo:                  strings.TrimSpace(codigo),
		nombre:                  strings.TrimSpace(nombre),
		precio:                  precio,
		caracteristicasTecnicas: make(map[string]string),
	}}
}

func (b *ActivoFijoBuilder) ConMarca(marca string) ConstructorActivoFijo {
	b.activo.marca = marca
	return b
}

func (b *ActivoFijoBuilder) ConModelo(modelo string) ConstructorActivoFijo {
	b.activo.modelo = modelo
	return b
}

func (b *ActivoFijoBuilder) ConNumeroSerie(numeroSerie string) ConstructorActivoFijo {
	b.activo.numeroSerie = numeroSerie
	return b
}

func (b *ActivoFijoBuilder) ConUbicacion(ubicacion string) ConstructorActivoFijo {
	b.activo.ubicacion = ubicacion
	return b
}

func (b *ActivoFijoBuilder) ConCustodio(custodio string) ConstructorActivoFijo {
	b.activo.custodio = custodio
	return b
}

func (b *ActivoFijoBuilder) ConFechaAdquisicion(fecha time.Time) ConstructorActivoFijo {
	b.activo.fechaAdquisicion = fecha
	return b
}

func (b *ActivoFijoBuilder) ConGarantia(meses int) ConstructorActivoFijo {
	b.activo.garantiaMeses = meses
	return b
}

func (b *ActivoFijoBuilder) ConProveedor(proveedor string) ConstructorActivoFijo {
	b.activo.proveedor = proveedor
	return b
}

// ConCaracteristica agrega una fila a la ficha técnica. Puede llamarse
// varias veces; si el nombre se repite, el último valor reemplaza al anterior.
func (b *ActivoFijoBuilder) ConCaracteristica(nombre, valor string) ConstructorActivoFijo {
	b.activo.caracteristicasTecnicas[nombre] = valor
	return b
}

func (b *ActivoFijoBuilder) ConObservaciones(observaciones string) ConstructorActivoFijo {
	b.activo.observaciones = observaciones
	return b
}

// Build valida el activo y entrega una copia terminada. La ficha técnica se
// copia para que, si se sigue usando el builder, el activo ya construido no
// cambie. Si hay varios problemas, el error los reporta todos.
func (b *ActivoFijoBuilder) Build() (ActivoFijo, error) {
	var errs []error
	if b.activo.codigo == "" {
		errs = append(errs, errors.New("el código institucional es obligatorio"))
	}
	if b.activo.nombre == "" {
		errs = append(errs, errors.New("el nombre o descripción es obligatorio"))
	}
	if b.activo.precio <= 0 {
		errs = append(errs, errors.New("el precio de adquisición debe ser mayor a cero"))
	}
	if b.activo.garantiaMeses < 0 {
		errs = append(errs, errors.New("la garantía no puede ser negativa"))
	}
	if len(errs) > 0 {
		return ActivoFijo{}, errors.Join(errs...)
	}

	activo := b.activo
	activo.caracteristicasTecnicas = maps.Clone(b.activo.caracteristicasTecnicas)
	return activo, nil
}
