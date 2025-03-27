import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.Comparator;


public class Tablero {

    private Celda[][] matrixTablero;
    private int tamanioTablero = 0;
    private Random rand;

    private ArrayList<Point> iniciosYFinalesCarreteras = new ArrayList<>(); //guarda los puntos de inicio y final de las carreteras
    private ArrayList<ArrayList<Point>> carreteras = new ArrayList<>(); //guarda los puntos de las carreteras
    private ArrayList<Vehiculo> vehiculos = new ArrayList<>(); //guarda los vehiculos que tiene
    private ArrayList<Celda> celdaConConflico = new ArrayList<>();

    private int intentosGeneracion = 0;


    public Tablero(int tamanioTablero) {
        this.tamanioTablero = tamanioTablero;
        this.matrixTablero = new Celda[tamanioTablero][tamanioTablero]; //por defecto es vacio.
        this.rand = new Random();
        inicializarCeldas();
    }//contructor

    private void inicializarCeldas() {
        for (int i = 0; i < tamanioTablero; i++) {
            for (int j = 0; j < tamanioTablero; j++) {
                matrixTablero[i][j] = Factoria.generarCelda();
            }
        }
    }


    public boolean generarCarreteras(int numCarreteras, String errorMensaje) {

        ArrayList<Point> puntosCarreteraHorizontal = new ArrayList<>();//genero una carretera fantasma por donde empezar
        boolean demasiadasCarreteras = false;

        do {
            int xInicial = rand.nextInt(tamanioTablero - 2) + 1;
            int yInicial = rand.nextInt(tamanioTablero - 2) + 1;
            if (yInicial == tamanioTablero - 1) yInicial--;
            puntosCarreteraHorizontal.add(new Point(xInicial, yInicial));

        } while (generarCarreterasRecursivamente(puntosCarreteraHorizontal, iniciosYFinalesCarreteras, false, 0, numCarreteras) && intentosGeneracion < 9999);

        if (intentosGeneracion >= 9999) {
            System.out.println(errorMensaje);
            demasiadasCarreteras = true;
            iniciosYFinalesCarreteras.clear();
            carreteras.clear();
            intentosGeneracion = 0;
            inicializarCeldas();
        }//reseteo del tablero en caso de que se sobrepasen los intentos
        return demasiadasCarreteras;
    }//devuelve un true si ha tenido demasiados intentos, esto ayuda a que si se pasa un numero muy grande no se quede pensando en bucle

    private boolean generarCarreterasRecursivamente(ArrayList<Point> puntosCarreteraBase, ArrayList<Point> iniciosYFinalesCarreteras, boolean esVertical, int n_carretera, int max_carreteras) {
        char tipoCarretera = esVertical ? 'v' : 'h';
        puntosCarreteraBase.removeAll(iniciosYFinalesCarreteras);
        boolean noConseguido = true;
        int indicePunto, fijo, variablePunto, inicio, fin;
        Point puntoBase;
        ArrayList<Point> nuevaCarretera = new ArrayList<>();

        indicePunto = rand.nextInt(puntosCarreteraBase.size()); //el primer punto lo genero de forma aletoria
        fijo = 0;
        inicio = 0;
        fin = 0;

        intentosGeneracion++;
        int contadorPuntos = 0;
        int maxPuntos = puntosCarreteraBase.size();

        if (n_carretera >= max_carreteras || intentosGeneracion >= 9999) {
            noConseguido = false;
        } else {
            carreteras.add(new ArrayList<>());
        }
        while (contadorPuntos < maxPuntos && noConseguido && n_carretera < max_carreteras) {

            puntoBase = puntosCarreteraBase.get(indicePunto);
            fijo = esVertical ? puntoBase.y : puntoBase.x;  // Fila para horizontal, Columna para vertical
            variablePunto = esVertical ? puntoBase.x : puntoBase.y;  // Columna para horizontal, Fila para vertical
            do {
                inicio = controlarErrorRandomCarretera(1, variablePunto, true); //rand.nextInt(1, variablePunto);
                fin = controlarErrorRandomCarretera(variablePunto, tamanioTablero, false);   //rand.nextInt(variablePunto + 1, tamanioTablero - 1)? : variablePunto;
            } while (fin - inicio <= 3);

            if (!comprobarAdyacentesYPosicion(inicio, fin, fijo, tipoCarretera, esVertical)) { //si es posible plantar llamo a la siguiente
                //generar puntos

                for (int variable = inicio; variable <= fin; variable++) {
                    Point nuevoPunto = esVertical ? new Point(variable, fijo) : new Point(fijo, variable);
                    nuevaCarretera.add(nuevoPunto);
                    if (esVertical) {
                        indexacionSimple(tipoCarretera, variable, fijo);
                        carreteras.get(n_carretera).add(new Point(variable, fijo));
                    } else {
                        indexacionSimple(tipoCarretera, fijo, variable);
                        carreteras.get(n_carretera).add(new Point(fijo, variable));
                    }
                }


                //los indexo y si no lo consigo los desindexo
                noConseguido = generarCarreterasRecursivamente(nuevaCarretera, iniciosYFinalesCarreteras, !esVertical, n_carretera + 1, max_carreteras);

                if (noConseguido) {
                    for (int variable = inicio; variable <= fin; variable++) {
                        if (esVertical) {
                            desindexacion(tipoCarretera, variable, fijo);
                        } else {
                            desindexacion(tipoCarretera, fijo, variable);
                        }

                    }
                    carreteras.remove(n_carretera);
                }

            }
            if (noConseguido) {
                indicePunto++;
                if (indicePunto >= maxPuntos) indicePunto = 0;
                contadorPuntos++;

            }

        }

        if (!noConseguido && n_carretera < max_carreteras) { //en caso de que lo haya conseguido guarda el inicio y final
            iniciosYFinalesCarreteras.add(esVertical ? new Point(inicio, fijo) : new Point(fijo, inicio));
            iniciosYFinalesCarreteras.add(esVertical ? new Point(fin, fijo) : new Point(fijo, fin));

        }
        //si lo consigue se sale

        return noConseguido;
    }


    private void desindexacion(char tipoCarretera, int x, int y) {
        if (matrixTablero[x][y].get_Cruce()) matrixTablero[x][y].set_Cruce(false);
        else if (matrixTablero[x][y].get_Carretera() && matrixTablero[x][y].get_TipoCarretera() == tipoCarretera)
            matrixTablero[x][y].set_Carretera(' ');
    }

    private void indexacionSimple(char tipoCarretera, int x, int y) {
        if (matrixTablero[x][y].get_Carretera()) matrixTablero[x][y].set_Cruce(true);
        else matrixTablero[x][y].set_Carretera(tipoCarretera);
    }

    private boolean comprobarAdyacentesYPosicion(int inicio, int fin, int fijo, char tipoCarretera, boolean esVertical) {
        boolean tieneAdyacente = false;
        for (int variable = inicio; variable <= fin; variable++) {
            if (esVertical) {
                if (matrixTablero[variable][fijo + 1].get_TipoCarretera() == tipoCarretera || matrixTablero[variable][fijo - 1].get_TipoCarretera() == tipoCarretera) {
                    tieneAdyacente = true;
                } else if (matrixTablero[variable][fijo].get_Carretera() && matrixTablero[variable][fijo].get_TipoCarretera() == tipoCarretera) {
                    tieneAdyacente = true;
                }

            } else {
                if (matrixTablero[fijo + 1][variable].get_TipoCarretera() == tipoCarretera || matrixTablero[fijo - 1][variable].get_TipoCarretera() == tipoCarretera) {
                    tieneAdyacente = true;
                } else if (matrixTablero[fijo][variable].get_Carretera() && matrixTablero[fijo][variable].get_TipoCarretera() == tipoCarretera) {
                    tieneAdyacente = true;
                }
            }
        }
        //tablero
        //si es vertical
        //comprobar respectivamente a lo largo de la longitud si encuentro otro del mismo tipo devuelve true
        return tieneAdyacente;
    }

    private int controlarErrorRandomCarretera(int inicioRandom, int finRandom, boolean esInicio) {
        int valor;
        try {
            if (esInicio) valor = rand.nextInt(inicioRandom, finRandom);
            else valor = rand.nextInt(inicioRandom + 1, finRandom - 1);
        } catch (IllegalArgumentException e) {
            valor = inicioRandom;
        }
        return valor;
    }

    public void imprimirTablero2() {


        for (int i = 0; i < tamanioTablero + 2; i++) {

            System.out.print(aniadirCadenaConEspacios("-"));
        }
        System.out.println();
        for (int i = 0; i < tamanioTablero; i++) {
            System.out.print(aniadirCadenaConEspacios("|")); //borde derecho
            for (int j = 0; j < tamanioTablero; j++) {

                if (matrixTablero[i][j].get_TipoCarretera() == 'h') {
                    if (matrixTablero[i][j].toString() != "#" && matrixTablero[i][j].toString() != "X")
                        System.out.print(Colores.GREEN + aniadirCadenaConEspacios("" + matrixTablero[i][j]) + Colores.RESET);
                    else
                        System.out.print(Colores.BLUE + aniadirCadenaConEspacios("" + matrixTablero[i][j]) + Colores.RESET);
                } else if (matrixTablero[i][j].get_TipoCarretera() == 'v') {
                    if (matrixTablero[i][j].toString() != "#" && matrixTablero[i][j].toString() != "X")
                        System.out.print(Colores.GREEN + aniadirCadenaConEspacios("" + matrixTablero[i][j]) + Colores.RESET);
                    else
                        System.out.print(Colores.YELLOW + aniadirCadenaConEspacios("" + matrixTablero[i][j]) + Colores.RESET);
                } else System.out.print(aniadirCadenaConEspacios("" + matrixTablero[i][j]));
            }
            System.out.print(aniadirCadenaConEspacios("|")); //borde izquierdo
            System.out.println();
        }

        for (int j = 0; j < tamanioTablero + 2; j++) {
            System.out.print(aniadirCadenaConEspacios("-"));
        }
        System.out.println();


    }

    private String aniadirCadenaConEspacios(String cadenaImprimir) {
        String caracteres = "" + (carreteras.size());
        int numMaxCaracteres = caracteres.length() + 4;//+2 dos por los [ ]
        int aniadirEspacios = numMaxCaracteres - cadenaImprimir.length();
        for (int e = 0; e < aniadirEspacios; e++) {
            cadenaImprimir += " ";
        }
        return cadenaImprimir;
    }


    //metodos de juego

    public void addVehiculoArray(Vehiculo v) {
        vehiculos.add(v);
    }

    public void inicializarVehiculos() {
        for (Vehiculo v : vehiculos) {
            Point inicio = v.get_Inicio();
            Point actual = v.get_Posicion();
            Point destino = v.get_Destino();

            int pos;
            if (actual != inicio) {
                pos = v.get_Direccion();
                quitarVehiculo(actual.x, actual.y, pos);
                v.set_Posicion(inicio);
            }

            Factoria.generarCamino(this, v);

            pos = v.calcularDireccion(); //vuelvo a calcularlo porque la lista ha cambiado
            v.set_Direccion(pos);

            agregarVehiculo(inicio.x, inicio.y, v, pos);
            agregarDestino(destino.x, destino.y, v);


        }
    }


    private void agregarDestino(int fil, int col, Vehiculo vehiculo) {
        if (fil >= 0 && fil < tamanioTablero && col >= 0 && col < tamanioTablero) {
            matrixTablero[fil][col].setId_Destino(vehiculo);
            matrixTablero[fil][col].set_EsDestino(true);
        }
    }


    public void moverVehiculos() { //muevo los vehiculos y detecto conflictos
        for (Vehiculo v : vehiculos) {

            //coger vehiculo

            //coger actual posicion del vehiculo
            Point actualPos = v.get_Posicion();
            Point finalPos = v.get_Destino();
            if (actualPos != finalPos) {


                //coger velocidad del vehiculo y hacer en bucle for o while la cantidad de movimientos
                int velocidad = v.get_Velocidad().getVelocidad();

                //mirar la posicion a la que se quiere mover
                for (int i = 0; i < velocidad; i++) {
                    actualPos = v.get_Posicion();
                    int actualX = actualPos.x;
                    int actualY = actualPos.y;
                    Point nuevaPos = v.get_SiguientePos();
                    if (nuevaPos != null) {//en el caso de que un vehiculo se mueva dos casillas y solo llegue en una
                        int nextX = nuevaPos.x;
                        int nextY = nuevaPos.y;
                        Celda celdaAux = matrixTablero[nextX][nextY];
                        int direccion = v.calcularDireccion();
                        if (celdaAux.get_Carretera() && !celdaAux.get_Cruce()) {
                            if (celdaAux.get_Vehiculo(direccion) == null) { //es que esta vacia
                                quitarVehiculo(actualX, actualY, v.get_Direccion()); //quito vehiculo de vieja pos
                                agregarVehiculo(nextX, nextY, v, direccion); //pongo vehiculo en nueva pos
                                v.set_Posicion(nuevaPos); //actualizo pos de vehiculo
                                v.set_Direccion(direccion);
                                v.avanzarRuta(); //borro un paso de ruta
                            }
                        }//en caso de que sea ruta de forma normal lo gestiono aqui
                        else if (celdaAux.get_Cruce()) {

                            if (!celdaConConflico.contains(celdaAux)) celdaConConflico.add(celdaAux);
                            celdaAux.add_Conflicto(v);
                        }
                    }
                }
            }//si no es que ya ha llegado
        }

        if (!celdaConConflico.isEmpty()) {
            resolverConflicto();
        }
        // si se ha generado conflicto lo gestiono despues para ordenar la cola de conflictos.
    }

    private void resolverConflicto() {
        for (Celda c : celdaConConflico) {
            ArrayList<Vehiculo> conflictos = c.get_Conflictos();
            Vehiculo vAux;
            if (conflictos.size() == 1) {
                //muevo el vehiculo
                vAux = conflictos.getFirst();

            }//cuando solo se genero un coflicto en ese cruce
            else {

                ArrayList<Vehiculo> conflictosAux = new ArrayList<>();
                PriorityQueue<Vehiculo> colaPrioridad = new PriorityQueue<>(Comparator.comparingInt(Vehiculo::get_Prioridad).reversed());
                for (Vehiculo v : conflictos) {
                    int prioridad = v.calcularPrioridad();
                    v.set_Prioridad(prioridad);
                    colaPrioridad.add(v);
                }
                conflictosAux = new ArrayList<>(colaPrioridad);

                vAux = conflictosAux.getFirst(); //con esto solo ejecuto al primero, los demas proceden a esperar a la siguiente iteracion


            }//en caso que haya mas conflictos lo soluciono aqui
            Point actualPos = vAux.get_Posicion();
            Point nuevaPos = vAux.get_SiguientePos();
            int nextX = nuevaPos.x;
            int nextY = nuevaPos.y;
            quitarVehiculo(actualPos.x, actualPos.y, vAux.get_Direccion()); //quito vehiculo de vieja pos
            agregarVehiculo(nextX, nextY, vAux, 0); //pongo vehiculo en nueva pos
            vAux.set_Posicion(nuevaPos); //actualizo pos de vehiculo
            vAux.set_Direccion(0);
            vAux.avanzarRuta(); //borro un paso de ruta

            //limpio el array para volver evaluarlo mas adelante
            c.limpiarConflictos();
        }
        celdaConConflico.clear();
    }


    //no lo utilizo

    public void agregarVehiculo(int fil, int col, Vehiculo vehiculo, int pos) {
        if (fil >= 0 && fil < tamanioTablero && col >= 0 && col < tamanioTablero) {
            matrixTablero[fil][col].set_Vehiculo(vehiculo, pos);
        }
    }

    public void quitarVehiculo(int fil, int col, int pos) {
        if (fil >= 0 && fil < tamanioTablero && col >= 0 && col < tamanioTablero) {
            if (matrixTablero[fil][col].get_Vehiculo(pos) != null) matrixTablero[fil][col].set_Vehiculo(null, pos);
        }
    }


    public ArrayList<ArrayList<Point>> getCarreteras() {
        return carreteras;
    }

    public Celda get_Celda(int fil, int col) {
        return matrixTablero[fil][col];
    }

    public Celda[][] get_Tablero() {
        return matrixTablero;
    }

}
