package ar.edu.unlu.blackjack.Modelo;

import ar.edu.unlu.blackjack.Controlador.controlador;

public interface Observable {
    void addObserver(controlador controlador);
    void deleteObserver(controlador controlador);
    void notificarObservadores(Evento evento);
}
