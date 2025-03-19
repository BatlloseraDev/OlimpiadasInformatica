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


    public void generarCarreteras(int numCarreteras){ // Este dato lo tengo que controlar en el main, aqui llega que 100% es par
        int numVerticales= numCarreteras/2;
        int numHorizontales= numCarreteras/2;

        for(int i=0; i<numVerticales;i++){
            int col = rand.nextInt(tamanioTablero-2)+1;
            int inicio = rand.nextInt(tamanioTablero/2);                //Para asgurarme que empieza en una mitad del tablero
            int fin = rand.nextInt(tamanioTablero/2)+tamanioTablero/2;  //Para asegurarme que termina la otra mitad del tablero //Si se origina justo en el medio tocará borde por lo que comprobar si es borde si no restar 1
            //Guardar Inicio y Fin en un ArrayList para manejarlo, me lo dejo anotado para manejarlo mas adelante
            //Tambien debo de guardar este dato para que no se genere una carretera donde ya hay una carretera
            for(int fila= inicio; fila<=fin; fila++)
            {
                matrixTablero[fila][col].set_Carretera(); //dibujo la carretera
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
                }
                else{
                    matrixTablero[fil][col].set_Carretera(); //dibujo la carretera
                }
            }
        }//fin generar carreteras horizontales

        /*
        * Este codigo no comprueba si esta tratando sobreescribir las carreteras y buscar nuevo punto
        * No controla al 100% de no pintar en el borde
        * No controla que al menos haya n/2 cruces  
        -*Trabajando en ello solución propuesta agregar norma de generación donde carretera necesita tocar en >=1 punto con otra fallo regenerar tablero funciona 6 carreteras 3 cruces error impares
        * Simplemente genera carreteras aleatorias  
        -*Norma de generación comprobar que el camino compuesto de " " es posible entre "v" y "meta" si no regenerar. scrip de desplazamiento "v" +1 y solo posible en " " 
        -*Metodo de desplazamiento ineficiente optimizable formula de tanteo variable 1-3-2-1 introduce error de probabilidad posible solución impedir backtrackin a celdas ya ocupadas
        * */

    }




    public void imprimirTablero(){
        for(int j= 0; j<tamanioTablero+2; j++){
            System.out.print("-\t");
        }
        System.out.println();

        for(int i =0; i<tamanioTablero;i++){
            System.out.print("|\t"); //borde derecho
            for(int j=0; j<tamanioTablero;j++){
                System.out.print(matrixTablero[i][j]+"\t");
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

    public void agregarCarretera(int fil, int col){
        if(fil>=0 && fil<tamanioTablero && col>=0 && col<tamanioTablero){ //controlo no salirme del tablero
            matrixTablero[fil][col].set_Carretera();
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
