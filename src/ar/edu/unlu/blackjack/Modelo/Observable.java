package ar.edu.unlu.blackjack.Modelo;

import ar.edu.unlu.blackjack.Controlador.controladorConsolaGrafica;

public interface Observable {
    void addObserver(controladorConsolaGrafica controladorConsolaGrafica);
    void deleteObserver(controladorConsolaGrafica controladorConsolaGrafica);
    void notificarObservadores(Evento evento);
}
