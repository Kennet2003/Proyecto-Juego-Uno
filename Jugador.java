package ec.edu.espol.clases;

import java.util.ArrayList;

public class Jugador {
    private final String nombre;
    private ArrayList<Carta> mano;
    private boolean estadoDeBloqueo;

    public Jugador(String nombre, ArrayList<Carta> mano) {
        this.nombre = nombre;
        this.mano = mano;
        this.estadoDeBloqueo = false;
    }
    
    public Jugador(ArrayList<Carta> mano) {
        this("Máquina", mano);
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public Carta getCarta(int i) {
        if (0 <= i && i < mano.size())
            return mano.get(i);
        return null;
    }

    public boolean getEstadoDeBloqueo() {
        return estadoDeBloqueo;
    }

    public void activarEstadoDeBloqueo() {
        this.estadoDeBloqueo = true;
    }

    public void desactivarEstadoDeBloqueo() {
        this.estadoDeBloqueo = false;
    }

    public void anadirCartaMano(Carta c) {
        mano.add(c);
    }

    public void removerCartaMano(Carta c) {
        mano.remove(c);
    }

    public void imprimirMano() {
        StringBuilder msj = new StringBuilder();
        for (int i = 0; i < mano.size(); i++) {
            Carta c = mano.get(i);
            if (c instanceof Numerica) {
                Numerica cN = (Numerica) c;
                msj.append(cN.toString());
            }
            if (c instanceof Comodin) {
                Comodin cC = (Comodin) c;
                msj.append(cC.toString());
            }
            if (i < mano.size()-1)
                msj.append(" ");
        }    
        System.out.println("Mano de " + nombre + " => " + msj.toString());
    }
}