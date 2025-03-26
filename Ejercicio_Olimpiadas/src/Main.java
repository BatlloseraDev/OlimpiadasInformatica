public class Main {
    public static void main(String[] args) {


        //hacer menu de opciones
        boolean ejecutar = true;
        System.out.println("Bienvenido al programa de la simulación");

        while(ejecutar)
        {
            int opc = InputValidation.writeIntInput("Selecciona lo que desees realizar:\n(1).Iniciar nueva simulación\n(2).Salir","Por favor introduce un numero entero");
            switch(opc)
            {
                case 1:
                    game();
                    break;
                case 2:
                    ejecutar= false;
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        }
        System.out.println("Gracias por usar el programa");
    }

    public static void game(){

        int opc;
        int n_carreteras;
        boolean ejecutar=true;

        do {
            opc = InputValidation.writeIntInput("Introduce el tamaño del tablero: \n(1).Pequeño [20x20] \n(2).Mediano [30x30] \n(3).Grande [40x40] ","Por favor introduce un numero entero" );
        } while(opc < 1 || opc > 3);
        opc = (opc+1)*10; //me ahorro el crear el switch o una cadena de if

        Tablero tablero= Factoria.generarTablero(opc);
        System.out.println("Tablero creado vacio");

        do {
            opc= InputValidation.writeIntInput("Introducce un numero que sea par y positivo para el numero de carreteras: ","Por favor introduce un numero");

        }while (opc<=0 || opc%2!=0); //con esto ya siempre es par
        n_carreteras= opc;//una vez que genero una correcta lo guardo

        //test de generacion de carreteras
        tablero.generarCarreteras(n_carreteras);

        //tablero.imprimirIniciosyFinales();
        //tablero.imprimirCarreteras();
        //pedir vehiculos
        do{
            opc= InputValidation.writeIntInput("Introduce un numero de vehiculos a simular, no puede ser mayor al numero de carreteras: ","Por favor introduce un numero");
        }while(opc<=0 || opc>n_carreteras);

        tablero.imprimirTablero();
        System.out.println("Tablero con carreteras comprobadas");
        Factoria.generarVehiculosEnTablero(tablero,opc);

        while (ejecutar){

            int inGame = InputValidation.writeIntInput("Selecciona lo que desees realizar:\n" +
                    "(1).Iniciarlizar/Resetear simulacion\n" +
                    "(2).Avanzar la simulación\n" +
                    "(3).Salir","Por favor introduce un numero entero");

            switch (inGame){
                case 1://comienza la simulacion
                    tablero.inicializarVehiculos();
                    break;
                case 2: //pausar la simulacion
                    tablero.moverVehiculos();
                    break;
                case 3:
                    ejecutar= false;
                    break;
                default:
                    System.out.println("Opción no válida");
            }
            tablero.imprimirTablero();

        }



        //tablero.imprimirTablero();
        //tablero.imprimirVehiculos();


    }

}


