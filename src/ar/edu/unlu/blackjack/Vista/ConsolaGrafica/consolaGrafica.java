package ar.edu.unlu.blackjack.Vista.ConsolaGrafica;

import ar.edu.unlu.blackjack.Controlador.controladorConsolaGrafica;
import ar.edu.unlu.blackjack.Modelo.Jugador;
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

    controladorConsolaGrafica controlador;
    VistaConsola vista;
    private int jugadoresRestantes;
    private int cantJugadoresAgregados;
    private int estadoCantidadJugadores;
    private String nickname;
    private boolean vaJugadorActual;
    private boolean esperandoNickname;

    public consolaGrafica() {
        frame = new JFrame("--= Blackjack =--");
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.pack();
        frame.setLocationRelativeTo(null);
        txtSalida.setBackground(Color.WHITE);
        txtSalida.setForeground(Color.BLACK);
        frame.setSize(600, 600);
        txtSalida.setEditable(false);
        txtSalida.setAutoscrolls(true);
        txtSalida.setLineWrap(true);
        jugadoresRestantes = 0;
        cantJugadoresAgregados = 0;
        estadoCantidadJugadores = 1;
        esperandoNickname = true;

        btnEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSalida.append(txtEntrada.getText() + "\n");
                if (estadoCantidadJugadores == 1) {
                    solicitarCantidadJugadores(txtEntrada.getText());
                }else if(jugadoresRestantes > 0){
                    String texto = txtEntrada.getText();
                    if (esperandoNickname){
                        String nickname = texto;
                        txtSalida.append("Nombre ingresado: " + nickname + "\n");
                        procesarSaldoJugadores();
                        txtEntrada.setText(null);
                        esperandoNickname = false;
                    }else{
                        String saldo = texto;
                        txtSalida.append("Saldo ingresado: " + saldo + "\n");
                        txtEntrada.setText(null);
                        esperandoNickname = true;
                        procesarNicknameJugador(nickname, saldo);
                    }
                }else if(vaJugadorActual){
                    procesarTurnoJugador(txtEntrada.getText());
                }
                txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
                txtEntrada.setText("");
            }
        });
    }

    public void mostrarConsola(){
        frame.setVisible(true);
    }

    private void procesarTurnoJugador(String inputDecision) {
        vista.turnoJugadorActual();
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
            iniciarJuego();
        }
    }

    private void procesarNicknameJugador(String nickname, String saldo){
        controlador.configurarJugadores(nickname, Integer.parseInt(saldo));
        jugadoresRestantes--;
        cantJugadoresAgregados++;

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

    }
}

