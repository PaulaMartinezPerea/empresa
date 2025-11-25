package com.aprendec.dao;

import java.sql.*;
import java.util.*;
import com.aprendec.model.Empleado;

public class EmpleadoDAO {

    private static final EmpleadoDAO instancia = new EmpleadoDAO();

    private EmpleadoDAO() {}

    public static EmpleadoDAO getInstance() {
        return instancia;
    }

    public List<Empleado> listarEmpleados() throws Exception {
        return new DBOperation<List<Empleado>>() {
            @Override
            protected List<Empleado> doExecute(Connection con) throws Exception {

                List<Empleado> lista = new ArrayList<>();

                String sql = "SELECT e.* FROM empleados e";

                try (PreparedStatement ps = con.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        Empleado e = new Empleado(
                            rs.getString("dni"),
                            rs.getString("nombre"),
                            rs.getString("sexo").charAt(0),
                            rs.getInt("categoria"),
                            rs.getInt("anyos")
                        );
                        lista.add(e);
                    }
                }
                return lista;
            }
        }.execute();
    }

    public double buscarSalarioPorDni(String dni) throws Exception {

        return new DBOperation<Double>() {
            @Override
            protected Double doExecute(Connection con) throws Exception {

                String sql = "SELECT sueldo FROM nominas WHERE empleado_dni=?";
                double sueldo = 0;

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, dni);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) sueldo = rs.getDouble("sueldo");
                    }
                }
                return sueldo;
            }
        }.execute();
    }

    public void actualizarEmpleado(Empleado e) throws Exception {

        new DBOperation<Void>() {
            @Override
            protected Void doExecute(Connection con) throws Exception {

                String sql = "UPDATE empleados SET nombre=?, sexo=?, categoria=?, anyos=? WHERE dni=?";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, e.getNombre());
                    ps.setString(2, String.valueOf(e.getSexo()));
                    ps.setInt(3, e.getCategoria());
                    ps.setInt(4, e.getAniosTrabajados());
                    ps.setString(5, e.getDni());
                    ps.executeUpdate();
                }

                double nuevoSalario = calcularSueldo(e.getCategoria(), e.getAniosTrabajados());

                try (PreparedStatement ps2 = con.prepareStatement(
                      "UPDATE nominas SET sueldo=? WHERE empleado_dni=?")) {
                    ps2.setDouble(1, nuevoSalario);
                    ps2.setString(2, e.getDni());
                    ps2.executeUpdate();
                }
                return null;
            }
        }.execute();
    }

    private double calcularSueldo(int categoria, int anyos) {
        double base = switch (categoria) {
            case 1 -> 1000;
            case 2 -> 1400;
            case 3 -> 1800;
            case 4 -> 2300;
            default -> 900;
        };
        return Math.round(base + anyos * 25);
    }
}
