package ar.edu.unlu.blackjack.Vista.ConsolaGrafica;

import ar.edu.unlu.blackjack.Controlador.controladorConsolaGrafica;
import ar.edu.unlu.blackjack.Modelo.Carta;
import ar.edu.unlu.blackjack.Modelo.Mano;
import ar.edu.unlu.blackjack.Vista.IVista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class consolaGrafica implements IVista{
    private final JFrame frame;
    private JPanel contentPane;
    private JButton btnEnter;
    private JTextField txtEntrada;
    private JTextArea txtSalida;
    private JTextField blackjackV01aTextField;

    controladorConsolaGrafica controlador;
    private int jugadoresRestantes;
    private int cantJugadoresAgregados;
    private int estadoCantidadJugadores;
    private String nickname;
    private boolean vaJugadorActual;
    private boolean esperandoNickname;
    private boolean cargarApuestas;
    private boolean flagArrancoPartida;
    private int estadoJugador;
    private boolean vaDecisionJugador;
    private int estadoApuestas;
    private boolean flag;
    private boolean yaRepartio;
    private boolean seCargaronTodosLosDatos;
    private boolean finPartida;
    private boolean pasoCrupier;
    private boolean imprimioCartas;
    private boolean jugadorTuvoBlackjack;

    public consolaGrafica() {
        frame = new JFrame("=== Blackjack ===");
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.pack();
        frame.setLocationRelativeTo(null);
        txtSalida.setBackground(Color.WHITE);
        txtSalida.setForeground(Color.BLACK);
        frame.setSize(600, 400);
        txtSalida.setEditable(false);
        txtSalida.setAutoscrolls(true);
        txtSalida.setLineWrap(true);
        estadoJugador = -1;
        this.estadoApuestas = -1;
        jugadoresRestantes = 0;
        cantJugadoresAgregados = 0;
        estadoCantidadJugadores = 1;
        esperandoNickname = true;
        vaJugadorActual = false;
        cargarApuestas = false;
        flagArrancoPartida = false;
        yaRepartio = false;
        seCargaronTodosLosDatos = false;
        pasoCrupier = false;
        imprimioCartas = false;
        jugadorTuvoBlackjack = false;

        // Configuro para que el boton "Enviar" funcione con enter
        frame.getRootPane().setDefaultButton(btnEnter);

        btnEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSalida.append(txtEntrada.getText() + "\n");
                if (estadoCantidadJugadores == 1){
                    solicitarCantidadJugadores(txtEntrada.getText().toLowerCase());
                }else if(jugadoresRestantes > 0){
                    if (esperandoNickname){
                        procesarSaldoJugadores();
                        esperandoNickname = false;
                        nickname = txtEntrada.getText();
                    }else{
                        esperandoNickname = true;
                        procesarNicknameJugador(nickname, txtEntrada.getText());
                    }
                }else if (cargarApuestas){
                    procesarCargaApuestas(txtEntrada.getText());
                }else if (estadoJugador >= 0){
                    mostrarEstadosJugador(estadoJugador);
                    if (estadoJugador == 0 || estadoJugador > 1){
                        procesarDecisionJugador(txtEntrada.getText());
                    }
                }else if (vaDecisionJugador) {
                    procesarDecisionJugador(txtEntrada.getText().toLowerCase());
                    if ((seCargaronTodosLosDatos && SiguenMasJugadores() && controlador.getPuntajeMano() < 21)){
                        cicloPartida(); // cambia el turno
                        if (estadoJugador < 0) {
                            estadoJugador = checkEstadoMano(); // checkEstadoMano() debe ejecutarse cuando haya iniciado el juego.
                            if (estadoJugador >= 0) {
                                mostrarEstadosJugador(estadoJugador);
                            }
                        }
                    }
                }else if (seCargaronTodosLosDatos && controlador.getPuntajeMano() < 21) { // condicion para comenzar la partida
                    cicloPartida(); // cambia el turno
                    if (estadoJugador < 0) {
                        estadoJugador = checkEstadoMano(); // checkEstadoMano() debe ejecutarse cuando haya iniciado el juego.
                        if (estadoJugador >= 0) {
                            mostrarEstadosJugador(estadoJugador);
                        }
                    }
                }
                if (jugadorTuvoBlackjack){
                    if (controlador.getJugadorTieneBlackjack()){
                        controlador.cambiarTurnoJugador();
                    }
                }
                if (!SiguenMasJugadores() && flagArrancoPartida) finPartida = true;
                if (finPartida){
                    // cicloPartida();
                    turnoCrupier();
                }else if (pasoCrupier){
                    procesarGanadores();
                    reiniciarPartida();
                }
                txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
                txtEntrada.setText("");
            }
        });
    }

    private boolean SiguenMasJugadores(){
        return controlador.getIndiceJugadorActual() != controlador.getCantidadJugadoresTotal();
    }

    private void cambiarTurno(){
        controlador.cambiarTurnoJugador();
    }

    private void reiniciarPartida() {
        estadoJugador = -1;
        this.estadoApuestas = -1;
        jugadoresRestantes = 0;
        cantJugadoresAgregados = 0;
        estadoCantidadJugadores = 1;
        esperandoNickname = true;
        vaJugadorActual = false;
        cargarApuestas = false;
        flagArrancoPartida = false;
        yaRepartio = false;
        seCargaronTodosLosDatos = false;
        pasoCrupier = false;
        controlador.setIndiceJugador(0);
        controlador.setCantidadJugadoresTotales(0);
        controlador.clearJugadores();
        imprimioCartas = false;
        controlador.clearManoCrupier();
        controlador.resetBaraja();
        jugadorTuvoBlackjack = false;
    }

    public void setControlador(controladorConsolaGrafica controlador) {
        this.controlador = controlador;
    }

    private void procesarGanadores(){
        pasoCrupier = false;
        controlador.evaluandoGanadores();
        mostrarMensaje("=============================================");
        mostrarMensaje("Presione Enter para comenzar una nueva partida");
        mostrarMensaje("=============================================");
    }

    private void procesarCargaApuestas(String text) {
        if (text == null){
            mostrarMensaje(controlador.getNombreJugador() + ": ingrese el monto a apostar: ");
        }
        if (!controlador.cargarApuestaJugador(text)){
            mostrarMensaje("[!] El monto debe ser mayor que uno (1) y debe ser menor al saldo disponible." + " ($" + controlador.getSaldoJugadorActual() + ")");
            mostrarMensaje(controlador.getNombreJugador() + ": ingrese el monto a apostar: ");
            // return false;
        }else{
            if (controlador.getIndiceJugadorActual() < controlador.getCantidadJugadoresTotal()-1){
                controlador.cambiarTurnoJugador();
                mostrarMensaje(controlador.getNombreJugador() + ": ingrese el monto a apostar: ");
            }else if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()-1){
                mostrarMensaje("Presione Enter para continuar...");
                cargarApuestas = false;
                vaJugadorActual = true;
                estadoApuestas = 1;
                seCargaronTodosLosDatos = true;
                // controlador.cambiarTurnoJugador();
                controlador.setIndiceJugador(0);
            }
            // return true;
        }
    }

    private void iniciarPartida() {
        // Reparto las cartas
        while (controlador.getIndiceJugadorActual() != controlador.getCantidadJugadoresTotal()){
            controlador.repartirCartasIniciales(controlador.obtenerJugadorActual());
            mostrarManoJugadorVista();
            JOptionPane.showMessageDialog(frame, "Presione OK una vez visto las cartas.");
            JOptionPane.showMessageDialog(frame, "DEBUG: cartas restantes --> " + controlador.cartasRestantes());
            controlador.cambiarTurnoJugador();
        }
        if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()){
            controlador.cambiarTurnoJugador();
            // Coloco el indice en 0 nuevamente para que comience la partida
        }
        cargarApuestas = true;
        flagArrancoPartida = true;
//        seguirPartida();
//        procesarTurnoJugador();
    }

    private void cicloPartida() {
        if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()){
            // chequear condicion
            turnoCrupier();
            return;
        }
        while (controlador.getIndiceJugadorActual() != controlador.getCantidadJugadoresTotal() && !yaRepartio){
            controlador.repartirCartasIniciales(controlador.obtenerJugadorActual());
            mostrarManoJugadorVista();
            JOptionPane.showMessageDialog(frame, "Presione OK una vez visto las cartas.");
            // JOptionPane.showMessageDialog(frame, "DEBUG: cartas restantes --> " + controlador.cartasRestantes());
            controlador.cambiarTurnoJugador();
            finPartida = false;
        }
        yaRepartio = true;
        if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()){
            controlador.cambiarTurnoJugador();
            // Coloco el indice en 0 nuevamente para que comience la partida
        }
        cargarApuestas = false;
        flagArrancoPartida = true;
        // mostrarMensaje("DEBUG -> IndiceJugador = " + controlador.getIndiceJugadorActual());
        mostrarMensaje("Es el turno de: " + controlador.getNombreJugador() + "\n");
        if (controlador.getCantidadJugadoresTotal() > 1 && controlador.getIndiceJugadorActual() >= 1){
            mostrarManoJugadorVista();
        }
        mostrarMensaje("El saldo del jugador es de: " + controlador.getSaldoJugadorActual());
        if (!controlador.crupierTieneCarta()){
            controlador.crupierPideCarta();
            controlador.crupierPideCarta();
        }
        mostrarMensaje("Cartas restantes: " + controlador.cartasRestantes());
        mostrarMensaje("El crupier tiene: " + controlador.crupierMuestraPrimerCarta());
        vaDecisionJugador = true;

        flag = true; // Flag para intercambiar entre decision y check jugador
    }

    public void mostrarConsola(){
        frame.setVisible(true);
    }

    private void procesarTurnoJugador() {
        if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()){
            controlador.setIndiceJugador(0);
            // Reinicio el turno
        }
        if (controlador.getIndiceJugadorActual() != 0) controlador.cambiarTurnoJugador();
        // Y aumento el turno
        mostrarMensaje("DEBUG ---->  Turno jugador = " + controlador.getIndiceJugadorActual());
        if (!flagArrancoPartida) cicloPartida();
        // controlador.setIndiceJugador(0);
        vaJugadorActual = false;
        vaDecisionJugador = true;

        // jugarPartida();
        // Dentro de turnoJugadorActual cambia el turno del jugador
    }

    private void mostrarEstadosJugador(int estadoJugador){
        if (estadoJugador == 1){
            // mostrarManoJugadorVista();
            mostrarMensaje("Felicitaciones " + controlador.getNombreJugador() + " conseguiste BJ!");
            vaDecisionJugador = false;
            jugadorTuvoBlackjack = true;
            if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()-1){
                finPartida = true;
            }
        }else if (estadoJugador == 2){
            mostrarMensaje("Usted pagó el seguro por un valor de ($" + controlador.getApuestaJugador()/2 + " pesos).");
            mostrarMenuOpciones();
            vaDecisionJugador = true;
        }else if (estadoJugador == 3){
            mostrarMenuOpcionesPuedeDividir();
            vaDecisionJugador = true;
        }else if (estadoJugador == 4){
            mostrarMensaje("Se pasó de 21. Perdió la ronda.");
            controlador.cambiarTurnoJugador();
        }else if (estadoJugador == 5){
            mostrarMenuOpcionesYaPidio();
            vaDecisionJugador = true;
        }else if (estadoJugador == 0){
            mostrarMenuOpciones();
            vaDecisionJugador = true;
        }
        this.estadoJugador = -1; // Lo seteo en -1 para que vuelva a ser check

    }

    /**
     * Metodo que recibe la cantidad de jugadores que van a jugar inicialmente
     * @param input -> entrada por teclado del nickname del jugador
     */
    private void solicitarCantidadJugadores(String input) {
        try {
            int cantidad = Integer.parseInt(input);
            if (cantidad >= 1 && cantidad <= 7){
                jugadoresRestantes = cantidad;
                mostrarMensaje("Cantidad de jugadores: " + jugadoresRestantes + "\n");
                estadoCantidadJugadores = 0;
                procesarNombresJugadores();
            }else{
                this.mostrarMensaje("[!] Debes ingresar una cantidad entre 1 y 7\n");
                this.mostrarMensaje("Ingrese la cantidad de jugadores (1 - 7): ");
            }
        } catch (NumberFormatException e) {
            this.mostrarMensaje("[!] Debes ingresar una cantidad entre 1 y 7\n");
            this.mostrarMensaje("Ingrese la cantidad de jugadores (1 - 7): ");
        }
    }


    private void procesarNombresJugadores(){
        if (jugadoresRestantes > 0){
            this.mostrarMensaje("Ingrese el nombre del jugador " + (cantJugadoresAgregados+1) + ": ");
        }else{
            this.mostrarMensajeConSaltoLinea("Los jugadores fueron agregados con éxito.");
        }
    }
    private void procesarSaldoJugadores(){
        if (jugadoresRestantes > 0){
            this.mostrarMensaje("Ingrese el saldo del jugador " + (cantJugadoresAgregados+1) + ": ");
        }else{
            this.mostrarMensajeConSaltoLinea("Los saldos de los jugadores fueron agregados con éxito.");
        }
    }

    private void procesarNicknameJugador(String nickname, String saldo){
        controlador.configurarJugadores(nickname, Integer.parseInt(saldo));
        mostrarMensaje("Jugador cargado con exito...");
        jugadoresRestantes--;
        cantJugadoresAgregados++;
        if (jugadoresRestantes == 0){
            controlador.setCantidadJugadoresTotales(cantJugadoresAgregados);
            mostrarMensaje(controlador.getNombreJugador() + ": ingrese el monto a apostar: ");
            this.cargarApuestas = true;
        }
        procesarNombresJugadores();

    }

    /**
     *
     */
    @Override
    public void iniciarJuego() {
        mostrarConsola();
        this.mostrarMensaje("Bienvenido al Blackjack!\n");
        this.mostrarMensaje("Por favor, ingrese la cantidad de jugadores (1 - 7): ");
    }

    /**
     * @param mensaje
     */
    @Override
    public void mostrarMensajeConSaltoLinea(String mensaje) {

    }

    /**
     * @param mensaje -> le pasa el mensaje para que se muestre en pantalla de la consola.
     */
    @Override
    public void mostrarMensaje(String mensaje) {
        txtSalida.append(mensaje + "\n");
    }
    /**
     *
     */
    @Override
    public void mostrarManoJugadorVista() {
        mostrarMensaje("");
        int sumatoriaPuntaje = 0;
        int aux = 0;
        int ases = 0;
        mostrarMensaje(controlador.getNombreJugador() + " tiene las siguientes cartas:");
        for (Carta cartaJugador : controlador.getCartasMano()) {
            mostrarMensaje(cartaJugador.getValor() + " de " + cartaJugador.getPalo());
            sumatoriaPuntaje += cartaJugador.getValorNumerico();
            if (cartaJugador.getValorNumerico() == 11) ases++;
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
            mostrarMensaje("El puntaje actual es de: " + (controlador.getPuntajeMano()-10) + "/" + controlador.getPuntajeMano());
        }else mostrarMensaje("El puntaje del jugador es de: " + controlador.getPuntajeMano());
        mostrarMensaje("");
    }
    public void separadorMensaje15s(String mensaje) {
        txtSalida.append("%-15s" + mensaje);
    }
    public void separadorMensaje19s(String mensaje) {
        txtSalida.append("\t%-19s" + mensaje);
    }

    /**
     * Metodo donde muestra las manos del jugador cuando divide
     */
    @Override
    public void mostrarManosDivididasJugadorVista() {
        int sumatoriaPuntaje1 = 0;
        int sumatoriaPuntaje2 = 0;
        int ases1 = 0;
        int ases2 = 0;
        int aux1 = 0;
        int aux2 = 0;
        mostrarMensaje("===================================================");
        mostrarMensaje(controlador.getNombreJugador() + " tiene las siguientes cartas en ambas manos: ");
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
        mostrarMensaje("");
        if ((controlador.getJugadorTieneAsMano1() && sumatoriaPuntaje1 < 21) && aux1 < 21){
            mostrarMensaje("El puntaje actual de la mano 1 es: " + (controlador.getPuntajeMano1()-10) + "/" + controlador.getPuntajeMano1());
        }else mostrarMensaje("El puntaje actual de la mano 1 es: " + controlador.getPuntajeMano1());
        if ((controlador.getJugadorTieneAsMano2() && sumatoriaPuntaje2 < 21) && aux2 < 21){
            mostrarMensaje("El puntaje actual de la mano 2 es: " + (controlador.getPuntajeMano2()-10) + "/" + controlador.getPuntajeMano2());
        }else mostrarMensaje("El puntaje actual de la mano 2 es: " + controlador.getPuntajeMano2());
        mostrarMensaje("===================================================");
    }

    /**
     *
     */
    @Override
    public void mostrarManoCrupierVista() {
        mostrarMensaje("");
        int sumatoriaPuntaje = 0;
        int ases = 0;
        int aux = 0;
        mostrarMensaje("El crupier tiene:");
        for (Carta carta : controlador.getManoCrupier()) {
            mostrarMensaje(carta.getValor() + " de " + carta.getPalo());
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
            mostrarMensaje("El puntaje actual del crupier es de: " + (controlador.getPuntajeCrupier()-10) + "/" + controlador.getPuntajeCrupier());
        }else mostrarMensaje("El puntaje actual del crupier es de: " + controlador.getPuntajeCrupier());
    }

    /**
     * Metodo para controlar al Crupier una vez se finaliza la opcion de pedir/doblar/dividir/plantarse
     */
    @Override
    public void turnoCrupier() {
        while (controlador.crupierDebePedirCarta()){
            mostrarMensaje("El crupier está obteniendo una carta...");
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            controlador.crupierPideCarta();
            mostrarManoCrupierVista();
            imprimioCartas = true;
        }
        if (!imprimioCartas) mostrarManoCrupierVista();

        // Verifico si la mano se paso de 21
        if (controlador.crupierSePaso21()){
            mostrarMensaje("El crupier se pasó de los 21.");
        }else mostrarMensaje("El crupier se planta en " + controlador.getPuntajeCrupier() + ".");
        vaJugadorActual = false;
        finPartida = false;
        if (controlador.getPuntajeCrupier() >= 17){
            mostrarMensaje("====================================");
            mostrarMensaje("Evaluando ganadores...");
            mostrarMensaje("Presione Enter para continuar...");
            pasoCrupier = true;
            seCargaronTodosLosDatos = false;
        }
    }

    /**
     * Metodo para desarrollar el juego del Jugador.
     */
    @Override
    public void turnoJugadorActual() {
        boolean insurance = false;
        boolean yaPidio = false;
        boolean yaPregunto = false;
        boolean flagDoblo = false;
        boolean yaMostroMano = false;
        Mano mano = controlador.obtenerManoJugador();
        if (mano != null){
            if (controlador.getJugadorTieneBlackjack()){
                mostrarManoJugadorVista();
                mostrarMensaje("Felicitaciones " + controlador.getNombreJugador() + " conseguiste BJ!");
            }
            while ((!controlador.getSePaso21Index(0) && controlador.getPuntajeMano() != 21) && !flagDoblo){
                if (!yaMostroMano) mostrarManoJugadorVista();
                yaMostroMano = true;
                mostrarMensaje("Es el turno del jugador: " + controlador.getNombreJugador());
                if (controlador.getCrupierTieneAsPrimera() && !yaPregunto){
                    while (!insurance && controlador.getSaldoJugadorActual() >= controlador.getApuestaJugador()/2){
                        int seguroBlackjack = JOptionPane.showConfirmDialog(frame, "ATENCIÓN! El crupier tiene un As de primera.", "¿Desea pagar el seguro?", JOptionPane.YES_NO_OPTION);
                        if (seguroBlackjack == JOptionPane.YES_OPTION){
                            controlador.setPagoSeguroJugador(controlador.obtenerJugadorActual(), true);
                            mostrarMensaje("El jugador " + controlador.getNombreJugador() + " decidió pagar el seguro.");
                            controlador.ajustarSaldoJugador(-(controlador.getApuestaJugador()/2));
                            insurance = true;
                        }else{
                            mostrarMensaje(controlador.getNombreJugador() + " decidió no pagar el seguro.");
                        }
                        yaPregunto = true;
                        break;
                    }
                    if (controlador.getSaldoJugadorActual() < controlador.getApuestaJugador()/2){
                        mostrarMensaje("[!] No podés pagar el seguro debido que no tenés saldo suficiente (" + controlador.getSaldoJugadorActual() + ").");
                    }
                }
                // ===========================================================================
                if (controlador.getJugadorPuedeDividir()){
                    mostrarMensaje(controlador.getNombreJugador() + ": ingrese 'c' para pedir, 'd' para doblar, 's' para dividir o 'p' para plantarse: ");
                }else if(!controlador.getJugadorPuedeDividir()){
                    mostrarMensaje(controlador.getNombreJugador() + ": ingrese 'c' para pedir, 'd' para doblar o 'p' para plantarse: ");
                }
                else if (!controlador.getJugadorPuedeDividir() && controlador.getJugadorPidioCarta()) {
                    mostrarMensaje(controlador.getNombreJugador() + ": ingrese 'c' para pedir o 'p' para plantarse: ");
                }
            }
        }
    }

    /**
     *
     */
    @Override
    public void evaluarGanadoresBlackjack() {

    }

    /**
     *
     */
    @Override
    public void evaluarGanadoresNOBlackjack() {

    }

    private int checkEstadoMano(){
        Mano mano = controlador.obtenerManoJugador();
        if (mano != null) {
            if (controlador.getJugadorTieneBlackjack()) {
                mostrarManoJugadorVista();
                // mostrarMensaje("Felicitaciones " + controlador.getNombreJugador() + " conseguiste BJ!");
                return 1; // 1 == Blackjack al Jugador
            }
            if (controlador.getCrupierTieneAsPrimera() && !controlador.getJugadorPidioCarta()){
                if (controlador.getSaldoJugadorActual() >= controlador.getApuestaJugador()/2){
                    int seguroBlackjack = JOptionPane.showConfirmDialog(frame, "ATENCIÓN! El crupier tiene un As de primera.", "¿Desea pagar el seguro?", JOptionPane.YES_NO_OPTION);
                    if (seguroBlackjack == JOptionPane.YES_OPTION) {
                        controlador.setPagoSeguroJugador(controlador.obtenerJugadorActual(), true);
                        // mostrarMensaje("El jugador " + controlador.getNombreJugador() + " decidió pagar el seguro.");
                        controlador.ajustarSaldoJugador(-(controlador.getApuestaJugador() / 2));
                        return 2; // Jugador Pago Seguro BJ
                    } else {
                        mostrarMensaje(controlador.getNombreJugador() + " decidió no pagar el seguro.");
                    }
                }else mostrarMensaje("[!] No podés pagar el seguro debido que no tenés saldo suficiente (" + controlador.getSaldoJugadorActual() + ").");
            }
            if (controlador.getJugadorPuedeDividir() && controlador.getSaldoJugadorActual() >= controlador.getApuestaJugador()){
                // El jugador puede dividir por lo que retorno un '3'
                return 3; // puede dividir
            }
            if (controlador.getSePaso21Index(0)){
                return 4; // Jugador se pasa de 21.
            }
            if (controlador.getJugadorPidioCarta()) return 5; // Muestra Menu sin posibilidad de doblar ni dividir.

            return 0; // Continua el juego normalmente
        }
        flag = false;
        return -1; // Error en el chequeo
    }
    private void procesarDecisionJugador(String decision){
        if (decision.equals("c")){
            mostrarMensaje(controlador.getNombreJugador() + " pidió una carta.");
            controlador.setJugadorPidioCarta(true);
            controlador.recibirCartaJugador();
            mostrarManoJugadorVista();
            if (!controlador.getSePaso21ManoPrincipal()){
                mostrarMenuOpcionesYaPidio();
                vaDecisionJugador = true;
            }
        }else if (decision.equals("p")){
            mostrarMensaje(controlador.getNombreJugador() + " se plantó con " + controlador.getPuntajeMano() + ".");
        }else if (decision.equals("s")){
            if ((controlador.getSaldoJugadorActual() >= controlador.getApuestaJugador()) && controlador.compararDosCartasIguales()){
                mostrarMensaje(controlador.getNombreJugador() + " dividió la mano.");
                cargarManoDividida();
            }
        }else if (decision.equals("d")){
            if (controlador.getSaldoJugadorActual() >= controlador.getApuestaJugador() && !controlador.getJugadorPidioCarta()){
                mostrarMensaje(controlador.getNombreJugador() + " dobló la mano.");
                controlador.recibirCartaJugador();
                controlador.jugadorDobloMano();
                controlador.setJugadorPidioCarta(true);
                mostrarManoJugadorVista();
                vaDecisionJugador = false;
            }else if (controlador.getJugadorPidioCarta()){
                mostrarMensaje("[!] No podés doblar dado que ya pediste una carta!");
            }else mostrarMensaje("[!] No podés doblar dado que no tenés saldo suficiente!");
        }else mostrarMensaje("[!] Lo que se ingresó no es válido.");
        vaJugadorActual = true;
        if (controlador.getPuntajeMano() == 21){
            this.mostrarMensaje("Felicitaciones, conseguiste 21!");
            cambiarTurno();
            vaDecisionJugador = false;
        }else if (controlador.getSePaso21ManoPrincipal()){
            this.mostrarMensaje("Te pasaste de 21. Perdiste.");
            vaDecisionJugador = false;
        }

        if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()-1 && (controlador.getSePaso21ManoPrincipal() || decision.equals("p"))){
            finPartida = true;
            vaDecisionJugador = false;
        }
        if (decision.equals("p")) cambiarTurno();
        if (decision.equals("d")) cambiarTurno();
    }

    private void cargarManoDividida() {
        mostrarMensaje("Dividiendo manos...");
        controlador.dividirManoJugador();

    }

    private void mostrarMenuOpciones(){
        mostrarMensaje(controlador.getNombreJugador() + ": ingrese 'c' para pedir, 'd' para doblar o 'p' para plantarse: ");
    }
    private void mostrarMenuOpcionesYaPidio(){
        mostrarMensaje(controlador.getNombreJugador() + ": ingrese 'c' para pedir o 'p' para plantarse: ");
    }
    private void mostrarMenuOpcionesPuedeDividir(){
        mostrarMensaje(controlador.getNombreJugador() + ": ingrese 'c' para pedir, 'd' para doblar, 's' para dividir o 'p' para plantarse: ");
    }
}

