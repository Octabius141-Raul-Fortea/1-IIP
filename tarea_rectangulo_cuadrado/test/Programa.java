package test;

import java.util.Scanner;
import java.util.Locale;
/**
 * Compara el área de un rectángulo con el área de un cuadrado
 * @author (iip)
 * @version (2022)
 */
public class Programa {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        teclado.useLocale(Locale.US);

        System.out.println("> Introduce la base del rectángulo");
        double baseDelRectangulo = teclado.nextDouble();

        System.out.println("> Introduce la altura del rectángulo");
        double alturaDelRectangulo = teclado.nextDouble();

        System.out.println("> Introduce el lado del cuadrado");
        double ladoDelCuadrado = teclado.nextDouble();

        double areaDelRectangulo = baseDelRectangulo * alturaDelRectangulo;
        System.out.println("El área del rectángulo es " + areaDelRectangulo);

        double areaDelCuadrado = ladoDelCuadrado * ladoDelCuadrado;
        System.out.println("El área del cuadrado es " + areaDelCuadrado);

        if (areaDelRectangulo > areaDelCuadrado) {
            System.out.println("El área del rectángulo es mayor que el área del cuadrado");

        } else if (areaDelRectangulo < areaDelCuadrado) {
            System.out.println("El área del rectángulo es menor que el área del cuadrado");

        } else {
            System.out.println("El área del rectángulo es igual que el área del cuadrado");
        }
    }
}
