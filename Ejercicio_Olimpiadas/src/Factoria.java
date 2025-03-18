public class Factoria {

    public static Tablero generarTablero(int n){
        return new Tablero(n);
    }

    public static Celda generarCelda(){
        return new Celda();
    }

    public static Vehiculo generarVehiculo(int id, int velocidad){
        return new Vehiculo(id,velocidad);
    }

}
