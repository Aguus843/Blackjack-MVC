package ar.edu.unlu.blackjack.Controlador;
import ar.edu.unlu.blackjack.Modelo.*;

import java.util.List;

public class controladorConsolaGrafica implements Observador{
    private BlackjackJuego modelo;
    private ar.edu.unlu.blackjack.Vista.ConsolaGrafica.consolaGrafica vista;

    public controladorConsolaGrafica(ar.edu.unlu.blackjack.Vista.ConsolaGrafica.consolaGrafica vista){
        this.vista = vista;
    }

    public void configurarJugadores(String nickname, float saldo){
        modelo.configurarJugadores(nickname, saldo);
    }

    public void setModelo(BlackjackJuego modelo){
        this.modelo = modelo;
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
        modelo.evaluarGanadoresInterfazGrafica();
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

    public boolean cargarApuestaJugador(String monto){
        return modelo.realizarApuestaConsolaGrafica(monto);
    }

    public boolean crupierTieneCarta(){
        return modelo.hasCrupierCard();
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

    public float getSaldoJugadorActual(){
        return modelo.getSaldoJugador();
    }

    public int ingresarSeguroBlackjack(){
        return modelo.ingresarSeguroBlackjack();
    }

    public float getApuestaJugador(){
        return modelo.getApuestaJugador();
    }

    public float getApuestaJugadorMano2(){
        return modelo.getApuestaJugadorMano2();
    }

    public void ajustarSaldoJugador(float monto){
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

    public List<Carta> getCartasMano(){
        return modelo.getListaCartasMano();
    }

    public void setPagoSeguroJugador(Jugador jugador, boolean b){
        modelo.setPagoSeguroJugador(jugador, b);
    }

    public float getMontoApostado(){
        return modelo.getMontoApostado();
    }

    public void setMontoApostado(float monto){
        modelo.setMontoApostado(monto);
    }

    public boolean getPagoSeguroJugador(){
        return modelo.getPagoSeguroJugador();
    }

    public void setJugadorPidioCarta(boolean b){
        modelo.setJugadorPidioCarta(b);
    }

    public boolean getJugadorPidioCarta(){
        return modelo.getJugadorPidioCarta();
    }

    public void clearJugadores(){
        modelo.clearJugadores();
    }

    public void clearManoCrupier(){
        modelo.clearCartasCrupier();
    }

    public void resetBaraja(){
        modelo.reiniciarBarajaMazo();
    }

    public void setNickname(String nickname){
        modelo.setNickname(nickname);
    }

    public void setSaldo(float saldo){
        modelo.setSaldo(saldo);
    }

    public String getNickname(){
        return modelo.getNickname();
    }

    public float getSaldo(){
        return modelo.getSaldo();
    }

    public boolean getJugadorSePlanto(){
        return modelo.getJugadorSePlanto();
    }

    public void setJugadorSePlanto(boolean b){
        modelo.setJugadorSePlanto(b);
    }

    public boolean getTieneBlackjackPorIndiceMano(int indice){
        return modelo.getTieneBlackjackManoIndice(indice);
    }

    public void setJugadorDividio(boolean b){
        modelo.setJugadorDividio(b);
    }

    public boolean getJugadorDividio(){
        return modelo.getJugadorDividio();
    }

    public int manoAUsar(){
        if (!getJugadorDividio()) return 0;
        else if (getJugadorDividio() && (getJugadorSePlanto() || getSePaso21ManoPrincipal())) return 1; // si divide y se planta o se pasa, se utiliza la mano 2
        else return 0; // si divide y no se planta se usa la mano 1
    }

    public boolean getJugadorDoblo(){
        return modelo.getJugadorDoblo();
    }

    public void setJugadorDoblo(boolean b, int indiceMano){
        modelo.setJugadorDoblo(b, indiceMano);
    }


    @Override
    public void update(Observable o, Object arg){
        if (arg instanceof Evento){
            switch ((Evento) arg){
                case ESPACIADOR_EN_CHAT:
                    this.vista.mostrarMensaje("========================================================");
                    break;
                case BLACKJACK:
                    this.vista.mostrarMensaje("Felicitaciones, obtuviste un BJ!");
                    break;
                case CRUPIER_BLACKJACK:
                    this.vista.mostrarMensaje("El crupier obtuvo BJ!");
                    break;
                case SALDO_RESTADO:
                    // this.vista.mostrarJugadorSaldoRestado();
                    this.vista.mostrarMensaje("Se le restó " + (-this.getMontoApostado()) + " del saldo disponible.");
                    break;
                case EMPATO_JUGADOR:
                    this.vista.mostrarMensaje("Empataste con el crupier. Se te devolvió el monto apostado.");
                    break;
                case JUGADOR_APOSTO:
                    this.vista.mostrarMensaje("Apostaste el monto de " + this.getApuestaJugador());
                    break;
                case SALDO_AGREGADO:
                    this.vista.mostrarMensaje("Se te agregó saldo a tu cuenta! " + this.getApuestaJugador()*2);
                    break;
                case SALDO_AGREGADO_EMPATE:
                    this.vista.mostrarMensaje("Se te devolvió el monto apostado! (" + this.getApuestaJugador() + ")");
                    break;
                case JUGADOR_PAGO_SEGURO:
                    this.vista.mostrarMensaje("Pagaste el seguro de Blackjack! (" + this.getApuestaJugador()/2 + ")");
                    break;
                case CRUPIER_BLACKJACK_Y_EMPATE:
                    this.vista.mostrarMensaje("Como ambos tuvieron Blackjack, se te concedió el empate! Se te devolvió el monto apostado.");
                    break;
                case DEVUELTO_POR_SEGURO:
                    this.vista.mostrarMensaje("Dado que el crupier tuvo BJ y vos también, se te devolvió el monto apostado por el seguro.");
                case PERDIO_JUGADOR:
                    this.vista.mostrarMensaje("El jugador " + this.getNombreJugador() + " ha perdido.");
                    break;
                case GANADOR_JUGADOR:
                    this.vista.mostrarMensaje("Felicidades " + getNombreJugador() + " ganaste!");
                    break;
                case MANO_FINALIZADA:
                    this.vista.mostrarMensaje("Mano de finalizada");
                    break;
                case JUGADA_REALIZADA:
                    this.vista.mostrarMensaje("null");
                    break;
                case PUNTUACION_FINAL_JUGADOR:
                    this.vista.mostrarMensaje("El puntaje final de " + this.getNombreJugador() + " es: " + getPuntajeMano());
                    break;
                case PUNTUACION_FINAL_CRUPIER:
                    this.vista.mostrarMensaje("El puntaje final del crupier es: " + this.getPuntajeCrupier());
                    break;
                case ADJUDICAR_GANANCIA:
                    this.vista.mostrarMensaje("Felicitaciones! Ganaste la apuesta --> ($" + this.getApuestaJugador()*2 + ").");
                    break;
                case ADJUDICAR_GANANCIA_BJ:
                    this.vista.mostrarMensaje("Felicitaciones! Ganaste la apuesta con un BJ --> ($" + this.getApuestaJugador()*2.5 + ").");
                case APUESTA_AMBAS_MANOS:
                    this.vista.mostrarMensaje(this.getNombreJugador() + ": tu apuesta para ambas manos son -> Mano 1 (" + this.getApuestaJugador() + ") -> Mano 2 (" + this.getApuestaJugadorMano2() + ").");
                    break;
                case PUNTUACION_MANO1:
                    this.vista.mostrarMensaje("========================= MANO 1 =========================\n");
                    this.vista.mostrarMensaje("Puntuación final de la mano 1 de " + this.getNombreJugador() + " es --> " + this.getPuntajeMano1());
                    break;
                case PUNTUACION_MANO2:
                    this.vista.mostrarMensaje("\n========================= MANO 2 =========================\n");
                    this.vista.mostrarMensaje("Puntuación final de la mano 2 de " + this.getNombreJugador() + " es --> " + this.getPuntajeMano2());
                    break;
                default:
                    break;
            }
        }
    }
}
