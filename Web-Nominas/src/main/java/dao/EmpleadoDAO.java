package dao;

import model.Empleado;
import model.Nomina;
import conexion.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) que gestiona todas las operaciones CRUD
 * relacionadas con la entidad Empleado y sus nóminas en la base de datos.
 */
public class EmpleadoDAO {

    //Objetos para la conexión y ejecucion de consultas
    private Connection connection;
    private PreparedStatement statement;
    private boolean estadoOperacion;

    /**
     * Obtiene una lista con todos los empleados registrados en la base de datos,
     * incluyendo su información y, si existe, el sueldo asociado en la tabla nominas.
     */
    public List<Empleado> obtenerTodos() {
        List<Empleado> empleados = new ArrayList<>();
        ResultSet rs = null;
        try {
            //Establecer conexión con la base de datos
            connection = Conexion.getConnection();

            //Consulta SQL con LEFT JOIN para traer los empleados
            String sql = "SELECT e.dni, e.nombre, e.genero, e.categoria, e.anyos_trabajados, n.sueldo " +
                         "FROM empleados e LEFT JOIN nominas n ON e.dni = n.dni " +
                         "ORDER BY e.nombre";

            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();

            //Recorre el resultado y crear Empleado
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
            //Cierre para evitar problemas
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return empleados;
    }

    /**
     * Busca y devuelve un empleado según su número de DNI.
     * Si no existe, devuelve null.
     */
    public Empleado buscarPorDNI(String dni) {
        Empleado emp = null;
        ResultSet rs = null;
        try {
            connection = Conexion.getConnection();
            String sql = "SELECT * FROM empleados WHERE dni = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, dni);
            rs = statement.executeQuery();

            //Si existe el registro, crea un objeto Empleado con los datos obtenidos
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

    /**
     * Modifica los datos de un empleado existente (excepto el sueldo, que se calcula).
     * Si el empleado no tiene nómina, la crea.
     * Se usa una transacción para asegurar que ambos cambios (empleado y nómina) se apliquen juntos.
     */
    public boolean modificarEmpleado(Empleado emp) throws SQLException {
        Connection c = null;
        PreparedStatement psUpdateEmp = null;
        PreparedStatement psUpdateNom = null;
        PreparedStatement psInsertNom = null;
        ResultSet rs = null;

        //Consultas SQL utilizadas
        String sqlUpdateEmp = "UPDATE empleados SET nombre = ?, genero = ?, categoria = ?, anyos_trabajados = ? WHERE dni = ?";
        String sqlUpdateNom = "UPDATE nominas SET sueldo = ? WHERE dni = ?";
        String sqlInsertNom = "INSERT INTO nominas(dni, sueldo) VALUES(?, ?)";
        String sqlCheckNom = "SELECT id FROM nominas WHERE dni = ?";

        try {
            c = Conexion.getConnection();
            c.setAutoCommit(false); //Desactiva autocommit

            //Actualiza los datos del empleado
            psUpdateEmp = c.prepareStatement(sqlUpdateEmp);
            psUpdateEmp.setString(1, emp.getNombre());
            psUpdateEmp.setString(2, String.valueOf(emp.getSexo()));
            psUpdateEmp.setInt(3, emp.getCategoria());
            psUpdateEmp.setInt(4, emp.getAnyos());
            psUpdateEmp.setString(5, emp.getDni());
            int rowsEmp = psUpdateEmp.executeUpdate();

            //Calcula un nuevo sueldo en base a categoría y años
            double nuevoSueldo = calcularSueldo(emp.getCategoria(), emp.getAnyos());

            //Verifica si ya existe una nómina para este empleado
            PreparedStatement psCheck = c.prepareStatement(sqlCheckNom);
            psCheck.setString(1, emp.getDni());
            rs = psCheck.executeQuery();
            boolean existeNomina = rs.next();
            rs.close();
            psCheck.close();

            //Si ya tiene nómina se actualiza
            if (existeNomina) {
                psUpdateNom = c.prepareStatement(sqlUpdateNom);
                psUpdateNom.setDouble(1, nuevoSueldo);
                psUpdateNom.setString(2, emp.getDni());
                psUpdateNom.executeUpdate();
            } 
            //Si no tiene se inserta nueva nómina
            else {
                psInsertNom = c.prepareStatement(sqlInsertNom);
                psInsertNom.setString(1, emp.getDni());
                psInsertNom.setDouble(2, nuevoSueldo);
                psInsertNom.executeUpdate();
            }

            c.commit();
            return rowsEmp > 0; //Devuelve true si se actualizo 
        } catch (SQLException ex) {
            //En caso de error, deshace los cambios
            if (c != null) c.rollback();
            throw ex;
        } finally {
            if (rs != null) rs.close();
            if (psUpdateEmp != null) psUpdateEmp.close();
            if (psUpdateNom != null) psUpdateNom.close();
            if (psInsertNom != null) psInsertNom.close();
            if (c != null) {
                c.setAutoCommit(true); // Volver al modo autocommit
                c.close();
            }
        }
    }

    /**
     * Obtiene el sueldo actual asociado al DNI de un empleado.
     * Si no existe, devuelve -1.
     */
    public double obtenerSueldoPorDNI(String dni) {
        double sueldo = -1;
        ResultSet rs = null;
        try {
            connection = Conexion.getConnection();
            String sql = "SELECT sueldo FROM nominas WHERE dni = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, dni);
            rs = statement.executeQuery();

            //Si existe una nomina, obtener el sueldo
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
    
    /**
     * Calcula el sueldo de un empleado en función de su categoría y años trabajados.
     * Cada categoría tiene un sueldo base diferente y se añade un incremento por años.
     */
    private double calcularSueldo(int categoria, int anyos_trabajados) {
        double base;

        //Define el sueldo base según la categoría
        switch (categoria) {
            case 1: base = 1000; break;
            case 2: base = 1400; break;
            case 3: base = 1800; break;
            case 4: base = 2300; break;
            default: base = 900; break; 
        }

        //Calcula el sueldo total (base + 25 por año trabajado)
        return Math.round((base + anyos_trabajados * 25) * 100.0) / 100.0;
    }
}
