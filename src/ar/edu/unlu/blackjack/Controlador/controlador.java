package ar.edu.unlu.blackjack.Controlador;
import ar.edu.unlu.blackjack.Modelo.*;
import ar.edu.unlu.blackjack.Vista.VistaConsola;

import java.util.List;

public class controlador {
    public Blackjack_SaldoYManos modelo;
    public VistaConsola vista;

    public controlador(){
        this.modelo = new Blackjack_SaldoYManos();
        // this.vista = new VistaConsola();
    }

    public void configurarJugadores(String nickname, int saldo){
        modelo.configurarJugadores(nickname, saldo);
    }

    public boolean realizarApuesta(){
        return modelo.realizarApuesta();
    }

    public void crupierPideCarta(){
        modelo.crupierPideCarta();
    }

    public void repartirCartasIniciales(Jugador jugadorActual){
        modelo.repartirCartasIniciales(jugadorActual);
    }

    public String crupierMuestraPrimerCarta(){
        return modelo.crupierMuestraPrimerCarta();
    }

    public Jugador obtenerJugadorActual(){
        return modelo.getJugadorActualTurno();
    }

    public boolean terminoRonda(String ingreso){
        return modelo.getJugadorActualTurno().consultarSeguirJugando(ingreso);
    }
    public String cartasRestantes(){
        return modelo.ControladorCartasRestantes();
    }

    public void evaluandoGanadores(){
        modelo.evaluarGanadores();
    }

    public String ingresarPorTeclado(){
        return modelo.ingresarPorTeclado();
    }
    public void cambiarTurnoJugador(){
        modelo.cambiarTurno();
    }

    public int getIndiceJugadorActual(){
        return modelo.getIndice();
    }

    public int getCantidadJugadoresTotal(){
        return modelo.getCantidadJugadores();
    }

    public boolean cargarApuestaJugador(Jugador jugador){
        return modelo.cargarApuesta(jugador);
    }

    public boolean crupierTieneCarta(){
        return modelo.hasCrupierCard();
    }

    public void jugadorPideCarta(Jugador jugador){
        vista.mostrarMensajeConSaltoLinea(jugador.getNombre() + " pidió una carta");
    }
    public Mano obtenerManoJugador(){
        return modelo.getManoJugador();
    }

    public String getNombreJugador(){
        return modelo.getNombreJugador();
    }

    public boolean jugadorActualTieneAs(){
        return modelo.getJugadorTieneAs();
    }

    public boolean primerCartaEsAs(){
        return modelo.primerCartaTieneAs();
    }

    public int getPuntajeMano(){
        return modelo.getPuntajeManoJugador();
    }

    public boolean getJugadorTieneBlackjack(){
        return modelo.getJugadorTieneBlackjack();
    }

    public boolean getCrupierTieneAsPrimera(){
        return modelo.getCrupierTieneAsPrimeraCarta();
    }

    public int getSaldoJugadorActual(){
        return modelo.getSaldoJugador();
    }

    public int ingresarSeguroBlackjack(){
        return modelo.ingresarSeguroBlackjack();
    }

    public int getApuestaJugador(){
        return modelo.getApuestaJugador();
    }

    public void ajustarSaldoJugador(int monto){
        modelo.setAjustarApuestaJugador(monto);
    }

    public boolean getJugadorPuedeDividir(){
        return modelo.getJugadorPuedeDividir();
    }

    public void recibirCartaJugador(){
        modelo.setRecibirCarta();
    }

    public boolean compararDosCartasIguales(){
        return modelo.getComparacionCartasIguales();
    }

    public void dividirManoJugador(){
        modelo.setDividirMano();
    }

    public void setRepartirCartaAMano(int index){
        modelo.setRepartirCartaAMano(index);
    }

    public Mano getManoPrincipal(){
        return modelo.obtenerManoPrincipal();
    }

    public Mano getManoSecundaria(){
        return modelo.obtenerManoSecundaria();
    }

    public int getCantidadCartasMano1(){
        return modelo.CantidadCartasMano1();
    }

    public int getCantidadCartasMano2(){
        return modelo.CantidadCartasMano2();
    }

    public void setIndiceJugador(int index){
        modelo.setIndiceJugador(index);
    }

    public void setCantidadJugadoresTotales(int cantidad){
        modelo.setCantidadJugadores(cantidad);
    }

    public void jugadorDobloMano(){
        modelo.jugadorDobloMano();
    }

    public void jugadorDobloManoIndice(int index){
        modelo.jugadorDobloManoIndice(index);
    }

    public int getCantidadManosJugador(){
        return modelo.getCantidadManosJugador();
    }

    public Carta getCartasMano1(int index){
        return modelo.getCartasMano1(index);
    }

    public Carta getCartasMano2(int index){
        return modelo.getCartasMano2(index);
    }

    public boolean getJugadorTieneAsMano1(){
        return modelo.getJugadorTieneAsMano1();
    }

    public boolean getJugadorTieneAsMano2(){
        return modelo.getJugadorTieneAsMano2();
    }

    public int getPuntajeMano1(){
        return modelo.getPuntajeMano1();
    }

    public int getPuntajeMano2(){
        return modelo.getPuntajeMano2();
    }

    public boolean getSePaso21ManoPrincipal(){
        return modelo.getSePaso21();
    }

    public boolean getSePaso21Index(int indice){
        return modelo.getSePaso21Index(indice);
    }

    public List<Carta> getManoCrupier(){
        return modelo.getManoCrupier();
    }

    public int getPuntajeCrupier(){
        return modelo.getPuntajeCrupier();
    }

    public boolean crupierDebePedirCarta(){
        return modelo.crupierDebePedirCarta();
    }

    public void crupierPedirCarta(){
        modelo.crupierPideCarta();
    }

    public boolean crupierSePaso21(){
        return modelo.crupierSePaso21();
    }

    public int getPuntajeManosIndices(int indice){
        return modelo.getPuntajeManoIndices(indice);
    }

    public List<Mano> getManosJugador(){
        return modelo.getManosJugador();
    }
}
