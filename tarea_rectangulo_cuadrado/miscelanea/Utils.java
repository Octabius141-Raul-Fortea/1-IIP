package miscelanea;

import static org.junit.Assert.fail;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Normalizer;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {

    public static final String sJunit = "\nJUNIT OUTPUT:\n";

    public static final double ASSERT_DELTA = 0.000001;

    private static Gson gson = new GsonBuilder().create();
    private static Gson gsonP = new GsonBuilder().setPrettyPrinting().create();

    public static String toJsonPretty(Object obj) {
        return gsonP.toJson(obj);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    private static Random generator = new Random(1000);

    public static void resetRandomGenerator(long seed) {
        generator = new Random(seed);
    }

    public static double valorRealRandomEnSeed(double a, double b) {

        double valorEn_0_1 = generator.nextDouble();
        double valorEn_0_bMenosa = valorEn_0_1 * (b - a);
        double valorEn_a_b = valorEn_0_bMenosa + a;
        double res = Math.round(valorEn_a_b * 100.0) / 100.0;
        return res;
    }

    /**
     * Devuelve un valor real aleatorio del intervalo [a, b[, usando Math.random().
     * Debe cumplirse que 0 <=a < b
     */
    public static double valorRealRandomEn(double a, double b) {

        double valorEn_0_1 = Math.random();
        double valorEn_0_bMenosa = valorEn_0_1 * (b - a);
        double valorEn_a_b = valorEn_0_bMenosa + a;
        double res = Math.round(valorEn_a_b * 100.0) / 100.0;
        return res;
    }

        /**
     * Devuelve un valor real aleatorio del intervalo [a, b[, usando Math.random().
     * Debe cumplirse que 0 <=a < b
     */
    public static double valorRealRandomNoDecRoundEn(double a, double b) {

        double valorEn_0_1 = Math.random();
        double valorEn_0_bMenosa = valorEn_0_1 * (b - a);
        double valorEn_a_b = valorEn_0_bMenosa + a;
        return valorEn_a_b;
    }
    /**
     * Devuelve un valor entero aleatorio del intervalo [a,b], usando Math.random().
     * Debe cumplirse que 0 <=a < b
     */
    public static int valorEnteroRandomEn(int a, int b) {

        double valorEn_0_1 = Math.random();
        double valorEn_0_bMenosAmasUno = valorEn_0_1 * (b - a + 1);
        double valorEn_a_bMas1 = valorEn_0_bMenosAmasUno + a;

        int valorEnteroEn_a_b = (int) valorEn_a_bMas1;

        return valorEnteroEn_a_b;
    }

    public static long valorLongRandomEn(long a, long b) {

        double valorEn_0_1 = Math.random();
        double valorEn_0_bMenosAmasUno = valorEn_0_1 * (b - a + 1);
        double valorEn_a_bMas1 = valorEn_0_bMenosAmasUno + a;

        long valorEnteroEn_a_b = (int) valorEn_a_bMas1;

        return valorEnteroEn_a_b;
    }

    public static String cleanString(String src) {

        String res = Utils.removeSpecialLines(src);

        res = cleanStringNoCase(src);

        return res;
    }

    public static String cleanStringNoCase(String src) {

        String res = Utils.removeSpecialLines(src);

        res = Normalizer.normalize(src, Normalizer.Form.NFD).replaceAll(" *", "").replaceAll("[^\\p{ASCII}]", "")
                .trim();

        res = removeSpecialLines(res);
        return res;

    }

    public static String removeExtraSpaces(String src) {

        String res = src.replaceAll(" +", " ").trim();

        // res = removeSpecialLines(res);
        return res;

    }

    public static String removeSpecialLines(String src) {

        src = src.replaceAll("\r", "");
        String[] lines = src.split("\n");

        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].trim();
            if (lines[i].matches(">.*")) {
                lines[i] = "";
            }
        }

        StringBuilder finalStringBuilder = new StringBuilder("");
        for (String s : lines) {
            if (!s.equals("")) {
                finalStringBuilder.append(s).append("\n");
            }
        }
        String finalString = finalStringBuilder.toString();

        return finalString;
    }

    public static String indent(String src, int level) {

        StringBuilder spacesBuilder = new StringBuilder("");
        for (int i = 0; i < level; i++) {
            spacesBuilder.append("    ");
        }
        String spaces = spacesBuilder.toString();

        String[] lines = src.split("\n");

        StringBuilder finalStringBuilder = new StringBuilder("");
        for (String s : lines) {
            finalStringBuilder.append(spaces).append(s).append("\n");

        }
        String finalString = finalStringBuilder.toString();

        return finalString;
    }

    private static String[] Beginning = { "Kr", "Ca", "Ra", "Mrok", "Cru", "Ray", "Bre", "Zed", "Drak", "Mor", "Jag",
            "Mer", "Jar", "Mjol", "Zork", "Mad", "Cry", "Zur", "Creo", "Azak", "Azur", "Rei", "Cro", "Mar", "Luk" };
    private static String[] Middle = { "air", "ir", "mi", "sor", "mee", "clo", "red", "cra", "ark", "arc", "miri",
            "lori", "cres", "mur", "zer", "marac", "zoir", "slamar", "salmar", "urak" };
    private static String[] End = { "d", "ed", "ark", "arc", "es", "er", "der", "tron", "med", "ure", "zur", "cred",
            "mur" };

    private static Random rand = new Random();

    public static String generateName() {

        return Beginning[rand.nextInt(Beginning.length)] + Middle[rand.nextInt(Middle.length)]
                + End[rand.nextInt(End.length)];

    }

    public static String exceptionDesc(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    public static void excepcionInesperada(Exception e, String s) {

        s += "ATENCIÓN! SE HA PRODUCIDO UNA EXCEPCIÓN INESPERADA\n";
        s += "LOS DATOS DE LA EXCEPCIÓN SON:\n";
        s += Utils.exceptionDesc(e);
        s += "\n\n";
        fail(s + Utils.sJunit);
    }
 
    public static String mainTestFailureMsg() {
        String msg = "";

        msg += "\n  ABRE EL SIGUIENTE FICHERO EN EL NAVEGADOR PARA VER LA CAUSA:\n";
        msg += "  " + Log.getLogDir() + "main.html\n";
        msg = Utils.addHeaderAndFooter(msg);

        return msg;
    }

    public static String mainTestFailureMsg(Log log) {
        String msg = "";
        msg += "\nLA SALIDA DE TU PROGRAMA NO ES LA QUE SE ESPERABA \n\n";

        msg += "\nREVISA LOS SIGUIENTES FICHEROS EN\n";
        msg += "LA CARPETA " + Log.getLogDir() + ":\n";
        msg += "   output-expected.txt (CONTIENE LA SALIDA ESPERADA) \n";
        msg += "   output-obtained.txt      (CONTIENE LA SALIDA DE TU PROGRAMA) \n";
        msg += "Y LOCALIZA LAS DIFERENCIAS ENTRE AMBOS \n\n";

        msg += "EL TEST HA UTILIZADO LOS DATOS DE ENTRADA CONTENIDOS EN EL FICHERO  \n";
        msg += "   input.txt \n";
        msg += "PUEDES UTILIZARLOS PARA REPRODUCIR LA EJECUCIÓN QUE HA PROVOCADO EL FALLO DEL TEST\n";

        msg = Utils.addHeaderAndFooter(msg);

        return msg;
    }

    public static String traceTestFailureMsg() {
        String msg = "";
        msg += "\nLA SECUENCIA DE LLAMADAS A MÉTODOS NO HA SIDO LA ESPERADA \n\n";

        msg += "\nREVISA LOS SIGUIENTES FICHEROS EN\n";
        msg += "LA CARPETA " + Log.getLogDir() + ":\n";
        msg += "   trace-expected.txt (CONTIENE LA SECUENCIA ESPERADA) \n";
        msg += "   trace-obtained.txt      (CONTIENE LA SECUENCIA DE TU PROGRAMA) \n";
        msg += "Y LOCALIZA LAS DIFERENCIAS ENTRE AMBOS \n\n";

        msg += "EL TEST HA UTILIZADO LOS DATOS DE ENTRADA CONTENIDOS EN EL FICHERO  \n";
        msg += "   input.txt \n";
        msg += "PUEDES UTILIZARLOS PARA REPRODUCIR LA EJECUCIÓN QUE HA PROVOCADO EL FALLO DEL TEST\n";

        msg = Utils.addHeaderAndFooter(msg);

        return msg;
    } 

    private static String line = "\n--------------------------------------------------------------------------\n";

    public static String addHeaderAndFooter(String msg) {

        return line + "\nEL TEST HA FALLADO\n" + line + msg + line + sJunit;

    }

    // public static String addLines(String msg) {

    // return line + msg + line;

    // }

}
