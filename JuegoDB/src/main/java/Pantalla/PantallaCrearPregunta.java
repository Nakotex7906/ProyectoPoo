package Pantalla;

import Database.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class PantallaCrearPregunta extends JFrame {
    private JTextArea areaTextoPregunta;
    private JComboBox<String> comboCategorias;
    private JTextField campoTextoCategoria;
    private JTextField[] camposTextoRespuestas;
    private ButtonGroup grupoBotones;
    private JRadioButton[] botonesRadioRespuestas;

    public PantallaCrearPregunta() {
        super("Crear una Pregunta");
        setSize(851, 565);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Color grisOscuro = new Color(0x2C3E50);
        getContentPane().setBackground(grisOscuro);

        botonesRadioRespuestas = new JRadioButton[4];
        camposTextoRespuestas = new JTextField[4];
        grupoBotones = new ButtonGroup();

        agregarComponentesGui();
    }

    private void agregarComponentesGui() {
        Color grisOscuro = new Color(0x2C3E50);
        Color azul = new Color(0x3498DB);
        Color blanco = new Color(0xFFFFFF);
        Color amarillo = new Color(0xF1C40F);
        Color naranjo = new Color(0xE67E22);

        // Etiqueta de título
        JLabel etiquetaTitulo = new JLabel("Crea tu pregunta");
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        etiquetaTitulo.setBounds(50, 15, 310, 29);
        etiquetaTitulo.setForeground(azul);
        add(etiquetaTitulo);

        // Etiqueta de pregunta
        JLabel etiquetaPregunta = new JLabel("Pregunta: ");
        etiquetaPregunta.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetaPregunta.setBounds(50, 60, 93, 20);
        etiquetaPregunta.setForeground(naranjo);
        add(etiquetaPregunta);

        // Área de texto de la pregunta
        areaTextoPregunta = new JTextArea();
        areaTextoPregunta.setFont(new Font("Arial", Font.BOLD, 16));
        areaTextoPregunta.setBounds(50, 90, 310, 110);
        areaTextoPregunta.setForeground(grisOscuro);
        areaTextoPregunta.setLineWrap(true);
        areaTextoPregunta.setWrapStyleWord(true);
        add(areaTextoPregunta);

        // Etiqueta de categoría
        JLabel etiquetaCategoria = new JLabel("Categoría: ");
        etiquetaCategoria.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetaCategoria.setBounds(50, 250, 93, 20);
        etiquetaCategoria.setForeground(naranjo);
        add(etiquetaCategoria);

        // ComboBox de categorías
        List<String> categorias = JDBC.obtenerCategorias();
        categorias.add("Nueva Categoría"); // Añadir opción para nueva categoría
        comboCategorias = new JComboBox<>(categorias.toArray(new String[0]));
        comboCategorias.setFont(new Font("Arial", Font.BOLD, 16));
        comboCategorias.setBounds(50, 280, 310, 36);
        comboCategorias.setForeground(grisOscuro);
        comboCategorias.addActionListener(e -> toggleCampoTextoCategoria());
        add(comboCategorias);

        // Campo de texto para nueva categoría
        campoTextoCategoria = new JTextField();
        campoTextoCategoria.setFont(new Font("Arial", Font.BOLD, 16));
        campoTextoCategoria.setBounds(50, 320, 310, 36);
        campoTextoCategoria.setForeground(grisOscuro);
        campoTextoCategoria.setVisible(false);
        add(campoTextoCategoria);

        agregarComponentesRespuestas();

        // Botón de enviar
        JButton botonEnviar = new JButton("Agregar");
        botonEnviar.setFont(new Font("Arial", Font.BOLD, 16));
        botonEnviar.setBounds(300, 450, 262, 45);
        botonEnviar.setForeground(blanco);
        botonEnviar.setBackground(amarillo);
        botonEnviar.addActionListener(this::manejarEnvio);
        add(botonEnviar);

        // Etiqueta de volver
        JLabel etiquetaVolver = new JLabel("Volver");
        etiquetaVolver.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetaVolver.setBounds(300, 500, 262, 20);
        etiquetaVolver.setForeground(azul);
        etiquetaVolver.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaVolver.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PantallaPrincipal pantallaPrincipal = new PantallaPrincipal();
                pantallaPrincipal.setLocationRelativeTo(PantallaCrearPregunta.this);
                dispose();
                pantallaPrincipal.setVisible(true);
            }
        });
        add(etiquetaVolver);
    }

    private void agregarComponentesRespuestas() {
        Color grisOscuro = new Color(0x2C3E50);
        Color naranjo = new Color(0xE67E22);

        // Espacio vertical entre cada componente de respuesta
        int espacioVertical = 100;

        // Crear 4 etiquetas de respuesta, 4 botones de radio y 4 campos de texto
        IntStream.range(0, 4).forEach(i -> {
            // Etiqueta de respuesta
            JLabel etiquetaRespuesta = new JLabel("Respuesta #" + (i + 1));
            etiquetaRespuesta.setFont(new Font("Arial", Font.BOLD, 16));
            etiquetaRespuesta.setBounds(470, 60 + (i * espacioVertical), 120, 20);
            etiquetaRespuesta.setForeground(naranjo);
            add(etiquetaRespuesta);

            // Botón de radio
            botonesRadioRespuestas[i] = new JRadioButton();
            botonesRadioRespuestas[i].setBounds(440, 100 + (i * espacioVertical), 21, 21);
            botonesRadioRespuestas[i].setBackground(null);
            grupoBotones.add(botonesRadioRespuestas[i]);
            add(botonesRadioRespuestas[i]);

            // Campo de texto de respuesta
            camposTextoRespuestas[i] = new JTextField();
            camposTextoRespuestas[i].setBounds(470, 90 + (i * espacioVertical), 310, 36);
            camposTextoRespuestas[i].setFont(new Font("Arial", Font.PLAIN, 16));
            camposTextoRespuestas[i].setForeground(grisOscuro);
            add(camposTextoRespuestas[i]);
        });

        // Seleccionar el primer botón de radio por defecto
        botonesRadioRespuestas[0].setSelected(true);
    }

    private void toggleCampoTextoCategoria() {
        if (comboCategorias.getSelectedItem() != null && comboCategorias.getSelectedItem().toString().equals("Nueva Categoría")) {
            campoTextoCategoria.setVisible(true);
        } else {
            campoTextoCategoria.setVisible(false);
        }
    }

    private void manejarEnvio(ActionEvent e) {
        if (validarEntrada()) {
            String pregunta = areaTextoPregunta.getText();
            String categoria = comboCategorias.getSelectedItem().toString();
            if (categoria.equals("Nueva Categoría")) {
                categoria = campoTextoCategoria.getText().trim();
            }
            String[] respuestas = Arrays.stream(camposTextoRespuestas).map(JTextField::getText).toArray(String[]::new);
            int indiceCorrecto = IntStream.range(0, botonesRadioRespuestas.length)
                    .filter(i -> botonesRadioRespuestas[i].isSelected())
                    .findFirst()
                    .orElse(0);

            if (JDBC.guardarPregResp(pregunta, categoria, respuestas, indiceCorrecto)) {
                JOptionPane.showMessageDialog(this, "Pregunta agregada");
                reiniciarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar la pregunta");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error: entrada inválida");
        }
    }

    private boolean validarEntrada() {
        return !areaTextoPregunta.getText().trim().isEmpty() &&
                comboCategorias.getSelectedItem() != null &&
                (!comboCategorias.getSelectedItem().toString().equals("Nueva Categoría") ||
                        !campoTextoCategoria.getText().trim().isEmpty()) &&
                Arrays.stream(camposTextoRespuestas).noneMatch(field -> field.getText().trim().isEmpty());
    }

    private void reiniciarCampos() {
        areaTextoPregunta.setText("");
        comboCategorias.setSelectedIndex(0);
        campoTextoCategoria.setText("");
        Arrays.stream(camposTextoRespuestas).forEach(field -> field.setText(""));
        botonesRadioRespuestas[0].setSelected(true);
    }
}
