package activos

import (
	"slices"
	"strings"
	"testing"
	"time"
)

func TestSoloObligatorios(t *testing.T) {
	activo, err := NuevoActivoFijoBuilder("AF001", "Silla", 80).Build()
	if err != nil {
		t.Fatalf("error inesperado: %v", err)
	}
	if activo.Codigo() != "AF001" || activo.Nombre() != "Silla" || activo.Precio() != 80 {
		t.Errorf("obligatorios incorrectos: %q %q %v", activo.Codigo(), activo.Nombre(), activo.Precio())
	}
	if activo.Marca() != "" || activo.GarantiaMeses() != 0 || !activo.FechaAdquisicion().IsZero() {
		t.Error("los opcionales no asignados deberían quedar vacíos")
	}
	if len(activo.CaracteristicasTecnicas()) != 0 {
		t.Error("la ficha técnica debería estar vacía")
	}
}

func TestTodosLosPasos(t *testing.T) {
	fecha := time.Date(2026, 10, 12, 0, 0, 0, 0, time.UTC)
	activo, err := NuevoActivoFijoBuilder("AF001", "Computador", 1200).
		ConMarca("Dell").
		ConModelo("Latitude").
		ConNumeroSerie("SN-1").
		ConUbicacion("Cuenca").
		ConCustodio("Ana Pérez").
		ConFechaAdquisicion(fecha).
		ConGarantia(36).
		ConProveedor("Tecnología Andina").
		ConCaracteristica("procesador", "Intel Core i7").
		ConObservaciones("Nuevo").
		Build()
	if err != nil {
		t.Fatalf("error inesperado: %v", err)
	}

	verificar := map[string][2]string{
		"marca":         {activo.Marca(), "Dell"},
		"modelo":        {activo.Modelo(), "Latitude"},
		"numeroSerie":   {activo.NumeroSerie(), "SN-1"},
		"ubicacion":     {activo.Ubicacion(), "Cuenca"},
		"custodio":      {activo.Custodio(), "Ana Pérez"},
		"proveedor":     {activo.Proveedor(), "Tecnología Andina"},
		"observaciones": {activo.Observaciones(), "Nuevo"},
	}
	for campo, v := range verificar {
		if v[0] != v[1] {
			t.Errorf("%s = %q, se esperaba %q", campo, v[0], v[1])
		}
	}
	if !activo.FechaAdquisicion().Equal(fecha) {
		t.Errorf("fecha = %v, se esperaba %v", activo.FechaAdquisicion(), fecha)
	}
	if activo.GarantiaMeses() != 36 {
		t.Errorf("garantía = %d, se esperaba 36", activo.GarantiaMeses())
	}
	if v, ok := activo.Caracteristica("procesador"); !ok || v != "Intel Core i7" {
		t.Errorf("procesador = %q (%v), se esperaba Intel Core i7", v, ok)
	}
}

func TestValidacionDeObligatorios(t *testing.T) {
	casos := []struct {
		nombre  string
		builder ConstructorActivoFijo
		mensaje string
	}{
		{"código vacío", NuevoActivoFijoBuilder("  ", "Silla", 80), "código"},
		{"nombre vacío", NuevoActivoFijoBuilder("AF001", "", 80), "nombre"},
		{"precio cero", NuevoActivoFijoBuilder("AF001", "Silla", 0), "precio"},
		{"precio negativo", NuevoActivoFijoBuilder("AF001", "Silla", -5), "precio"},
		{"garantía negativa", NuevoActivoFijoBuilder("AF001", "Silla", 80).ConGarantia(-1), "garantía"},
	}

	for _, c := range casos {
		t.Run(c.nombre, func(t *testing.T) {
			_, err := c.builder.Build()
			if err == nil {
				t.Fatal("se esperaba un error")
			}
			if !strings.Contains(err.Error(), c.mensaje) {
				t.Errorf("el error %q debería mencionar %q", err, c.mensaje)
			}
		})
	}
}

func TestReportaTodosLosErrores(t *testing.T) {
	_, err := NuevoActivoFijoBuilder("", "", 0).Build()
	if err == nil {
		t.Fatal("se esperaba un error")
	}
	for _, mensaje := range []string{"código", "nombre", "precio"} {
		if !strings.Contains(err.Error(), mensaje) {
			t.Errorf("el error %q debería mencionar %q", err, mensaje)
		}
	}
}

func TestActivoConstruidoEsInmutable(t *testing.T) {
	b := NuevoActivoFijoBuilder("AF001", "Computador", 1200).ConCaracteristica("procesador", "i5")
	activo, err := b.Build()
	if err != nil {
		t.Fatalf("error inesperado: %v", err)
	}

	// Seguir usando el builder no debe afectar al activo ya construido.
	b.ConCaracteristica("procesador", "i9").ConCaracteristica("memoria RAM", "32 GB").ConMarca("HP")
	if v, _ := activo.Caracteristica("procesador"); v != "i5" {
		t.Errorf("procesador = %q, el activo cambió después de Build()", v)
	}
	if _, ok := activo.Caracteristica("memoria RAM"); ok {
		t.Error("el activo recibió una característica agregada después de Build()")
	}
	if activo.Marca() != "" {
		t.Error("el activo recibió una marca asignada después de Build()")
	}

	// Modificar la ficha devuelta por el getter tampoco debe afectarlo.
	ficha := activo.CaracteristicasTecnicas()
	ficha["procesador"] = "alterado"
	if v, _ := activo.Caracteristica("procesador"); v != "i5" {
		t.Errorf("procesador = %q, el getter expuso el mapa interno", v)
	}
}

func TestDirectorComputador(t *testing.T) {
	b := NuevoActivoFijoBuilder("AF001", "Computador", 1200)
	DirectorInventario{}.ConstruirComputador(b, "Dell", "Latitude", "SN-1", "Intel Core i7", 36)
	activo, err := b.Build()
	if err != nil {
		t.Fatalf("error inesperado: %v", err)
	}
	if activo.Marca() != "Dell" || activo.Modelo() != "Latitude" || activo.NumeroSerie() != "SN-1" || activo.GarantiaMeses() != 36 {
		t.Errorf("receta de computador incompleta: %v", activo)
	}
	if v, _ := activo.Caracteristica("procesador"); v != "Intel Core i7" {
		t.Errorf("procesador = %q", v)
	}
}

func TestDirectorMesa(t *testing.T) {
	b := NuevoActivoFijoBuilder("AF002", "Mesa", 350)
	DirectorInventario{}.ConstruirMesa(b, "Madera", "200 x 90 cm", "Sala de juntas", "Ana Pérez")
	activo, err := b.Build()
	if err != nil {
		t.Fatalf("error inesperado: %v", err)
	}
	if activo.Ubicacion() != "Sala de juntas" || activo.Custodio() != "Ana Pérez" {
		t.Errorf("receta de mesa incompleta: %v", activo)
	}
	if v, _ := activo.Caracteristica("material"); v != "Madera" {
		t.Errorf("material = %q", v)
	}
	// Lo que no aplica a una mesa no existe, en lugar de quedar vacío.
	if _, ok := activo.Caracteristica("procesador"); ok {
		t.Error("una mesa no debería tener procesador")
	}
	if activo.Marca() != "" {
		t.Error("la receta de mesa no asigna marca")
	}
}

func TestDirectorMonitor(t *testing.T) {
	b := NuevoActivoFijoBuilder("AF003", "Monitor", 280)
	DirectorInventario{}.ConstruirMonitor(b, "LG", "27GP850", "2560 x 1440")
	activo, err := b.Build()
	if err != nil {
		t.Fatalf("error inesperado: %v", err)
	}
	if activo.Marca() != "LG" || activo.Modelo() != "27GP850" {
		t.Errorf("receta de monitor incompleta: %v", activo)
	}
	if v, _ := activo.Caracteristica("resolución"); v != "2560 x 1440" {
		t.Errorf("resolución = %q", v)
	}
}

func TestDirectorConPasosExtra(t *testing.T) {
	b := NuevoActivoFijoBuilder("AF001", "Computador", 1200)
	DirectorInventario{}.ConstruirComputador(b, "Dell", "Latitude", "SN-1", "Intel Core i7", 36)
	activo, err := b.ConObservaciones("Asignado a TI").Build()
	if err != nil {
		t.Fatalf("error inesperado: %v", err)
	}
	if activo.Observaciones() != "Asignado a TI" || activo.Marca() != "Dell" {
		t.Error("deberían conservarse tanto la receta como el paso extra")
	}
}

// builderEspia implementa ConstructorActivoFijo solo para anotar qué pasos
// se llaman. Sirve para comprobar que el Director depende únicamente de la
// interfaz y no de ActivoFijoBuilder.
type builderEspia struct {
	pasos []string
}

func (e *builderEspia) anotar(paso string) ConstructorActivoFijo {
	e.pasos = append(e.pasos, paso)
	return e
}

func (e *builderEspia) ConMarca(string) ConstructorActivoFijo       { return e.anotar("marca") }
func (e *builderEspia) ConModelo(string) ConstructorActivoFijo      { return e.anotar("modelo") }
func (e *builderEspia) ConNumeroSerie(string) ConstructorActivoFijo { return e.anotar("numeroSerie") }
func (e *builderEspia) ConUbicacion(string) ConstructorActivoFijo   { return e.anotar("ubicacion") }
func (e *builderEspia) ConCustodio(string) ConstructorActivoFijo    { return e.anotar("custodio") }
func (e *builderEspia) ConGarantia(int) ConstructorActivoFijo       { return e.anotar("garantia") }
func (e *builderEspia) ConProveedor(string) ConstructorActivoFijo   { return e.anotar("proveedor") }
func (e *builderEspia) ConObservaciones(string) ConstructorActivoFijo {
	return e.anotar("observaciones")
}
func (e *builderEspia) ConFechaAdquisicion(time.Time) ConstructorActivoFijo {
	return e.anotar("fechaAdquisicion")
}
func (e *builderEspia) ConCaracteristica(nombre, _ string) ConstructorActivoFijo {
	return e.anotar("caracteristica:" + nombre)
}
func (e *builderEspia) Build() (ActivoFijo, error) {
	e.anotar("build")
	return ActivoFijo{}, nil
}

func TestDirectorSoloConoceLaInterfaz(t *testing.T) {
	casos := []struct {
		nombre   string
		receta   func(ConstructorActivoFijo)
		esperado []string
	}{
		{
			"computador",
			func(b ConstructorActivoFijo) {
				DirectorInventario{}.ConstruirComputador(b, "Dell", "Latitude", "SN-1", "i7", 36)
			},
			[]string{"marca", "modelo", "numeroSerie", "caracteristica:procesador", "garantia"},
		},
		{
			"mesa",
			func(b ConstructorActivoFijo) {
				DirectorInventario{}.ConstruirMesa(b, "Madera", "200 x 90 cm", "Sala", "Ana")
			},
			[]string{"caracteristica:material", "caracteristica:dimensiones", "ubicacion", "custodio"},
		},
		{
			"monitor",
			func(b ConstructorActivoFijo) {
				DirectorInventario{}.ConstruirMonitor(b, "LG", "27GP850", "2560 x 1440")
			},
			[]string{"marca", "modelo", "caracteristica:resolución"},
		},
	}

	for _, c := range casos {
		t.Run(c.nombre, func(t *testing.T) {
			espia := &builderEspia{}
			c.receta(espia)
			if !slices.Equal(espia.pasos, c.esperado) {
				t.Errorf("pasos = %v, se esperaba %v", espia.pasos, c.esperado)
			}
		})
	}
}
