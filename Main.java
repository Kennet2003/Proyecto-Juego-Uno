package ec.edu.espol.main;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

import ec.edu.espol.clases.*;
// autores:
// Toapanta Jose
// Mejillon Kennet
// Tinoco Ariela
public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("¡Bienvenido al Juego de UNO!\n");
        Juego game = new Juego();
        Jugador[] players = game.getJugadores();
        Jugador player = players[0];
        Jugador machine = players[1];
        int actualPlayerHandSize = player.getMano().size();
        int actualMachineHandSize = machine.getMano().size();
        int menorNumeroCartas = actualPlayerHandSize;
        int turn = 0;

        while (menorNumeroCartas != 0) {

            System.out.println();

            ArrayList<Carta> baraja = game.getBaraja();
            int cartasRestantesBaraja = baraja.size();
            ArrayList<Carta> lineaJuego = game.getLineaJuego();
            int cartasLineaJuego = lineaJuego.size();
            //Esta condición se ha adjuntado en caso de que la baraja se quede sin cartas
            //Si esto sucede entonces la línea de juego debe tener la mayoría de cartas 
            //Así todas las cartas de la línea de juego menos la última pasarán a formar parte de la baraja y
            //A su vez esta baraja se volverá a mezclar para así asegurar la aleatoriedad
            if (cartasRestantesBaraja == 0) {
                for (int i = 0; i < cartasLineaJuego-1; i++) {
                    Carta cartaExtraida = lineaJuego.remove(0);
                    baraja.add(cartaExtraida);
                }
                Collections.shuffle(baraja);
            }

            //Esta condición permite ver que jugador tiene el turno actual
            if (turn == 0) {
                System.out.println("Turno de " + player.getNombre() + ":");
            }
            else if (turn == 1) {
                System.out.println("Turno de " + machine.getNombre() + ":");
            }

            System.out.println();
            System.out.println("Cartas restantes en la baraja: " + cartasRestantesBaraja);
            game.mostrarUltimaCartaLinea();
            player.imprimirMano();
            machine.imprimirMano();

            if (turn == 0) {

                System.out.print("Índice de carta a jugar (0 es el primero o -1 para robar): ");
                int indiceCarta = sc.nextInt();
                sc.nextLine();
                //Este es un validador en caso de que el jugador no este seleccionando de entre alguno de los índices permitidos
                while (!(-1 <= indiceCarta && indiceCarta < actualPlayerHandSize)) {
                    System.out.print("Índice de carta a jugar (0 es el primero o -1 para robar): ");
                    indiceCarta = sc.nextInt();
                    sc.nextLine();
                }
                boolean possibilityToContinue = false;
                if (indiceCarta != -1) {
                    Carta selectedCard = player.getCarta(indiceCarta);
                    possibilityToContinue = game.agregarCartaLinea(selectedCard, player, machine);
                }
                //Si la carta del jugador actual sí se pudo agregar a la línea de juego entonces se da paso al siguiente jugador
                if (possibilityToContinue == true && machine.getEstadoDeBloqueo() == false) {
                    turn = 1;
                }
                else if (indiceCarta == -1) {
                    //Si el jugador decidió robar entonces se da paso al siguiente jugador
                    System.out.print("El jugador toma una carta de la baraja\n");
                    Carta cartaExtraida = game.removerUltimaCartaBaraja();
                    player.anadirCartaMano(cartaExtraida);
                    turn = 1;
                }
                else if (possibilityToContinue == false) {
                    //Si la carta del jugador actual no se pudo agregar a la línea de juego 
                    //Entonces este absorbe una carta del tope de la baraja y se da paso al siguiente jugador
                    System.out.print("No es posible jugar esa carta => El jugador toma una carta de la baraja");
                    System.out.println();
                    Carta cartaExtraida = game.removerUltimaCartaBaraja();
                    player.anadirCartaMano(cartaExtraida);
                    turn = 1;
                }

            } 
            else if (turn == 1) {
                
                //Mientras se cumpla que una carta no se puede agregar a la línea de juego y el indice de la iteración
                //sea menor al número de cartas actuales en la mano de la máquina entonces funcionará el while
                boolean possibilityToContinue = false;
                int j = 0;
                while (!possibilityToContinue && j < actualMachineHandSize) {
                Carta selectedCard = machine.getCarta(j);
                possibilityToContinue = game.agregarCartaLinea(selectedCard, machine, player);
                    if (possibilityToContinue) {
                        System.out.print("Índice de carta a jugar (0 es el primero): " + j);
                        System.out.println();
                    }
                j++;
                }

                //Si la carta del jugador actual sí se pudo agregar a la línea de juego entonces se da paso al siguiente jugador
                if (possibilityToContinue == true && player.getEstadoDeBloqueo() == false) {
                    turn = 0;
                }
                else if (possibilityToContinue == false) {
                    //Si la carta del jugador actual no se pudo agregar a la línea de juego 
                    //Entonces este absorbe una carta del tope de la baraja y se da paso al siguiente jugador
                    System.out.print("No hay cartas en la mano para jugar => El jugador toma una carta de la baraja");
                    System.out.println();
                    Carta cartaExtraida = game.removerUltimaCartaBaraja();
                    machine.anadirCartaMano(cartaExtraida);
                    turn = 0;
                }

            actualPlayerHandSize = player.getMano().size();
            actualMachineHandSize = machine.getMano().size();
            menorNumeroCartas = Math.min(actualPlayerHandSize, actualMachineHandSize);

            //Esta condición se activa cuando a algún jugador le queda alguna carta
            if (actualPlayerHandSize == 1 || actualMachineHandSize == 1)
                System.out.println("UNO");
            }

        }

        //Esta condición permite determinar el ganador
        if (actualPlayerHandSize == 0) 
            System.out.println("\n¡" + player.getNombre() + " ha ganado el juego!");
        else 
            System.out.println("\n¡" + machine.getNombre() + " ha ganado el juego!");

    }
}