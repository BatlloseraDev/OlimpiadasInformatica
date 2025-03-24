public class Celda {

    private boolean esDestino;
    private boolean esCarretera;
    private Vehiculo vehiculo;
    private boolean cruce;
    private char tipoCarretera = ' '; // ' ' no es ningun tipo, 'v' es vertical, 'h' es horizontal

    //Celda no determina lo que es por lo que no tiene que manejarlo el constructor

    public Celda() { //generacion de cadena vacia
        this.esCarretera=false;
        this.vehiculo= null;
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


    public void set_Vehiculo(Vehiculo vehiculo)
    {
        this.vehiculo= vehiculo;
    }

    public Vehiculo get_Vehiculo()
    {
        return vehiculo;
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
        if(vehiculo!=null && esDestino) cadena= "["+vehiculo+"]";
        else if(vehiculo!=null) cadena= ""+vehiculo;
        else if(cruce) cadena= "X";
        else if(esCarretera) cadena= "#";
        else cadena = " "; //si no es vacio.

        return cadena;
    }


}
