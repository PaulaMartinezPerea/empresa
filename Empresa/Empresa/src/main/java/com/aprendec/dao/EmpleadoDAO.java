package com.aprendec.dao;

import java.sql.*;
import java.util.*;
import com.aprendec.model.Empleado;
import com.aprendec.conexion.Conexion;

public class EmpleadoDAO {

	/**
     * PATRON SINGLETON:
     * Garantiza que solo exista una instancia de EmpleadoDAO
     */
    private static EmpleadoDAO instancia; 

    /**
     * Constructor privado, evita instanciacion directa
     */
    private EmpleadoDAO() {}

    /**
     * Metodo estatico que devuelve la instancia
     */
    public static synchronized EmpleadoDAO getInstance() {
        if (instancia == null) {
            instancia = new EmpleadoDAO();
        }
        return instancia;
    }

    /**
     * PATRON DAO:
     * Encapsula todas las operaciones de acceso a datos
     */
    public List<Empleado> listarEmpleados() throws Exception {
        //PATRON TEMPLATE METHOD:
        DBOperation<List<Empleado>> op = new DBOperation<List<Empleado>>() {
            @Override
            protected List<Empleado> doExecute(Connection con) throws Exception {
                List<Empleado> lista = new ArrayList<>();
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT e.* FROM empleados e LEFT JOIN nominas n ON e.dni = n.empleado_dni");
                     ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        Empleado e = new Empleado();
                        e.setDni(rs.getString("dni"));
                        e.setNombre(rs.getString("nombre"));
                        e.setSexo(rs.getString("sexo").charAt(0));
                        e.setCategoria(rs.getInt("categoria"));
                        e.setAniosTrabajados(rs.getInt("anyos"));
                        lista.add(e);
                    }
                }
                return lista;
            }
        };
        return op.execute();
    }

    public double buscarSalarioPorDni(String dni) throws Exception {
    	/**
         * PATRON TEMPLATE METHOD:
         */
        DBOperation<Double> op = new DBOperation<Double>() {
            @Override
            protected Double doExecute(Connection con) throws Exception {
                double sueldo = 0;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT n.sueldo FROM empleados e JOIN nominas n ON e.dni = n.empleado_dni WHERE e.dni=?")) {
                    ps.setString(1, dni);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) sueldo = rs.getDouble("sueldo");
                    }
                }
                return sueldo;
            }
        };
        return op.execute();
    }

    public void actualizarEmpleado(Empleado e) throws Exception {
    	/**
         * PATRON TEMPLATE METHOD:
         */
        DBOperation<Void> op = new DBOperation<Void>() {
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

                //Recalcula el salario
                double nuevoSalario = calcularSueldo(e.getCategoria(), e.getAniosTrabajados());
                try (PreparedStatement ps2 = con.prepareStatement(
                        "UPDATE nominas SET sueldo=? WHERE empleado_dni=?")) {
                    ps2.setDouble(1, nuevoSalario);
                    ps2.setString(2, e.getDni());
                    ps2.executeUpdate();
                }
                return null;
            }
        };
        op.execute();
    }

    private double calcularSueldo(int categoria, int anyos) {
        double base;
        switch (categoria) {
            case 1: base = 1000; break;
            case 2: base = 1400; break;
            case 3: base = 1800; break;
            case 4: base = 2300; break;
            default: base = 900; break;
        }
        return Math.round(base + anyos * 25);
    }

    /**
     * PATRON TEMPLATE METHOD:
     * Define una plantilla para operaciones con base de datos, permite reutilizar la estructura: abrir conexion, ejecutar y cerrar 
     * Deja que cada operacion tenga su logica especifica
     */
    private abstract class DBOperation<T> {
        public T execute() throws Exception {
            try (Connection con = Conexion.getConnection()) {
                return doExecute(con);
            }
        }

        protected abstract T doExecute(Connection con) throws Exception;
    }
}
