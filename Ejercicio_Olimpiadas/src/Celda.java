import java.util.ArrayList;

public class Celda {

    private boolean esDestino;
    private Vehiculo id_Destino;
    private boolean esCarretera;
    private Vehiculo[] vehiculo; //las carreteras son de doble sentido a menos que sea un cruce
    private boolean cruce;
    private char tipoCarretera = ' '; // ' ' no es ningun tipo, 'v' es vertical, 'h' es horizontal
    private ArrayList<Vehiculo> conflictos = new ArrayList<>();//en este caso la celda con conflicto

    //Celda no determina lo que es por lo que no tiene que manejarlo el constructor
    //una celda puede tener dos vehiculos mirando de forma opuesta salvo que sea cruce,
    //por ende vechiculo[0] es arriba y derecha
    //vehiculo[1] es abajo y izquierda


    public Celda() { //generacion de cadena vacia
        this.esCarretera=false;
        this.vehiculo= new Vehiculo[2];
        this.cruce = false;
        this.esDestino= false;
    }



    //Getters And Setters

    public  void set_Cruce(boolean cruce){
        this.cruce = cruce;
    }

    public boolean get_Cruce(){
        return cruce;
    }


    public void set_Carretera(char tipo){
        this.tipoCarretera= tipo;
        this.esCarretera= tipo != ' ';
    }
    public boolean get_Carretera(){
        return esCarretera;
    }

    public char get_TipoCarretera(){return tipoCarretera;}


    public void set_Vehiculo(Vehiculo vehiculo, int pos)
    {
        this.vehiculo[pos]= vehiculo;
    }

    public void setId_Destino(Vehiculo vehiculo){
        this.id_Destino= vehiculo;
    }
    public Vehiculo get_Vehiculo(int pos)
    {
        return vehiculo[pos];
    }

    public void set_EsDestino(boolean esDestino){
        this.esDestino= esDestino;
    }

    public boolean get_EsDestino(){
        return esDestino;
    }


    //Return de informacion formateada como cadena de texto
    @Override
    public String toString(){
        String cadena;
        if(esDestino) cadena= "["+id_Destino+"]";
        else if(vehiculo[0]!=null || vehiculo[1]!=null){
            cadena = "";
            if(vehiculo[0]!=null) cadena+=vehiculo[0];
            if(vehiculo[1]!=null) cadena+=vehiculo[1];
        }
        else if(cruce) cadena= "X";
        else if(esCarretera) cadena= "#";
        else cadena = " "; //si no es vacio.

        return cadena;
    }
    public void add_Conflicto(Vehiculo v){
        if(!conflictos.contains(v)) conflictos.add(v); //evito duplicidades
    }

    public ArrayList<Vehiculo> get_Conflictos(){
        return conflictos;
    }
    public void limpiarConflictos(){
        conflictos.clear();
    }

}
