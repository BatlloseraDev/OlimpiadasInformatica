public class Vehiculo {
    private int id;
    private int velocidad; //de una a dos casillas

    public Vehiculo(int id, int velocidad){
        this.id=id;
        this.velocidad=velocidad;
    }


    @Override
    public String toString(){
        return id+"";//Representacion del vehiculo en el mapa
    }
}
