import Pantalla.PantallaPrincipal;

import javax.swing.*;

public class App {

    public static void main(String[] args) {

        // asegura que las tareas de la GUI de Swing se ejecuten en el hilo de despacho de eventos (EDT)
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                // crea y muestra la ventana de la pantalla principal
                new PantallaPrincipal().setVisible(true);
            }
        });

    }
}