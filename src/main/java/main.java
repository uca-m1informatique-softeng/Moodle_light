import java.util.Scanner;


/**
 * boocle principale communication
 */
public class main {
    public static void main(String[] args) {
        boolean run = true;
        while(run){
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Choice :\n " +
                    "0 nam : add module \n " +
                    "1 nam text : create Text\n " +
                    "2 nam text : create questionaire" );
            String request = myObj.nextLine();
            System.out.println("request is: " + request);

        }
    }
}
