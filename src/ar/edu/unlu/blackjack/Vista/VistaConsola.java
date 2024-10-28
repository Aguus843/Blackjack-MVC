package ar.edu.unlu.blackjack.Vista;

import ar.edu.unlu.blackjack.Modelo.Jugador;
import ar.edu.unlu.blackjack.Controlador.controlador;
import ar.edu.unlu.blackjack.Modelo.*;

import java.util.List;
import java.util.Scanner;

public class VistaConsola {
    private final controlador controlador;
    Scanner scanner;

    public VistaConsola() {
        this.controlador = new controlador();
        scanner = new Scanner(System.in);
    }

    public void mostrarMensajeConSaltoLinea(String mensaje) {
        System.out.println(mensaje);
    }
    public void mostrarMensaje(String mensaje){
        System.out.printf("%s", mensaje);
    }

    public String ingresarTexto() {
        return scanner.nextLine();
    }

    public String mostrarCartas() {
        return controlador.cartasRestantes();
    }

    public static void main(String[] args) {
        VistaConsola vistaConsola = new VistaConsola();
        vistaConsola.iniciarJuego();
    }
    /*
     * Metodo que inicia el juego y carga los jugadores con sus apuestas
     * @return
     * */
    public void iniciarJuego() {
        boolean seCargoJugadorYApuestas = false;
        boolean seCargoApuesta = false;
        mostrarMensajeConSaltoLinea("Bienvenido al Blackjack!");
        controlador.configurarJugadores();
        // Cargo las apuestas de todos los jugadores
        while (controlador.getIndiceJugadorActual() != controlador.getCantidadJugadoresTotal()) {
            while (!seCargoJugadorYApuestas){
                mostrarMensajeConSaltoLinea(controlador.obtenerJugadorActual().getNombre() + " tu saldo actual es de " + controlador.obtenerJugadorActual().getSaldo() + " pesos.");
                while (controlador.getIndiceJugadorActual() != controlador.getCantidadJugadoresTotal()) {
                    while (!seCargoApuesta){
                        mostrarMensaje(controlador.obtenerJugadorActual().getNombre() + ": ingrese el monto a apostar: ");
                        if (!controlador.cargarApuestaJugador(controlador.obtenerJugadorActual())){
                            mostrarMensajeConSaltoLinea("[!] El monto debe ser mayor que uno (1) y debe ser mayor al saldo disponible.");
                        }
                        seCargoApuesta = true;
                    }
                    if (controlador.getIndiceJugadorActual() != controlador.getCantidadJugadoresTotal()) {
                        controlador.cambiarTurnoJugador();
                        seCargoApuesta = false;
                    }
                }
                if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()) {
                    controlador.cambiarTurnoJugador(); // Pongo el índice en cero para mostrar las cartas de todos los jugadores
                }
                // Reparto las cartas a cada jugador
                while (controlador.getIndiceJugadorActual() != controlador.getCantidadJugadoresTotal()) {
                    controlador.repartirCartasIniciales(controlador.obtenerJugadorActual());
                    controlador.cambiarTurnoJugador();
                }
                if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()) {
                    controlador.cambiarTurnoJugador(); // Pongo el índice en cero nuevamente para que los jugadores puedan jugar
                }
                seCargoJugadorYApuestas = true;
            }
            mostrarMensajeConSaltoLinea("Es el turno de: " + controlador.obtenerJugadorActual().getNombre());
            mostrarMensajeConSaltoLinea("El saldo del jugador es de " + controlador.obtenerJugadorActual().getSaldo());
            if (controlador.crupierTieneCarta() == false){
                controlador.crupierPideCarta();
                controlador.crupierPideCarta();
            }
            mostrarMensajeConSaltoLinea("Cartas restantes: " + mostrarCartas());
            mostrarMensajeConSaltoLinea("El crupier tiene " + controlador.crupierMuestraPrimerCarta());
            controlador.turnoJugador(controlador.obtenerJugadorActual());
            // controlador.cambiarTurnoJugador();
            // Cambia el turno al siguiente para que cargue la apuesta correspondiente
            // El juego no puede comenzar si todos los jugadores no han apostado
        }
        controlador.esTurnoCrupier();
        controlador.evaluandoGanadores();
    }
    public void mostrarManoJugadorVista(Jugador jugador) {
        mostrarMensajeConSaltoLinea("");
        int sumatoriaPuntaje = 0;
        mostrarMensajeConSaltoLinea(controlador.getNombreJugador() + " tiene las siguientes cartas:");
        for (Carta carta : controlador.modelo.getListaCartasMano()) {
            mostrarMensajeConSaltoLinea(carta.getValor() + " de" + carta.getPalo());
            sumatoriaPuntaje += carta.getValorNumerico();
        }
        if ((controlador.jugadorActualTieneAs() && sumatoriaPuntaje <= 20) || (controlador.jugadorActualTieneAs() && controlador.primerCartaEsAs())){
            mostrarMensajeConSaltoLinea("El puntaje actual es de: " + (controlador.getPuntajeMano()-10) + "/" + controlador.getPuntajeMano());
        }else mostrarMensajeConSaltoLinea("El puntaje del jugador es de: " + controlador.getPuntajeMano());
    }

    public void mostrarManosDivididasJugadorVista(){
        int sumatoriaPuntaje1 = 0;
        int sumatoriaPuntaje2 = 0;
        mostrarMensajeConSaltoLinea("================================================");
        mostrarMensajeConSaltoLinea(controlador.getNombreJugador() + " tiene las siguientes cartas en ambas manos: ");
        Carta cartasMano1;
        Carta cartaMano2;

        int cantidadCartas = Math.max(controlador.getCantidadCartasMano1(), controlador.getCantidadCartasMano2());
        mostrarMensaje("-= MANO 1=-\t\t\t\t\t\t\t\t\t\t\t -= MANO 2 =-");
        for (int i = 0; i < cantidadCartas; i++){
            // seguir desde aca
        }
    }

    public void turnoJugadorActual(){
        int indiceJugador = controlador.getIndiceJugadorActual();
        mostrarMensajeConSaltoLinea("[DEBUG] Indice jugador: " + indiceJugador + ", jugador: " + controlador.getNombreJugador());
        boolean insurance = false;
        boolean yaPidio = false;
        boolean yaPregunto = false;
        boolean yaRevisoBlackjack = false;
        boolean flagDoblo = false;
        String ingreso;
        Mano mano = controlador.obtenerManoJugador();
        if (mano != null){
            mostrarManoJugadorVista(controlador.obtenerJugadorActual());
            controlador.crupierMuestraPrimerCarta();
            while ((controlador.getPuntajeMano() > 21 && controlador.getPuntajeMano() != 21) && !flagDoblo){
                mostrarMensajeConSaltoLinea("Es el turno del jugador: " + controlador.getNombreJugador());
                if (controlador.getJugadorTieneBlackjack() && !yaRevisoBlackjack){
                    mostrarManoJugadorVista(controlador.obtenerJugadorActual());
                    mostrarMensajeConSaltoLinea("Felicitaciones " + controlador.getNombreJugador() + " conseguiste BJ!");
                    break;
                }else yaRevisoBlackjack = true;
                if (controlador.getCrupierTieneAsPrimera() && !yaPregunto){
                    while (!insurance && controlador.getSaldoJugadorActual() >= controlador.getApuestaJugador()/2){
                        int ingresoSeguro = controlador.ingresarSeguroBlackjack();
                        if (ingresoSeguro == 1){
                            mostrarMensajeConSaltoLinea(controlador.getNombreJugador() + " decidió pagar el seguro.");
                            controlador.ajustarSaldoJugador(-(controlador.getApuestaJugador() / 2));
                            insurance = true;
                            yaPregunto = true;
                            break;
                        }else{
                            mostrarMensajeConSaltoLinea(controlador.getNombreJugador() + " decidió no pagar el seguro.");
                            yaPregunto = true;
                            break;
                        }
                    }
                    if (controlador.getSaldoJugadorActual() < controlador.getApuestaJugador()/2){
                        mostrarMensajeConSaltoLinea("[!] No podes pagar el seguro dado que no tenés saldo suficiente.");
                    }
                }
                if (controlador.getJugadorPuedeDividir()){
                    mostrarMensajeConSaltoLinea(controlador.getNombreJugador() + ": ingrese 'c' para pedir, 'd' para doblar, 's' para dividir o 'p' para plantarse: ");
                }else if (!controlador.getJugadorPuedeDividir()) mostrarMensajeConSaltoLinea(controlador.getNombreJugador() + ": ingrese 'c' para pedir, 'd' para doblar o 'p' para plantarse: ");
                else if (!controlador.getJugadorPuedeDividir() && yaPidio) mostrarMensajeConSaltoLinea(controlador.getNombreJugador() + ": ingrese 'c' para pedir o 'p' para plantarse: ");
                ingreso = controlador.ingresarPorTeclado().toLowerCase();
                if (ingreso.equals("c")){
                    controlador.recibirCartaJugador();
                    mostrarManoJugadorVista(controlador.obtenerJugadorActual());
                    if (controlador.getPuntajeMano() > 21){
                        mostrarMensajeConSaltoLinea("Se pasó de los 21. Perdió el juego!");
                        break;
                    }
                    yaPidio = true;
                }else if (ingreso.equals("p")){
                    mostrarMensajeConSaltoLinea(controlador.getNombreJugador() + " se plantó");
                    break;
                }else if (ingreso.equals("s")){
                    if ((controlador.getSaldoJugadorActual() >= controlador.getApuestaJugador()) && controlador.compararDosCartasIguales()){
                        mostrarMensajeConSaltoLinea(controlador.getNombreJugador() + " decidió dividir");
                        controlador.dividirManoJugador();
                        // Arranco un ciclo while para cargar ambas manos, creando un array de manos
                        List<Mano> manos = controlador.obtenerJugadorActual().getManos();
                        boolean terminoMano1 = false;
                        boolean terminoMano2 = false;
                        controlador.setRepartirCartaAMano(0);
                        controlador.setRepartirCartaAMano(1);
                        for (int i = 0; i < manos.size(); i++){
                            terminoMano1 = false;
                            while (!terminoMano1){
                                if (manos.get(i).tieneBlackjack()){
                                    mostrarMensajeConSaltoLinea("Felicitaciones " + controlador.getNombreJugador() + "! conseguiste BJ en la mano " + i + ".");
                                    terminoMano1 = true;
                                    break;
                                }
                                while ((!manos.get(i).sePaso21() && manos.get(i).getPuntaje() != 21) && !flagDoblo){
                                    String ingresoDividir;

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
