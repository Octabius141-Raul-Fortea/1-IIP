 

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import miscelanea.UnitTestsHelper;
import miscelanea.Utils;

public class UnitTests {

    public UnitTests() {
        // Nada que cambiar aquí
    }

    @Before
    public void setUp() {

        // Pasa como argumentos la clase con el metodo main a probar y la clase con el
        // método main correcto
        UnitTestsHelper.setUpTestMain("Programa",
                "test.Programa");
    }

    @After
    public void tearDown() {
        // Nada que cambiar aquí
        UnitTestsHelper.tearDownTestMain();
    }

    private static String inData(double bR, double aR, double lC) {
        // Construye un string con los datos de entrada por teclado que se pasará a la
        // ejecución de main
        String s;
        s = Double.toString(bR) + "\n" + Double.toString(aR) + "\n" + Double.toString(lC);
        return s;
    }

    @Test
    public void mainTest() {

        // Prepara los valores del test y realiza la prueba llamando a
        // UnitTestsHelper.main_test_internal(inData(...))
        // Este método puede llamarse tantas veces como se quiera, para así poder
        // ejecutar una batería de pruebas, cambiando por ejemplo los valores de entrada


        int bR;
        int aR;
        int lC;

        bR = Utils.valorEnteroRandomEn(1, 20);
        aR = Utils.valorEnteroRandomEn(1, 20);
        lC = Utils.valorEnteroRandomEn(50, 100);
        UnitTestsHelper.main_test_internal(inData(bR, aR, lC));

        bR = Utils.valorEnteroRandomEn(50, 100);
        aR = Utils.valorEnteroRandomEn(50, 100);
        lC = Utils.valorEnteroRandomEn(1, 20);
        UnitTestsHelper.main_test_internal(inData(bR, aR, lC));

        bR = 100;
        aR = 100;
        lC = 100;

        UnitTestsHelper.main_test_internal(inData(bR, aR, lC));
    }
}