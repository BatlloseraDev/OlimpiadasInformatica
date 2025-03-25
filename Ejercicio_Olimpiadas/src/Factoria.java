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
        ArrayList<ArrayList<Point>> carreterasInicios= new ArrayList<>(tablero.getCarreteras());
        ArrayList<ArrayList<Point>> carreterasFinales= new ArrayList<>(tablero.getCarreteras());


        for(int i=0; i<n_vehiculos;i++){
            int carreteraRandom =new Random().nextInt(carreterasInicios.size());
            Point puntoInicial;
            Celda celdaAux;
            do{
                puntoInicial= carreterasInicios.get(carreteraRandom).get(new Random().nextInt(carreterasInicios.get(carreteraRandom).size()-2)+1 );// el random no puede ser ni 0 ni el maximo
                celdaAux= tablero.get_Celda(puntoInicial.x,puntoInicial.y);
            }while(celdaAux.get_EsDestino() || celdaAux.get_Cruce()); //mientras que sea disitinto de destino
            carreterasInicios.remove(carreteraRandom);

            int carreteraRandom2;
            Point puntoFinalAux1, puntoFinalAux2;
            Celda celda1, celda2;
            Point puntoFinal = null;
            do {
                carreteraRandom2= new Random().nextInt(carreterasFinales.size());
                puntoFinalAux1= carreterasFinales.get(carreteraRandom2).getFirst();
                puntoFinalAux2= carreterasFinales.get(carreteraRandom2).getLast();

                celda1 = tablero.get_Celda(puntoFinalAux1.x,puntoFinalAux1.y);
                celda2 = tablero.get_Celda(puntoFinalAux2.x,puntoFinalAux2.y);

                if(celda1.get_EsDestino() && celda2.get_EsDestino()){
                    carreterasFinales.remove(carreteraRandom2);
                }
                else if(!celda1.get_EsDestino() || !celda2.get_EsDestino()){
                    if(!celda1.get_EsDestino()){
                        puntoFinal = puntoFinalAux1;
                    }
                    else{
                        puntoFinal = puntoFinalAux2;
                    }
                }
            }while (puntoFinal==null);


            tablero.addVehiculoArray(generarVehiculo(i+1,puntoInicial,puntoFinal));
         /*   Point puntoInicial = puntos.get(new  Random().nextInt(puntos.size()));
            puntos.remove(puntoInicial);
            Point puntoFinal = puntos.get(new  Random().nextInt(puntos.size()));
            puntos.remove(puntoFinal);
            tablero.addVehiculoArray(generarVehiculo(i+1,puntoInicial,puntoFinal));*/
        }

    }
}
