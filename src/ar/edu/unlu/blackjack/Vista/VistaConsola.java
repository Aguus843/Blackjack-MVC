package ar.edu.unlu.blackjack.Vista;

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
    public void separadorMensaje14s(String mensaje){
        System.out.printf("%-14s", mensaje);
    }
    public void separadorMensaje19s(String mensaje){
        System.out.printf("\t%-19s", mensaje);
    }
    public void separadorMensaje15s(String mensaje){
        System.out.printf("%-15s", mensaje);
    }

    public String mostrarCartas() {
        return controlador.cartasRestantes();
    }

    public static void main(String[] args) {
        VistaConsola vistaConsola = new VistaConsola();
        vistaConsola.iniciarJuego();
    }
    public int solicitarCantidadJugadores(){
        mostrarMensaje("Ingrese la cantidad de jugadores (1 - 7): ");
        boolean salir = false;
        while (salir == false){
            String input = scanner.nextLine();
            try {
                int cantidad = Integer.parseInt(input);
                if (cantidad <= 1 || cantidad >= 7){
                    return cantidad;
                }else{
                    mostrarMensaje("[!] Debes ingresar una cantidad entre 1 y 7: ");
                }
            }catch (NumberFormatException e){
                mostrarMensaje("[!] Debes ingresar una cantidad entre 1 y 7.\n");
            }
        }
        return -1;
    }
    /*
     * Metodo que inicia el juego y carga los jugadores con sus apuestas
     * @return
     * */
    public void iniciarJuego() {
        boolean seCargoJugadorYApuestas = false;
        boolean seCargoApuesta = false;
        int cantJugadores = 0;
        mostrarMensajeConSaltoLinea("Bienvenido al Blackjack!");
        cantJugadores = solicitarCantidadJugadores();
        for (int i = 0; i < cantJugadores; i++) {
            int nroJugador = i+1;
            mostrarMensaje("Ingrese el nombre del jugador " + nroJugador + ": ");
            String nickname = scanner.nextLine();
            mostrarMensaje("Ingrese el saldo del jugador " + nroJugador + ": ");
            controlador.configurarJugadores(nickname, scanner.nextInt());
        }
        // Cargo las apuestas de todos los jugadores
        controlador.setIndiceJugador(0);
        controlador.setCantidadJugadoresTotales(cantJugadores);
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
                    mostrarManoJugadorVista();
                    mostrarMensaje("Presione Enter para continuar...");
                    controlador.ingresarPorTeclado();
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
            // controlador.turnoJugador(controlador.obtenerJugadorActual());
            turnoJugadorActual();
            // controlador.cambiarTurnoJugador();
            // Cambia el turno al siguiente para que cargue la apuesta correspondiente
            // El juego no puede comenzar si todos los jugadores no han apostado
        }
        // controlador.esTurnoCrupier();
        turnoCrupier();
        controlador.evaluandoGanadores();
    }
    public void mostrarManoJugadorVista() {
        mostrarMensajeConSaltoLinea("");
        int sumatoriaPuntaje = 0;
        int aux = 0;
        int ases = 0;
        mostrarMensajeConSaltoLinea(controlador.getNombreJugador() + " tiene las siguientes cartas:");
        for (Carta carta : controlador.modelo.getListaCartasMano()) {
            mostrarMensajeConSaltoLinea(carta.getValor() + " de " + carta.getPalo());
            sumatoriaPuntaje += carta.getValorNumerico();
            if (carta.getValorNumerico() == 11) ases++;
        }
        while (sumatoriaPuntaje > 21 && ases > 0){
            aux = sumatoriaPuntaje;
            sumatoriaPuntaje -= 10;
            ases--;
            if (ases == 1){
                aux = sumatoriaPuntaje;
                sumatoriaPuntaje -= 10;
                ases--;
            }
        }
        if ((controlador.jugadorActualTieneAs() && sumatoriaPuntaje <= 20) && aux < 21){
            mostrarMensajeConSaltoLinea("El puntaje actual es de: " + (controlador.getPuntajeMano()-10) + "/" + controlador.getPuntajeMano());
        }else mostrarMensajeConSaltoLinea("El puntaje del jugador es de: " + controlador.getPuntajeMano());
        mostrarMensajeConSaltoLinea("");
    }


    public void mostrarManosDivididasJugadorVista(){
        int sumatoriaPuntaje1 = 0;
        int sumatoriaPuntaje2 = 0;
        int ases1 = 0;
        int ases2 = 0;
        int aux1 = 0;
        int aux2 = 0;
        mostrarMensajeConSaltoLinea("===================================================");
        mostrarMensajeConSaltoLinea(controlador.getNombreJugador() + " tiene las siguientes cartas en ambas manos: ");
        Carta cartasMano1;
        Carta cartaMano2;

        int cantidadCartas = Math.max(controlador.getCantidadCartasMano1(), controlador.getCantidadCartasMano2());
        mostrarMensaje("-= MANO 1=-\t\t\t\t\t\t\t -= MANO 2 =-\n");
        for (int i = 0; i < cantidadCartas; i++){
            if (i < controlador.getCantidadCartasMano1()){
                cartasMano1 = controlador.getCartasMano1(i);
                mostrarMensaje(cartasMano1.getValor() + " de " + cartasMano1.getPalo());
                sumatoriaPuntaje1 += cartasMano1.getValorNumerico();
                if (cartasMano1.getValorNumerico() == 11) ases1++;
            }else separadorMensaje15s("");;
            if (i < controlador.getCantidadCartasMano2()){
                cartaMano2 = controlador.getCartasMano2(i);
                separadorMensaje19s("");
                mostrarMensaje(cartaMano2.getValor() + " de " + cartaMano2.getPalo() + "\n");
                sumatoriaPuntaje2 += cartaMano2.getValorNumerico();
                if (cartaMano2.getValorNumerico() == 11) ases2++;
            }else separadorMensaje15s("");
        }
        while (sumatoriaPuntaje1 > 21 && ases1 > 0){
            aux1 = sumatoriaPuntaje1;
            sumatoriaPuntaje1 -= 10;
            ases1--;
            if (ases1 == 1){
                aux1 = sumatoriaPuntaje1;
                sumatoriaPuntaje1 -= 10;
                ases1--;
            }
        }
        while (sumatoriaPuntaje2 > 21 && ases2 > 0){
            aux2 = sumatoriaPuntaje2;
            sumatoriaPuntaje2 -= 10;
            ases2--;
            if (ases2 == 1){
                aux2 = sumatoriaPuntaje2;
                sumatoriaPuntaje2 -= 10;
                ases2--;
            }
        }
        mostrarMensajeConSaltoLinea("");
        if ((controlador.getJugadorTieneAsMano1() && sumatoriaPuntaje1 < 21) && aux1 < 21){
            mostrarMensajeConSaltoLinea("El puntaje actual de la mano 1 es: " + (controlador.getPuntajeMano1()-10) + "/" + controlador.getPuntajeMano1());
        }else mostrarMensajeConSaltoLinea("El puntaje actual de la mano 1 es: " + controlador.getPuntajeMano1());
        if ((controlador.getJugadorTieneAsMano2() && sumatoriaPuntaje2 < 21) && aux2 < 21){
            mostrarMensajeConSaltoLinea("El puntaje actual de la mano 2 es: " + (controlador.getPuntajeMano2()-10) + "/" + controlador.getPuntajeMano2());
        }else mostrarMensajeConSaltoLinea("El puntaje actual de la mano 2 es: " + controlador.getPuntajeMano2());
        mostrarMensajeConSaltoLinea("===================================================");
    }
    public void mostrarManoCrupierVista(){
        mostrarMensajeConSaltoLinea("");
        int sumatoriaPuntaje = 0;
        int ases = 0;
        int aux = 0;
        mostrarMensajeConSaltoLinea("El crupier tiene:");
        for (Carta carta : controlador.getManoCrupier()) {
            mostrarMensajeConSaltoLinea(carta.getValor() + " de " + carta.getPalo());
            sumatoriaPuntaje += carta.getValorNumerico();
            if (carta.getValorNumerico() == 1) ases++;
        }
        while (sumatoriaPuntaje > 21 && ases > 0){
            aux = sumatoriaPuntaje;
            sumatoriaPuntaje -= 10;
            ases--;
            if (ases == 1){
                aux = sumatoriaPuntaje;
                sumatoriaPuntaje -= 10;
                ases--;
            }
        }
        if ((controlador.getCrupierTieneAsPrimera() && sumatoriaPuntaje < 21) && aux < 21){
            mostrarMensajeConSaltoLinea("El puntaje actual del crupier es de: " + (controlador.getPuntajeCrupier()-10) + "/" + controlador.getPuntajeCrupier());
        }else mostrarMensajeConSaltoLinea("El puntaje actual del crupier es de: " + controlador.getPuntajeCrupier());
    }
    public void turnoCrupier(){
        mostrarMensajeConSaltoLinea("Turno del crupier.");
        mostrarManoCrupierVista();

        // Obtiene una carta hasta tener 17 o más.
        while (controlador.crupierDebePedirCarta()){
            mostrarMensajeConSaltoLinea("El crupier está obteniendo una carta...");
            try {
                Thread.sleep(1500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            controlador.crupierPideCarta();
            mostrarManoCrupierVista();
        }

        // Verifico si se paso de 21.
        if (controlador.crupierSePaso21()){
            mostrarMensajeConSaltoLinea("El crupier se pasó de los 21.");
        }else {
            mostrarMensajeConSaltoLinea("El crupier se planta en " + controlador.getPuntajeCrupier() + ".");
        }
    }

    public void turnoJugadorActual(){
        int indiceJugador = controlador.getIndiceJugadorActual();
        mostrarMensajeConSaltoLinea("[DEBUG] Indice jugador: " + indiceJugador + ", jugador: " + controlador.getNombreJugador());
        boolean insurance = false;
        boolean yaPidio = false;
        boolean yaPregunto = false;
        boolean flagDoblo = false;
        boolean yaMostroMano = false;
        String ingreso;
        Mano mano = controlador.obtenerManoJugador();
        if (mano != null){
            if (controlador.getJugadorTieneBlackjack()){
                mostrarManoJugadorVista();
                mostrarMensajeConSaltoLinea("Felicitaciones " + controlador.getNombreJugador() + " conseguiste BJ!");
            }
            while ((!controlador.getSePaso21ManoPrincipal() && controlador.getPuntajeMano() != 21) && !flagDoblo){
                if (!yaMostroMano) mostrarManoJugadorVista();
                yaMostroMano = true;
                mostrarMensajeConSaltoLinea("Es el turno del jugador: " + controlador.getNombreJugador());
                if (controlador.getCrupierTieneAsPrimera() && !yaPregunto){
                    while (!insurance && controlador.getSaldoJugadorActual() >= controlador.getApuestaJugador()/2){
                        mostrarMensajeConSaltoLinea("ATENCIÓN! Presione enter para continuar...");
                        controlador.ingresarPorTeclado();
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
                    mostrarMensaje(controlador.getNombreJugador() + ": ingrese 'c' para pedir, 'd' para doblar, 's' para dividir o 'p' para plantarse: ");
                }else if (!controlador.getJugadorPuedeDividir()) mostrarMensaje(controlador.getNombreJugador() + ": ingrese 'c' para pedir, 'd' para doblar o 'p' para plantarse: ");
                else if (!controlador.getJugadorPuedeDividir() && yaPidio) mostrarMensaje(controlador.getNombreJugador() + ": ingrese 'c' para pedir o 'p' para plantarse: ");
                ingreso = controlador.ingresarPorTeclado().toLowerCase();
                if (ingreso.equals("c")){
                    controlador.recibirCartaJugador();
                    mostrarManoJugadorVista();
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
                        List<Mano> manos = controlador.getManosJugador();
                        boolean termino = false;
                        boolean terminoMano2 = false;
                        boolean imprimioPrimeraVez = false;
                        controlador.setRepartirCartaAMano(0);
                        controlador.setRepartirCartaAMano(1);
                        for (int i = 0; i < manos.size(); i++){
                            termino = false;
                            while (!termino){
                                if (manos.get(i).tieneBlackjack()){
                                    mostrarMensajeConSaltoLinea("Felicitaciones " + controlador.getNombreJugador() + "! conseguiste BJ en la mano " + i + ".");
                                    termino = true;
                                    break;
                                }
                                while ((!manos.get(i).sePaso21() && manos.get(i).getPuntaje() != 21) && !flagDoblo){
                                    String ingresoDividir;
                                    if (!imprimioPrimeraVez) mostrarManosDivididasJugadorVista();
                                    imprimioPrimeraVez = true;
                                    mostrarMensajeConSaltoLinea("Es el turno de: " + controlador.getNombreJugador() + " con la mano " + (i+1) + " ---> (" + controlador.getPuntajeManosIndices(i) + ")");
                                    mostrarMensaje(controlador.getNombreJugador() + ": ingrese 'c' para pedir, 'd' para doblar o 'p' para plantarse: ");
                                    ingreso = controlador.ingresarPorTeclado().toLowerCase();
                                    if (ingreso.equals("c")){
                                        controlador.setRepartirCartaAMano(i);
                                        mostrarManosDivididasJugadorVista();
                                        if (controlador.getSePaso21Index(i)){
                                            mostrarMensajeConSaltoLinea("Se paso de los 21. Perdió el juego.");
                                            termino = true;
                                            break;
                                        }
                                        yaPidio = true;
                                        break;
                                    }else if (ingreso.equals("p")){
                                        mostrarMensajeConSaltoLinea(controlador.getNombreJugador() + " se plantó con la mano " + (i + 1) + ".");
                                        termino = true;
                                        break;
                                    }else if (ingreso.equals("d")){
                                        if (controlador.getSaldoJugadorActual() >= controlador.getApuestaJugador() && !yaPidio){
                                            mostrarMensajeConSaltoLinea(controlador.getNombreJugador() + " dobló con la mano " + (i + 1) + ".");
                                            controlador.setRepartirCartaAMano(i);
                                            controlador.jugadorDobloManoIndice(i);
                                            flagDoblo = true;
                                            termino = true;
                                            break;
                                        }else if (yaPidio){
                                            mostrarMensajeConSaltoLinea("[!] No podés doblar dado que ya pediste una carta!");
                                        }else {
                                            mostrarMensajeConSaltoLinea("[!] No podés doblar dado que no tenés saldo suficiente!");
                                        }
                                    }else{
                                        mostrarMensajeConSaltoLinea("[!] Lo que se ingresó no es válido.");
                                    }
                                }
                            }
                            if (i == 1) terminoMano2 = true;
                            if (terminoMano2){
                                mostrarMensajeConSaltoLinea("------ Resumen ambas manos ------");
                                mostrarManosDivididasJugadorVista();
                                break;
                            }
                        }
                    }else{
                        mostrarMensajeConSaltoLinea("[!] No podés dividir dado que no tenés dos cartas iguales!");
                    }
                }
                else if (ingreso.equals("d")){
                    if (controlador.getSaldoJugadorActual() >= controlador.getApuestaJugador() && !yaPidio){
                        mostrarMensajeConSaltoLinea(controlador.getNombreJugador() + " dobló.");
                        controlador.recibirCartaJugador();
                        controlador.jugadorDobloMano();
                        flagDoblo = true;
                        break;
                    }else if(yaPidio){
                        mostrarMensajeConSaltoLinea("[!] No podés doblar dado que ya pediste una carta!");
                    }else mostrarMensajeConSaltoLinea("[!] No podés doblar dado que no tenés saldo suficiente!");
                }else mostrarMensajeConSaltoLinea("[!] Lo que se ingresó no es válido!");
            }
        }
        controlador.cambiarTurnoJugador();
    }
}
