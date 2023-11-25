/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import Clases.ConexionBaseDeDatos;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MostrarTablaServlet")
public class MostrarTablaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Realiza la conexión a la base de datos
            ConexionBaseDeDatos conexionBD = new ConexionBaseDeDatos();
            connection = conexionBD.conectar();

            // Consulta para obtener todos los libros con su tipo de pasta
            String consulta = "SELECT l.*, t.descripcion AS tipo_pasta_desc FROM libro l "////////
                    + "JOIN tipo_pasta t ON l.id_tipo_pasta = t.id_tipo_pasta";
            preparedStatement = connection.prepareStatement(consulta);
            resultSet = preparedStatement.executeQuery();

            // Construye una tabla HTML con los datos de los libros
            StringBuilder htmlTable = new StringBuilder();
            htmlTable.append("<table border=\"1\">");
            htmlTable.append("<thead><tr>"
                    + "<th>#</th>"
                    + "<th>isbn</th>"////// cambiar desde aqui
                    + "<th>Titulo libro</th>"
                    + "<th>Ano publicacion</th>"
                    + "<th>Autor</th>"
                    + "<th>Clasificacion</th>"
                    + "<th>Cantidad paginas</th>"
                    + "<th>Tipo pasta</th>"
                    + "<th>Acciones</th>"
                    + "</tr></thead>");

            htmlTable.append("<tbody>");

            int numeroSecuencia = 1;

            while (resultSet.next()) {
                htmlTable.append("<tr>");
                htmlTable.append("<td>").append(numeroSecuencia++).append("</td>");
                htmlTable.append("<td>").append(resultSet.getString("isbn")).append("</td>");
                htmlTable.append("<td>").append(resultSet.getString("titulo_libro")).append("</td>");
                htmlTable.append("<td>").append(resultSet.getString("anio_publicacion")).append("</td>");
                htmlTable.append("<td>").append(resultSet.getString("autor")).append("</td>");
                htmlTable.append("<td>").append(resultSet.getString("clasificacion")).append("</td>");
                htmlTable.append("<td>").append(resultSet.getString("cantidad_paginas")).append("</td>");
                htmlTable.append("<td>").append(resultSet.getString("tipo_pasta_desc")).append("</td>");
                htmlTable.append("<td>");

                htmlTable.append("<button class=\"editar-btn\" type=\"button\" onclick=\"editarProducto('")
    .append(resultSet.getString("isbn"))
    .append("','")
    .append(resultSet.getString("titulo_libro"))
    .append("','")
    .append(resultSet.getString("anio_publicacion"))
    .append("','")
    .append(resultSet.getString("autor"))
    .append("','")
    .append(resultSet.getString("clasificacion"))
    .append("','")
    .append(resultSet.getString("cantidad_paginas"))
    .append("','")
    .append(resultSet.getString("id_tipo_pasta"))
    .append("')\">Editar</button>");

                htmlTable.append("<button class=\"eliminar-btn\" type=\"button\" onclick=\"eliminarProducto('")
                        .append(resultSet.getString("isbn")).append("')\">Eliminar</button>");

                htmlTable.append("</td>");
                htmlTable.append("</tr>");
            }

            htmlTable.append("</tbody></table>");

            // Imprime la tabla HTML en la respuesta
            out.println(htmlTable.toString());

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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