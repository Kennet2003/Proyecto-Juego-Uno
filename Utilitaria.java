package ec.edu.espol.clases;
import java.util.ArrayList;
import java.util.Collections;

public class Utilitaria {

    static final char[] colores = {'R','A','V','Z','N'};
    static final String[] comodines = {"^","&","%","+4","+2"};

    public static ArrayList<Carta> crearBaraja() {
        ArrayList<Carta> baraja = new ArrayList<>();
        //Inicialización y llenado de la baraja de manera ordenada
        for (int i = 0; i < colores.length; i++) {
            //Creación de cartas numéricas rojas, amarillas, verdes y azules; del 0 al 9 por cada color
            for (int j = 0; j <= 9; j++) {
                if (colores[i] != 'N')
                    baraja.add(new Numerica(colores[i], j));
            }

            for (int k = 0; k < comodines.length; k++) {
                //Creacion de 2 cartas comodín rojas, amarillas, verdes y azules; de tipo reverse, bloqueo, más 4 y más 2 por cada color
                if (colores[i] != 'N' && !comodines[k].equals("%")) {
                    baraja.add(new Comodin(colores[i],comodines[k]));
                    baraja.add(new Comodin(colores[i],comodines[k]));
                }
                //Creacion de 2 cartas comodín negras; de tipo más 4, más 2 y cambio de color por cada color
                if (colores[i] == 'N' && !comodines[k].equals("^") && !comodines[k].equals("&")) {
                    baraja.add(new Comodin(colores[i],comodines[k]));
                    baraja.add(new Comodin(colores[i],comodines[k]));
                }
            }
        }
        //Introducción de la aleatoriedad al mezclar la baraja
        Collections.shuffle(baraja);
        return baraja;
    }

    public static char[] getColores() {
        return colores;
    }

    public static String[] getComodines() {
        return comodines;
    }   
}