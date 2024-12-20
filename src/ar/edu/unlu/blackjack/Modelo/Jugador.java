package ar.edu.unlu.blackjack.Modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Jugador {
    private String nombre;
    private List<Mano> manos;
    private int puntaje;
    private Saldo saldo;
    private float apuesta;
    private float apuestaMano2;
    private boolean doblo;
    private boolean pagoSeguro;
    private Scanner scanner;
    private boolean esTurno;
    private boolean seBajo;
    private float montoDeApuesta;
    private boolean pidioCarta;
    private boolean sePlanto;
    private boolean jugadorDividio;

    public Jugador(String nombre, float saldoInicial) {
        this.nombre = nombre;
        this.manos = new ArrayList<>();
        this.montoDeApuesta = 0;
        this.pidioCarta = false;
        this.puntaje = 0;
        this.saldo = new Saldo(saldoInicial);
        this.apuesta = 0;
        this.apuestaMano2 = 0;
        this.doblo = false;
        this.pagoSeguro = false;
        this.esTurno = false;
        this.seBajo = false;
        this.jugadorDividio = false;
        this.scanner = new Scanner(System.in);
    }
    public boolean getDoblo(){
        if (multiplesManos()){
            if (getManoActual().getDoblo()){
                return true;
            }
            return getMano2().getDoblo();
        }else return getManoActual().getDoblo();
    }
    public void setDoblo(boolean doblo, int indice){
        manos.get(indice).setDoblo(doblo);
    }
    public boolean getSePlanto(){
        return this.sePlanto;
    }
    public void setSePlanto(boolean sePlanto){
        this.sePlanto = sePlanto;
    }
    public void setJugadorDividio(boolean jugadorDividio){
        this.jugadorDividio = jugadorDividio;
    }
    public boolean getJugadorDividio(){
        return this.jugadorDividio;
    }
    public float getSaldo(){
        return saldo.getSaldo();
    }
    public void ajustarSaldo(float monto){
        if (monto > 0){
            saldo.agregarSaldo(monto);
            System.out.println("Saldo agregado: " + monto);
        }else {
            // saldo.retirarSaldo(-monto);
            if (!saldo.retirarSaldo(-monto)) {
                // throw new IllegalArgumentException("[!] Monto insuficiente para hacer la apuesta.");
                System.out.println("Saldo insuficiente para apostar: " + monto);
            }
        }
    }
    public boolean getSeBajo(){
        return this.seBajo;
    }
    public void setJugadorPidioCarta(boolean pidioCarta){
        this.pidioCarta = pidioCarta;
    }
    public boolean getJugadorPidioCarta(){
        return this.pidioCarta;
    }

    public boolean consultarSeguirJugando(String seguir){
        return seguir.equals("1");
    }
    // Metodo que reparte a UNA mano.
    public void repartirCartaAMano(int indexMano, Carta carta){
        List<Mano> manos = getManos();
        if (indexMano  >= 0 && indexMano < manos.size()){
            manos.get(indexMano).recibirCarta(carta);
        }else System.out.println("El indice de mano no es valido.");
    }
    // Metodo que reparte a ambas manos
    public void repartirATodasLasManos(Carta carta){
        for (Mano mano : manos){
            mano.recibirCarta(carta);
        }
    }

    public float mostrarSaldo(){
        return getSaldo();
    }
    public String getNombre() {
        return this.nombre;
    }
    public float getApuesta(){
        return this.apuesta;
    }
    public float getApuestaMano2(){
        return this.apuestaMano2;
    }
    public void setApuestaMano2(float monto){
        this.apuestaMano2 = monto;
    }
    public void setApuesta(float monto){
        this.apuesta = monto;
    }
    public void setMonto(float monto){
        this.montoDeApuesta = monto;
    }
    public float getMontoDeApuesta(){
        return this.montoDeApuesta;
    }
    public boolean getPagoSeguro(){
        return this.pagoSeguro;
    }
    public void agregarMano(){
        manos.add(new Mano());
    }
    public void iniciarMano(){
        manos.clear();
        manos.add(new Mano());
    }
    // Getter de mano actual (Cuando NO hay division)
    public Mano getManoActual(){
        return manos.getFirst();
    }
    public Mano getMano2(){
        return manos.get(1);
    }
    // Getter de Manos
    public List<Mano> getManos(){
        return manos;
    }
    public boolean multiplesManos(){
        return manos.size() > 1;
    }

    public void mostrarManos(){
        int cantManos = getManos().size();
        System.out.println();
        if (cantManos == 0) return;
        if (cantManos == 1){
            int sumatoriaPuntaje = 0;
            System.out.println(getNombre() + " tiene las siguientes cartas:");
            Mano mano = getManoActual();
            for (Carta carta : getManoActual().getMano()) {
                System.out.printf("%s de %s\n", carta.getValor(), carta.getPalo());
                sumatoriaPuntaje += carta.getValorNumerico();
                manos.getFirst().actualizarPuntaje();
                manos.getFirst().setPuntaje(manos.getFirst().getPuntaje());
            }
            if (manos.getFirst().tieneAs() && sumatoriaPuntaje <= 20){
                System.out.printf("El puntaje actual es de: %d/%d\n", manos.getFirst().getPuntaje()-10, manos.getFirst().getPuntaje());
            }else System.out.println("El puntaje actual es de: " + manos.getFirst().getPuntaje());
            System.out.println("===========================================");

        }else mostrarManosDivididas();
    }

    public void mostrarManosDivididas(){
        int sumatoriaPuntaje1 = 0;
        int sumatoriaPuntaje2 = 0;
        System.out.println("===================================================");
        System.out.println(getNombre() + " tiene las siguientes cartas en ambas manos:");
        Carta cartasMano1;
        Carta cartasMano2;
        int puntajedebug;

        int cantidadCartas = Math.max(getManoActual().getMano().size(), getMano2().getMano().size());
        System.out.printf("-= MANO 1=-\t\t\t\t\t %-17s\n", "-= MANO 2 =-");
        for (int i = 0; i < cantidadCartas; i++){
            if (i < getManoActual().getMano().size()){
                cartasMano1 = getManoActual().getMano().get(i);
                System.out.printf("%s %s", cartasMano1.getValor(), cartasMano1.getPalo());
                sumatoriaPuntaje1 += cartasMano1.getValorNumerico();
            }else System.out.printf("%-11s", "");
            if (i < getMano2().getMano().size()){
                cartasMano2 = getMano2().getMano().get(i);
                System.out.printf("%-17s %s de %s\n", "", cartasMano2.getValor(), cartasMano2.getPalo());
                sumatoriaPuntaje2 += cartasMano2.getValorNumerico();
                // manos.get(1).setPuntaje(sumatoriaPuntaje2);
            }else System.out.printf("%-15s\n", "");
//            puntajedebug = manos.getFirst().getPuntaje();
//            System.out.printf("[DEBUG] El puntaje es %d\n", puntajedebug);
        }
        System.out.println();
        if (getManoActual().tieneAs() && (getManoActual().getPuntaje()-10 + getManoActual().getPuntaje() != 32)){
            System.out.printf("El puntaje actual de la mano %d es de: %d/%d\n", 1, getManoActual().getPuntaje()-10, getManoActual().getPuntaje());
        }else System.out.printf("El puntaje actual de la mano %d es de: %d\n", 1, getManoActual().getPuntaje());
        if (getMano2().tieneAs() && (getMano2().getPuntaje()-10 + getMano2().getPuntaje() != 32)){
            System.out.printf("El puntaje actual de la mano %d es de: %d/%d\n", 2, getMano2().getPuntaje()-10, getMano2().getPuntaje());
        }else System.out.printf("El puntaje actual de la mano %d es de: %d\n", 2, getMano2().getPuntaje());
        System.out.println("===================================================");
    }

    public boolean puedeDividir(){
        Mano mano = getManoActual();
        if (mano != null){
            return mano.getMano().getFirst().getValor().equals(mano.getMano().get(1).getValor());
        }
        return false;
    }
    public void setPagoSeguro(boolean b) {
        this.pagoSeguro = b;
    }
//    public boolean tieneBlackjack(){
//        List<Mano> manos = getManos();
//        for (Mano mano : manos) {
//            if ((mano.getManoCrupier().getFirst().equals("A")) && (mano.getManoCrupier().get(1).equals("10") || mano.getManoCrupier().get(1).equals("J") || mano.getManoCrupier().get(1).equals("Q") || mano.getManoCrupier().get(1).equals("K"))) {
//                return true;
//            } else
//                return (mano.getManoCrupier().getFirst().equals("10") || mano.getManoCrupier().getFirst().equals("J") || mano.getManoCrupier().getFirst().equals("Q") || mano.getManoCrupier().getFirst().equals("K") && mano.getManoCrupier().get(1).getValor().equals("A"));
//        }
//        return false;
//    }
    public boolean tieneBlackjack(){
        List<Mano> manos = getManos();
        for (Mano mano : manos) {
            String primeraCarta = mano.getMano().getFirst().getValor(); // Asegúrate de que `getValor()` devuelve el valor como String
            String segundaCarta = mano.getMano().get(1).getValor();

            if (primeraCarta.equals("A") && (segundaCarta.equals("10") || segundaCarta.equals("J") || segundaCarta.equals("Q") || segundaCarta.equals("K"))) {
                return true;
            } else if ((primeraCarta.equals("10") || primeraCarta.equals("J") || primeraCarta.equals("Q") || primeraCarta.equals("K")) && segundaCarta.equals("A")) {
                return true;
            }
        }
        return false;
    }
}
