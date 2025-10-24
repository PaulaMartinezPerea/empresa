package model;

public class Nomina {

	// Atributos
	private String dni;
	private double sueldo;

	// Constructor:
	public Nomina(String dni, double sueldo) {
		this.dni = dni;
		this.sueldo = sueldo;
	}

	//Getters y Setters:
	public String getDni() {
		return dni;
	}

	public void setDni() {
		this.dni = dni;
	}

	public double getSueldo() {
		return sueldo;
	}

	public void setSueldo(double sueldo) {
		this.sueldo = sueldo;
	}
}