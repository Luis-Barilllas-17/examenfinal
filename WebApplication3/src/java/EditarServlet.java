/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import Clases.ConexionBaseDeDatos;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EditarServlet")
public class EditarServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String isbn = request.getParameter("isbn");
        String titulo_libro = request.getParameter("titulo_libro");
        String anio_publicacion = request.getParameter("anio_publicacion");
        String autor = request.getParameter("autor");
        String clasificacion = request.getParameter("clasificacion");
        String cantidad_paginas = request.getParameter("cantidad_paginas");
        int id_tipo_pasta = Integer.parseInt(request.getParameter("id_tipo_pasta"));

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            ConexionBaseDeDatos conexionBD = new ConexionBaseDeDatos();
            connection = conexionBD.conectar();

            String consulta = "UPDATE libro SET isbn = ?, titulo_libro = ?, anio_publicacion = ?, autor = ?, clasificacion = ?, cantidad_paginas = ?, id_tipo_pasta = ? WHERE isbn = ?";

            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, titulo_libro);
            preparedStatement.setString(3, anio_publicacion);
            preparedStatement.setString(4, autor);
            preparedStatement.setString(5, clasificacion);
            preparedStatement.setString(6, cantidad_paginas); // Corregido el nombre de la columna
            preparedStatement.setInt(7, id_tipo_pasta);
            preparedStatement.setString(8, isbn); // Where condition using the ISBN

            preparedStatement.executeUpdate();

            response.sendRedirect("index.html");
        } catch (SQLException ex) {
            ex.printStackTrace();
            response.sendRedirect("editar.html");
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
