package dao;

import model.Empleado;
import model.Nomina;
import conexion.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    private Connection connection;
    private PreparedStatement statement;
    private boolean estadoOperacion;

    //Obtener todos los empleados
    public List<Empleado> obtenerTodos() {
        List<Empleado> empleados = new ArrayList<>();
        ResultSet rs = null;
        try {
            connection = Conexion.getConnection();
            String sql = "SELECT e.dni, e.nombre, e.genero, e.categoria, e.anyos_trabajados, n.sueldo " +
            		 "FROM empleados e LEFT JOIN nominas n ON e.dni = n.dni " +
            		 "ORDER BY e.nombre";
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();

            while (rs.next()) {
                empleados.add(new Empleado(
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("genero").charAt(0),
                        rs.getInt("categoria"),
                        rs.getInt("anyos_trabajados")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return empleados;
    }

    //Buscar empleado por DNI
    public Empleado buscarPorDNI(String dni) {
        Empleado emp = null;
        ResultSet rs = null;
        try {
            connection = Conexion.getConnection();
            String sql = "SELECT * FROM empleados WHERE dni = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, dni);
            rs = statement.executeQuery();

            if (rs.next()) {
                emp = new Empleado(
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("genero").charAt(0),
                        rs.getInt("categoria"),
                        rs.getInt("anyos_trabajados")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return emp;
    }

    //Modificar datos de un empleado (menos sueldo)
    public boolean modificarEmpleado(Empleado emp) throws SQLException {
    	Connection c = null;
        PreparedStatement psUpdateEmp = null;
        PreparedStatement psUpdateNom = null;
        PreparedStatement psInsertNom = null;
        ResultSet rs = null;

        String sqlUpdateEmp = "UPDATE empleados SET nombre = ?, genero = ?, categoria = ?, anyos_trabajados = ? WHERE dni = ?";
        String sqlUpdateNom = "UPDATE nominas SET sueldo = ? WHERE dni = ?";
        String sqlInsertNom = "INSERT INTO nominas(dni, sueldo) VALUES(?, ?)";
        String sqlCheckNom = "SELECT id FROM nominas WHERE dni = ?";

        try {
            c = Conexion.getConnection();
            c.setAutoCommit(false);

            psUpdateEmp = c.prepareStatement(sqlUpdateEmp);
            psUpdateEmp.setString(1, emp.getNombre());
            psUpdateEmp.setString(2, String.valueOf(emp.getSexo()));
            psUpdateEmp.setInt(3, emp.getCategoria());
            psUpdateEmp.setInt(4, emp.getAnyos());
            psUpdateEmp.setString(5, emp.getDni());
            int rowsEmp = psUpdateEmp.executeUpdate();

            double nuevoSueldo = calcularSueldo(emp.getCategoria(), emp.getAnyos());

            PreparedStatement psCheck = c.prepareStatement(sqlCheckNom);
            psCheck.setString(1, emp.getDni());
            rs = psCheck.executeQuery();
            boolean existeNomina = rs.next();
            rs.close();
            psCheck.close();

            if (existeNomina) {
                psUpdateNom = c.prepareStatement(sqlUpdateNom);
                psUpdateNom.setDouble(1, nuevoSueldo);
                psUpdateNom.setString(2, emp.getDni());
                psUpdateNom.executeUpdate();
            } else {
                psInsertNom = c.prepareStatement(sqlInsertNom);
                psInsertNom.setString(1, emp.getDni());
                psInsertNom.setDouble(2, nuevoSueldo);
                psInsertNom.executeUpdate();
            }

            c.commit();
            return rowsEmp > 0;
        } catch (SQLException ex) {
            if (c != null) c.rollback();
            throw ex;
        } finally {
            if (rs != null) rs.close();
            if (psUpdateEmp != null) psUpdateEmp.close();
            if (psUpdateNom != null) psUpdateNom.close();
            if (psInsertNom != null) psInsertNom.close();
            if (c != null) {
                c.setAutoCommit(true);
                c.close();
            }
        }
    }

    //Obtener sueldo por DNI
    public double obtenerSueldoPorDNI(String dni) {
        double sueldo = -1;
        ResultSet rs = null;
        try {
            connection = Conexion.getConnection();
            String sql = "SELECT sueldo FROM nominas WHERE dni = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, dni);
            rs = statement.executeQuery();

            if (rs.next()) {
                sueldo = rs.getDouble("sueldo");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return sueldo;
    }
    
    //Calculo por categoria segun los años trabajados
    private double calcularSueldo(int categoria, int anyos_trabajados) {
        double base;
        switch (categoria) {
            case 1: base = 1000; break;
            case 2: base = 1400; break;
            case 3: base = 1800; break;
            case 4: base = 2300; break;
            default: base = 900; break;
        }
        return Math.round((base + anyos_trabajados * 25) * 100.0) / 100.0;
    }
}
