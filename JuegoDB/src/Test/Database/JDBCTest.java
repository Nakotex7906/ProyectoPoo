package Database;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class JDBCTest {

    @BeforeEach
    void configurar() throws SQLException {
        Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/preguntas_gui_db", "root", ""
        );

        // Crear una nueva categoría para las pruebas si no existe
        Statement declaracion = conexion.createStatement();
        declaracion.execute("INSERT IGNORE INTO CATEGORIA (CATEGORIA_NOMBRE) VALUES ('Prueba')");

        conexion.close();
    }

    @AfterEach
    void limpiar() throws SQLException {
        Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/preguntas_gui_db", "root", ""
        );

        Statement declaracion = conexion.createStatement();

        // Asegurarse de que las subconsultas devuelvan un solo valor usando IN
        declaracion.execute("DELETE FROM RESPUESTA WHERE PREGUNTA_ID IN (SELECT PREGUNTA_ID FROM PREGUNTA WHERE CATEGORIA_ID IN (SELECT CATEGORIA_ID FROM CATEGORIA WHERE CATEGORIA_NOMBRE = 'Prueba'))");
        declaracion.execute("DELETE FROM PREGUNTA WHERE CATEGORIA_ID IN (SELECT CATEGORIA_ID FROM CATEGORIA WHERE CATEGORIA_NOMBRE = 'Prueba')");
        declaracion.execute("DELETE FROM CATEGORIA WHERE CATEGORIA_NOMBRE = 'Prueba'");

        conexion.close();
    }

    @Test
    void testGuardarPregResp() throws SQLException {
        String pregunta = "¿Cuánto es 2 + 2?";
        String categoria = "Prueba";
        String[] respuestas = {"3", "4", "5", "6"};
        int indiceCorrecto = 1;

        boolean resultado = JDBC.guardarPregResp(pregunta, categoria, respuestas, indiceCorrecto);

        assertTrue(resultado);

        Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/preguntas_gui_db", "root", ""
        );

        Statement declaracion = conexion.createStatement();
        ResultSet rs = declaracion.executeQuery("SELECT * FROM PREGUNTA WHERE PREGUNTA_TEXTO = '¿Cuánto es 2 + 2?'");
        assertTrue(rs.next());
        int idPregunta = rs.getInt("pregunta_id");

        rs = declaracion.executeQuery("SELECT * FROM RESPUESTA WHERE PREGUNTA_ID = " + idPregunta);
        int contador = 0;
        while (rs.next()) {
            contador++;
        }
        assertEquals(4, contador);

        conexion.close();
    }

    @Test
    void testObtenerPreguntas() throws SQLException {
        Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/preguntas_gui_db", "root", ""
        );

        Statement declaracion = conexion.createStatement();

        // Obtener el ID de la categoría de prueba
        ResultSet rs = declaracion.executeQuery("SELECT CATEGORIA_ID FROM CATEGORIA WHERE CATEGORIA_NOMBRE = 'Prueba'");
        assertTrue(rs.next());
        int idCategoria = rs.getInt("CATEGORIA_ID");

        // Insertar la pregunta de prueba
        declaracion.execute("INSERT INTO PREGUNTA (CATEGORIA_ID, PREGUNTA_TEXTO) VALUES (" + idCategoria + ", '¿Cuánto es 2 + 2?')");

        Categoria categoria = new Categoria(idCategoria, "Prueba");

        ArrayList<Pregunta> preguntas = JDBC.obtenerPreguntas(categoria);

        assertNotNull(preguntas);
        assertEquals(1, preguntas.size());
        assertEquals("¿Cuánto es 2 + 2?", preguntas.get(0).getTextoPregunta());

        conexion.close();
    }

    @Test
    void testObtenerCategoria() throws SQLException {
        Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/preguntas_gui_db", "root", ""
        );

        Categoria categoria = JDBC.obtenerCategoria("Prueba");

        assertNotNull(categoria);
        assertEquals("Prueba", categoria.getNombreCategoria());

        conexion.close();
    }

    @Test
    void testObtenerCategorias() throws SQLException {
        Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/preguntas_gui_db", "root", ""
        );

        Statement declaracion = conexion.createStatement();
        declaracion.execute("INSERT IGNORE INTO CATEGORIA (CATEGORIA_NOMBRE) VALUES ('Matemáticas')");
        declaracion.execute("INSERT IGNORE INTO CATEGORIA (CATEGORIA_NOMBRE) VALUES ('Ciencia')");

        ArrayList<String> categorias = JDBC.obtenerCategorias();

        assertNotNull(categorias);
        assertTrue(categorias.contains("Prueba"));
        assertTrue(categorias.contains("Matemáticas"));
        assertTrue(categorias.contains("Ciencia"));

        conexion.close();
    }


    @Test
    void testObtenerRespuestas() throws SQLException {
        Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/preguntas_gui_db", "root", ""
        );

        Statement declaracion = conexion.createStatement();

        // Obtener el ID de la categoría de prueba
        ResultSet rs = declaracion.executeQuery("SELECT CATEGORIA_ID FROM CATEGORIA WHERE CATEGORIA_NOMBRE = 'Prueba'");
        assertTrue(rs.next());
        int idCategoria = rs.getInt("CATEGORIA_ID");

        // Insertar la pregunta de prueba
        declaracion.execute("INSERT INTO PREGUNTA (CATEGORIA_ID, PREGUNTA_TEXTO) VALUES (" + idCategoria + ", '¿Cuánto es 3 + 3?')");

        // Obtener el ID de la pregunta insertada
        rs = declaracion.executeQuery("SELECT PREGUNTA_ID FROM PREGUNTA WHERE PREGUNTA_TEXTO = '¿Cuánto es 3 + 3?' AND CATEGORIA_ID = " + idCategoria);
        assertTrue(rs.next());
        int idPregunta = rs.getInt("PREGUNTA_ID");

        // Insertar respuestas para la pregunta de prueba
        declaracion.execute("INSERT INTO RESPUESTA (PREGUNTA_ID, RESPUESTA_TEXTO, ES_CORRECTO) VALUES (" + idPregunta + ", '5', 0)");
        declaracion.execute("INSERT INTO RESPUESTA (PREGUNTA_ID, RESPUESTA_TEXTO, ES_CORRECTO) VALUES (" + idPregunta + ", '6', 1)");
        declaracion.execute("INSERT INTO RESPUESTA (PREGUNTA_ID, RESPUESTA_TEXTO, ES_CORRECTO) VALUES (" + idPregunta + ", '7', 0)");
        declaracion.execute("INSERT INTO RESPUESTA (PREGUNTA_ID, RESPUESTA_TEXTO, ES_CORRECTO) VALUES (" + idPregunta + ", '8', 0)");

        Pregunta pregunta = new Pregunta(idPregunta, idCategoria, "¿Cuánto es 3 + 3?");

        ArrayList<Respuesta> respuestas = JDBC.obtenerRespuestas(pregunta);

        assertNotNull(respuestas);
        assertEquals(4, respuestas.size());

        assertEquals("5", respuestas.get(0).getTextoRespuesta());
        assertFalse(respuestas.get(0).esCorrecta());

        assertEquals("6", respuestas.get(1).getTextoRespuesta());
        assertTrue(respuestas.get(1).esCorrecta());

        assertEquals("7", respuestas.get(2).getTextoRespuesta());
        assertFalse(respuestas.get(2).esCorrecta());
        assertEquals("8", respuestas.get(3).getTextoRespuesta());
        assertFalse(respuestas.get(3).esCorrecta());

        conexion.close();
    }
}