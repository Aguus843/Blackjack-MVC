import ar.edu.unlu.blackjack.Controlador.Controlador;
import ar.edu.unlu.blackjack.Controlador.controladorConsolaGrafica;
import ar.edu.unlu.blackjack.Modelo.BlackjackJuego;
import ar.edu.unlu.blackjack.Vista.ConsolaGrafica.consolaGrafica;
import ar.edu.unlu.blackjack.Vista.VistaConsola;

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
//        VistaConsola vistaConsola = new VistaConsola();
//        Controlador controlador2 = new Controlador(vistaConsola);
//        vistaConsola.setControlador(controlador2);
//        controlador2.setModelo(new BlackjackJuego());
//        vistaConsola.iniciarJuego();
    }
}
