package com.aprendec.controller;

import com.aprendec.controller.commands.*;

public class CommandFactory {

    public static Action getAction(String accion) {
        if (accion == null) return new DefaultCommand();

        return switch (accion) {
            case "listar" -> new ListarEmpleadosCommand();
            case "sueldo" -> new MostrarSueldoCommand();
            case "modificar" -> new ModificarEmpleadoCommand();
            default -> new DefaultCommand();
        };
    }
}
