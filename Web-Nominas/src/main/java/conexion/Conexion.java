package conexion;
 
import java.sql.Connection;
import java.sql.SQLException;
 
import javax.sql.DataSource;
 
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
 
//Conexion a la base de datos
public class Conexion {
 private static BasicDataSource dataSource = null;
 
 private static DataSource getDataSource() {
  if (dataSource == null) {
   dataSource = new BasicDataSource();
   dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
   dataSource.setUsername("root");
   dataSource.setPassword("root");		 
   dataSource.setUrl("jdbc:mysql://localhost:3306/empresa?useTimezone=true&serverTimezone=UTC");	
   dataSource.setInitialSize(20);
   dataSource.setMaxIdle(15);
   dataSource.setMaxTotal(20);
  }
  return dataSource;
 }
 
 public static Connection getConnection() throws SQLException {
  return getDataSource().getConnection();
 }
}