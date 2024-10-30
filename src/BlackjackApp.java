import ar.edu.unlu.blackjack.Controlador.controladorConsolaGrafica;
import ar.edu.unlu.blackjack.Modelo.BlackjackJuego;
import ar.edu.unlu.blackjack.Vista.ConsolaGrafica.consolaGrafica;

public class BlackjackApp {
    public static void main(String[] args) {
        // Consola Grafica
        consolaGrafica vista = new consolaGrafica();
        BlackjackJuego modelo = new BlackjackJuego();
        controladorConsolaGrafica controlador = new controladorConsolaGrafica(vista);
        modelo.addObserver(controlador);
        controlador.setModelo(modelo);
        vista.setControlador(controlador);
        vista.iniciarJuego();

        // Consola Normal

    }
}
