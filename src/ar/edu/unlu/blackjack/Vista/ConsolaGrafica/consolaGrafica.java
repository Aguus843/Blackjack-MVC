package ar.edu.unlu.blackjack.Vista.ConsolaGrafica;

import ar.edu.unlu.blackjack.Controlador.controladorConsolaGrafica;
import ar.edu.unlu.blackjack.Modelo.Carta;
import ar.edu.unlu.blackjack.Modelo.Mano;
import ar.edu.unlu.blackjack.Vista.IVista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.List;

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
    private boolean esperandoSaldo;
    private boolean seCargaronJugadores;
    private boolean inicioMano2;

    public consolaGrafica() {
        frame = new JFrame("Consola Blackjack");
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.pack();
        frame.setLocationRelativeTo(null);
        txtSalida.setBackground(Color.BLACK);
        txtSalida.setForeground(Color.WHITE);
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
        esperandoSaldo = false;
        seCargaronJugadores = false;
        inicioMano2 = false;


        // Configuro para que el botón "Enviar" funcione con enter
        frame.getRootPane().setDefaultButton(btnEnter);

        btnEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSalida.append(txtEntrada.getText() + "\n");
                if (estadoCantidadJugadores == 1){
                    solicitarCantidadJugadores(txtEntrada.getText().toLowerCase());
                }
                if (jugadoresRestantes > 0 && !seCargaronJugadores) {
                    // proceso de carga de nickname y saldo del jugador
                    if (esperandoNickname){
                        procesarCargaNickname();
                    }else if (!txtEntrada.getText().matches("\\d*\\.?\\d+")){
                        controlador.setNickname(txtEntrada.getText());
                        procesarCargaSaldo();
                    }else if (esperandoSaldo){
                        float saldo = Float.parseFloat(txtEntrada.getText());
                        controlador.setSaldo(saldo);
                        esperandoNickname = false;
                        esperandoSaldo = false;
                        cargarJugador();
                    }
                    if (jugadoresRestantes == 0){
                        controlador.setIndiceJugador(0);
                        mostrarMensaje("Comenzando la carga de apuestas...");
                        procesarCargaApuestas("");
                        seCargaronJugadores = true;
                        jugadoresRestantes = cantJugadoresAgregados;
                        controlador.setCantidadJugadoresTotales(cantJugadoresAgregados);
                    }
                }else if (jugadoresRestantes > 0){
                    procesarCargaApuestas(txtEntrada.getText());
                    if (jugadoresRestantes == 0){
                        controlador.setIndiceJugador(0);
                        mostrarMensaje("Comenzando partida...");
                        cicloPartida();
                        if (estadoJugador < 0){
                            estadoJugador = checkEstadoMano();
                            if (estadoJugador >= 0) mostrarEstadosJugador(estadoJugador);
                        }
                    }
                    // ------- lógica partida comenzada --------
                }else if (flagArrancoPartida){
                    procesarDecisionJugador(txtEntrada.getText().toLowerCase(), controlador.manoAUsar());
                    if (!chequearSiTerminoPartida()){
                        if (estadoJugador < 0 && controlador.getJugadorDividio()){
                            estadoJugador = checkEstadoManosDivididas(controlador.manoAUsar());
                            if (estadoJugador >= 0) mostrarEstadosJugador(estadoJugador);
                        }
                    }else if (estadoJugador < 0 && !flag){ // flag -> jugador intento dividir
                        estadoJugador = checkEstadoMano();
                        if (estadoJugador > 0) mostrarEstadosJugador(estadoJugador);
                    }
                }else if (estadoJugador >= 0){
                    mostrarEstadosJugador(estadoJugador);
                    if (estadoJugador == 0 || estadoJugador > 1) {
                        procesarDecisionJugador(txtEntrada.getText().toLowerCase(), controlador.manoAUsar());
                    }
                }else if (vaDecisionJugador){
                    procesarDecisionJugador(txtEntrada.getText().toLowerCase(), controlador.manoAUsar());
                }

                // condición para chequear si termino la partida contando el índice de jugadores -> si es fin termina y el crupier agarra cartas
                if (chequearSiTerminoPartida() && flagArrancoPartida){
                    procesarGanadores();
                    reiniciarPartida();
                }
                txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
                txtEntrada.setText("");
            }
        });
    }
    private boolean chequearSiTerminoPartida(){
        return controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal();
    }

    private void cargarJugador(){
        controlador.configurarJugadores(controlador.getNickname(), (float) controlador.getSaldo());
        esperandoNickname = true;
        esperandoSaldo = false;
        jugadoresRestantes--;
        cantJugadoresAgregados++;
        cambiarTurno();
        if (jugadoresRestantes > 0){
            procesarCargaNickname();
        }
    }
    private void procesarCargaNickname() {
        if (jugadoresRestantes == 0){
            return;
        }
        mostrarMensaje("Ingrese el nombre del jugador " + (cantJugadoresAgregados+1) + ": ");
        esperandoNickname = false;
        esperandoSaldo = true;

    }
    private void procesarCargaSaldo() {
        if (jugadoresRestantes == 0){
            mostrarMensaje("Todos los jugadores fueron agregados con éxito.");
            return;
        }
        mostrarMensaje("Ingrese el saldo del jugador " + (cantJugadoresAgregados+1) + ": ");
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
        seCargaronJugadores = false;
        inicioMano2 = false;
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

    private void procesarCargaApuestas(String monto) {
        if (!monto.matches("\\d*\\.?\\d+") && controlador.getNombreJugador() != null){
            this.mostrarMensaje(controlador.getNombreJugador() + ": ingrese el monto a apostar (Saldo: $" + controlador.getSaldo() + "): ");
            return;
        }
        if (!Objects.equals(monto, "")){
            controlador.setMontoApostado(Float.parseFloat(monto));
            if (!controlador.cargarApuestaJugador(monto)) {
                mostrarMensaje("Error al apostar. Monto excedido.");
                procesarCargaApuestas("");
                return;
            }
            jugadoresRestantes--;
        }
        if (jugadoresRestantes > 0){
            txtEntrada.setText("");
            cambiarTurno();
            procesarCargaApuestas("");
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
            // mostrarManoJugadorVista();
            // JOptionPane.showMessageDialog(frame, "Presione OK una vez visto las cartas.");
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
            // mostrarManoJugadorVista();
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
            cambiarTurno(); // ya con BJ no se puede seguir pidiendo ni plantarse.
            if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()){
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
        }else if (estadoJugador == 6){
            if (!controlador.getJugadorDividio()){
                cambiarTurno();
                vaDecisionJugador = false;
            }else{
                if (!inicioMano2){
                    // no inicio mano 2
                    cargarManoDividida();
                    // sigue con la siguiente mano
                }else{
                    cambiarTurno();
                    // si esta la mano iniciada y se planta se cambia el turno
                }
            }
        }
        else if (estadoJugador == 0){
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
                // procesarNombresJugadores();
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
        controlador.configurarJugadores(nickname, Float.parseFloat(saldo));
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
        txtSalida.update(txtSalida.getGraphics());
        // txtEntrada.setText("");
    }
    public void mostrarMensaje2(String mensaje){
        txtSalida.append(mensaje);
        txtSalida.update(txtSalida.getGraphics());
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
        mostrarMensaje("-= MANO 1=-\t\t -= MANO 2 =-\n");
        for (int i = 0; i < cantidadCartas; i++){
            if (i < controlador.getCantidadCartasMano1()){
                cartasMano1 = controlador.getCartasMano1(i);
                mostrarMensaje2(cartasMano1.getValor() + " de " + cartasMano1.getPalo() + "       ");
                sumatoriaPuntaje1 += cartasMano1.getValorNumerico();
                if (cartasMano1.getValorNumerico() == 11) ases1++;
            }else mostrarMensaje2("\t");
            if (i < controlador.getCantidadCartasMano2()){
                cartaMano2 = controlador.getCartasMano2(i);
                mostrarMensaje2("\t");
                mostrarMensaje2(cartaMano2.getValor() + " de " + cartaMano2.getPalo() + "\n");
                sumatoriaPuntaje2 += cartaMano2.getValorNumerico();
                if (cartaMano2.getValorNumerico() == 11) ases2++;
            }else mostrarMensaje2("\t");
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
            if (controlador.getJugadorSePlanto()){
                return 6; // 6 = se planta el jugador
            }
            if (!controlador.getJugadorDividio()) mostrarManoJugadorVista();
            if (controlador.getJugadorTieneBlackjack()) {
                // mostrarManoJugadorVista();
                // mostrarMensaje("Felicitaciones " + controlador.getNombreJugador() + " conseguiste BJ!");
                return 1; // 1 == Blackjack al Jugador
            }
            if (controlador.getCrupierTieneAsPrimera() && !controlador.getJugadorPidioCarta()){
                if (controlador.getSaldoJugadorActual() >= controlador.getApuestaJugador()/2){
                    int seguroBlackjack = JOptionPane.showConfirmDialog(
                            frame, // JFrame
                            "ATENCIÓN! El crupier tiene un As de primera.",
                            "¿Desea pagar el seguro?",
                            JOptionPane.YES_NO_OPTION
                    );
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
            if (controlador.getSePaso21Index(0)){
                return 4; // Jugador se pasa de 21.
            }
            if (controlador.getJugadorPuedeDividir() && controlador.getSaldoJugadorActual() >= controlador.getApuestaJugador()){
                // El jugador puede dividir por lo que retorno un '3'
                return 3; // puede dividir
            }
            if (controlador.getJugadorPidioCarta()) return 5; // Muestra Menu sin posibilidad de doblar ni dividir.

            return 0; // Continua el juego normalmente
        }
        flag = false;
        return -1; // Error en el chequeo
    }
    private void procesarDecisionJugador(String decision, int i){
        if (decision.equals("c")){
            mostrarMensaje(controlador.getNombreJugador() + " pidió una carta.");
            controlador.setJugadorPidioCarta(true);
            // controlador.recibirCartaJugador();
            controlador.setRepartirCartaAMano(i);
            if (controlador.getManosJugador().size() == 1) mostrarManoJugadorVista();
            else mostrarManosDivididasJugadorVista();
            if (!controlador.getSePaso21Index(i)){
                mostrarMenuOpcionesYaPidio();
                vaDecisionJugador = true;
            }
        }else if (decision.equals("p")){
            if (controlador.getManosJugador().size() == 2) mostrarMensaje(controlador.getNombreJugador() + " se plantó con " + controlador.getPuntajeManosIndices(i) + " en la mano " + (i+1) + ".");
            else mostrarMensaje(controlador.getNombreJugador() + " se plantó con " + controlador.getPuntajeMano() + ".");
            controlador.setJugadorSePlanto(true);
        }else if (decision.equals("s")){
            if ((controlador.getSaldoJugadorActual() >= controlador.getApuestaJugador()) && controlador.compararDosCartasIguales() && !controlador.getJugadorDividio()){
                mostrarMensaje(controlador.getNombreJugador() + " dividió la mano.");
                dividirMano();
            }else if (controlador.getJugadorDividio()){
                mostrarMensaje("[!] -> Ya dividiste. No podés dividir dos veces!");
            }
            else{
                // mostrarMensaje("[!] No podés dividir dado que no tenés saldo suficiente!");
                mostrarMensaje("[!] -> Saldo no disponible para dividir.");
                estadoJugador = checkEstadoMano();
                if (estadoJugador >= 0) mostrarEstadosJugador(estadoJugador);
                flag = true;
                return;
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
        if (!controlador.getJugadorSePlanto()){
            if (controlador.getPuntajeMano() == 21){
                this.mostrarMensaje("Felicitaciones, conseguiste 21!");
                cambiarTurno();
                vaDecisionJugador = false;
            }else if (controlador.getSePaso21ManoPrincipal()){
                this.mostrarMensaje("Te pasaste de 21. Perdiste.");
                vaDecisionJugador = false;
                cambiarTurno();
            }else if (controlador.getJugadorDoblo()){
                cambiarTurno();
            }
        }else cambiarTurno();
        if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()){
            finPartida = true;
            vaDecisionJugador = false;
        }
        // if (decision.equals("p") || decision.equals("d")) cambiarTurno();
    }
    private void dividirMano(){
        mostrarMensaje("Dividiendo manos...");
        controlador.dividirManoJugador();
        controlador.setRepartirCartaAMano(0);
        controlador.setRepartirCartaAMano(1);
        controlador.setJugadorDividio(true);
        mostrarManosDivididasJugadorVista();
    }

    private void cargarManoDividida() {
        List<Mano> manos = controlador.getManosJugador();
        boolean termino;
        boolean terminoMano2 = false;
        boolean imprimePrimeraVez = false;
        if (!inicioMano2 && controlador.manoAUsar() == 1){
            // seteo como false que pidio carta para continuar con la mano 2
            controlador.setJugadorPidioCarta(false);
            controlador.setJugadorSePlanto(false);
            estadoJugador = checkEstadoManosDivididas(1);
            inicioMano2 = true;
            // doy inicio a la mano 2
            mostrarEstadosJugador(estadoJugador);
        }
    }
    private int checkEstadoManosDivididas(int indiceMano){
        // mismo que checkEstadoMano() pero con lógica de manos divididas (2 manos para un jugador).
        List<Mano> manos = controlador.getManosJugador();
        if (manos != null){
            if (controlador.getJugadorSePlanto()){
                return 6; // jugador se plantó con la mano i
            }
            // mostrarManosDivididasJugadorVista();
            if (controlador.getTieneBlackjackPorIndiceMano(indiceMano)){
                return 1; // blackjack para la mano i
            }
            // No se consulta por seguro dado que para eso primero se consulta con una sola mano y luego se procede al juego
            if (controlador.getSePaso21Index(indiceMano)) return 4;
            if (controlador.getJugadorPidioCarta()) return 5;
            return 0; // retorna 0 y sigue normalmente
        }
        return -1;
    }

    private void mostrarMenuOpciones(){
        if (controlador.getJugadorDividio()){
            if (!inicioMano2){
                mostrarMensaje("MANO " + (controlador.manoAUsar() + 1) + " --> " + controlador.getNombreJugador() + ": ingrese 'c' para pedir, 'd' para doblar o 'p' para plantarse: ");
            }else mostrarMensaje("MANO " + (controlador.manoAUsar() + 2) + " --> " + controlador.getNombreJugador() + ": ingrese 'c' para pedir, 'd' para doblar o 'p' para plantarse: ");
        }
        else mostrarMensaje(controlador.getNombreJugador() + ": ingrese 'c' para pedir, 'd' para doblar o 'p' para plantarse: ");
    }
    private void mostrarMenuOpcionesYaPidio(){
        if (controlador.getJugadorDividio()){
            if (!inicioMano2){
                mostrarMensaje("MANO " + (controlador.manoAUsar() + 1) + " --> " + controlador.getNombreJugador() + ": ingrese 'c' para pedir o 'p' para plantarse: ");
            }else mostrarMensaje("MANO " + (controlador.manoAUsar() + 2) + " --> " + controlador.getNombreJugador() + ": ingrese 'c' para pedir o 'p' para plantarse: ");
        }
        else mostrarMensaje(controlador.getNombreJugador() + ": ingrese 'c' para pedir o 'p' para plantarse: ");
    }
    private void mostrarMenuOpcionesPuedeDividir(){
        mostrarMensaje(controlador.getNombreJugador() + ": ingrese 'c' para pedir, 'd' para doblar, 's' para dividir o 'p' para plantarse: ");
    }
}

