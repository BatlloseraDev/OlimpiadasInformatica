import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Tablero {

    private Celda[][] matrixTablero;
    private int tamanioTablero= 0;
    private Random rand ;

   //Deberia de crear un ArryList de inicios y finales aqui

    public Tablero(int tamanioTablero) {
        this.tamanioTablero = tamanioTablero;
        this.matrixTablero = new Celda[tamanioTablero][tamanioTablero]; //por defecto es vacio.
        this.rand = new Random();
        inicializarCeldas();

    }

    private void inicializarCeldas(){
        for(int i= 0; i< tamanioTablero; i++){
            for(int j=0; j<tamanioTablero;j++){
                matrixTablero[i][j]= Factoria.generarCelda();
            }
        }
    }

    //metodo 2
    public void generarCarreteras2(int numCarreteras){
        int numVerticales= numCarreteras/2;
        int numHorizontales= numCarreteras/2;
        int n_cruces = numCarreteras/2;
        int cruces_Generados= 0;

//        ArrayList<Point> carreterasVerticales = new ArrayList<>(); //me va a sevir para poder comprobar las carreteras
        ArrayList<Point> carreterasHorizontales = new ArrayList<>();

        for(int i=0; i<numVerticales;i++){
            int col ;
            int inicio = rand.nextInt(tamanioTablero/2)+1;
            int fin = rand.nextInt(tamanioTablero/2)+tamanioTablero/2;
            if(fin==tamanioTablero-1) fin--;

            do{
                col = rand.nextInt(tamanioTablero-2)+1;

            }while (existeCarreteraEnColumna(col, inicio, fin));//si en toda la distacia de la cadena no hay otra carretera la hace

            //Indexacion final
            for(int fila= inicio; fila<=fin; fila++)
            {
                matrixTablero[fila][col].set_Carretera('v');
                //carreterasVerticales.add(new Point(fila,col));
            }
        } //fin generacion de carreteras verticasles con comprobante de que no se quenere una nueva donde ya habia una

        //generar carreteras horizontales
        for(int i=0; i<numHorizontales;i++){
            int fil ;
            int inicio = rand.nextInt(tamanioTablero/2)+1;
            int fin = rand.nextInt(tamanioTablero/2)+tamanioTablero/2;
            if(fin==tamanioTablero-1) fin--;
            do{
                fil = rand.nextInt(tamanioTablero-2)+1;
            }while (existeCarreteraEnFila(fil, inicio, fin)); //problema que si se quiere poner en un sitio que cruce con una carretera mal


            //Indexacion final
            for(int col= inicio; col<=fin; col++) {
                if(matrixTablero[fil][col].get_Carretera()){
                    matrixTablero[fil][col].set_Cruce();
                    cruces_Generados++;
                }else{
                    matrixTablero[fil][col].set_Carretera('h');
                    carreterasHorizontales.add(new Point(fil,col));
                }
            } //aun no se encarga de asegurar que haya n/2 cruces
        }

    }
    private boolean existeCarreteraEnColumna(int col, int inicio, int fin){
        boolean existe = false;
        for(int i=inicio; i<fin;i++){
            if(matrixTablero[i][col].get_Carretera()) existe= true;
        }
        return existe;
    }
    private boolean existeCarreteraEnFila(int fil, int inicio, int fin){
        boolean existe = false;
        for(int j=0; j<tamanioTablero;j++){
            if(matrixTablero[fil][j].get_Carretera() && matrixTablero[fil][j].get_TipoCarretera()=='h') existe= true;
        }
        return existe;
    }

    //metodo 1
    /*public void generarCarreteras(int numCarreteras){ // Este dato lo tengo que controlar en el main, aqui llega que 100% es par
        int numVerticales= numCarreteras/2;
        int numHorizontales= numCarreteras/2;
        int n_cruces = numCarreteras/2;
        int cruces_Generados= 0;

        for(int i=0; i<numVerticales;i++){
            int col = rand.nextInt(tamanioTablero-2)+1;
            int inicio = rand.nextInt(tamanioTablero/2);                //Para asgurarme que empieza en una mitad del tablero
            int fin = rand.nextInt(tamanioTablero/2)+tamanioTablero/2;  //Para asegurarme que termina la otra mitad del tablero //Si se origina justo en el medio tocará borde por lo que comprobar si es borde si no restar 1
            //Guardar Inicio y Fin en un ArrayList para manejarlo, me lo dejo anotado para manejarlo mas adelante
            //Tambien debo de guardar este dato para que no se genere una carretera donde ya hay una carretera
            for(int fila= inicio; fila<=fin; fila++)
            {
                matrixTablero[fila][col].set_Carretera('v'); //dibujo la carretera
            }
        }//fin generar carreteras verticales

        for(int i=0; i<numHorizontales;i++){
            int fil = rand.nextInt(tamanioTablero-2)+1;
            int inicio = rand.nextInt(tamanioTablero/2);                //Para asgurarme que empieza en una mitad del tablero
            int fin = rand.nextInt(tamanioTablero/2)+tamanioTablero/2;  //Para asegurarme que termina en la otra mitad
            //Recordar optimizacion ArrayList para controlar que no esta ocupada ya dicha carretera

            for(int col= inicio; col<=fin; col++) {
                if(matrixTablero[fil][col].get_Carretera()){
                    matrixTablero[fil][col].set_Cruce(); //como ya es carretera se convierte en cruce
                    n_cruces--;
                }
                else{
                    matrixTablero[fil][col].set_Carretera('h'); //dibujo la carretera
                }
            }
        }//fin generar carreteras horizontales

        *//*
        * Este codigo no comprueba si esta tratando sobreescribir las carreteras y buscar nuevo punto
        * No controla al 100% de no pintar en el borde
        * No controla que al menos haya n/2 cruces
        * Simplemente genera carreteras aleatorias
        * *//*

    }*/




    public void imprimirTablero(){
        for(int j= 0; j<tamanioTablero+2; j++){
            System.out.print("-\t");
        }
        System.out.println();

        for(int i =0; i<tamanioTablero;i++){
            System.out.print("|\t"); //borde derecho
            for(int j=0; j<tamanioTablero;j++){


                if(matrixTablero[i][j].get_TipoCarretera()== 'h') System.out.print(Colores.BLUE+matrixTablero[i][j]+Colores.RESET+"\t");
                else if(matrixTablero[i][j].get_TipoCarretera()== 'v') System.out.print(Colores.YELLOW+matrixTablero[i][j]+Colores.RESET+"\t");
                else  System.out.print(matrixTablero[i][j]+"\t");
            }
            System.out.print("|"); //borde izquierdo
            System.out.println();
        }

        for(int j= 0;j<tamanioTablero+2; j++)
        {
            System.out.print("-\t");
        }
        System.out.println();
    }

    //metodos de juego
    //no lo utilizo
    public void agregarCarretera(int fil, int col, char tipo){
        if(fil>=0 && fil<tamanioTablero && col>=0 && col<tamanioTablero){ //controlo no salirme del tablero
            matrixTablero[fil][col].set_Carretera(tipo);
        }
    }

    public void agregarCruce(int fil,int col){
        if(fil>=0 && fil<tamanioTablero && col>=0 && col<tamanioTablero){
            matrixTablero[fil][col].set_Cruce();
        }
    }

    public void agregarVehiculo(int fil, int col, Vehiculo vehiculo){
        if(fil>=0 && fil<tamanioTablero && col>=0 && col<tamanioTablero){
            matrixTablero[fil][col].set_Vehiculo(vehiculo);
        }
    }
    public void quitarVehiculo(int fil, int col){
        if(fil>=0 && fil<tamanioTablero && col>=0 && col<tamanioTablero){
            if(matrixTablero[fil][col].get_Vehiculo()!=null) matrixTablero[fil][col].set_Vehiculo(null);
        }
    }
}
