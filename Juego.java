package ec.edu.espol.clases;

import java.util.ArrayList;
import java.util.Scanner;

public class Juego {
    private ArrayList<Carta> baraja;
    private ArrayList<Carta> lineaJuego;
    private Jugador[] jugadores = new Jugador[2]; 

    public Juego() {
        //Inicialización de la baraja de forma aleatoria con todas las cartas del juego
        this.baraja = Utilitaria.crearBaraja();
        //Inicialización de la linea de juego
        this.lineaJuego = new ArrayList<>();
        //Inicialización de las manos del jugador y de la máquina
        ArrayList<Carta> manoJugador = new ArrayList<>();
        ArrayList<Carta> manoMaquina = new ArrayList<>();
        //Solicitud del nombre del jugador
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese su nombre: ");
        String nombreJugador = sc.nextLine();
        //Llenado de las manos del jugador y de la máquina de manera aleatoria, 7 cartas por jugador
        int numeroCartasPorJugador = 7;
        for (int i = 0; i < numeroCartasPorJugador*2; i++) {
            int indAleatorio = (int) (Math.random() * this.baraja.size());
            Carta cartaExtraida = baraja.remove(indAleatorio);
            if (i < numeroCartasPorJugador)
                manoJugador.add(cartaExtraida);
            else
                manoMaquina.add(cartaExtraida);
        }
        //Inicialización del jugador y de la máquina en base a sus a sus atributos obtenidos
        jugadores[0] = (new Jugador(nombreJugador,manoJugador));
        jugadores[1] = (new Jugador(manoMaquina));
        int indAleatorio = (int) (Math.random() * this.baraja.size());
        //Obtención y validación del índice de una carta numérica aleatoria
        while (!(baraja.get(indAleatorio) instanceof Numerica)) {
            indAleatorio = (int) (Math.random() * this.baraja.size());
        }
        //Obtención de una carta numérica aleatoria y adición de esta como primera carta de la línea de juego
        Carta cartaExtraida = baraja.remove(indAleatorio);
        lineaJuego.add(cartaExtraida);
    }

    public ArrayList<Carta> getBaraja() {
        return baraja;
    }

    public ArrayList<Carta> getLineaJuego() {
        return lineaJuego;
    }

    public Jugador[] getJugadores() {
        return jugadores;
    }

    public void mostrarUltimaCartaLinea() {
        Carta ultimaCartaLinea = lineaJuego.get(lineaJuego.size()-1);
        System.out.println("Línea de juego => " + ultimaCartaLinea.toString());
    }

    public Carta removerUltimaCartaBaraja() {
        Carta ultimaCartaBaraja = baraja.remove(baraja.size()-1);
        return ultimaCartaBaraja;
    }

    public boolean agregarCartaLinea(Carta actualPlayerCard, Jugador actualPlayer, Jugador nextPlayer) {

        Carta ultimaCartaLinea = lineaJuego.get(lineaJuego.size()-1);
        boolean posibilityToAdd = false;

        //Suponga que el jugador había bloqueado el turno de la máquina o que la máquina había bloqueado el turno del jugador
        //Esta condición se asegura de desactivar dicho estado de bloqueo en el siguiente turno
        if (nextPlayer.getEstadoDeBloqueo())
            nextPlayer.desactivarEstadoDeBloqueo();
        
        //Estas condiciones se encargan de validar si se puede o no agregar una carta específica a la línea de juego
        if (actualPlayerCard instanceof Numerica && ultimaCartaLinea instanceof Numerica) {

            Numerica cartaJugadorActual = (Numerica) actualPlayerCard;
            Numerica lastCardLine = (Numerica) ultimaCartaLinea;
            if (cartaJugadorActual.getColor() == lastCardLine.getColor()) {
                lineaJuego.add(actualPlayerCard);
                actualPlayer.removerCartaMano(actualPlayerCard);
                posibilityToAdd = true;
            }
            else if (cartaJugadorActual.getNumero() == lastCardLine.getNumero()) {
                lineaJuego.add(actualPlayerCard);
                actualPlayer.removerCartaMano(actualPlayerCard);
                posibilityToAdd = true;
            }

        } 
        else if (actualPlayerCard instanceof Numerica && ultimaCartaLinea instanceof Comodin) {

            if (actualPlayerCard.getColor() == ultimaCartaLinea.getColor()) {
                lineaJuego.add(actualPlayerCard);
                actualPlayer.removerCartaMano(actualPlayerCard);
                posibilityToAdd = true;
            }

        }
        else if (actualPlayerCard instanceof Comodin && (ultimaCartaLinea instanceof Numerica || ultimaCartaLinea instanceof Comodin)) {

            if (actualPlayerCard.getColor() == 'N') {

                lineaJuego.add(actualPlayerCard);
                actualPlayer.removerCartaMano(actualPlayerCard);
                Comodin newLastCardLine = (Comodin) lineaJuego.get(lineaJuego.size()-1);
                
                //Si la nueva carta comodin agregada por un jugador a la linea de juego es de tipo +4 
                //Se toman 4 cartas del tope de la baraja y son asignadas a la mano del siguiente jugador
                if (newLastCardLine.getTipo() == "+4") {
                    for (int i = 0; i < 4; i++) {
                        Carta cartaExtraida = baraja.remove(baraja.size()-1);
                        nextPlayer.anadirCartaMano(cartaExtraida);
                    }
                }

                //Si la nueva carta comodin agregada por un jugador a la linea de juego es de tipo +2 
                //Se toman 2 cartas del tope de la baraja y son asignadas a la mano del siguiente jugador
                if (newLastCardLine.getTipo() == "+2") {
                    for (int i = 0; i < 2; i++) {
                        Carta cartaExtraida = baraja.remove(baraja.size()-1);
                        nextPlayer.anadirCartaMano(cartaExtraida);
                    }
                }

                //Si fue el jugador humano quien agregó la nueva carta comodín a la línea de juego
                //Se le solicitará que ingrese el nuevo color
                if (!actualPlayer.getNombre().equalsIgnoreCase("Máquina")) {
                    Scanner sc = new Scanner(System.in);
                    System.out.print("Nuevo Color (R|A|V|Z): ");
                    String nuevoColor = sc.nextLine();
                    //Este es un validador en caso de que el jugador no este seleccionando de entre alguno de los colores permitidos
                    while(!(nuevoColor.equalsIgnoreCase("R")||nuevoColor.equalsIgnoreCase("A")||nuevoColor.equalsIgnoreCase("V")||nuevoColor.equalsIgnoreCase("Z"))) {
                        System.out.print("Nuevo Color (R|A|V|Z): ");
                        nuevoColor = sc.nextLine();
                        sc.close();
                    }
                    char newColor = nuevoColor.toUpperCase().charAt(0);
                    newLastCardLine.setColor(newColor);
                }
                else {
                    //Si fue la máquina quien agregó la nueva carta comodín a la línea de juego
                    //Esta seleccionará un color aleatorio de entre los colores permitidos
                    int indAleatorio = (int) (Math.random() * 4);
                    char newColor = Utilitaria.colores[indAleatorio];
                    newLastCardLine.setColor(newColor);
                    System.out.println("Nuevo Color: " + newColor);
                }

                posibilityToAdd = true;

            } 
            else if (actualPlayerCard.getColor() == ultimaCartaLinea.getColor()) {

                lineaJuego.add(actualPlayerCard);
                actualPlayer.removerCartaMano(actualPlayerCard);
                Comodin newLastCardLine = (Comodin) lineaJuego.get(lineaJuego.size()-1);

                if (newLastCardLine.getTipo() == "^" || newLastCardLine.getTipo() == "&") 
                    nextPlayer.activarEstadoDeBloqueo();
                if (newLastCardLine.getTipo() == "%") {
                    //Si fue el jugador humano quien agregó la nueva carta comodín a la línea de juego
                    //Se le solicitará que ingrese el nuevo color
                    if (!actualPlayer.getNombre().equalsIgnoreCase("Máquina")) {
                        Scanner sc = new Scanner(System.in);
                        System.out.print("Nuevo Color: ");
                        String nuevoColor = sc.nextLine();
                        //Este es un validador en caso de que el jugador no este seleccionando de entre alguno de los colores permitidos
                        while(!(nuevoColor.equalsIgnoreCase("R")||nuevoColor.equalsIgnoreCase("A")||nuevoColor.equalsIgnoreCase("V")||nuevoColor.equalsIgnoreCase("Z"))) {
                            System.out.print("Nuevo Color: ");
                            nuevoColor = sc.nextLine();
                            sc.close();
                        }
                        char newColor = nuevoColor.toUpperCase().charAt(0);
                        newLastCardLine.setColor(newColor);
                    }
                    else {
                        //Si fue la máquina quien agregó la nueva carta comodín a la línea de juego
                        //Esta seleccionará un color aleatorio de entre los colores permitidos
                        int indAleatorio = (int) (Math.random() * 4);
                        char newColor = Utilitaria.colores[indAleatorio];
                        newLastCardLine.setColor(newColor);
                        System.out.println("Nuevo Color: " + newColor);
                    }

                }
                //Si la nueva carta comodin agregada por un jugador a la linea de juego es de tipo +4 
                //Se toman 4 cartas del tope de la baraja y son asignadas a la mano del siguiente jugador
                if (newLastCardLine.getTipo() == "+4") {
                    for (int i = 0; i < 4; i++) {
                        Carta cartaExtraida = baraja.remove(baraja.size()-1);
                        nextPlayer.anadirCartaMano(cartaExtraida);
                    }
                }
                //Si la nueva carta comodin agregada por un jugador a la linea de juego es de tipo +2 
                //Se toman 2 cartas del tope de la baraja y son asignadas a la mano del siguiente jugador
                if (newLastCardLine.getTipo() == "+2") {
                    for (int i = 0; i < 2; i++) {
                        Carta cartaExtraida = baraja.remove(baraja.size()-1);
                        nextPlayer.anadirCartaMano(cartaExtraida);
                    }
                }

                posibilityToAdd = true;

            }
        }

        return posibilityToAdd;

    }
}