package ar.edu.unlu.blackjack.Vista;

import ar.edu.unlu.blackjack.Modelo.Jugador;

public interface IVista {
    void iniciarJuego();
    void mostrarMensajeConSaltoLinea(String mensaje);
    void mostrarMensaje(String mensaje);
    void mostrarMenuJuego(Jugador jugador);
    void mostrarManoJugadorVista();
    void mostrarManosDivididasJugadorVista();
    void mostrarManoCrupierVista();
    void turnoCrupier();
    void turnoJugadorActual();
}
