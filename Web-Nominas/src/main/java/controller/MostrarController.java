package controller;

import dao.EmpleadoDAO;
import model.Empleado;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet encargado de manejar operaciones sobre los empleados.
 * @WebServlet("/empresa") define la URL donde se accederá al servlet.
 */
@WebServlet("/empresa")
public class MostrarController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    //Instancia del DAO para acceder a la base de datos
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    /**
     * Constructor del servlet
     */
    public MostrarController() {
        super();
    }

    /**
     * Método que maneja las solicitudes GET
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Obtiene opcion para ver que acción hacer
        String opcion = request.getParameter("opcion");

        //Si no se proporciona ninguna opción, redirige a pagina.jsp
        if (opcion == null) {
            response.sendRedirect("pagina.jsp");
            return;
        }

        //Se ejecuta la acción según opcion
        switch (opcion) {
            case "mostrar":
                mostrarTodos(request, response);
                break;
            case "verSueldo":
                mostrarFormularioSueldo(request, response);
                break;
            case "modificar":
                mostrarFormularioModificar(request, response);
                break;
            default:
                response.sendRedirect("pagina.jsp");
                break;
        }
    }

    /**
     * Método que maneja las solicitudes POST
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String opcion = request.getParameter("opcion");

        //Si no se proporciona ninguna opción, redirige a pagina.jsp
        if (opcion == null) {
            response.sendRedirect("pagina.jsp");
            return;
        }

        //Se ejecuta la acción según opcion
        switch (opcion) {
            case "verSueldo":
                buscarSueldo(request, response);
                break;
            case "modificar":
                try {
                    modificarEmpleado(request, response);
                } catch (ServletException | IOException | SQLException emp) {
                    //Imprime la excepción
                    emp.printStackTrace();
                }
                break;
            default:
                response.sendRedirect("pagina.jsp");
                break;
        }
    }

    /**
     * Muestra todos los empleados
     */
    private void mostrarTodos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Empleado> empresa = empleadoDAO.obtenerTodos(); //Obtiene todos los empleados
        request.setAttribute("empresa", empresa); //Los coloca como atributo en la request
        request.getRequestDispatcher("/views/mostrarEmpleados.jsp").forward(request, response); //Redirige a mi mostrarEmpleados.jsp
    }

    /**
     * Muestra el formulario para consultar sueldo por DNI
     */
    private void mostrarFormularioSueldo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/views/consultarSueldo.jsp").forward(request, response); //Redirige a mi consultarSueldo.jsp
    }

    /**
     * Busca el sueldo de un empleado según el DNI ingresado
     */
    private void buscarSueldo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dni = request.getParameter("dni");

        //Si no se introduce un dni salta un mensaje de error, si se introduce un dni valido continua la opcion
        if (dni == null || dni.trim().isEmpty()) {
            request.setAttribute("error", "Debe introducir un DNI.");
            request.getRequestDispatcher("/views/consultarSueldo.jsp").forward(request, response); //Redirige a mi consultarSueldo.jsp
            return;
        }

        double sueldo = empleadoDAO.obtenerSueldoPorDNI(dni); //Obtiene el sueldo

        //Si el sueldo es menor a 0 salta un mensaje de que no hay sueldo, si es mayor a 0 muestra el dni del empleado y su sueldo
        if (sueldo < 0) {
            request.setAttribute("mensaje", "No se encontró el sueldo para el DNI: " + dni);
        } else {
            request.setAttribute("mensaje", "El sueldo del empleado con DNI " + dni + " es: " + sueldo);
        }

        request.getRequestDispatcher("/views/consultarSueldo.jsp").forward(request, response); //Redirige a mi consultarSueldo.jsp
    }

    /**
     * Muestra el formulario para modificar un empleado (Mi apartado para modificar no logre que funcionara)
     */
    private void mostrarFormularioModificar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dni = request.getParameter("dni");

        if (dni != null) {
            Empleado emp = empleadoDAO.buscarPorDNI(dni); //Busca empleado por dni (como en el apartado de mostrar el sueldo)
            if (emp != null) {
                request.setAttribute("empresa", emp);
                request.getRequestDispatcher("/views/modificarEmpleado.jsp").forward(request, response); //Redirige a mi modificarEmpleado.jsp
            } else {
                request.setAttribute("error", "No se encontró el empleado con DNI: " + dni);
                request.getRequestDispatcher("/views/modificarEmpleado.jsp").forward(request, response); //Redirige a mi modificarEmpleado.jsp
            }
        } else {
            //Si no hay dni, muestra el formulario para buscar al empleado
            request.getRequestDispatcher("/views/buscarEmpleadoModificar.jsp").forward(request, response); //Redirige a mi buscarEmpleadoModificar.jsp
        }
    }

    /**
     * Modifica los datos de un empleado según los datos del formulario (Mi apartado para modificar no logre que funcionara)
     */
    private void modificarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        //Obtiene los datos del formulario
        String dni = request.getParameter("dni");
        String nombre = request.getParameter("nombre");
        String genero = request.getParameter("genero");
        String categoriaStr = request.getParameter("categoria");
        String anyosStr = request.getParameter("anyos_trabajados");

        //Validación de campos vacios, los datos no pueden estar vacios
        if (dni == null || nombre == null || genero == null || categoriaStr == null || anyosStr == null
                || dni.trim().isEmpty() || nombre.trim().isEmpty() || genero.trim().isEmpty()
                || categoriaStr.trim().isEmpty() || anyosStr.trim().isEmpty()) {

            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("/views/modificarEmpleado.jsp").forward(request, response); //Redirige a mi modificarEmpleado.jsp
            return;
        }

        try {
        	//Convierte a entero
            int categoria = Integer.parseInt(categoriaStr); 
            int anyos_trabajados = Integer.parseInt(anyosStr); 

            Empleado emp = new Empleado(nombre, dni, (char) 0, categoria, anyos_trabajados); //Crea el objeto empleado con sus datos

            boolean modificado = empleadoDAO.modificarEmpleado(emp); //Llama a mi EmpleadoDAO para modificar

            //Si se modifico correctamente, muestra un mensaje de que fue bien, si no lo contrario
            if (modificado) {
                request.setAttribute("mensaje", "Empleado modificado correctamente.");
                request.setAttribute("empresa", emp);
            } else {
                request.setAttribute("error", "No se pudo modificar el empleado.");
            }

            request.getRequestDispatcher("/views/modificarEmpleado.jsp").forward(request, response); //Redirige a mi modificarEmpleado.jsp

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Categoría y años trabajados deben ser números válidos.");
            request.getRequestDispatcher("/views/modificarEmpleado.jsp").forward(request, response); //Redirige a mi modificarEmpleado.jsp
        }
    }
}
