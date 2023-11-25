/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import Clases.ConexionBaseDeDatos;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EliminarServlet")
public class EliminarServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Recuperar el parámetro del código del producto a eliminar
            String isbn = request.getParameter("isbn");////llave primaria

            // Realiza la conexión a la base de datos
            ConexionBaseDeDatos conexionBD = new ConexionBaseDeDatos();
            connection = conexionBD.conectar();

            // Consulta para eliminar al producto por código
            String consulta = "DELETE FROM libro WHERE isbn = ?";/////// nombre de tabla / llave primaria
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, isbn);////
            preparedStatement.executeUpdate();

            // Imprime una respuesta para indicar que la eliminación fue exitosa
            out.println("Producto eliminado con éxito");

        } catch (SQLException ex) {
            ex.printStackTrace();
            out.println("Error al intentar eliminar el producto");
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}