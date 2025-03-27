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
    private int direccion; //0 Arriba y a la derecha. 1 Abajo y a la Izquierda
    private int prioridad= 0; //0.No tiene 1.baja 2.media 3.Alta

    public Vehiculo(int id, Velocidades velocidad, Point inicio, Point destino){
        this.id=id;
        this.velocidad=velocidad;
        this.posicion= inicio;
        this.destino=destino;
        this.inicio= inicio;
        this.camino= new ArrayList<>();
        this.direccion= 0;
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


    public int calcularDireccion() {
        int direccion;
        if(camino.size()>0){
            int resX = camino.get(1).x - posicion.x;
            int resY = camino.get(1).y - posicion.y;

            //es imposible que se de el caso de que los dos sean distintos de 0 ya que solo voy a interpretar paso a paso
            if (resX != 0) { //si es distinto de 0 es que se ha movido en esta direccion
                direccion = resX == 1 ? 1: 0;
            } else{
                direccion = resY == 1 ? 1 : 0;
            }
        }
        else direccion = 0; // en caso de que se produzca un error lo seteo a 0

        return direccion;

    }

    public int get_Direccion(){
        return direccion;
    }

    public void set_Direccion(int direccion){
        this.direccion= direccion;
    }

    public Velocidades get_Velocidad(){
        return this.velocidad;
    }

    public Point get_SiguientePos(){
        return camino.size()>1 ? camino.get(1) : null;
    }

    public Point get_PosEnRuta(int pos){
        return camino.size()>pos ? camino.get(pos) : null;
    }

    public void avanzarRuta(){
        if(camino.size()>1) camino.removeFirst();
    }

    public void set_Prioridad(int p){
        this.prioridad= p;
    }

    public int get_Prioridad(){
        return prioridad;
    }
    public int calcularPrioridad(){
        int prio = 0;

        if(camino.size()<3) prio = 1; //esto quiere decir que el coche termina en el cruce
        else if(camino.size()>=3){
            Point pos1, pos2;
            pos1 = camino.get(0);
            pos2 = camino.get(2);
            int disX= pos2.x - pos1.x;
            int disY = pos2.y - pos1.y;
            if(disX==0 || disY==0) prio= 3; //prio alta ya que no cambia de direccion
            else prio= 2; //prio media ya que cambia de direccion
        }
        return prio;
    }


}
