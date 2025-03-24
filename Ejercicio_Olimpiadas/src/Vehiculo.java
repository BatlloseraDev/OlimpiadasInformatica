import java.awt.*;

public class Vehiculo {
    private int id;
    private Velocidades velocidad; //de una a dos casillas
    private Point posicion;
    private Point destino;
    private Point inicio;

    public Vehiculo(int id, Velocidades velocidad, Point inicio, Point destino){
        this.id=id;
        this.velocidad=velocidad;
        this.posicion= inicio;
        this.destino=destino;
        this.inicio= inicio;
    }


    @Override
    public String toString(){
        return id+"";//Representacion del vehiculo en el mapa
    }


    public Point get_Inicio()
    {
        return inicio;
    }

    public Point get_Posicion(){
        return posicion;
    }
    public Point get_Destino(){
        return destino;
    }
    //tendre que crear una clase de mover vehiculo.

    //cada vehiculo evaluara si puede moverse
}
