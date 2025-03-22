import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Tablero {

    private Celda[][] matrixTablero;
    private int tamanioTablero= 0;
    private Random rand ;
    private  ArrayList<Point> iniciosYFinalesCarreteras=  new ArrayList<>();;

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

    //Metodo 3 Refactorizado

    public void generarCarreteras3(int numCarreteras) {
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

/*
        while (carreterasGeneradasVerticales + carreterasGeneradasHorizontales < numCarreteras) {

            generarCarreteraVerticalDesdeHorizontal(puntosCarreteraVertical, puntosCarreteraHorizontal, iniciosYFinalesCarreteras);
            carreterasGeneradasVerticales++;
            puntosCarreteraHorizontal.clear();
            generarCarreteraHorizontalDesdeVertical(puntosCarreteraHorizontal, puntosCarreteraVertical, iniciosYFinalesCarreteras);
            carreterasGeneradasHorizontales++;
            puntosCarreteraVertical.clear();
        }*/
    }

    private boolean generarCarreterasRecursivamente(ArrayList<Point> puntosCarreteraBase, ArrayList<Point> iniciosYFinalesCarreteras, boolean esVertical, int n_carretera, int max_carreteras){
        char tipoCarretera = esVertical ? 'v' : 'h';
        puntosCarreteraBase.removeAll(iniciosYFinalesCarreteras);
        boolean noConseguido = true;
        int indicePunto ,fijo,variablePunto,inicio,fin;
        Point puntoBase;
        ArrayList<Point> nuevaCarretera = new ArrayList<>();

        indicePunto = rand.nextInt(puntosCarreteraBase.size()); //el primer punto lo genero de forma aletoria
        puntoBase = puntosCarreteraBase.get(0);
        fijo =0;
        variablePunto = 0;
        inicio = 0;
        fin = 0;


        int contadorPuntos= 0;
        int maxPuntos=puntosCarreteraBase.size();

        if( n_carretera>=max_carreteras) noConseguido= false;
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
                   if(esVertical)indexacionSimple( tipoCarretera, variable, fijo);
                   else indexacionSimple( tipoCarretera, fijo, variable);
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
                    }
                }

            }
            if(noConseguido) {
                indicePunto++;
                if(indicePunto>=maxPuntos)indicePunto=0;
                contadorPuntos++;

            }

        }

        if(!noConseguido){ //en caso de que lo haya conseguido guarda el inicio y final
            iniciosYFinalesCarreteras.add(esVertical ? new Point(inicio, fijo) : new Point(fijo, inicio));
            iniciosYFinalesCarreteras.add(esVertical ? new Point(fin, fijo) : new Point(fijo, fin));

        }
        //si lo consigue se sale

        return noConseguido;
    }

    private void generarCarreteraVerticalDesdeHorizontal(ArrayList<Point> puntosCarreteraVertical, ArrayList<Point> puntosCarreteraHorizontal, ArrayList<Point> iniciosYFinalesCarreteras) {
        generarCarretera(puntosCarreteraVertical, puntosCarreteraHorizontal, iniciosYFinalesCarreteras, 'v', true);
    }

    private void generarCarreteraHorizontalDesdeVertical(ArrayList<Point> puntosCarreteraHorizontal, ArrayList<Point> puntosCarreteraVertical, ArrayList<Point> iniciosYFinalesCarreteras) {
        generarCarretera(puntosCarreteraHorizontal, puntosCarreteraVertical, iniciosYFinalesCarreteras, 'h', false);
    }

    private void generarCarretera(
            ArrayList<Point> puntosNuevaCarretera,
            ArrayList<Point> puntosCarreteraBase,
            ArrayList<Point> iniciosYFinalesCarreteras,
            char tipoCarretera,
            boolean esVertical) {

        puntosCarreteraBase.removeAll(iniciosYFinalesCarreteras);

        int indicePunto ,fijo,variablePunto,inicio,fin;
        Point puntoBase;

        //empezar bucle aqui donde empiezo este bucle es donde tengo que generar la recursividad
        do{
            indicePunto = rand.nextInt(puntosCarreteraBase.size());
            puntoBase = puntosCarreteraBase.get(indicePunto);
            fijo = esVertical ? puntoBase.y : puntoBase.x;  // Fila para horizontal, Columna para vertical
            variablePunto = esVertical ? puntoBase.x : puntoBase.y;  // Columna para horizontal, Fila para vertical

            do {
                inicio = controlarErrorRandomCarretera(1, variablePunto, true); //rand.nextInt(1, variablePunto);
                fin = controlarErrorRandomCarretera(variablePunto, tamanioTablero, false);   //rand.nextInt(variablePunto + 1, tamanioTablero - 1)? : variablePunto;
            } while (fin - inicio <= 3);


        }while(comprobarAdyacentesYPosicion(inicio,fin,fijo,tipoCarretera,esVertical)); //controlo que no se genere ninguna carretera al lado de otra del mismo tipo

        iniciosYFinalesCarreteras.add(esVertical ? new Point(inicio, fijo) : new Point(fijo, inicio));
        iniciosYFinalesCarreteras.add(esVertical ? new Point(fin, fijo) : new Point(fijo, fin));

        //indexacion final
        for (int variable = inicio; variable <= fin; variable++) {
            if (esVertical) {
                indexacion(puntosNuevaCarretera, tipoCarretera, variable, fijo);
              /*  if(matrixTablero[variable][fijo].get_Carretera()) matrixTablero[variable][fijo].set_Cruce();
                else {
                    matrixTablero[variable][fijo].set_Carretera(tipoCarretera);
                    puntosNuevaCarretera.add(new Point(variable, fijo));
                }*/
            } else {
                indexacion(puntosNuevaCarretera, tipoCarretera, fijo, variable);
            }
        }
    }

/*

    private void generarPrimeraCarreteraVertical(ArrayList<Point> puntosCarreteraVertical, ArrayList<Point> iniciosYFinalesCarreteras) {
        int inicioFila = rand.nextInt(tamanioTablero / 2) + 1;
        int finFila = rand.nextInt(tamanioTablero / 2) + tamanioTablero / 2;
        if (finFila == tamanioTablero - 1) finFila--;
        int columna = rand.nextInt(tamanioTablero - 2) + 1;

        iniciosYFinalesCarreteras.add(new Point(inicioFila, columna));
        iniciosYFinalesCarreteras.add(new Point(finFila, columna));

        for (int fila = inicioFila; fila <= finFila; fila++) {
            matrixTablero[fila][columna].set_Carretera('v');
            puntosCarreteraVertical.add(new Point(fila, columna));
        }
    }

    private void generarCarreteraVerticalDesdeHorizontal(ArrayList<Point> puntosCarreteraVertical, ArrayList<Point> puntosCarreteraHorizontal, ArrayList<Point> iniciosYFinalesCarreteras) {
        puntosCarreteraHorizontal.removeAll(iniciosYFinalesCarreteras);
        int indicePunto = rand.nextInt(puntosCarreteraHorizontal.size());
        Point puntoHorizontal = puntosCarreteraHorizontal.get(indicePunto);
        int columna = puntoHorizontal.y;
        int filaPunto = puntoHorizontal.x;

        int inicioFila;
        int finFila;
        do {
            inicioFila = rand.nextInt(1, filaPunto);
            finFila = rand.nextInt(filaPunto + 1, tamanioTablero - 2);
        } while (finFila - inicioFila <= 3);

        if (finFila == tamanioTablero - 1) finFila--;

        iniciosYFinalesCarreteras.add(new Point(inicioFila, columna));
        iniciosYFinalesCarreteras.add(new Point(finFila, columna));

        for (int fila = inicioFila; fila <= finFila; fila++) {
            matrixTablero[fila][columna].set_Carretera('v');
            puntosCarreteraVertical.add(new Point(fila, columna));
        }
    }




    private void generarCarreteraHorizontalDesdeVertical(ArrayList<Point> puntosCarreteraHorizontal, ArrayList<Point> puntosCarreteraVertical, ArrayList<Point> iniciosYFinalesCarreteras) {
        puntosCarreteraVertical.removeAll(iniciosYFinalesCarreteras);
        int indicePunto = rand.nextInt(puntosCarreteraVertical.size());
        Point puntoVertical = puntosCarreteraVertical.get(indicePunto);
        int columnaPunto = puntoVertical.y;
        int fila = puntoVertical.x;

        int inicioColumna;
        int finColumna;
        do {
            inicioColumna = rand.nextInt(1, columnaPunto);
            finColumna = rand.nextInt(columnaPunto + 1, tamanioTablero - 2);
        } while (finColumna - inicioColumna <= 3);

        iniciosYFinalesCarreteras.add(new Point(fila, inicioColumna));
        iniciosYFinalesCarreteras.add(new Point(fila, finColumna));

        for (int columna = inicioColumna; columna <= finColumna; columna++) {
            matrixTablero[fila][columna].set_Carretera('h');
            puntosCarreteraHorizontal.add(new Point(fila, columna));
        }
    }
*/

    private void desindexacion(char tipoCarretera, int x, int y){
        if(matrixTablero[x][y].get_Cruce()) matrixTablero[x][y].set_Cruce(false);
        else if(matrixTablero[x][y].get_Carretera() && matrixTablero[x][y].get_TipoCarretera()==tipoCarretera) matrixTablero[x][y].set_Carretera(' ');
    }
    private void indexacionSimple(char tipoCarretera, int x, int y) {
        if(matrixTablero[x][y].get_Carretera()) matrixTablero[x][y].set_Cruce(true);
        else matrixTablero[x][y].set_Carretera(tipoCarretera);
    }

    private void indexacion(ArrayList<Point> puntosNuevaCarretera, char tipoCarretera, int x, int y) {
        if(matrixTablero[x][y].get_Carretera()) matrixTablero[x][y].set_Cruce(true);
        else matrixTablero[x][y].set_Carretera(tipoCarretera);
        puntosNuevaCarretera.add(new Point(x, y));
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


/*
    //metodo 3

    public void generarCarreteras3(int numCarreteras){

        int numVerticales= numCarreteras/2;
        int numHorizontales= numCarreteras/2;
        int n_cruces = numCarreteras/2;
        int cruces_Generados= 0;
        ArrayList<Point> lastCarreteraVertical  = new ArrayList<>();
        ArrayList<Point> lastCarreteraHorizontal  = new ArrayList<>();
        ArrayList<Point> inic_finales= new ArrayList<>();
        int nV = 0;
        int nH= 0;

        while (nH+nV<numCarreteras){ //Genero todas las carreteras a parir de una anterior de tal forma que siempre va a haber un cruce

            if (nH == 0) { // En caso de que sea la primera carretera se crea sin tener en cuenta la anterior
                int col;
                int inicio = rand.nextInt(tamanioTablero / 2) + 1;
                int fin = rand.nextInt(tamanioTablero / 2) + tamanioTablero / 2;
                if (fin == tamanioTablero - 1) fin--;
                col = rand.nextInt(tamanioTablero-2)+1;

                //Guardo el inicio y el final de la carretera
                inic_finales.add(new Point(inicio,col));
                inic_finales.add(new Point(fin,col));

                //Indexacion en el mapa
                for(int fila= inicio; fila<=fin; fila++)
                {
                    matrixTablero[fila][col].set_Carretera('v');
                    lastCarreteraVertical.add(new Point(fila,col));//se añade al arrayList
                }
                nV++;
            }else{

                lastCarreteraHorizontal.removeAll(inic_finales);
                int i = rand.nextInt(lastCarreteraHorizontal.size()-1);
                int colH = lastCarreteraHorizontal.get(i).y;
                int filH = lastCarreteraHorizontal.get(i).x;
                int inicioV;
                int finV;
                do{
                    inicioV = rand.nextInt(1,filH);
                    finV = rand.nextInt(filH+1,tamanioTablero-2);
                }while (finV-inicioV<=3);

                if (finV == tamanioTablero - 1) finV--;

                //Guardo el inicio y el final de la carretera
                inic_finales.add(new Point(inicioV,colH));
                inic_finales.add(new Point(finV,colH));

                //Indexacion en el mapa
                for(int fila= inicioV; fila<=finV; fila++)
                {
                    matrixTablero[fila][colH].set_Carretera('v');
                    lastCarreteraVertical.add(new Point(fila,colH));//se añade al arrayList
                }
                nV++;
            }
            lastCarreteraHorizontal.clear();


            //Carretera Horizontal a traves de un punto anterior
            lastCarreteraVertical.removeAll(inic_finales);
            int i = rand.nextInt(lastCarreteraVertical.size());
            int colV = lastCarreteraVertical.get(i).y;
            int filV = lastCarreteraVertical.get(i).x;
            int inicio;
            int fin;

            do{
                inicio = rand.nextInt(1,colV);
                fin = rand.nextInt(colV+1,tamanioTablero-2);
            }while (fin-inicio<=3); //Me aseguro que no se cree una carretera demasasiado pqeueña

            inic_finales.add(new Point(filV,inicio));
            inic_finales.add(new Point(filV,fin));

            for(int col= inicio; col<=fin; col++){
                matrixTablero[filV][col].set_Carretera('h');
                lastCarreteraHorizontal.add(new Point(filV,col));

            }
            nH++;
            lastCarreteraVertical.clear();
        }



    }

*/


/*
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
*/

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

    public void limpiarPuntosNoIniciales(){

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

    }//despues de la generacion compruebo cada punto si ese punto es un giro o un cruce considero que no es un destino ni de inicio ni de final

    private int mirarAdyacentes(int fil, int col){

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



    public void imprimirIniciosyFinales(){
        for(int i = 0; i<iniciosYFinalesCarreteras.size();i++){
            int x= iniciosYFinalesCarreteras.get(i).x;
            int y= iniciosYFinalesCarreteras.get(i).y;

            System.out.println("Punto "+i+": ("+x+","+y+")");
        }

    }


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
}
