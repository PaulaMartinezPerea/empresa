package com.aprendec.controller;

import com.aprendec.dao.EmpleadoDAO;
import com.aprendec.model.Empleado;
import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/empresa")
public class Controller extends HttpServlet {
    private static final long serialVersionUID = 1L;

    //PATRON COMMAND:
    //Cada accion del sistema se encapsula como un comando ejecutable
    private interface Action {
        void execute(HttpServletRequest request, HttpServletResponse response, EmpleadoDAO dao) throws Exception;
    }

    private Map<String, Action> actions = new HashMap<>();

    @Override
    public void init() throws ServletException {
    	
        //PATRON FACTORY METHOD:
        //Creamos los comandos usando un metodo fabrica, facilita añadir o cambiar acciones sin modificar init()
        actions.put("listar", createAction("listar"));
        actions.put("sueldo", createAction("sueldo"));
        actions.put("modificar", createAction("modificar"));
    }

    //Crea la accion solicitada
    private Action createAction(String type) {
        switch (type) {
            case "listar":
                return this::listarEmpleados;
            case "sueldo":
                return this::mostrarSueldo;
            case "modificar":
                return this::modificarEmpleado;
            default:
                return this::accionPorDefecto;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //PATRON FRONT CONTROLLER:
        //Centraliza todas las peticiones del sistema en un solo punto /empresa, decide que comando ejecutar segun el parametro accion
        String accion = request.getParameter("accion");
        EmpleadoDAO dao = EmpleadoDAO.getInstance(); //Cambiado ya que ahora en el DAO implemente el patron de diseño singleton

        try {
            Action action = actions.getOrDefault(accion, this::accionPorDefecto);
            action.execute(request, response, dao);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("exception", e);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    //COMANDOS, PATRON COMMAND:
    private void listarEmpleados(HttpServletRequest request, HttpServletResponse response, EmpleadoDAO dao)
            throws Exception {
        List<Empleado> lista = dao.listarEmpleados();
        request.setAttribute("empleados", lista);
        request.getRequestDispatcher("views/MostrarEmpleados.jsp").forward(request, response);
    }

    private void mostrarSueldo(HttpServletRequest request, HttpServletResponse response, EmpleadoDAO dao)
            throws Exception {
        String dni = request.getParameter("dni");
        if (dni != null) {
            double sueldo = dao.buscarSalarioPorDni(dni);
            request.setAttribute("dni", dni);
            request.setAttribute("sueldo", sueldo);
        }
        request.getRequestDispatcher("views/MostrarSalario.jsp").forward(request, response);
    }

    private void modificarEmpleado(HttpServletRequest request, HttpServletResponse response, EmpleadoDAO dao)
            throws Exception {
        if ("guardar".equals(request.getParameter("op"))) {
            Empleado e = new Empleado();
            e.setDni(request.getParameter("dni"));
            e.setNombre(request.getParameter("nombre"));
            e.setSexo(request.getParameter("sexo").charAt(0));
            e.setCategoria(Integer.parseInt(request.getParameter("categoria")));
            e.setAniosTrabajados(Integer.parseInt(request.getParameter("anyos")));
            dao.actualizarEmpleado(e);
        }
        request.getRequestDispatcher("views/ModificarDatos.jsp").forward(request, response);
    }

    private void accionPorDefecto(HttpServletRequest request, HttpServletResponse response, EmpleadoDAO dao)
            throws Exception {
        response.sendRedirect("pagina.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
