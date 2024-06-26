package Database;

public class Respuesta {

    private int idRespuesta;
    private int idPregunta;
    private String textoRespuesta;
    private boolean esCorrecta;

    public Respuesta(int idRespuesta, int idPregunta, String textoRespuesta, boolean esCorrecta) {
        this.idRespuesta = idRespuesta;
        this.idPregunta = idPregunta;
        this.textoRespuesta = textoRespuesta;
        this.esCorrecta = esCorrecta;
    }

    public int getIdRespuesta() {
        return idRespuesta;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public String getTextoRespuesta() {
        return textoRespuesta;
    }

    public boolean esCorrecta() {
        return esCorrecta;
    }
}