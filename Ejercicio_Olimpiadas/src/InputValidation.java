import java.util.Scanner;

public class InputValidation {

    public static int writeIntInput(String message, String errorMessage){ //me dio el venazo de hacer esto en ingles

        Scanner scanner = new Scanner(System.in);
        Integer value;

        do{
            System.out.println(message);
            value= validateIntInput(scanner.nextLine());
            if(value==null){
                System.out.println(errorMessage);
            }

        }while(value==null);

        return value;
    }


    public static int validateIntInput(String input) {

        Integer valueToReturn;
        try {
            valueToReturn = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            valueToReturn = null;
        }
        return valueToReturn;
    }

}
