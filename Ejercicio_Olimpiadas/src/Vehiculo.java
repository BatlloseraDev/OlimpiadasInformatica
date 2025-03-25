import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Vehiculo {
    private int id;
    private Velocidades velocidad; //de una a dos casillas
    private Point posicion;
    private Point destino;
    private Point inicio;
    private ArrayList<Point> camino;


    public Vehiculo(int id, Velocidades velocidad, Point inicio, Point destino){
        this.id=id;
        this.velocidad=velocidad;
        this.posicion= inicio;
        this.destino=destino;
        this.inicio= inicio;
        this.camino= new ArrayList<>();
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

    public void set_Posicion(Point posicion) {
        this.posicion=posicion;
    }
    //tendre que crear una clase de mover vehiculo.


    public void set_Camino(List<Point> camino){
        this.camino= new ArrayList<>(camino);
    }
    //cada vehiculo evaluara si puede moverse
}
