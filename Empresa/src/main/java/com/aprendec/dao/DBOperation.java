package com.aprendec.dao;

import java.sql.Connection;
import com.aprendec.conexion.Conexion;

public abstract class DBOperation<T> {

    public T execute() throws Exception {
        try (Connection con = Conexion.getDataSource().getConnection()) {
            return doExecute(con);
        }
    }

    protected abstract T doExecute(Connection con) throws Exception;
}
