import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Factoria {

    public static Tablero generarTablero(int n) {
        return new Tablero(n);
    }

    public static Celda generarCelda() {
        return new Celda();
    }

    public static Vehiculo generarVehiculo(int id, Point posicion, Point destino) {

        Velocidades velocidad = generarVelocidadRandom();
        return new Vehiculo(id, velocidad, posicion, destino);
    }

    private static Velocidades generarVelocidadRandom() {
        Velocidades[] valores = Velocidades.values();
        return valores[new Random().nextInt(valores.length)];
    }


    public static void generarVehiculosEnTablero(Tablero tablero, int n_vehiculos) {
        ArrayList<ArrayList<Point>> carreterasInicios = new ArrayList<>(tablero.getCarreteras());

        ArrayList<Point> puntosFinales = new ArrayList<>();
        for (ArrayList<Point> carretera : carreterasInicios) {
            puntosFinales.add(carretera.getFirst());
            puntosFinales.add(carretera.getLast());
        }


        for (int i = 0; i < n_vehiculos; i++) {
            int carreteraRandom = new Random().nextInt(carreterasInicios.size());
            Point puntoInicial;
            Celda celdaAux;
            do {
                puntoInicial = carreterasInicios.get(carreteraRandom).get(new Random().nextInt(carreterasInicios.get(carreteraRandom).size() - 2) + 1);// el random no puede ser ni 0 ni el maximo
                celdaAux = tablero.get_Celda(puntoInicial.x, puntoInicial.y);
            } while (celdaAux.get_EsDestino() || celdaAux.get_Cruce()); //mientras que sea disitinto de destino
            carreterasInicios.remove(carreteraRandom);

            int puntoRandom;
            Point puntoFinal;

            puntoRandom = new Random().nextInt(puntosFinales.size());
            puntoFinal = puntosFinales.get(puntoRandom);
            puntosFinales.remove(puntoRandom);

            tablero.addVehiculoArray(generarVehiculo(i + 1, puntoInicial, puntoFinal));

        }

    }

    public static void generarCamino(Tablero tablero, Vehiculo vehiculo) {

        Point inicio = vehiculo.get_Inicio();
        Point destino = vehiculo.get_Destino();
        List<Point> camino = PathFinder.encontrarElCaminoMasCorto(tablero.get_Tablero(), inicio.x, inicio.y, destino.x, destino.y);

        vehiculo.set_Camino(camino);
    }


}
