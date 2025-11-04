package com.aprendec.dao;

import java.sql.*;
import java.util.*;
import com.aprendec.model.Empleado;
import com.aprendec.conexion.Conexion;

public class EmpleadoDAO {

    // Mostrar todos los empleados
    public List<Empleado> listarEmpleados() throws Exception {
        List<Empleado> lista = new ArrayList<>();
        Connection con = Conexion.getConnection();

        String sql = "SELECT e.* FROM empleados e LEFT JOIN nominas n ON e.dni = n.empleado_dni";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Empleado e = new Empleado();
            e.setDni(rs.getString("dni"));
            e.setNombre(rs.getString("nombre"));
            e.setSexo(rs.getString("sexo").charAt(0));
            e.setCategoria(rs.getInt("categoria"));
            e.setAniosTrabajados(rs.getInt("anyos"));
            lista.add(e);
        }
        con.close();
        return lista;
    }

    // Buscar salario por DNI
    public double buscarSalarioPorDni(String dni) throws Exception {
        Connection con = Conexion.getConnection();
        String sql = "SELECT e.nombre, e.dni, n.sueldo\r\n"
        		+ "FROM empleados e\r\n"
        		+ "JOIN nominas n ON e.dni = n.empleado_dni\r\n"
        		+ "WHERE e.dni = ?\r\n"
        		+ "";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, dni);
        ResultSet rs = ps.executeQuery();
        double sueldo = 0;
        if (rs.next()) sueldo = rs.getDouble("sueldo");
        con.close();
        return sueldo;
    }

    // Obtener un empleado por DNI
    public Empleado obtenerEmpleado(String dni) throws Exception {
        Connection con = Conexion.getConnection();
        String sql = "SELECT e.*, n.sueldo FROM empleados e LEFT JOIN nominas n ON e.dni = n.empleado_dni WHERE e.dni=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, dni);
        ResultSet rs = ps.executeQuery();
        Empleado e = null;
        if (rs.next()) {
            e = new Empleado();
            e.setDni(rs.getString("dni"));
            e.setNombre(rs.getString("nombre"));
            e.setSexo(rs.getString("sexo").charAt(0));
            e.setCategoria(rs.getInt("categoria"));
            e.setAniosTrabajados(rs.getInt("anyos"));
        }
        con.close();
        return e;
    }

    
    // Actualizar datos
    public void actualizarEmpleado(Empleado e) throws Exception {
        Connection con = Conexion.getConnection();
        String sql = "UPDATE empleados e SET nombre=?, sexo=?, categoria=?, anyos=? WHERE e.dni=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, e.getNombre());
        ps.setString(2, String.valueOf(e.getSexo()));
        ps.setInt(3, e.getCategoria());
        ps.setInt(4, e.getAniosTrabajados());
        ps.setString(5, e.getDni());
        ps.executeUpdate();

        
		
        // Recalcular salario
        double nuevoSalario = calcularSueldo(e.getCategoria(),e.getAniosTrabajados() );
        PreparedStatement ps2 = con.prepareStatement("UPDATE nominas n SET sueldo=? WHERE n.empleado_dni=?");
        ps2.setDouble(1, nuevoSalario);
        ps2.setString(2, e.getDni());
        ps2.executeUpdate();

        con.close();
    }
    private double calcularSueldo(int categoria, int anyos) {

		double base;
		switch (categoria) {
		case 1:
			base = 1000;
			break;
		case 2:
			base = 1400;
			break;
		case 3:
			base = 1800;
			break;

		case 4:
			base = 2300;
			break;

		default:
			base = 900;

			break;
		}
		return Math.round(base + anyos * 25);
	}

}

