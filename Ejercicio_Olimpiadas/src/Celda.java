public class Celda {

    private boolean esCarretera;
    private Vehiculo vehiculo;
    private boolean cruce;

    //Celda no determina lo que es por lo que no tiene que manejarlo el constructor

    public Celda() { //generacion de cadena vacia
        this.esCarretera=false;
        this.vehiculo= null;
        this.cruce = false;
    }



    //Getters And Setters

    public  void set_Cruce(){
        this.cruce = true;
    }

    public boolean get_Cruce(){
        return cruce;
    }


    public void set_Carretera(){
        this.esCarretera=true;
    }
    public boolean get_Carretera(){
        return esCarretera;
    }



    public void set_Vehiculo(Vehiculo vehiculo)
    {
        this.vehiculo= vehiculo;
    }

    public Vehiculo get_Vehiculo()
    {
        return vehiculo;
    }




    //Return de informacion formateada como cadena de texto
    @Override
    public String toString(){
        String cadena;
        if(vehiculo!=null) cadena= "V";
        else if(cruce) cadena= "X";
        else if(esCarretera) cadena= "#";
        else cadena = " "; //si no es vacio.

        return cadena;
    }


}
