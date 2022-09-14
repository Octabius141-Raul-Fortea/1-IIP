 

import java.util.Scanner;
import java.util.Locale;

/**
 * Compara el área de un rectángulo con el área de un círculo
 * 
 * @author (iip)
 * @version (2022)
 *
 */
public class Programa {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        teclado.useLocale(Locale.US);

        System.out.println("> Introduce la base del rectángulo");
        double baseDelRectangulo = teclado.nextDouble();

        System.out.println("> Introduce la altura del rectángulo");
        double alturaDelRectangulo = teclado.nextDouble();

        System.out.println("> Introduce el radio del círculo");
        double radioDelCirculo = teclado.nextDouble();

        double areaDelRectangulo = baseDelRectangulo * alturaDelRectangulo;
        System.out.println("El área del rectángulo es " + areaDelRectangulo);

        double areaDelCirculo = 3.1416 * radioDelCirculo * radioDelCirculo;
        System.out.println("El área del círculo es " + areaDelCirculo);

        if (areaDelRectangulo > areaDelCirculo) {
            System.out.println("El área del rectángulo es mayor que el área del círculo");

        } else if (areaDelRectangulo < areaDelCirculo) {
            System.out.println("El área del rectángulo es menor que el área del círculo");

        } else {
            System.out.println("El área del rectángulo es igual que el área del círculo");
        }
    }
}
