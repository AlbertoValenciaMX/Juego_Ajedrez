package Modelo;

public class Movimientos {

    String[][] tablero;

    // Metodo que comprueba si un movimiento es valido o no
    public boolean esPosibleEsteMovimiento(String[][] tablero, String posicionAntigua, String posicionNueva) {
        this.tablero = tablero;
        String[] posicionesPosibles;
        posicionesPosibles = movimientosAmodificados(tablero, posicionAntigua);

        if (posicionesPosibles != null) {
            for (int i = 0; i < posicionesPosibles.length; i++) {
                if (posicionesPosibles[i].equals(posicionNueva)) {
                    comprobarAcercaDeEnrroque(posicionAntigua);
                    return true;
                }
            }
        }
        return false;
    }

    // Metodo que comprueba si un enroque es posible o no
    private void comprobarAcercaDeEnrroque(String posAntigua) {
        if (posAntigua.equals("74")) {
            Controlador.Controlador.enrroqueReyA = false;
        } else if (posAntigua.equals("70")) {
            Controlador.Controlador.enrroqueTorreIzquierdaA = false;
        } else if (posAntigua.equals("77")) {
            Controlador.Controlador.enrroqueTorreDerechaA = false;
        } else if (posAntigua.equals("04")) {
            Controlador.Controlador.enrroqueReyB = false;
        } else if (posAntigua.equals("00")) {
            Controlador.Controlador.enrroqueTorreIzquierdaB = false;
        } else if (posAntigua.equals("07")) {
            Controlador.Controlador.enrroqueTorreDerechaB = false;
        }
    }

    // Metodo que regresa la nueva posicion al realizar un movimiento en las piezas
    // blancas
    public String[] movimientosAmodificados(String[][] tableroM, String posicion) {
        String[] posiblesMovimientos = movimientosFichas(tableroM, posicion);
        int x = Character.getNumericValue(posicion.charAt(1));
        int y = Character.getNumericValue(posicion.charAt(0));

        return modificacionTodosMovimientos(tableroM, posicion, posiblesMovimientos);
    }

    // Metodo que regresa la nueva posicion al realizar un movimiento en las piezas
    // negras
    public String[] movimientosBmodificados(String[][] tableroM, String posicion) {
        String[] posiblesMovimientos = movimientosFichas(tableroM, posicion);
        int x = Character.getNumericValue(posicion.charAt(1));
        int y = Character.getNumericValue(posicion.charAt(0));

        return modificacionTodosMovimientos(tableroM, posicion, posiblesMovimientos);
    }

    /*
     * Metodo que valida si en la nueva posicion se encuentra vacia, una pieza del
     * mismo color o una pieza del contrincante y que regresa un arreglo con las
     * nuevas posiciones
     */
    private String[] modificacionTodosMovimientos(String[][] tableroFuturo, String posicionInicial,
            String[] posicionesFinales) {
        String posicionesDefinitivas = "";

        int xInicial = Character.getNumericValue(posicionInicial.charAt(1));
        int yInicial = Character.getNumericValue(posicionInicial.charAt(0));

        // Validacion de si en la nueva posicion se encuentra o no una pieza
        String ficha = fichaDeLaCasilla(tableroFuturo, yInicial, xInicial);
        if (posicionesFinales != null) {
            for (int i = 0; i < posicionesFinales.length; i++) {
                try {
                    String posicionFinal = posicionesFinales[i];
                    int xFinal = Character.getNumericValue(posicionFinal.charAt(1));
                    int yFinal = Character.getNumericValue(posicionFinal.charAt(0));

                    String tableroM[][] = new String[8][8];

                    copiarTableroPrimeroAlSegundo(tableroFuturo, tableroM);

                    tableroM[yFinal][xFinal] = tableroM[yInicial][xInicial];
                    tableroM[yInicial][xInicial] = "";

                    if (ficha.charAt(0) == 'A') {
                        if (reyAenJaque(tableroM) == false) {
                            posicionesDefinitivas += "" + yFinal + "" + xFinal + "_";
                        }
                    } else if (ficha.charAt(0) == 'B') {
                        if (reyBenJaque(tableroM) == false) {
                            posicionesDefinitivas += "" + yFinal + "" + xFinal + "_";
                        }
                    }
                } catch (Exception ex) {
                }

            }

            String[] arrayPosiciones = posicionesDefinitivas.split("_");
            return arrayPosiciones;
        }
        return null;
    }

    // Metodo de Creacion de una copia del tablero (Cuando sufre alguna
    // modificacion)
    private void copiarTableroPrimeroAlSegundo(String[][] tableroOrigen, String[][] tableroCopia) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tableroCopia[i][j] = tableroOrigen[i][j];
            }
        }
    }

    /*
     * Metodo que controla y manda llamar todos los posibles movimientos
     * correspondiente al elegir una pieza en el tablero (piezas del bot) para
     * regresar los movimientos validos
     */
    public String[] todosLosMovimientosFichasB(String[][] tableroM) {

        String posicionesTotales = "";

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String ficha = fichaDeLaCasilla(tableroM, i, j);

                // Validacion de cual ficha ha sido seleccionada
                if (ficha.equals("B_torre")) {
                    posicionesTotales += transformarApalabra(movimientoTorreBSoloAtaque(tableroM, i, j));
                } else if (ficha.equals("B_alfil")) {
                    posicionesTotales += transformarApalabra(movimientoAlfilBSoloAtaque(tableroM, i, j));
                } else if (ficha.equals("B_reina")) {
                    posicionesTotales += transformarApalabra(movimientoReinaBSoloAtaque(tableroM, i, j));
                } else if (ficha.equals("B_caballo")) {
                    posicionesTotales += transformarApalabra(movimientoCaballoBSoloAtaque(tableroM, i, j));
                } else if (ficha.equals("B_rey")) {
                    posicionesTotales += transformarApalabra(movimientoReyBSoloAtaque(tableroM, i, j));
                } else if (ficha.equals("B_peon")) {
                    posicionesTotales += transformarApalabra(movimientoPeonBSoloAtaque(tableroM, i, j));
                }

            }
        }

        String[] arrayTotal = posicionesTotales.split("_");
        return arrayTotal;

    }

    // Metodo que valida y regresa el movimiento valido de la pieza seleccionada
    // (piezas bot)
    public String[] movimientoAtaqueFichaB(String[][] tableroM, int i, int j) {

        String posicionesTotales = "";

        String ficha = fichaDeLaCasilla(tableroM, i, j);

        if (ficha.equals("B_torre")) {
            posicionesTotales += transformarApalabra(movimientoTorreBSoloAtaque(tableroM, i, j));
        } else if (ficha.equals("B_alfil")) {
            posicionesTotales += transformarApalabra(movimientoAlfilBSoloAtaque(tableroM, i, j));
        } else if (ficha.equals("B_reina")) {
            posicionesTotales += transformarApalabra(movimientoReinaBSoloAtaque(tableroM, i, j));
        } else if (ficha.equals("B_caballo")) {
            posicionesTotales += transformarApalabra(movimientoCaballoBSoloAtaque(tableroM, i, j));
        } else if (ficha.equals("B_rey")) {
            posicionesTotales += transformarApalabra(movimientoReyBSoloAtaque(tableroM, i, j));
        } else if (ficha.equals("B_peon")) {
            posicionesTotales += transformarApalabra(movimientoPeonBSoloAtaque(tableroM, i, j));
        }

        String[] arrayTotal = posicionesTotales.split("_");
        return arrayTotal;

    }

    // Metodo que valida y regresa el movimiento valido de la pieza seleccionada
    // (piezas Adversario)
    public String[] movimientoAtaqueFichaA(String[][] tableroM, int i, int j) {

        String posicionesTotales = "";

        String ficha = fichaDeLaCasilla(tableroM, i, j);

        if (ficha.equals("A_torre")) {
            posicionesTotales += transformarApalabra(movimientoTorreASoloAtaque(tableroM, i, j));
        } else if (ficha.equals("A_alfil")) {
            posicionesTotales += transformarApalabra(movimientoAlfilASoloAtaque(tableroM, i, j));
        } else if (ficha.equals("A_reina")) {
            posicionesTotales += transformarApalabra(movimientoReinaASoloAtaque(tableroM, i, j));
        } else if (ficha.equals("A_caballo")) {
            posicionesTotales += transformarApalabra(movimientoCaballoASoloAtaque(tableroM, i, j));
        } else if (ficha.equals("A_rey")) {
            posicionesTotales += transformarApalabra(movimientoReyASoloAtaque(tableroM, i, j));
        } else if (ficha.equals("A_peon")) {
            posicionesTotales += transformarApalabra(movimientoPeonASoloAtaque(tableroM, i, j));
        }

        String[] arrayTotal = posicionesTotales.split("_");
        return arrayTotal;

    }

    /*
     * Metodo que controla y manda llamar todos los posibles movimientos
     * correspondiente al elegir una pieza en el tablero (piezas del adversario)
     * para regresar los movimientos validos
     */
    public String[] todosLosMovimientosFichasA(String[][] tableroM) {

        String posicionesTotales = "";

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String ficha = fichaDeLaCasilla(tableroM, i, j);

                if (ficha.equals("A_torre")) {
                    posicionesTotales += transformarApalabra(movimientoTorreASoloAtaque(tableroM, i, j));
                } else if (ficha.equals("A_alfil")) {
                    posicionesTotales += transformarApalabra(movimientoAlfilASoloAtaque(tableroM, i, j));
                } else if (ficha.equals("A_reina")) {
                    posicionesTotales += transformarApalabra(movimientoReinaASoloAtaque(tableroM, i, j));
                } else if (ficha.equals("A_caballo")) {
                    posicionesTotales += transformarApalabra(movimientoCaballoASoloAtaque(tableroM, i, j));
                } else if (ficha.equals("A_rey")) {
                    posicionesTotales += transformarApalabra(movimientoReyASoloAtaque(tableroM, i, j));
                } else if (ficha.equals("A_peon")) {
                    posicionesTotales += transformarApalabra(movimientoPeonASoloAtaque(tableroM, i, j));
                }

            }
        }
        String[] arrayTotal = posicionesTotales.split("_");
        return arrayTotal;

    }

    private String transformarApalabra(String[] array) {
        String palabra = "";
        for (int i = 0; i < array.length; i++) {
            palabra += array[i] + "_";
        }
        return palabra;
    }

    // Metodo que controla y manda a llamar el movimiento de cualquier ficha en el
    // tablero
    public String[] movimientosFichas(String[][] tableroM, String posicion) {
        int x = Character.getNumericValue(posicion.charAt(1));
        int y = Character.getNumericValue(posicion.charAt(0));
        String ficha = fichaDeLaCasilla(tableroM, y, x);
        if (ficha.equals("A_peon")) {
            return movimientoPeonA(tableroM, y, x);
        } else if (ficha.equals("B_peon")) {
            return movimientoPeonB(tableroM, y, x);
        } else if (ficha.equals("A_torre")) {
            return movimientoTorreA(tableroM, y, x);
        } else if (ficha.equals("B_torre")) {
            return movimientoTorreB(tableroM, y, x);
        } else if (ficha.equals("A_alfil")) {
            return movimientoAlfilA(tableroM, y, x);
        } else if (ficha.equals("B_alfil")) {
            return movimientoAlfilB(tableroM, y, x);
        } else if (ficha.equals("A_caballo")) {
            return movimientoCaballoA(tableroM, y, x);
        } else if (ficha.equals("B_caballo")) {
            return movimientoCaballoB(tableroM, y, x);
        } else if (ficha.equals("A_reina")) {
            return movimientoReinaA(tableroM, y, x);
        } else if (ficha.equals("B_reina")) {
            return movimientoReinaB(tableroM, y, x);
        } else if (ficha.equals("A_rey")) {
            return movimientoReyA(tableroM, y, x);
        } else if (ficha.equals("B_rey")) {
            return movimientoReyB(tableroM, y, x);
        }

        return null;
    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) del peon
     * del adversario que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoPeonA(String[][] tableroM, int y, int x) {
        String posicionesPosibles = "";

        if (tableroM[y - 1][x].equals("")) {
            posicionesPosibles += "" + (y - 1) + x + "_";
        }
        try {
            if (tableroM[y - 2][x].equals("") && y == 6) {
                posicionesPosibles += "" + (y - 2) + (x) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsnegra(tableroM, y - 1, x + 1)) {
                posicionesPosibles += "" + (y - 1) + (x + 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsnegra(tableroM, y - 1, x - 1)) {
                posicionesPosibles += "" + (y - 1) + (x - 1) + "_";
            }
        } catch (Exception ex) {
        }

        String[] arrayPosicionesPosibles = posicionesPosibles.split("_");

        return arrayPosicionesPosibles;

    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de ataque
     * del peon del adversario que valida y regresa los posibles movimientos del
     * mismo
     */
    private String[] movimientoPeonASoloAtaque(String[][] tableroM, int y, int x) {
        String posicionesPosibles = "";
        try {
            posicionesPosibles += "" + (y - 1) + "" + (x + 1) + "_";
        } catch (Exception ex) {
        }
        try {
            posicionesPosibles += "" + (y - 1) + "" + (x - 1) + "_";
        } catch (Exception ex) {
        }

        String array[] = posicionesPosibles.split("_");
        return array;

    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de ataque
     * del peon del bot que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoPeonBSoloAtaque(String[][] tableroM, int y, int x) {
        String posicionesPosibles = "";

        try {
            posicionesPosibles += "" + (y + 1) + "" + (x + 1) + "_";
        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y + 1) + "" + (x - 1) + "_";
        } catch (Exception ex) {
        }

        String array[] = posicionesPosibles.split("_");
        return array;

    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) del peon
     * del bot que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoPeonB(String[][] tableroM, int y, int x) {
        String posicionesPosibles = "";

        if (tableroM[y + 1][x].equals("")) {
            posicionesPosibles += "" + (y + 1) + x + "_";
        }
        try {
            if (tableroM[y + 2][x].equals("") && y == 1 && tableroM[y + 1][x].equals("")) {
                posicionesPosibles += "" + (y + 2) + (x) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsBlanca(tableroM, y + 1, x - 1)) {
                posicionesPosibles += "" + (y + 1) + (x - 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsBlanca(tableroM, y + 1, x + 1)) {
                posicionesPosibles += "" + (y + 1) + (x + 1) + "_";
            }

        } catch (Exception ex) {
        }

        String[] arrayPosicionesPosibles = posicionesPosibles.split("_");

        return arrayPosicionesPosibles;
    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de la
     * torre del adversario que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoTorreA(String[][] tableroM, int y, int x) {
        String posicionesPosibles = "";
        int i;
        boolean seguir;

        // Movimiento hacia abajo
        seguir = true;
        i = y;
        do {
            i++;

            try {
                if (tableroM[i][x].equals("")) {
                    posicionesPosibles += "" + i + x + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, i, x)) {
                    posicionesPosibles += "" + i + x + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsBlanca(tableroM, i, x)) {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        // Movimiento hacia arriba
        seguir = true;
        i = y;
        do {
            i--;

            try {
                if (tableroM[i][x].equals("")) {
                    posicionesPosibles += "" + i + x + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, i, x)) {
                    posicionesPosibles += "" + i + x + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsBlanca(tableroM, i, x)) {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        // Movimiento hacia la izquierda
        seguir = true;
        i = x;
        do {
            i--;

            try {
                if (tableroM[y][i].equals("")) {
                    posicionesPosibles += "" + y + i + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, y, i)) {
                    posicionesPosibles += "" + y + i + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y, i)) {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        // Movimiento hacia la derecha
        seguir = true;
        i = x;
        do {
            i++;

            try {
                if (tableroM[y][i].equals("")) {
                    posicionesPosibles += "" + y + i + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, y, i)) {
                    posicionesPosibles += "" + y + i + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y, i)) {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        String[] arrayPosicionesPosibles = posicionesPosibles.split("_");

        return arrayPosicionesPosibles;

    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de ataque
     * de la torre del adversario que valida y regresa los posibles movimientos del
     * mismo
     */
    private String[] movimientoTorreASoloAtaque(String[][] tableroM, int y, int x) {
        String posicionesPosibles = "";
        int i;
        boolean seguir;

        // Movimiento hacia abajo
        seguir = true;
        i = y;
        do {
            i++;

            try {
                if (tableroM[i][x].equals("")) {
                    posicionesPosibles += "" + i + x + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, i, x)) {
                    posicionesPosibles += "" + i + x + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsBlanca(tableroM, i, x)) {
                    posicionesPosibles += "" + i + x + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        // Movimiento hacia arriba
        seguir = true;
        i = y;
        do {
            i--;

            try {
                if (tableroM[i][x].equals("")) {
                    posicionesPosibles += "" + i + x + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, i, x)) {
                    posicionesPosibles += "" + i + x + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsBlanca(tableroM, i, x)) {
                    posicionesPosibles += "" + i + x + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        // Movimiento hacia la izquierda
        seguir = true;
        i = x;
        do {
            i--;

            try {
                if (tableroM[y][i].equals("")) {
                    posicionesPosibles += "" + y + i + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, y, i)) {
                    posicionesPosibles += "" + y + i + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y, i)) {
                    posicionesPosibles += "" + y + i + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        // Movimiento hacia la derecha
        seguir = true;
        i = x;
        do {
            i++;

            try {
                if (tableroM[y][i].equals("")) {
                    posicionesPosibles += "" + y + i + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, y, i)) {
                    posicionesPosibles += "" + y + i + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y, i)) {
                    posicionesPosibles += "" + y + i + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        String[] arrayPosicionesPosibles = posicionesPosibles.split("_");

        return arrayPosicionesPosibles;
    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de ataque
     * de la torre del bot que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoTorreBSoloAtaque(String[][] tableroM, int y, int x) {
        String posicionesPosibles = "";
        int i;
        boolean seguir;

        // Movimiento hacia abajo
        seguir = true;
        i = y;
        do {
            i++;

            try {
                if (tableroM[i][x].equals("")) {
                    posicionesPosibles += "" + i + x + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, i, x)) {
                    posicionesPosibles += "" + i + "" + x + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsnegra(tableroM, i, x)) {
                    posicionesPosibles += "" + i + "" + x + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        // Movimiento hacia arriba
        seguir = true;
        i = y;
        do {
            i--;

            try {
                if (tableroM[i][x].equals("")) {
                    posicionesPosibles += "" + i + x + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, i, x)) {
                    posicionesPosibles += "" + i + x + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsnegra(tableroM, i, x)) {
                    posicionesPosibles += "" + i + "" + x + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        // Movimiento hacia la izquierda
        seguir = true;
        i = x;
        do {
            i--;

            try {
                if (tableroM[y][i].equals("")) {
                    posicionesPosibles += "" + y + i + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y, i)) {
                    posicionesPosibles += "" + y + i + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsnegra(tableroM, y, i)) {
                    posicionesPosibles += "" + y + i + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        // Movimiento hacia la derecha
        seguir = true;
        i = x;
        do {
            i++;

            try {
                if (tableroM[y][i].equals("")) {
                    posicionesPosibles += "" + y + i + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y, i)) {
                    posicionesPosibles += "" + y + i + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsnegra(tableroM, y, i)) {
                    posicionesPosibles += "" + y + i + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        String[] arrayPosicionesPosibles = posicionesPosibles.split("_");

        return arrayPosicionesPosibles;

    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de la
     * torre del bot que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoTorreB(String[][] tableroM, int y, int x) {
        String posicionesPosibles = "";
        int i;
        boolean seguir;

        // Movimiento hacia abajo
        seguir = true;
        i = y;
        do {
            i++;

            try {
                if (tableroM[i][x].equals("")) {
                    posicionesPosibles += "" + i + x + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, i, x)) {
                    posicionesPosibles += "" + i + "" + x + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsnegra(tableroM, i, x)) {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        // Movimiento hacia arriba
        seguir = true;
        i = y;
        do {
            i--;

            try {
                if (tableroM[i][x].equals("")) {
                    posicionesPosibles += "" + i + x + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, i, x)) {
                    posicionesPosibles += "" + i + x + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsnegra(tableroM, i, x)) {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        // Movimiento hacia la izquierda
        seguir = true;
        i = x;
        do {
            i--;

            try {
                if (tableroM[y][i].equals("")) {
                    posicionesPosibles += "" + y + i + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y, i)) {
                    posicionesPosibles += "" + y + i + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsnegra(tableroM, y, i)) {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        // Movimiento hacia la derecha
        seguir = true;
        i = x;
        do {
            i++;

            try {
                if (tableroM[y][i].equals("")) {
                    posicionesPosibles += "" + y + i + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y, i)) {
                    posicionesPosibles += "" + y + i + "_";
                    seguir = false;
                } else if (comprobarSiLaFichaEsnegra(tableroM, y, i)) {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        String[] arrayPosicionesPosibles = posicionesPosibles.split("_");

        return arrayPosicionesPosibles;

    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) del alfil
     * del adversario que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoAlfilA(String[][] tableroM, int y, int x) {
        boolean seguir;
        int i;
        String posicionesPosibles = "";

        seguir = true;
        i = 0;
        do {
            i++;
            try {
                if (tableroM[y + i][x + i].equals("")) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, y + i, x + i)) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        seguir = true;
        i = 0;
        do {
            i--;
            try {
                if (tableroM[y + i][x + i].equals("")) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, y + i, x + i)) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        seguir = true;
        i = 0;
        do {
            i++;
            try {
                if (tableroM[y - i][x + i].equals("")) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, y - i, x + i)) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        seguir = true;
        i = 0;
        do {
            i--;
            try {
                if (tableroM[y - i][x + i].equals("")) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, y - i, x + i)) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        String[] arrayPosicionesPosibles = posicionesPosibles.split("_");

        return arrayPosicionesPosibles;

    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de atque
     * del alfil del adversario que valida y regresa los posibles movimientos del
     * mismo
     */
    private String[] movimientoAlfilASoloAtaque(String[][] tableroM, int y, int x) {
        boolean seguir;
        int i;
        String posicionesPosibles = "";

        seguir = true;
        i = 0;
        do {
            i++;
            try {
                if (tableroM[y + i][x + i].equals("")) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, y + i, x + i)) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        seguir = true;
        i = 0;
        do {
            i--;
            try {
                if (tableroM[y + i][x + i].equals("")) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, y + i, x + i)) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        seguir = true;
        i = 0;
        do {
            i++;
            try {
                if (tableroM[y - i][x + i].equals("")) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, y - i, x + i)) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        seguir = true;
        i = 0;
        do {
            i--;
            try {
                if (tableroM[y - i][x + i].equals("")) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsnegra(tableroM, y - i, x + i)) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        String[] arrayPosicionesPosibles = posicionesPosibles.split("_");

        return arrayPosicionesPosibles;

    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de ataque
     * del alfil del bot que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoAlfilBSoloAtaque(String[][] tableroM, int y, int x) {
        boolean seguir;
        int i;
        String posicionesPosibles = "";

        seguir = true;
        i = 0;
        do {
            i++;
            try {
                if (tableroM[y + i][x + i].equals("")) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y + i, x + i)) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        seguir = true;
        i = 0;
        do {
            i--;
            try {
                if (tableroM[y + i][x + i].equals("")) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y + i, x + i)) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        seguir = true;
        i = 0;
        do {
            i++;
            try {
                if (tableroM[y - i][x + i].equals("")) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y - i, x + i)) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        seguir = true;
        i = 0;
        do {
            i--;
            try {
                if (tableroM[y - i][x + i].equals("")) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y - i, x + i)) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        String[] arrayPosicionesPosibles = posicionesPosibles.split("_");

        return arrayPosicionesPosibles;

    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) del alfil
     * del bot que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoAlfilB(String[][] tableroM, int y, int x) {
        boolean seguir;
        int i;
        String posicionesPosibles = "";

        seguir = true;
        i = 0;
        do {
            i++;
            try {
                if (tableroM[y + i][x + i].equals("")) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y + i, x + i)) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        seguir = true;
        i = 0;
        do {
            i--;
            try {
                if (tableroM[y + i][x + i].equals("")) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y + i, x + i)) {
                    posicionesPosibles += "" + (y + i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        seguir = true;
        i = 0;
        do {
            i++;
            try {
                if (tableroM[y - i][x + i].equals("")) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y - i, x + i)) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        seguir = true;
        i = 0;
        do {
            i--;
            try {
                if (tableroM[y - i][x + i].equals("")) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                } else if (comprobarSiLaFichaEsBlanca(tableroM, y - i, x + i)) {
                    posicionesPosibles += "" + (y - i) + "" + (x + i) + "_";
                    seguir = false;
                } else {
                    seguir = false;
                }
            } catch (Exception ex) {
                seguir = false;
            }

        } while (seguir);

        String[] arrayPosicionesPosibles = posicionesPosibles.split("_");

        return arrayPosicionesPosibles;

    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) del
     * caballo del adversario que valida y regresa los posibles movimientos del
     * mismo
     */
    private String[] movimientoCaballoA(String[][] tableroM, int y, int x) {
        String posicionesPosibles = "";

        try {
            // Movimiento arriba-iaquierda
            if (tableroM[y - 2][x - 1].equals("") || comprobarSiLaFichaEsnegra(tableroM, y - 2, x - 1)) {
                posicionesPosibles += "" + (y - 2) + "" + (x - 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            // Movimiento arriba-derecha
            if (tableroM[y - 2][x + 1].equals("") || comprobarSiLaFichaEsnegra(tableroM, y - 2, x + 1)) {
                posicionesPosibles += "" + (y - 2) + "" + (x + 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            // Movimiento abajo-izquierda
            if (tableroM[y + 2][x - 1].equals("") || comprobarSiLaFichaEsnegra(tableroM, y + 2, x - 1)) {
                posicionesPosibles += "" + (y + 2) + "" + (x - 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            // Movimiento abajo-derecha
            if (tableroM[y + 2][x + 1].equals("") || comprobarSiLaFichaEsnegra(tableroM, y + 2, x + 1)) {
                posicionesPosibles += "" + (y + 2) + "" + (x + 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            // Movimiento izquierda-arriba
            if (tableroM[y - 1][x - 2].equals("") || comprobarSiLaFichaEsnegra(tableroM, y - 1, x - 2)) {
                posicionesPosibles += "" + (y - 1) + "" + (x - 2) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            // Movimiento izquierda-abajo
            if (tableroM[y + 1][x - 2].equals("") || comprobarSiLaFichaEsnegra(tableroM, y + 1, x - 2)) {
                posicionesPosibles += "" + (y + 1) + "" + (x - 2) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            // Movimiento derecha-arriba
            if (tableroM[y - 1][x + 2].equals("") || comprobarSiLaFichaEsnegra(tableroM, y - 1, x + 2)) {
                posicionesPosibles += "" + (y - 1) + "" + (x + 2) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            // Movimiento derecha-abajo
            if (tableroM[y + 1][x + 2].equals("") || comprobarSiLaFichaEsnegra(tableroM, y + 1, x + 2)) {
                posicionesPosibles += "" + (y + 1) + "" + (x + 2) + "_";
            }
        } catch (Exception ex) {
        }

        String[] arregloPosicionesPosibles = posicionesPosibles.split("_");

        return arregloPosicionesPosibles;
    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de ataque
     * del caballo del adversario que valida y regresa los posibles movimientos del
     * mismo
     */
    private String[] movimientoCaballoASoloAtaque(String[][] tableroM, int y, int x) {
        String posicionesPosibles = "";

        try {
            posicionesPosibles += "" + (y - 2) + "" + (x - 1) + "_";
        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y - 2) + "" + (x + 1) + "_";

        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y + 2) + "" + (x - 1) + "_";

        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y + 2) + "" + (x + 1) + "_";

        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y - 1) + "" + (x - 2) + "_";

        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y + 1) + "" + (x - 2) + "_";

        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y - 1) + "" + (x + 2) + "_";

        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y + 1) + "" + (x + 2) + "_";

        } catch (Exception ex) {
        }

        String[] arregloPosicionesPosibles = posicionesPosibles.split("_");

        return arregloPosicionesPosibles;
    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de ataque
     * del caballo del bot que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoCaballoBSoloAtaque(String[][] tableroM, int y, int x) {
        String posicionesPosibles = "";

        try {
            posicionesPosibles += "" + (y - 2) + "" + (x - 1) + "_";
        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y - 2) + "" + (x + 1) + "_";

        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y + 2) + "" + (x - 1) + "_";

        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y + 2) + "" + (x + 1) + "_";

        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y - 1) + "" + (x - 2) + "_";

        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y + 1) + "" + (x - 2) + "_";

        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y - 1) + "" + (x + 2) + "_";

        } catch (Exception ex) {
        }

        try {
            posicionesPosibles += "" + (y + 1) + "" + (x + 2) + "_";

        } catch (Exception ex) {
        }

        String[] arregloPosicionesPosibles = posicionesPosibles.split("_");

        return arregloPosicionesPosibles;
    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) del
     * caballo del bot que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoCaballoB(String[][] tableroM, int y, int x) {
        String posicionesPosibles = "";

        try {
            // Movimiento arriba-iaquierda
            if (tableroM[y - 2][x - 1].equals("") || comprobarSiLaFichaEsBlanca(tableroM, y - 2, x - 1)) {
                posicionesPosibles += "" + (y - 2) + "" + (x - 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            // Movimiento arriba-derecha
            if (tableroM[y - 2][x + 1].equals("") || comprobarSiLaFichaEsBlanca(tableroM, y - 2, x + 1)) {
                posicionesPosibles += "" + (y - 2) + "" + (x + 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            // Movimiento abajo-izquierda
            if (tableroM[y + 2][x - 1].equals("") || comprobarSiLaFichaEsBlanca(tableroM, y + 2, x - 1)) {
                posicionesPosibles += "" + (y + 2) + "" + (x - 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            // Movimiento abajo-derecha
            if (tableroM[y + 2][x + 1].equals("") || comprobarSiLaFichaEsBlanca(tableroM, y + 2, x + 1)) {
                posicionesPosibles += "" + (y + 2) + "" + (x + 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            // Movimiento izquierda-arriba
            if (tableroM[y - 1][x - 2].equals("") || comprobarSiLaFichaEsBlanca(tableroM, y - 1, x - 2)) {
                posicionesPosibles += "" + (y - 1) + "" + (x - 2) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            // Movimiento izquierda-abajo
            if (tableroM[y + 1][x - 2].equals("") || comprobarSiLaFichaEsBlanca(tableroM, y + 1, x - 2)) {
                posicionesPosibles += "" + (y + 1) + "" + (x - 2) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            // Movimiento derecha-arriba
            if (tableroM[y - 1][x + 2].equals("") || comprobarSiLaFichaEsBlanca(tableroM, y - 1, x + 2)) {
                posicionesPosibles += "" + (y - 1) + "" + (x + 2) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            // Movimiento derecha-abajo
            if (tableroM[y + 1][x + 2].equals("") || comprobarSiLaFichaEsBlanca(tableroM, y + 1, x + 2)) {
                posicionesPosibles += "" + (y + 1) + "" + (x + 2) + "_";
            }
        } catch (Exception ex) {
        }

        String[] arregloPosicionesPosibles = posicionesPosibles.split("_");

        return arregloPosicionesPosibles;
    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de la
     * reina del adversario que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoReinaA(String[][] tableroM, int y, int x) {
        String[] movimientoDiagonal = movimientoAlfilA(tableroM, y, x);
        String[] movimientoRecto = movimientoTorreA(tableroM, y, x);

        int numeroPosiciones = movimientoDiagonal.length + movimientoRecto.length;

        String[] movimientosReina = new String[numeroPosiciones];

        int n = 0;

        for (int i = 0; i < movimientoDiagonal.length; i++) {
            movimientosReina[n] = movimientoDiagonal[i];
            n++;
        }

        for (int i = 0; i < movimientoRecto.length; i++) {
            movimientosReina[n] = movimientoRecto[i];
            n++;
        }

        return movimientosReina;
    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de ataque
     * de la reina del adversario que valida y regresa los posibles movimientos del
     * mismo
     */
    private String[] movimientoReinaASoloAtaque(String[][] tableroM, int y, int x) {
        String[] movimientoDiagonal = movimientoAlfilASoloAtaque(tableroM, y, x);
        String[] movimientoRecto = movimientoTorreASoloAtaque(tableroM, y, x);

        int numeroPosiciones = movimientoDiagonal.length + movimientoRecto.length;

        String[] movimientosReina = new String[numeroPosiciones];

        int n = 0;

        for (int i = 0; i < movimientoDiagonal.length; i++) {
            movimientosReina[n] = movimientoDiagonal[i];
            n++;
        }

        for (int i = 0; i < movimientoRecto.length; i++) {
            movimientosReina[n] = movimientoRecto[i];
            n++;
        }

        return movimientosReina;
    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de ataque
     * de la reina del bot que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoReinaBSoloAtaque(String[][] tableroM, int y, int x) {
        String[] movimientoDiagonal = movimientoAlfilBSoloAtaque(tableroM, y, x);
        String[] movimientoRecto = movimientoTorreBSoloAtaque(tableroM, y, x);

        int numeroPosiciones = movimientoDiagonal.length + movimientoRecto.length;

        String[] movimientosReina = new String[numeroPosiciones];

        int n = 0;

        for (int i = 0; i < movimientoDiagonal.length; i++) {
            movimientosReina[n] = movimientoDiagonal[i];
            n++;
        }

        for (int i = 0; i < movimientoRecto.length; i++) {
            movimientosReina[n] = movimientoRecto[i];
            n++;
        }

        return movimientosReina;
    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de la
     * reina del bot que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoReinaB(String[][] tableroM, int y, int x) {
        String[] movimientoDiagonal = movimientoAlfilB(tableroM, y, x);
        String[] movimientoRecto = movimientoTorreB(tableroM, y, x);

        int numeroPosiciones = movimientoDiagonal.length + movimientoRecto.length;

        String[] movimientosReina = new String[numeroPosiciones];

        int n = 0;

        for (int i = 0; i < movimientoDiagonal.length; i++) {
            movimientosReina[n] = movimientoDiagonal[i];
            n++;
        }

        for (int i = 0; i < movimientoRecto.length; i++) {
            movimientosReina[n] = movimientoRecto[i];
            n++;
        }

        return movimientosReina;
    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) del rey
     * del adversario que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoReyA(String[][] tableroM, int y, int x) {

        String posicionesPosibles = "";

        try {
            if (comprobarSiLaFichaEsBlanca(tableroM, y - 1, x - 1) == false) {
                posicionesPosibles += "" + (y - 1) + "" + (x - 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsBlanca(tableroM, y - 1, x) == false) {
                posicionesPosibles += "" + (y - 1) + "" + (x) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsBlanca(tableroM, y - 1, x + 1) == false) {
                posicionesPosibles += "" + (y - 1) + "" + (x + 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsBlanca(tableroM, y, x - 1) == false) {
                posicionesPosibles += "" + (y) + "" + (x - 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsBlanca(tableroM, y, x + 1) == false) {
                posicionesPosibles += "" + (y) + "" + (x + 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsBlanca(tableroM, y + 1, x - 1) == false) {
                posicionesPosibles += "" + (y + 1) + "" + (x - 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsBlanca(tableroM, y + 1, x) == false) {
                posicionesPosibles += "" + (y + 1) + "" + (x) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsBlanca(tableroM, y + 1, x + 1) == false) {
                posicionesPosibles += "" + (y + 1) + "" + (x + 1) + "_";
            }
        } catch (Exception ex) {
        }

        // Enrroque
        if (Controlador.Controlador.enrroqueReyA == true) {
            if (tableroM[7][1].equals("") && tableroM[7][2].equals("") && tableroM[7][3].equals("")
                    && Controlador.Controlador.enrroqueTorreIzquierdaA == true) {
                posicionesPosibles += "" + "72" + "_";
            }
            if (tableroM[7][5].equals("") && tableroM[7][6].equals("")
                    && Controlador.Controlador.enrroqueTorreDerechaA == true) {
                posicionesPosibles += "" + "76" + "_";
            }
        }

        String[] arregloPosicionesPosibles = posicionesPosibles.split("_");

        return arregloPosicionesPosibles;
    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de ataque
     * del rey del adversario que valida y regresa los posibles movimientos del
     * mismo
     */
    private String[] movimientoReyASoloAtaque(String[][] tableroM, int y, int x) {

        String posicionesPosibles = "";

        if (y > 0 && x > 0) {
            posicionesPosibles += "" + (y - 1) + "" + (x - 1) + "_";
        }

        try {
            if (y > 0 && x < 7) {
                posicionesPosibles += "" + (y - 1) + "" + (x + 1) + "_";
            }
        } catch (Exception ex) {

        }

        try {
            if (y > 0) {
                posicionesPosibles += "" + (y - 1) + "" + (x) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (x > 0) {
                posicionesPosibles += "" + (y) + "" + (x - 1) + "_";
            }

        } catch (Exception ex) {

        }

        try {
            if (x < 7) {
                posicionesPosibles += "" + (y) + "" + (x + 1) + "_";
            }

        } catch (Exception ex) {

        }

        try {
            if (y < 7 && x > 0) {
                posicionesPosibles += "" + (y + 1) + "" + (x - 1) + "_";
            }

        } catch (Exception ex) {

        }

        try {
            if (y < 7) {
                posicionesPosibles += "" + (y + 1) + "" + (x) + "_";
            }

        } catch (Exception ex) {

        }

        try {
            if (y < 7 && x < 7) {
                posicionesPosibles += "" + (y + 1) + "" + (x + 1) + "_";
            }

        } catch (Exception ex) {

        }

        String[] arregloPosicionesPosibles = posicionesPosibles.split("_");

        return arregloPosicionesPosibles;
    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) de ataque
     * del rey del bot que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoReyBSoloAtaque(String[][] tableroM, int y, int x) {

        String posicionesPosibles = "";

        if (y > 0 && x > 0) {
            posicionesPosibles += "" + (y - 1) + "" + (x - 1) + "_";
        }

        try {
            if (y > 0) {
                posicionesPosibles += "" + (y) + "" + (x - 1) + "_";

            }

        } catch (Exception ex) {

        }

        try {
            if (y > 0 && x < 7) {
                posicionesPosibles += "" + (y - 1) + "" + (x + 1) + "_";
            }
        } catch (Exception ex) {

        }

        try {
            if (x > 0) {
                posicionesPosibles += "" + (y) + "" + (x - 1) + "_";
            }

        } catch (Exception ex) {

        }

        try {
            if (x < 7) {
                posicionesPosibles += "" + (y) + "" + (x + 1) + "_";
            }

        } catch (Exception ex) {

        }

        try {
            if (y < 7 && x > 7) {
                posicionesPosibles += "" + (y + 1) + "" + (x - 1) + "_";
            }

        } catch (Exception ex) {

        }

        try {
            if (y < 7) {
                posicionesPosibles += "" + (y + 1) + "" + (x) + "_";
            }

        } catch (Exception ex) {

        }

        try {
            if (y < 7 && x < 7) {
                posicionesPosibles += "" + (y + 1) + "" + (x + 1) + "_";
            }

        } catch (Exception ex) {

        }

        String[] arregloPosicionesPosibles = posicionesPosibles.split("_");

        return arregloPosicionesPosibles;
    }

    /*
     * Metodo de movimiento permitido (conforme a las reglas del ajedrez) del rey
     * del bot que valida y regresa los posibles movimientos del mismo
     */
    private String[] movimientoReyB(String[][] tableroM, int y, int x) {

        String posicionesPosibles = "";

        try {
            if (comprobarSiLaFichaEsnegra(tableroM, y - 1, x - 1) == false) {
                posicionesPosibles += "" + (y - 1) + "" + (x - 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsnegra(tableroM, y - 1, x) == false) {
                posicionesPosibles += "" + (y - 1) + "" + (x) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsnegra(tableroM, y - 1, x + 1) == false) {
                posicionesPosibles += "" + (y - 1) + "" + (x + 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsnegra(tableroM, y, x - 1) == false) {
                posicionesPosibles += "" + (y) + "" + (x - 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsnegra(tableroM, y, x + 1) == false) {
                posicionesPosibles += "" + (y) + "" + (x + 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsnegra(tableroM, y + 1, x - 1) == false) {
                posicionesPosibles += "" + (y + 1) + "" + (x - 1) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsnegra(tableroM, y + 1, x) == false) {
                posicionesPosibles += "" + (y + 1) + "" + (x) + "_";
            }
        } catch (Exception ex) {
        }

        try {
            if (comprobarSiLaFichaEsnegra(tableroM, y + 1, x + 1) == false) {
                posicionesPosibles += "" + (y + 1) + "" + (x + 1) + "_";
            }
        } catch (Exception ex) {
        }

        String[] arregloPosicionesPosibles = posicionesPosibles.split("_");

        return arregloPosicionesPosibles;
    }

    // Metodo que obtiene la posición de la ficha o pieza
    private String fichaDeLaCasilla(String[][] tableroM, int y, int x) {
        return tableroM[y][x];
    }

    // Metodo que valida si la ficha es negra (del bot)
    private boolean comprobarSiLaFichaEsnegra(String[][] tableroM, int y, int x) {
        if (!tableroM[y][x].equals("")) {
            return (tableroM[y][x].charAt(0) == 'B') ? true : false;
        }
        return false;
    }

    // Metodo que valida si la ficha es blanca (del adversario)
    private boolean comprobarSiLaFichaEsBlanca(String[][] tableroM, int y, int x) {
        if (!tableroM[y][x].equals("")) {
            return (tableroM[y][x].charAt(0) == 'A') ? true : false;
        }
        return false;
    }

    // Metodo que valida si el rey del adversario se encuentra en Jaque
    private boolean reyAenJaque(String[][] tableroM) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (fichaDeLaCasilla(tableroM, i, j).equals("A_rey")) {
                    String posicionRey = "" + i + "" + j;

                    String[] movimientosEnemigos = todosLosMovimientosFichasB(tableroM);

                    for (int x = 0; x < movimientosEnemigos.length; x++) {
                        if (movimientosEnemigos[x].equals(posicionRey)) {
                            return true;
                        }
                    }
                    return false;

                }
            }
        }
        return false;
    }

    // Metodo que valida si el rey del bot se encuentra en Jaque
    private boolean reyBenJaque(String[][] tableroM) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (fichaDeLaCasilla(tableroM, i, j).equals("B_rey")) {
                    String posicionRey = "" + i + "" + j;

                    String[] movimientosEnemigos = todosLosMovimientosFichasA(tableroM);

                    for (int x = 0; x < movimientosEnemigos.length; x++) {
                        if (movimientosEnemigos[x].equals(posicionRey)) {
                            return true;
                        }
                    }
                    return false;

                }
            }
        }
        return false;
    }

}
