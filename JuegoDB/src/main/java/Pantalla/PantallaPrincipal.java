package Pantalla;

import Database.Categoria;
import Database.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PantallaPrincipal extends JFrame {

    private JComboBox<String> menuCategorias;
    private JTextField campoNumPreguntas;

    public PantallaPrincipal() {

        super("Pantalla Principal");
        Color grisOscuro = new Color(0x2C3E50);

        // Aumento del tamaño en un 10% para mantener la simetría
        int width = (int) (400 * 1.1);
        int height = (int) (565 * 1.1);
        setSize(width, height);

        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(grisOscuro);

        agregarComponentes();
    }

    private void agregarComponentes() {

        Color blanco = new Color(0xFFFFFF);
        Color azul = new Color(0x3498DB);
        Color amarillo = new Color(0xF1C40F);
        Color naranjo = new Color(0xE67E22);

        // Etiqueta de título
        JLabel etiquetaTitulo = new JLabel("¡Juego de Preguntas!");
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        etiquetaTitulo.setBounds(0, 20, getWidth(), 40);
        etiquetaTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaTitulo.setForeground(azul);
        add(etiquetaTitulo);

        // Etiqueta de elegir categoría
        JLabel etiquetaEligeCategoria = new JLabel("Elige una categoría");
        etiquetaEligeCategoria.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetaEligeCategoria.setBounds(0, 100, getWidth(), 20);
        etiquetaEligeCategoria.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaEligeCategoria.setForeground(naranjo);
        add(etiquetaEligeCategoria);

        // Menú desplegable de categorías
        ArrayList<String> listaCategorias = JDBC.obtenerCategorias();
        menuCategorias = new JComboBox<>(listaCategorias.toArray(new String[0]));
        menuCategorias.setBounds(40, 140, getWidth() - 80, 40);
        menuCategorias.setFont(new Font("Arial", Font.BOLD, 16));
        menuCategorias.setForeground(Color.BLACK);
        add(menuCategorias);

        // Etiqueta de número de preguntas
        JLabel etiquetaNumPreguntas = new JLabel("N° de preguntas:");
        etiquetaNumPreguntas.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetaNumPreguntas.setBounds(40, 200, 150, 20);
        etiquetaNumPreguntas.setHorizontalAlignment(SwingConstants.RIGHT);
        etiquetaNumPreguntas.setForeground(naranjo);
        add(etiquetaNumPreguntas);

        // Campo de texto de número de preguntas
        campoNumPreguntas = new JTextField("10");
        campoNumPreguntas.setFont(new Font("Arial", Font.BOLD, 16));
        campoNumPreguntas.setBounds(200, 200, getWidth() - 240, 30);
        campoNumPreguntas.setForeground(Color.BLACK);
        add(campoNumPreguntas);

        // Botón de empezar
        JButton botonEmpezar = new JButton("Empezar");
        botonEmpezar.setFont(new Font("Arial", Font.BOLD, 16));
        botonEmpezar.setBounds(85, 290, getWidth() - 170, 45);
        botonEmpezar.setBackground(amarillo);
        botonEmpezar.setForeground(blanco);
        botonEmpezar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarEntrada()) {
                    // Obtener categoría
                    Categoria categoria = JDBC.obtenerCategoria(menuCategorias.getSelectedItem().toString());

                    // Categoría inválida
                    if (categoria == null) return;

                    int numPreguntas = Integer.parseInt(campoNumPreguntas.getText());

                    // Cargar pantalla de quiz
                    PantallaPreguntas pantallaPregunta = new PantallaPreguntas(categoria, numPreguntas);
                    pantallaPregunta.setLocationRelativeTo(PantallaPrincipal.this);

                    // Cerrar esta pantalla
                    PantallaPrincipal.this.dispose();

                    // Mostrar pantalla de quiz
                    pantallaPregunta.setVisible(true);
                }
            }
        });
        add(botonEmpezar);

        // Botón de salir
        JButton botonSalir = new JButton("Salir");
        botonSalir.setFont(new Font("Arial", Font.BOLD, 16));
        botonSalir.setBounds(85, 350, getWidth() - 170, 45);
        botonSalir.setBackground(amarillo);
        botonSalir.setForeground(blanco);
        botonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar esta pantalla
                PantallaPrincipal.this.dispose();
            }
        });
        add(botonSalir);

        // Botón de crear una pregunta
        JButton botonCrearPregunta = new JButton("Crear una pregunta");
        botonCrearPregunta.setFont(new Font("Arial", Font.BOLD, 16));
        botonCrearPregunta.setBounds(85, 410, getWidth() - 170, 45);
        botonCrearPregunta.setBackground(amarillo);
        botonCrearPregunta.setForeground(blanco);
        botonCrearPregunta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear pantalla de crear pregunta
                PantallaCrearPregunta pantallaCrearPregunta = new PantallaCrearPregunta();
                pantallaCrearPregunta.setLocationRelativeTo(PantallaPrincipal.this);

                // Cerrar esta pantalla
                PantallaPrincipal.this.dispose();

                // Mostrar pantalla de crear pregunta
                pantallaCrearPregunta.setVisible(true);
            }
        });
        add(botonCrearPregunta);
    }

    // Validación de entrada
    private boolean validarEntrada() {
        // El campo de número de preguntas no debe estar vacío
        if (campoNumPreguntas.getText().trim().isEmpty()) return false;

        // No se ha elegido ninguna categoría
        if (menuCategorias.getSelectedItem() == null) return false;

        return true;
    }
}
