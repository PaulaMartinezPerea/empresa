package com.aprendec.controller.commands;

import javax.servlet.http.*;
import com.aprendec.dao.EmpleadoDAO;

public interface Action {
    void execute(HttpServletRequest request, HttpServletResponse response, EmpleadoDAO dao) throws Exception;
}
