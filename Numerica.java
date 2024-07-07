package ec.edu.espol.clases;

public class Numerica extends Carta{
    private final int numero;

    public Numerica(char color, int numero) {
        super(color);
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return "[" + color + " " + numero + "]";
    }
}
