package model;

public class Empleado {

	//Atributos:
	private String nombre;
	private String dni;
	private char genero;
	private int categoria;
	private int anyos_trabajados;

	//Constructor:
	public Empleado(String nombre, String dni, char genero, int categoria, int anyos_trabajados) {
		this.nombre = nombre;
		this.dni = dni;
		this.genero = genero;
		this.categoria = categoria;
		this.anyos_trabajados = anyos_trabajados;
	}

	//Getters y Setters:
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public char getSexo() {
		return genero;
	}

	public void setSexo(char genero) {
		this.genero = genero;
	}

	public int getCategoria() {
		return categoria;
	}

	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}

	public int getAnyos() {
		return anyos_trabajados;
	}

	public void setAnyos(int anyos_trabajados) {
		this.anyos_trabajados = anyos_trabajados;
	}
}
