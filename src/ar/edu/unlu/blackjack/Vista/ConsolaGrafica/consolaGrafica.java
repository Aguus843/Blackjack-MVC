package ar.edu.unlu.blackjack.Vista.ConsolaGrafica;

import ar.edu.unlu.blackjack.Controlador.controladorConsolaGrafica;
import ar.edu.unlu.blackjack.Modelo.Jugador;
import ar.edu.unlu.blackjack.Modelo.Mano;
import ar.edu.unlu.blackjack.Vista.IVista;
import ar.edu.unlu.blackjack.Vista.VistaConsola;

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
    VistaConsola vista;
    private int jugadoresRestantes;
    private int cantJugadoresAgregados;
    private int estadoCantidadJugadores;
    private String nickname;
    private boolean vaJugadorActual;
    private boolean esperandoNickname;

    public consolaGrafica() {
        frame = new JFrame("Blackjack - Agustin Weisbek - POO");
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
        jugadoresRestantes = 0;
        cantJugadoresAgregados = 0;
        estadoCantidadJugadores = 1;
        esperandoNickname = true;
        vaJugadorActual = false;

        // Configuro para que el boton "Enviar" funcione con enter
        frame.getRootPane().setDefaultButton(btnEnter);

        btnEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSalida.append(txtEntrada.getText() + "\n");
                if (estadoCantidadJugadores == 1) {
                    solicitarCantidadJugadores(txtEntrada.getText());
                }else if(jugadoresRestantes > 0){
                    String texto = txtEntrada.getText();
                    if (esperandoNickname){
                        txtSalida.append("Nombre ingresado: " + texto + "\n");
                        procesarSaldoJugadores();
                        esperandoNickname = false;
                        nickname = texto;
                    }else{
                        txtEntrada.setText(null);
                        txtSalida.append("Saldo ingresado: " + texto + "\n");
                        esperandoNickname = true;
                        procesarNicknameJugador(nickname, texto);
                    }
                }else if(jugadoresRestantes == -1){
                    vaJugadorActual = true;
                }else if(vaJugadorActual){
                    procesarTurnoJugador();
                }
                txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
                txtEntrada.setText("");
                txtEntrada.requestFocus();
            }
        });
    }

    private void iniciarPartida() {
        // Seteo el turno al primer jugador y voy cargando las apuestas
        boolean seCargoApuesta = false;
        while (controlador.getIndiceJugadorActual() != controlador.getCantidadJugadoresTotal()){
            while (!seCargoApuesta){
                this.mostrarMensaje(controlador.obtenerJugadorActual() + ": ingrese el monto a apostar: ");
                if (!controlador.cargarApuestaJugador(controlador.obtenerJugadorActual())){
                    mostrarMensaje("[!] El monto debe ser mayor que uno (1) y debe ser menor al saldo disponible.");
                }
                seCargoApuesta = true;
            }
            if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()){
                controlador.cambiarTurnoJugador();
                seCargoApuesta = false;
            }
            // Leo al último jugador en caso de que sea > 1 los jugadores
            if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()-1){
                mostrarMensaje(controlador.getNombreJugador() + " tu saldo actual es de " + controlador.getSaldoJugadorActual() + " pesos.");
            }
        }
        if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()){
            controlador.cambiarTurnoJugador(); // Pongo denuevo el indice en 0 para recorrer todos los jugadores
            // y mostrar las cartas correspondientes.
        }
        // Reparto las cartas
        while (controlador.getIndiceJugadorActual() != controlador.getCantidadJugadoresTotal()){
            controlador.repartirCartasIniciales(controlador.obtenerJugadorActual());
            mostrarManoJugadorVista();
            JOptionPane.showMessageDialog(frame, "Presione OK una vez visto las cartas.");
            controlador.cambiarTurnoJugador();
        }
        if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()){
            controlador.cambiarTurnoJugador();
            // Coloco el indice en 0 nuevamente para que comience la partida
        }
        seCargoApuesta = true;
        procesarTurnoJugador();
    }

    private void jugarPartida() {
        if (controlador.getIndiceJugadorActual() == controlador.getCantidadJugadoresTotal()-1){
            // chequear condicion
            turnoCrupier();
            return;
        }
        mostrarMensaje("DEBUG -> IndiceJugador = " + controlador.getIndiceJugadorActual());
        mostrarMensaje("Es el turno de: " + controlador.getNombreJugador() + "\n");
        mostrarMensaje("El saldo del jugador es de: " + controlador.getSaldoJugadorActual() + "\n");
        if (!controlador.crupierTieneCarta()){
            controlador.crupierPideCarta();
            controlador.crupierPideCarta();
        }
        mostrarMensaje("Cartas restantes: " + controlador.cartasRestantes());
        mostrarMensaje("El crupier tiene: " + controlador.crupierMuestraPrimerCarta());
        turnoJugadorActual();

        procesarTurnoJugador();
    }

    public void mostrarConsola(){
        frame.setVisible(true);
    }

    private void procesarTurnoJugador() {
        controlador.cambiarTurnoJugador();
        jugarPartida();
        // Dentro de turnoJugadorActual cambia el turno del jugador
    }


    public void setControlador(controladorConsolaGrafica controlador) {
        this.controlador = controlador;
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
            // Llama al iniciarJuego() una vez se complete los saldos de los jugadores.
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
            JOptionPane.showMessageDialog(frame, "Presione el botón para iniciar la partida");
            mostrarMensaje("Iniciando juego...\n");
            jugarPartida();
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
     * @param jugador
     */
    @Override
    public void mostrarMenuJuego(Jugador jugador) {

    }

    /**
     *
     */
    @Override
    public void mostrarManoJugadorVista() {

    }

    /**
     *
     */
    @Override
    public void mostrarManosDivididasJugadorVista() {

    }

    /**
     *
     */
    @Override
    public void mostrarManoCrupierVista() {

    }

    /**
     *
     */
    @Override
    public void turnoCrupier() {

    }

    /**
     *
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

        }
    }
}

