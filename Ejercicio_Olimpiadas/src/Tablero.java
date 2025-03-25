import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Tablero {

    private Celda[][] matrixTablero;
    private int tamanioTablero= 0;
    private Random rand ;
    private  ArrayList<Point> iniciosYFinalesCarreteras=  new ArrayList<>(); //en vez de guardar los puntos
    //Un arrayList de arralistPoint

    private ArrayList<ArrayList<Point>> carreteras = new ArrayList<>(); //guarda los puntos de las carreteras


    private ArrayList<Vehiculo> vehiculos = new ArrayList<>(); //guarda los vehiculos que tiene

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


    public void generarCarreteras(int numCarreteras) {
        int numVerticales = numCarreteras / 2;
        int numHorizontales = numCarreteras / 2;
        int carreterasGeneradasVerticales = 0;
        int carreterasGeneradasHorizontales = 0;
        ArrayList<Point> puntosCarreteraVertical = new ArrayList<>();
        ArrayList<Point> puntosCarreteraHorizontal = new ArrayList<>();
        //ArrayList<Point> iniciosYFinalesCarreteras = new ArrayList<>();


        do{
            int xInicial= rand.nextInt(tamanioTablero - 2) + 1;
            int yInicial= rand.nextInt(tamanioTablero - 2) + 1;
            if (yInicial == tamanioTablero - 1) yInicial--;
            puntosCarreteraHorizontal.add(new Point(xInicial,yInicial));
        }while(generarCarreterasRecursivamente(puntosCarreteraHorizontal,iniciosYFinalesCarreteras,false,0,numCarreteras));



        //llamar a generar carreteras aqui

    }

    private boolean generarCarreterasRecursivamente(ArrayList<Point> puntosCarreteraBase, ArrayList<Point> iniciosYFinalesCarreteras, boolean esVertical, int n_carretera, int max_carreteras){
        char tipoCarretera = esVertical ? 'v' : 'h';
        puntosCarreteraBase.removeAll(iniciosYFinalesCarreteras);
        boolean noConseguido = true;
        int indicePunto ,fijo,variablePunto,inicio,fin;
        Point puntoBase;
        ArrayList<Point> nuevaCarretera = new ArrayList<>();

        indicePunto = rand.nextInt(puntosCarreteraBase.size()); //el primer punto lo genero de forma aletoria
        fijo =0;
        inicio = 0;
        fin = 0;


        int contadorPuntos= 0;
        int maxPuntos=puntosCarreteraBase.size();

        if( n_carretera>=max_carreteras){
            noConseguido= false;
        }
        else {
            carreteras.add(new ArrayList<>());
        }
        while(contadorPuntos<maxPuntos && noConseguido && n_carretera<max_carreteras){

            puntoBase = puntosCarreteraBase.get(indicePunto);
            fijo = esVertical ? puntoBase.y : puntoBase.x;  // Fila para horizontal, Columna para vertical
            variablePunto = esVertical ? puntoBase.x : puntoBase.y;  // Columna para horizontal, Fila para vertical
            do {
                inicio = controlarErrorRandomCarretera(1, variablePunto, true); //rand.nextInt(1, variablePunto);
                fin = controlarErrorRandomCarretera(variablePunto, tamanioTablero, false);   //rand.nextInt(variablePunto + 1, tamanioTablero - 1)? : variablePunto;
            } while (fin - inicio <= 3);

            if(!comprobarAdyacentesYPosicion(inicio,fin,fijo,tipoCarretera,esVertical)){ //si es posible plantar llamo a la siguiente
                //generar puntos

                for(int variable = inicio; variable <= fin; variable++){
                    Point nuevoPunto = esVertical? new Point(variable,fijo) : new Point(fijo,variable);
                    nuevaCarretera.add(nuevoPunto);
                   if(esVertical){
                       indexacionSimple( tipoCarretera, variable, fijo);
                       carreteras.get(n_carretera).add(new Point(variable,fijo));
                   }
                   else {
                       indexacionSimple( tipoCarretera, fijo, variable);
                       carreteras.get(n_carretera).add(new Point(fijo,variable));
                   }
                }



                //los indexo y si no lo consigo los desindexo
                noConseguido  = generarCarreterasRecursivamente(nuevaCarretera,iniciosYFinalesCarreteras,!esVertical, n_carretera+1,max_carreteras);

                if(noConseguido){
                    for (int variable = inicio; variable <= fin; variable++) {
                        if (esVertical) {
                            desindexacion( tipoCarretera, variable, fijo);
                        } else {
                            desindexacion( tipoCarretera, fijo, variable);
                        }
                        carreteras.remove(n_carretera);
                    }
                }

            }
            if(noConseguido) {
                indicePunto++;
                if(indicePunto>=maxPuntos)indicePunto=0;
                contadorPuntos++;

            }

        }

        if(!noConseguido && n_carretera<max_carreteras){ //en caso de que lo haya conseguido guarda el inicio y final
            iniciosYFinalesCarreteras.add(esVertical ? new Point(inicio, fijo) : new Point(fijo, inicio));
            iniciosYFinalesCarreteras.add(esVertical ? new Point(fin, fijo) : new Point(fijo, fin));

        }
        //si lo consigue se sale

        return noConseguido;
    }


    private void desindexacion(char tipoCarretera, int x, int y){
        if(matrixTablero[x][y].get_Cruce()) matrixTablero[x][y].set_Cruce(false);
        else if(matrixTablero[x][y].get_Carretera() && matrixTablero[x][y].get_TipoCarretera()==tipoCarretera) matrixTablero[x][y].set_Carretera(' ');
    }
    private void indexacionSimple(char tipoCarretera, int x, int y) {
        if(matrixTablero[x][y].get_Carretera()) matrixTablero[x][y].set_Cruce(true);
        else matrixTablero[x][y].set_Carretera(tipoCarretera);
    }

    private boolean comprobarAdyacentesYPosicion(int inicio, int fin,int fijo, char tipoCarretera,boolean esVertical){
        boolean tieneAdyacente = false;
        for(int variable = inicio; variable<=fin; variable++){
            if(esVertical) {
                if(matrixTablero[variable][fijo+1].get_TipoCarretera()==tipoCarretera || matrixTablero[variable][fijo-1].get_TipoCarretera()==tipoCarretera){
                    tieneAdyacente= true;
                }else if(matrixTablero[variable][fijo].get_Carretera() && matrixTablero[variable][fijo].get_TipoCarretera()==tipoCarretera){
                    tieneAdyacente= true;
                }

            }else{
                if(matrixTablero[fijo+1][variable].get_TipoCarretera()==tipoCarretera || matrixTablero[fijo-1][variable].get_TipoCarretera()==tipoCarretera){
                    tieneAdyacente= true;
                }else if(matrixTablero[fijo][variable].get_Carretera() && matrixTablero[fijo][variable].get_TipoCarretera()==tipoCarretera){
                    tieneAdyacente= true;
                }
            }
        }

        //tablero
        //si es vertical
        //comprobar respectivamente a lo largo de la longitud si encuentro otro del mismo tipo devuelve true
        return tieneAdyacente;
    }

    private int controlarErrorRandomCarretera(int inicioRandom, int finRandom, boolean esInicio){
        int valor;
        try {
            if(esInicio)valor = rand.nextInt(inicioRandom, finRandom);
            else valor=rand.nextInt(inicioRandom+1,finRandom-1);
        }
        catch (IllegalArgumentException e){
            valor= inicioRandom;
        }
        return valor;
    }

   /* public void limpiarPuntosNoIniciales(){

        int sizePuntos = iniciosYFinalesCarreteras.size();

        for(int i=0; i<sizePuntos;i++) {
            int nAdyacentes=0;
            int x=iniciosYFinalesCarreteras.get(i).x;
            int y= iniciosYFinalesCarreteras.get(i).y;
            nAdyacentes= mirarAdyacentes(x,y);

            if (nAdyacentes > 1){
                if(nAdyacentes==2 && matrixTablero[x][y].get_Cruce()) matrixTablero[x][y].set_Cruce(false);
                else if(nAdyacentes==3 && !matrixTablero[x][y].get_Cruce()) matrixTablero[x][y].set_Cruce(true);
                iniciosYFinalesCarreteras.remove(i);
                i--;
                sizePuntos--;
            }

        }
        //por cada punto comprobar sus adyacentes solo vertical y horizontalmente
        //si tiene mas de 1 ya no es candidato a punto
        //si tiene 2 es un giro
        //si tiene 3 es un cruce

    }//despues de la generacion compruebo cada punto si ese punto es un giro o un cruce considero que no es un destino ni de inicio ni de final*/

 /*   private int mirarAdyacentes(int fil, int col){

        int[][] direcciones = {{-1,0},{1,0},{0,-1}, {0,1}};
        int nAdyacentes=0;
        for(int[] dir: direcciones){
            int nuevaFil = fil+dir[0];
            int nuevaCol = col+dir[1];
            if(nuevaFil>=0 && nuevaFil<tamanioTablero && nuevaCol>=0 && nuevaCol<tamanioTablero){//controlo no salirme
                if(matrixTablero[nuevaFil][nuevaCol].get_Carretera()) nAdyacentes++;
            }
        }

        return nAdyacentes;
    }
*/


/*
    public void imprimirIniciosyFinales(){
        for(int i = 0; i<iniciosYFinalesCarreteras.size();i++){
            int x= iniciosYFinalesCarreteras.get(i).x;
            int y= iniciosYFinalesCarreteras.get(i).y;

            System.out.println("Punto "+i+": ("+x+","+y+")");
        }

    }
*/


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


    public ArrayList<Point> getIniciosYFinalesCarreteras() {
        return iniciosYFinalesCarreteras;
    }

    public void addVehiculoArray(Vehiculo v) {
        vehiculos.add(v);
    }

    public void inicializarVehiculos()
    {
        for (Vehiculo v : vehiculos)
        {
            Point inicio = v.get_Inicio();
            Point destino = v.get_Destino();
            agregarVehiculo(inicio.x,inicio.y,v);
            agregarDestino(destino.x,destino.y,v);
        }
    }


    private void agregarDestino(int fil, int col,Vehiculo vehiculo){
        if(fil>=0 && fil<tamanioTablero && col>=0 && col<tamanioTablero){
            matrixTablero[fil][col].setId_Destino(vehiculo);
            matrixTablero[fil][col].set_EsDestino(true);
        }
    }


    //no lo utilizo
    public void agregarCarretera(int fil, int col, char tipo){
        if(fil>=0 && fil<tamanioTablero && col>=0 && col<tamanioTablero){ //controlo no salirme del tablero
            matrixTablero[fil][col].set_Carretera(tipo);
        }
    }

    public void agregarCruce(int fil,int col){
        if(fil>=0 && fil<tamanioTablero && col>=0 && col<tamanioTablero){
            matrixTablero[fil][col].set_Cruce(true);
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

    public void imprimirCarreteras(){

        for(ArrayList<Point> carretera: carreteras){
            System.out.print(""+carreteras.indexOf(carretera));
            for(Point punto: carretera){
                System.out.print("("+punto.x+","+punto.y+")");
            }
            System.out.println();
        }
    }
    public ArrayList<ArrayList<Point>> getCarreteras(){
        return carreteras;
    }

    public Celda get_Celda(int fil, int col)
    {
        return matrixTablero[fil][col];
    }
}
