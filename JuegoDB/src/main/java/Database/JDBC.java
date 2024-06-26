package Database;

import java.sql.*;
import java.util.ArrayList;

public class JDBC {

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/preguntas_gui_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    public static boolean guardarPregResp(String pregunta, String categoria, String[] respuestas, int indiceCorrecto) {
        try {
            // establecer una conexión con la base de datos
            Connection conexion = DriverManager.getConnection(
                    DB_URL, DB_USERNAME, DB_PASSWORD
            );

            // insertar la categoría si es nueva, de lo contrario, recuperarla de la base de datos
            Categoria categoriaObj = obtenerCategoria(categoria);
            if (categoriaObj == null) {
                // insertar nueva categoría en la base de datos
                categoriaObj = insertarCategoria(categoria);
            }

            // insertar pregunta en la base de datos
            Pregunta preguntaObj = insertarPregunta(categoriaObj, pregunta);

            // insertar respuestas en la base de datos
            return insertarRespuestas(preguntaObj, respuestas, indiceCorrecto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // métodos de pregunta
    public static ArrayList<Pregunta> obtenerPreguntas(Categoria categoria) {
        ArrayList<Pregunta> preguntas = new ArrayList<>();
        try {
            Connection conexion = DriverManager.getConnection(
                    DB_URL, DB_USERNAME, DB_PASSWORD
            );

            // consulta que recupera todas las preguntas de una categoría en orden aleatorio
            PreparedStatement consultaObtenerPreguntas = conexion.prepareStatement(
                    "SELECT * FROM PREGUNTA JOIN CATEGORIA " +
                            "ON PREGUNTA.CATEGORIA_ID = CATEGORIA.CATEGORIA_ID " +
                            "WHERE CATEGORIA.CATEGORIA_NOMBRE = ? ORDER BY RAND()"
            );
            consultaObtenerPreguntas.setString(1, categoria.getNombreCategoria());

            ResultSet resultado = consultaObtenerPreguntas.executeQuery();
            while (resultado.next()) {
                int idPregunta = resultado.getInt("pregunta_id");
                int idCategoria = resultado.getInt("categoria_id");
                String preguntaTexto = resultado.getString("pregunta_texto");
                preguntas.add(new Pregunta(idPregunta, idCategoria, preguntaTexto));
            }

            return preguntas;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // devuelve null si no pudo encontrar las preguntas en la base de datos
        return null;
    }

    private static Pregunta insertarPregunta(Categoria categoria, String textoPregunta) {
        try {
            Connection conexion = DriverManager.getConnection(
                    DB_URL, DB_USERNAME, DB_PASSWORD
            );

            PreparedStatement consultaInsertarPregunta = conexion.prepareStatement(
                    "INSERT INTO PREGUNTA(CATEGORIA_ID, PREGUNTA_TEXTO) " +
                            "VALUES(?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            consultaInsertarPregunta.setInt(1, categoria.getIdCategoria());
            consultaInsertarPregunta.setString(2, textoPregunta);
            consultaInsertarPregunta.executeUpdate();

            // verificar el id de la pregunta
            ResultSet resultado = consultaInsertarPregunta.getGeneratedKeys();
            if (resultado.next()) {
                int idPregunta = resultado.getInt(1);
                return new Pregunta(idPregunta, categoria.getIdCategoria(), textoPregunta);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // devuelve null si hubo un error al insertar la pregunta en la base de datos
        return null;
    }

    // métodos de categoría
    public static Categoria obtenerCategoria(String categoria) {
        try {
            Connection conexion = DriverManager.getConnection(
                    DB_URL, DB_USERNAME, DB_PASSWORD
            );

            PreparedStatement consultaObtenerCategoria = conexion.prepareStatement(
                    "SELECT * FROM CATEGORIA WHERE CATEGORIA_NOMBRE = ?"
            );
            consultaObtenerCategoria.setString(1, categoria);

            // ejecutar consulta y almacenar resultados
            ResultSet resultado = consultaObtenerCategoria.executeQuery();
            if (resultado.next()) {
                // encontró la categoría
                int idCategoria = resultado.getInt("categoria_id");
                return new Categoria(idCategoria, categoria);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // devuelve null si no pudo encontrar la categoría en la base de datos
        return null;
    }

    public static ArrayList<String> obtenerCategorias() {
        ArrayList<String> listaCategorias = new ArrayList<>();
        try {
            Connection conexion = DriverManager.getConnection(
                    DB_URL, DB_USERNAME, DB_PASSWORD
            );

            Statement consultaObtenerCategorias = conexion.createStatement();
            ResultSet resultado = consultaObtenerCategorias.executeQuery("SELECT * FROM CATEGORIA");

            while (resultado.next()) {
                String nombreCategoria = resultado.getString("categoria_nombre");
                listaCategorias.add(nombreCategoria);
            }

            return listaCategorias;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Categoria insertarCategoria(String categoria) {
        try {
            Connection conexion = DriverManager.getConnection(
                    DB_URL, DB_USERNAME, DB_PASSWORD
            );

            PreparedStatement consultaInsertarCategoria = conexion.prepareStatement(
                    "INSERT INTO CATEGORIA(CATEGORIA_NOMBRE) " +
                            "VALUES(?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            consultaInsertarCategoria.setString(1, categoria);
            consultaInsertarCategoria.executeUpdate();

            ResultSet resultado = consultaInsertarCategoria.getGeneratedKeys();
            if (resultado.next()) {
                int idCategoria = resultado.getInt(1);
                return new Categoria(idCategoria, categoria);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static ArrayList<Respuesta> obtenerRespuestas(Pregunta pregunta) {
        ArrayList<Respuesta> respuestas = new ArrayList<>();
        try {
            Connection conexion = DriverManager.getConnection(
                    DB_URL, DB_USERNAME, DB_PASSWORD
            );

            // consulta que recupera todas las respuestas de una pregunta en orden aleatorio
            PreparedStatement consultaObtenerRespuestas = conexion.prepareStatement(
                    "SELECT * FROM PREGUNTA JOIN RESPUESTA " +
                            "ON PREGUNTA.PREGUNTA_ID = RESPUESTA.PREGUNTA_ID " +
                            "WHERE PREGUNTA.PREGUNTA_ID = ? ORDER BY RAND()"
            );
            consultaObtenerRespuestas.setInt(1, pregunta.getIdPregunta());

            ResultSet resultado = consultaObtenerRespuestas.executeQuery();
            while (resultado.next()) {
                int idRespuesta = resultado.getInt("respuesta_id");
                String textoRespuesta = resultado.getString("respuesta_texto");
                boolean esCorrecta = resultado.getBoolean("es_correcto");
                Respuesta respuesta = new Respuesta(idRespuesta, pregunta.getIdPregunta(), textoRespuesta, esCorrecta);
                respuestas.add(respuesta);
            }

            return respuestas;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // devuelve null si no pudo encontrar las respuestas en la base de datos
        return null;
    }

    private static boolean insertarRespuestas(Pregunta pregunta, String[] respuestas, int indiceCorrecto) {
        try {
            Connection conexion = DriverManager.getConnection(
                    DB_URL, DB_USERNAME, DB_PASSWORD
            );

            PreparedStatement consultaInsertarRespuesta = conexion.prepareStatement(
                    "INSERT INTO RESPUESTA(PREGUNTA_ID, RESPUESTA_TEXTO, ES_CORRECTO) " +
                            "VALUES(?, ?, ?)"
            );
            consultaInsertarRespuesta.setInt(1, pregunta.getIdPregunta());

            for (int i = 0; i < respuestas.length; i++) {
                consultaInsertarRespuesta.setString(2, respuestas[i]);

                if (i == indiceCorrecto) {
                    consultaInsertarRespuesta.setBoolean(3, true);
                } else {
                    consultaInsertarRespuesta.setBoolean(3, false);
                }

                consultaInsertarRespuesta.executeUpdate();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}