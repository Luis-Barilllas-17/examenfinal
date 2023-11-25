/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Clases.ConexionBaseDeDatos;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegistroServlet")
public class RegistroServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String isbn = request.getParameter("isbn");////////////1
            String titulo_libro = request.getParameter("titulo_libro");
            String anio_publicacion = request.getParameter("anio_publicacion");
            String autor = request.getParameter("autor");
            String clasificacion = request.getParameter("clasificacion");
            String cantidad_paginas = request.getParameter("cantidad_paginas");
            int id_tipo_pasta = Integer.parseInt(request.getParameter("id_tipo_pasta"));//////1

            Connection conn = null;
            PreparedStatement stmt = null;

            try {
                // Realiza la conexión a la base de datos
                ConexionBaseDeDatos conexion = new ConexionBaseDeDatos();
                conn = conexion.conectar();

                // Verifica si el libro ya está registrado
                String checkBookQuery = "SELECT * FROM libro WHERE isbn = ?";/////// llave primaria
                PreparedStatement checkBookStmt = conn.prepareStatement(checkBookQuery);
                checkBookStmt.setString(1, isbn);
                ResultSet bookResultSet = checkBookStmt.executeQuery();

                if (bookResultSet.next()) {
                    // El libro ya está registrado, muestra un mensaje de error
                    out.println("<script>alert('El Libro ya existe, actualízalo.'); window.location = 'index.html';</script>");
                } else {
                    // Inserta los datos en la tabla de libros
                    String insertBookQuery = "INSERT INTO libro (isbn, titulo_libro, anio_publicacion, autor, clasificacion, cantidad_paginas, id_tipo_pasta) VALUES (?, ?, ?, ?, ?, ?, ?)";///////3
                    stmt = conn.prepareStatement(insertBookQuery);
                    stmt.setString(1, isbn);/////// por el orden de la base de datos
                    stmt.setString(2, titulo_libro);
                    stmt.setString(3, anio_publicacion);
                    stmt.setString(4, autor);
                    stmt.setString(5, clasificacion);
                    stmt.setString(6, cantidad_paginas);  
                    stmt.setInt(7, id_tipo_pasta);/////// cambiar

                    int rowsAffected = stmt.executeUpdate();

                    if (rowsAffected > 0) {
                        // Registro exitoso
                        out.println("<script>alert('Registro exitoso'); window.location = 'index.html';</script>");
                    } else {
                        // Error en el registro
                        out.println("<script>alert('Error en el registro'); window.location = 'index.html';</script>");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}