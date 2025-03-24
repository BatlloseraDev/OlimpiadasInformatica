import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
public class Factoria {

    public static Tablero generarTablero(int n){
        return new Tablero(n);
    }

    public static Celda generarCelda(){
        return new Celda();
    }

    public static Vehiculo generarVehiculo(int id, Point posicion, Point destino){

        Velocidades velocidad = generarVelocidadRandom();
        return new Vehiculo(id,velocidad, posicion, destino);
    }

    private static Velocidades generarVelocidadRandom(){
        Velocidades[] valores = Velocidades.values();
        return valores[new Random().nextInt(valores.length)];
    }


    public static void generarVehiculosEnTablero(Tablero tablero, int n_vehiculos)
    {
        ArrayList<Point> puntos= new ArrayList<>(tablero.getIniciosYFinalesCarreteras());
        for(int i=0; i<n_vehiculos;i++){

            Point puntoInicial = puntos.get(new  Random().nextInt(puntos.size()));
            puntos.remove(puntoInicial);
            Point puntoFinal = puntos.get(new  Random().nextInt(puntos.size()));
            puntos.remove(puntoFinal);
            tablero.addVehiculoArray(generarVehiculo(i+1,puntoInicial,puntoFinal));
        }

    }
}
