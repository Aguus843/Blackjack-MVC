package ar.edu.unlu.blackjack.Modelo;

import ar.edu.unlu.blackjack.Controlador.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Blackjack_SaldoYManos implements Observable {
    private final Mazo mazo;
    private final List<Jugador> jugadores;
    private final Crupier crupier;
    private final Scanner scanner;
    private int cantidadJugadores;
    private int indiceJugador;
    private ArrayList<controlador> misObservadores;

    public Blackjack_SaldoYManos() {
        mazo = new Mazo();
        crupier = new Crupier();
        misObservadores = new ArrayList<>();
        jugadores = new ArrayList<Jugador>();
        this.indiceJugador = 0;
        scanner = new Scanner(System.in);

    }
    @Override
    public void addObserver(controlador controlador) {
        misObservadores.add(controlador);
    }
    @Override
    public void deleteObserver(controlador controlador) {
        misObservadores.remove(controlador);
    }
    @Override
    public void notificarObservadores(Evento evento){
        for (int i = 0; i < misObservadores.size(); i++){
            misObservadores.get(i).update(this, evento);
        }
    }
    public void crupierPideCarta(){
        crupier.pedirCarta();
    }
    public String crupierMuestraPrimerCarta(){
        return crupier.mostrarPrimeraCarta();
    }
    public Jugador getJugadorActualTurno(){
        return jugadores.get(indiceJugador);
    }
    public void cambiarTurno(){
        if (indiceJugador == jugadores.size()) this.indiceJugador = 0;
        else this.indiceJugador++;
    }
    public void setIndiceJugador(int indice){
        this.indiceJugador = indice;
    }
    public String ControladorCartasRestantes(){
        return String.valueOf(mazo.cartasRestantes());
    }
    public Mano getManoJugador(){
        return jugadores.get(indiceJugador).getManoActual();
    }
    public List<Carta> getListaCartasMano(){
        return jugadores.get(indiceJugador).getManoActual().getMano();
    }
    public String getNombreJugador(){
        return jugadores.get(indiceJugador).getNombre();
    }
    public boolean getJugadorTieneAs(){
        return jugadores.get(indiceJugador).getManos().getFirst().tieneAs();
    }
    public boolean getJugadorTieneAsMano1(){
        return jugadores.get(indiceJugador).getManoActual().tieneAs();
    }
    public boolean getJugadorTieneAsMano2(){
        return jugadores.get(indiceJugador).getMano2().tieneAs();
    }
    public int getPuntajeMano1(){
        return jugadores.get(indiceJugador).getManoActual().getPuntaje();
    }
    public int getPuntajeMano2(){
        return jugadores.get(indiceJugador).getMano2().getPuntaje();
    }
    public int getPuntajeManoIndices(int index){
        return jugadores.get(indiceJugador).getManos().get(index).getPuntaje();
    }
    public boolean primerCartaTieneAs(){
        return jugadores.get(indiceJugador).getManoActual().getMano().getFirst().equals("A");
    }
    public int getPuntajeManoJugador(){
        return jugadores.get(indiceJugador).getManoActual().getPuntaje();
    }
    public boolean getJugadorTieneBlackjack(){
        return jugadores.get(indiceJugador).tieneBlackjack();
    }
    public boolean getCrupierTieneAsPrimeraCarta(){
        return crupier.tieneAsPrimera();
    }
    public int getSaldoJugador(){
        return jugadores.get(indiceJugador).getSaldo();
    }
    public int ingresarSeguroBlackjack(){
        return jugadores.get(indiceJugador).getManoActual().seguroBlackjack(getJugadorActualTurno());
    }
    public int getApuestaJugador(){
        return jugadores.get(indiceJugador).getApuesta();
    }
    public void setAjustarApuestaJugador(int monto){
        jugadores.get(indiceJugador).ajustarSaldo(monto);
    }
    public void setRecibirCarta(){
        getManoJugador().recibirCarta(mazo.repartirCarta());
    }
    public boolean alMenosUnoSigueJugando(String ingreso){
        return ingreso.equals("1");
    }
    public String ingresarPorTeclado(){
        return scanner.nextLine();
    }
    public int getIndice(){
        return this.indiceJugador;
    }
    public int getCantidadJugadores(){
        return this.cantidadJugadores;
    }
    public void setCantidadJugadores(int cantidad){
        this.cantidadJugadores = cantidad;
    }
    public boolean hasCrupierCard(){
        return crupier.tieneCarta();
    }
    public boolean getJugadorPuedeDividir(){
        return jugadores.get(indiceJugador).puedeDividir();
    }
    public boolean getComparacionCartasIguales(){
        return getManoJugador().getMano().getFirst().getValor().equals(getManoJugador().getMano().get(1).getValor());
    }
    public void setDividirMano(){
        getJugadorActualTurno().getManoActual().dividirMano(getJugadorActualTurno());
    }
    public void setRepartirCartaAMano(int indice){
        getJugadorActualTurno().repartirCartaAMano(indice, mazo.repartirCarta());
    }
    public Mano obtenerManoPrincipal(){
        return getJugadorActualTurno().getManoActual();
    }
    public Mano obtenerManoSecundaria(){
        return getJugadorActualTurno().getMano2();
    }
    public int CantidadCartasMano1(){
        return getJugadorActualTurno().getManoActual().getMano().size();
    }
    public int CantidadCartasMano2(){
        return getJugadorActualTurno().getMano2().getMano().size();
    }
    public void jugadorDobloMano(){
        jugadores.get(indiceJugador).getManoActual().doblarMano(getJugadorActualTurno());
    }

    // Metodo para doblar la mano del jugador en caso de haber dividido.
    public void jugadorDobloManoIndice(int indice){
        jugadores.get(indiceJugador).getManos().get(indice).doblarMano(getJugadorActualTurno());
    }
    public int getCantidadManosJugador(){
        return jugadores.get(indiceJugador).getManos().size();
    }
    public Carta getCartasMano1(int indice){
        return jugadores.get(indiceJugador).getManoActual().getMano().get(indice);
    }
    public Carta getCartasMano2(int indice){
        return jugadores.get(indiceJugador).getMano2().getMano().get(indice);
    }
    public boolean getSePaso21(){
        return jugadores.get(indiceJugador).getManoActual().sePaso21();
    }
    public boolean getSePaso21Index(int index){
        return jugadores.get(indiceJugador).getManos().get(index).sePaso21();
    }
    public List<Carta> getManoCrupier(){
        return crupier.getMano();
    }
    public int getPuntajeCrupier(){
        return crupier.getPuntajeCrupier();
    }
    public boolean crupierDebePedirCarta(){
        return crupier.debePedirCarta();
    }
    public boolean crupierSePaso21(){
        return getPuntajeCrupier() > 21;
    }
    public List<Mano> getManosJugador(){
        return jugadores.get(indiceJugador).getManos();
    }
    public void setAjustarSaldo(Jugador jugador, int monto){
        jugador.ajustarSaldo(monto);
        if (monto > 0){
            notificarObservadores(Evento.SALDO_AGREGADO);
        }else notificarObservadores(Evento.SALDO_RESTADO);
    }
    public void setApuesta(Jugador jugador, int monto){
        jugador.setApuesta(monto);
        notificarObservadores(Evento.JUGADOR_APOSTO);
    }

    public boolean realizarApuesta(){
        if (this.indiceJugador == jugadores.size()) return true;
        int monto = Integer.parseInt(ingresarPorTeclado());
        if (monto > getJugadorActualTurno().getSaldo() || monto <= 1){
          return false;
        }
        setAjustarSaldo(getJugadorActualTurno(), -monto);
        setApuesta(getJugadorActualTurno(), monto);
        // getJugadorActualTurno().mostrarSaldo();
        getJugadorActualTurno().iniciarMano();
        return true;
    }
    public boolean cargarApuesta(Jugador jugador){
        return realizarApuesta();
    }

    public boolean jugadorActualSeBajo(){
        return getJugadorActualTurno().getSeBajo();
    }

    public void configurarJugadores(String nickname, int saldo){
        jugadores.add(new Jugador(nickname, saldo));
    }

    public void repartirCartasIniciales(Jugador jugador){
        Mano mano;
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < jugador.getManos().size(); j++){
                jugador.repartirCartaAMano(j, mazo.repartirCarta());
            }
        }
    }

    public void evaluarGanadores(){
        if (crupier.tieneBlackjack()) evaluarGanadoresBlackjack();
        else evaluarGanadoresNOBlackjack();
    }

    public void evaluarGanadoresBlackjack(){ // Entra si el crupier tiene Blackjack
        System.out.println("El crupier obtuvo blackjack.");
        notificarObservadores(Evento.CRUPIER_BLACKJACK);
        // for (Jugador jugador : jugadores){
        for (int i = 0; i < jugadores.size(); i++){
            List<Mano> manos = jugadores.get(i).getManos();
            if (jugadores.get(i).tieneBlackjack()){
                // System.out.println(jugador.getNombre() + ": el crupier también obtuvo Blackjack por lo que es un empate!");
                notificarObservadores(Evento.BLACKJACK);
                // System.out.println(jugador.getNombre() + " EMPATÓ!");
                notificarObservadores(Evento.CRUPIER_BLACKJACK_Y_EMPATE);
                devolverApuesta(jugadores.get(i), jugadores.get(i).getApuesta());
            }else{
                if (jugadores.get(i).getPagoSeguro()){
                    // System.out.println(jugador.getNombre() + ": pagó el seguro por lo que se le devuelve el monto apostado.");
                    // notificarObservadores(Evento.CRUPIER_BLACKJACK);
                    devolverApuesta(jugadores.get(i), jugadores.get(i).getApuesta()); // Se le devuelve el monto de la apuesta -> sería el seguro * 2
                }
                notificarObservadores(Evento.PERDIO_JUGADOR);
                // System.out.println(jugador.getNombre() + " PERDIÓ!");
            }
        }
    }

    public void devolverApuesta(Jugador jugador, int apuesta) {
        // System.out.printf("%s: debido al empate se te devolvió el monto apostado (%d)\n", jugador.getNombre(), apuesta);
        if (crupier.tieneBlackjack()) notificarObservadores(Evento.CRUPIER_BLACKJACK);
        else notificarObservadores(Evento.EMPATO_JUGADOR);
        jugador.ajustarSaldo(apuesta);
    }

    public void adjudicarGanancia(Jugador jugador, int apuesta){
        // System.out.printf("%s: felicitaciones! Ganaste la apuesta.\n", jugador.getNombre());
        notificarObservadores(Evento.GANADOR_JUGADOR);
        jugador.ajustarSaldo(2*apuesta);
    }

    public void evaluarGanadoresNOBlackjack(){ // Entra si el crupier NO tiene Blackjack
        int puntajeMano;
        int puntajeCrupier = crupier.getPuntaje();
        List<Mano> manos;
        System.out.println();
        for(Jugador jugador : jugadores){
            manos = jugador.getManos();
            if (jugador.multiplesManos()){
                for (int i = 0; i < 2; i++){
                    System.out.printf("==============================\n\t\tMANO %d\n==============================\n", i + 1);
                    // puntajeMano = jugador.getManos().get(i).getPuntaje();
                    puntajeMano = manos.get(i).getPuntaje();
                    System.out.printf("El puntaje final de la mano %d de %s es: %d --> ", i+1, jugador.getNombre(), jugador.getManos().get(i).getPuntaje());

                    if (manos.get(i).sePaso21()){
                        // System.out.println(jugador.getNombre() + " se paso de los 21. Perdiste!");
                        System.out.printf("PERDISTE!\n");
                    }else if (crupier.getPuntaje() > 21){
                        // System.out.println(jugador.getNombre() + " gana dado que el crupier se paso de 21.");
                        System.out.printf("GANASTE!\n");
                        adjudicarGanancia(jugador, jugador.getApuesta());

                    }else if (puntajeMano > puntajeCrupier){
                        // System.out.println(jugador.getNombre() + " GANA!");
                        System.out.printf("%s: tu mano es ganadora!", jugador.getNombre());
                        adjudicarGanancia(jugador, jugador.getApuesta());
                    }else if (puntajeMano < puntajeCrupier){
                        // System.out.println(jugador.getNombre() + " PERDIÓ!");
                        // System.out.printf("%s: el crupier obtuvo mas puntaje que vos. Perdiste la mano!", jugador.getNombre());
                        System.out.printf("PERDISTE!\n");
                    }else{
                        System.out.printf("EMPATASTE!\n");
                        devolverApuesta(jugador, jugador.getApuesta());
                    }
                    System.out.println();
                }
                System.out.println("El puntaje final del crupier es: " + puntajeCrupier);
            }else{
                System.out.println("========================================================");
                int puntajeManoUnica = jugador.getManoActual().getPuntaje();
                System.out.println();
                System.out.printf("El puntaje final de %s es: %d --> ", jugador.getNombre(), jugador.getManoActual().getPuntaje());
                if (jugador.getManoActual().sePaso21()){
                    System.out.println(jugador.getNombre() + " se paso de los 21. Perdiste!");
                }else if (crupier.getPuntaje() > 21){
                    System.out.println(jugador.getNombre() + " gana dado que el crupier se paso de 21.");
                    adjudicarGanancia(jugador, jugador.getApuesta());
                }else if (puntajeManoUnica > puntajeCrupier){
                    System.out.println(jugador.getNombre() + " GANA!");
                    adjudicarGanancia(jugador, jugador.getApuesta());
                }else if (puntajeManoUnica < puntajeCrupier){
                    System.out.println(jugador.getNombre() + " PERDIÓ!");
                }else{
                    System.out.println(jugador.getNombre() + " empató con el crupier!");
                    devolverApuesta(jugador, jugador.getApuesta());
                }
                System.out.println("El puntaje final del crupier es: " + puntajeCrupier);
            }
        }

    }
}
