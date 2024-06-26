package Database;

import java.util.ArrayList;

public class Pregunta {

    private int idPregunta;
    private int idCategoria;
    private String textoPregunta;
    private ArrayList<Respuesta> respuestas;

    public Pregunta(int idPregunta, int idCategoria, String textoPregunta) {
        this.idPregunta = idPregunta;
        this.idCategoria = idCategoria;
        this.textoPregunta = textoPregunta;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public ArrayList<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(ArrayList<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

}