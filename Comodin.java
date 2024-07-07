package ec.edu.espol.clases;

public class Comodin extends Carta {
    private final String tipo;

    public Comodin(char color,String tipo) {
        super(color);
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setColor(char color) {
        this.color=color;
    }

    @Override
    public String toString() {
        return "[" + color + " " + tipo + "]";
    }
}
