package matricula

import (
	"math"
	"testing"
)

const anioActual = 2026

func nuevoServicio() *ServicioMatricula {
	s := NuevoServicioMatricula()
	s.Registrar("auto", AutoFactory{})
	s.Registrar("camioneta", CamionetaFactory{})
	s.Registrar("camion", CamionFactory{})
	return s
}

func casiIgual(a, b float64) bool {
	return math.Abs(a-b) < 0.001
}

func TestCalculoMatriculaPorTipo(t *testing.T) {
	casos := []struct {
		nombre   string
		datos    DatosVehiculoDTO
		esperado float64
	}{
		{"auto con descuento por antigüedad", DatosVehiculoDTO{Tipo: "auto", Placa: "PBA-1234", AnioFabricacion: 2018, Avaluo: 12000}, 151.20},
		{"auto con descuento máximo", DatosVehiculoDTO{Tipo: "auto", Placa: "PBA-0001", AnioFabricacion: 2000, Avaluo: 12000}, 126.00},
		{"camioneta comercial", DatosVehiculoDTO{Tipo: "camioneta", Placa: "GSD-5678", AnioFabricacion: 2021, Avaluo: 35000, CapacidadPasajeros: 5, UsoComercial: true}, 906.25},
		{"camioneta particular", DatosVehiculoDTO{Tipo: "camioneta", Placa: "GSD-0001", AnioFabricacion: 2021, Avaluo: 35000, CapacidadPasajeros: 5}, 725.00},
		{"camión", DatosVehiculoDTO{Tipo: "camion", Placa: "ABC-9012", AnioFabricacion: 2015, Avaluo: 60000, Tonelaje: 12, NumeroEjes: 3}, 840.00},
		{"tipo sin distinguir mayúsculas", DatosVehiculoDTO{Tipo: " Camion ", Placa: "ABC-0001", AnioFabricacion: 2015, Avaluo: 60000, Tonelaje: 12, NumeroEjes: 3}, 840.00},
	}

	servicio := nuevoServicio()
	for _, c := range casos {
		t.Run(c.nombre, func(t *testing.T) {
			valor, err := servicio.ProcesarMatricula(c.datos, anioActual)
			if err != nil {
				t.Fatalf("error inesperado: %v", err)
			}
			if !casiIgual(valor, c.esperado) {
				t.Errorf("matrícula = %.2f, se esperaba %.2f", valor, c.esperado)
			}
		})
	}
}

func TestCadaFabricaCreaSuTipoConcreto(t *testing.T) {
	datos := DatosVehiculoDTO{Placa: "XYZ-1", AnioFabricacion: 2020, Avaluo: 10000, CapacidadPasajeros: 4, Tonelaje: 5, NumeroEjes: 2}

	casos := []struct {
		fabrica VehiculoFactory
		check   func(Vehiculo) bool
	}{
		{AutoFactory{}, func(v Vehiculo) bool { _, ok := v.(Auto); return ok }},
		{CamionetaFactory{}, func(v Vehiculo) bool { _, ok := v.(Camioneta); return ok }},
		{CamionFactory{}, func(v Vehiculo) bool { _, ok := v.(Camion); return ok }},
	}

	for _, c := range casos {
		v, err := c.fabrica.CrearVehiculo(datos)
		if err != nil {
			t.Fatalf("%T: error inesperado: %v", c.fabrica, err)
		}
		if !c.check(v) {
			t.Errorf("%T creó un %T", c.fabrica, v)
		}
		if v.Placa() != "XYZ-1" {
			t.Errorf("%T: Placa() = %q, se esperaba la promovida desde VehiculoBase", c.fabrica, v.Placa())
		}
	}
}

func TestErrores(t *testing.T) {
	casos := []struct {
		nombre string
		datos  DatosVehiculoDTO
	}{
		{"tipo no registrado", DatosVehiculoDTO{Tipo: "bus", Placa: "BUS-1", AnioFabricacion: 2020, Avaluo: 1000}},
		{"placa vacía", DatosVehiculoDTO{Tipo: "auto", AnioFabricacion: 2020, Avaluo: 1000}},
		{"avalúo inválido", DatosVehiculoDTO{Tipo: "auto", Placa: "A-1", AnioFabricacion: 2020}},
		{"camioneta sin pasajeros", DatosVehiculoDTO{Tipo: "camioneta", Placa: "C-1", AnioFabricacion: 2020, Avaluo: 1000}},
		{"camión sin tonelaje", DatosVehiculoDTO{Tipo: "camion", Placa: "K-1", AnioFabricacion: 2020, Avaluo: 1000, NumeroEjes: 2}},
		{"camión con un eje", DatosVehiculoDTO{Tipo: "camion", Placa: "K-2", AnioFabricacion: 2020, Avaluo: 1000, Tonelaje: 5, NumeroEjes: 1}},
	}

	servicio := nuevoServicio()
	for _, c := range casos {
		t.Run(c.nombre, func(t *testing.T) {
			if _, err := servicio.ProcesarMatricula(c.datos, anioActual); err == nil {
				t.Error("se esperaba un error")
			}
		})
	}
}

// Motocicleta simula una variante futura: se agrega sin modificar
// ServicioMatricula ni las fábricas existentes (principio Abierto/Cerrado).
type Motocicleta struct {
	VehiculoBase
}

func (m Motocicleta) CalcularMatricula(anioActual int) float64 {
	return m.avaluo * 0.01
}

type MotocicletaFactory struct{}

func (MotocicletaFactory) CrearVehiculo(datos DatosVehiculoDTO) (Vehiculo, error) {
	base, err := nuevoVehiculoBase(datos)
	if err != nil {
		return nil, err
	}
	return Motocicleta{VehiculoBase: base}, nil
}

func TestNuevaVarianteSinModificarElServicio(t *testing.T) {
	servicio := nuevoServicio()
	datos := DatosVehiculoDTO{Tipo: "motocicleta", Placa: "MT-001", AnioFabricacion: 2022, Avaluo: 3000}

	if _, err := servicio.ProcesarMatricula(datos, anioActual); err == nil {
		t.Fatal("antes de registrarla, la motocicleta no debería estar soportada")
	}

	servicio.Registrar("motocicleta", MotocicletaFactory{})
	valor, err := servicio.ProcesarMatricula(datos, anioActual)
	if err != nil {
		t.Fatalf("error inesperado: %v", err)
	}
	if !casiIgual(valor, 30.00) {
		t.Errorf("matrícula = %.2f, se esperaba 30.00", valor)
	}
}
