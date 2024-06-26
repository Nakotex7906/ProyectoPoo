package Pantalla;

import Database.Respuesta;
import Database.Categoria;
import Database.JDBC;
import Database.Pregunta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class PantallaPreguntas extends JFrame implements ActionListener {
    private JLabel etiquetaPuntuacion;
    private JTextArea areaTextoPregunta;
    private JButton[] botonesRespuesta;
    private JButton botonSiguiente;

    private Categoria categoria;
    private ArrayList<Pregunta> preguntas;
    private Pregunta preguntaActual;
    private int numeroPreguntaActual;
    private int numeroPreguntas;
    private int puntuacion;
    private boolean primeraOpcionSeleccionada;

    public PantallaPreguntas(Categoria categoria, int numeroPreguntas) {
        super("Juego de Quiz");
        Color grisOscuro = new Color(0x2C3E50);

        setSize(480, 700);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(grisOscuro);

        botonesRespuesta = new JButton[4];
        this.categoria = categoria;

        // Carga las preguntas basadas en la categoría
        preguntas = JDBC.obtenerPreguntas(categoria);

        // Ajusta el número de preguntas que extrae de la base de datos
        this.numeroPreguntas = Math.min(numeroPreguntas, preguntas.size());

        // Carga las respuestas para cada pregunta
        for (Pregunta pregunta : preguntas) {
            ArrayList<Respuesta> respuestas = JDBC.obtenerRespuestas(pregunta);
            pregunta.setRespuestas(respuestas);
        }

        // Carga la pregunta actual
        preguntaActual = preguntas.get(numeroPreguntaActual);

        agregarComponentes();

        // Listener para ajustar la posición de la etiqueta de puntuación cuando se redimensiona la ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                etiquetaPuntuacion.setBounds(getWidth() - 175, 15, 170, 20);
            }
        });
    }

    private void agregarComponentes() {
        Color grisOscuro = new Color(0x2C3E50);
        Color azul = new Color(0x3498DB);
        Color amarillo = new Color(0xF1C40F);
        Color blanco = new Color(0xFFFFFF);

        // Etiqueta de tema
        JLabel etiquetaTema = new JLabel("Tema: " + categoria.getNombreCategoria());
        etiquetaTema.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetaTema.setBounds(20, 20, 300, 20);
        etiquetaTema.setForeground(azul);
        add(etiquetaTema);

        // Etiqueta de puntuación
        etiquetaPuntuacion = new JLabel("Puntuación: " + puntuacion + "/" + numeroPreguntas);
        etiquetaPuntuacion.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetaPuntuacion.setBounds(getWidth() - 185, 20, 180, 20);
        etiquetaPuntuacion.setForeground(azul);
        add(etiquetaPuntuacion);

        // Área de texto de la pregunta
        areaTextoPregunta = new JTextArea(preguntaActual.getTextoPregunta());
        areaTextoPregunta.setFont(new Font("Arial", Font.BOLD, 28));
        areaTextoPregunta.setBounds(20, 60, 440, 120);
        areaTextoPregunta.setLineWrap(true);
        areaTextoPregunta.setWrapStyleWord(true);
        areaTextoPregunta.setEditable(false);
        areaTextoPregunta.setForeground(azul);
        areaTextoPregunta.setBackground(grisOscuro);
        add(areaTextoPregunta);

        agregarComponentesRespuestas();

        // Botón de regresar al título
        JButton botonRegresarTitulo = new JButton("Regresar al Título");
        botonRegresarTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        botonRegresarTitulo.setBounds(80, 500, 320, 40);
        botonRegresarTitulo.setBackground(amarillo);
        botonRegresarTitulo.setForeground(grisOscuro);
        botonRegresarTitulo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Carga la pantalla de título
                PantallaPrincipal pantallaPrincipal = new PantallaPrincipal();
                pantallaPrincipal.setLocationRelativeTo(PantallaPreguntas.this);

                // Cierra esta pantalla
                PantallaPreguntas.this.dispose();

                // Muestra la pantalla de título
                pantallaPrincipal.setVisible(true);
            }
        });
        add(botonRegresarTitulo);

        // Botón siguiente
        botonSiguiente = new JButton("Siguiente");
        botonSiguiente.setFont(new Font("Arial", Font.BOLD, 16));
        botonSiguiente.setBounds(300, 570, 120, 40);
        botonSiguiente.setBackground(amarillo);
        botonSiguiente.setForeground(grisOscuro);
        botonSiguiente.setVisible(false);
        botonSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Oculta el botón siguiente
                botonSiguiente.setVisible(false);

                primeraOpcionSeleccionada = false;

                // Pasa a la siguiente pregunta
                siguientePregunta();
            }
        });
        add(botonSiguiente);
    }

    private void agregarComponentesRespuestas() {
        Color grisOscuro = new Color(0x2C3E50);
        Color blanco = new Color(0xFFFFFF);

        int espacioVertical = 70;

        for (int i = 0; i < preguntaActual.getRespuestas().size(); i++) {
            Respuesta respuesta = preguntaActual.getRespuestas().get(i);

            JButton botonRespuesta = new JButton(respuesta.getTextoRespuesta());
            botonRespuesta.setBounds(80, 200 + (i * espacioVertical), 320, 50);
            botonRespuesta.setFont(new Font("Arial", Font.BOLD, 18));
            botonRespuesta.setHorizontalAlignment(SwingConstants.LEFT);
            botonRespuesta.setBackground(blanco);
            botonRespuesta.setForeground(grisOscuro);
            botonRespuesta.addActionListener(this);
            botonesRespuesta[i] = botonRespuesta;
            add(botonesRespuesta[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Color verdeClaro = new Color(0x00FF00);
        Color rojoBrillante = new Color(0xFF0000);
        JButton botonRespuesta = (JButton) e.getSource();

        // Encuentra la respuesta correcta
        Respuesta respuestaCorrecta = null;
        for (Respuesta respuesta : preguntaActual.getRespuestas()) {
            if (respuesta.esCorrecta()) {
                respuestaCorrecta = respuesta;
                break;
            }
        }

        // Verifica si la respuesta seleccionada es la correcta
        if (botonRespuesta.getText().equals(respuestaCorrecta.getTextoRespuesta())) {
            // Presiona la respuesta correcta, incrementa la puntuación
            if (!primeraOpcionSeleccionada) {
                puntuacion++;
                etiquetaPuntuacion.setText("Puntuación: " + puntuacion + "/" + numeroPreguntas);
            }

            // Cambia el botón a verde
            botonRespuesta.setBackground(verdeClaro);
        } else {
            // Presiona una respuesta incorrecta, cambia el botón a rojo
            botonRespuesta.setBackground(rojoBrillante);
        }

        // Temporizador para pasar automáticamente
        Timer timer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pasa a la siguiente pregunta
                siguientePregunta();
                ((Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(false);
        timer.start();

        primeraOpcionSeleccionada = false;
    }

    private void siguientePregunta() {
        // Verifica si ya estamos en la última pregunta
        if (numeroPreguntaActual < numeroPreguntas - 1) {
            // Actualiza la pregunta actual a la siguiente pregunta
            preguntaActual = preguntas.get(++numeroPreguntaActual);
            areaTextoPregunta.setText(preguntaActual.getTextoPregunta());

            // Restablece y actualiza los botones de respuesta
            for (int i = 0; i < preguntaActual.getRespuestas().size(); i++) {
                Respuesta respuesta = preguntaActual.getRespuestas().get(i);

                // Restablece el color de fondo del botón
                botonesRespuesta[i].setBackground(Color.WHITE);

                // Actualiza el texto de la respuesta
                botonesRespuesta[i].setText(respuesta.getTextoRespuesta());
            }

            // Oculta el botón siguiente
            botonSiguiente.setVisible(false);
        } else {
            // Si ya estamos en la última pregunta, muestra los resultados finales
            mostrarResultadoFinal();
        }
    }

    private void mostrarResultadoFinal() {
        // Muestra el resultado final
        JOptionPane.showMessageDialog(this, "Tu puntuación final es " + puntuacion + "/" + numeroPreguntas);
    }
}
