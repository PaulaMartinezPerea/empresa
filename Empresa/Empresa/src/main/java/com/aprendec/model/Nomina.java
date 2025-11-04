package com.aprendec.model;

public class Nomina {

	private String empleado_dni;
	private double sueldo;
	
	public Nomina(String empleado_dni,double sueldo) {
		this.empleado_dni = empleado_dni;
		this.sueldo = sueldo;
	}
	
	 public String getEmpleadoDni() { return empleado_dni; }
	 public void setEmpleadoDni(String empleado_dni) { this.empleado_dni = empleado_dni; }

	 public double getSueldo() { return sueldo; }
	 public void setSueldo(double sueldo) { this.sueldo = sueldo; }
}
