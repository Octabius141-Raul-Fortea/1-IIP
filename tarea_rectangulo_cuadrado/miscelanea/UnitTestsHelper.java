package miscelanea;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author (IIP)
 * @version (2022) T es la clase a probar y O es la clase correcta
 */

public class UnitTestsHelper<T, O> {

    final static int REP = 10;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Class<?> T_Class;
    private Class<?> O_Class;

    private static String testedClassName = "";

    public HashMap<String, Field> tested_fields = new HashMap<String, Field>();
    public HashMap<String, Field> ok_fields = new HashMap<String, Field>();

    public static String getLogDir() {
        return Log.getLogDir();
    }

    public static String reFormat5(String s) {

        String newS = "";

        int nivelArray = 0;
        int nivelObjeto = 0;
        int nivelObjetoSimple = 0;
        int prevNivelArray = 0;
        int prevNivelObjeto = 0;
        int inicio = 0;
        int fin = 0;
        boolean enObjetoSimple = true;
        char currentChar;

        if (s.indexOf('[') == -1 || s.indexOf('\n') == -1) {
            return s;
        }



        for (int i = 0; i < s.length(); i++) {

            prevNivelArray = nivelArray;
            prevNivelObjeto = nivelObjeto;

            currentChar = s.charAt(i);

            switch (currentChar) {
                case '[':
                    nivelArray++;
                    break;
                case ']':
                    nivelArray--;
                    break;
                case '{':
                    nivelObjeto++;
                    break;
                case '}':
                    nivelObjeto--;
                    break;
                default:
            }

            if (nivelArray > prevNivelArray) {
                enObjetoSimple = false;
            } else if (nivelObjeto > prevNivelObjeto && nivelObjeto >= nivelArray && !enObjetoSimple) {
                nivelObjetoSimple = nivelObjeto;
                enObjetoSimple = true;
                inicio = i;
            } else if (nivelObjeto < prevNivelObjeto && nivelObjeto == nivelObjetoSimple - 1 && enObjetoSimple) {
                String sinCambios = s.substring(fin, inicio + 1);
                newS = newS + sinCambios;

                String fragmento = s.substring(inicio, i + 1);
                fragmento = fragmento.replaceAll("\\s+", "");
                fragmento = fragmento.replaceAll("\\n", "");
                fragmento = fragmento.replaceAll(",", ", ");

                newS = newS + fragmento;
                enObjetoSimple = false;
                fin = i + 1;
            }
        }

        newS = newS + s.substring(fin);


        inicio = newS.indexOf('[') + 2;       
        inicio = newS.indexOf(' ', inicio);
        fin = newS.indexOf(']', inicio);
        String lines[] = newS.substring(inicio, fin).split("\\n");
        int numLineas = lines.length;
        if (newS.charAt(fin -1) == ' ') {
            numLineas--;
        }
        String res = "";
        for (int k = 0; k < numLineas; k++) {
            String index = "      " + k;
            res += index.substring(index.length() - 6) + lines[k] + "\n";
        }
        res = newS.substring(0, inicio) + res + newS.substring(fin);


        return res;
    }

    
    public static String reFormat4(String s) {

        String newS = "";

        int nivelArray = 0;
        int nivelObjeto = 0;
        int nivelObjetoSimple = 0;
        int prevNivelArray = 0;
        int prevNivelObjeto = 0;
        int inicio = 0;
        int fin = 0;
        boolean enObjetoSimple = true;
        char currentChar;

        for (int i = 0; i < s.length(); i++) {

            prevNivelArray = nivelArray;
            prevNivelObjeto = nivelObjeto;

            currentChar = s.charAt(i);

            switch (currentChar) {
                case '[':
                    nivelArray++;
                    break;
                case ']':
                    nivelArray--;
                    break;
                case '{':
                    nivelObjeto++;
                    break;
                case '}':
                    nivelObjeto--;
                    break;
                default:
            }

            if (nivelArray > prevNivelArray) {
                enObjetoSimple = false;
            } else if (nivelObjeto > prevNivelObjeto && nivelObjeto >= nivelArray && !enObjetoSimple) {
                nivelObjetoSimple = nivelObjeto;
                enObjetoSimple = true;
                inicio = i;
            } else if (nivelObjeto < prevNivelObjeto && nivelObjeto == nivelObjetoSimple - 1 && enObjetoSimple) {
                String sinCambios = s.substring(fin, inicio + 1);
                newS = newS + sinCambios;

                String fragmento = s.substring(inicio, i + 1);
                fragmento = fragmento.replaceAll("\\s+", "");
                fragmento = fragmento.replaceAll("\\n", "");
                fragmento = fragmento.replaceAll(",", ", ");

                newS = newS + fragmento;
                enObjetoSimple = false;
                fin = i + 1;
            }
        }

        newS = newS + s.substring(fin);

        return newS;
    }

    public static String reFormat3(String s) { // deprecated, pendiente de integrar con reFormat4

        int posIni = 0;
        int posEnc = 0;

        int posIni2 = 0;

        Pattern pattern = Pattern.compile(",\n *null");
        Matcher matcher = pattern.matcher(s);

        String newS = "";
        do {
            posEnc = s.indexOf("null", posIni);
            if (posEnc == -1) {
                break;
            }
            newS += s.substring(posIni, posEnc);

            int numNulls = 1;
            posIni2 = posEnc + 4;

            do {
                String sub = s.substring(posIni2, posIni2 + 2);
                if (sub.equals(",\n")) {
                    matcher.find(posIni2);

                    if (posIni2 == matcher.start()) {
                        numNulls++;
                        posIni2 = matcher.end();
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            } while (true);

            if (numNulls == 1) {
                newS += "null";
            } else {
                newS += "null x " + numNulls;
            }
            posIni = posIni2;
        } while (true);

        newS += s.substring(posIni2);
        return s; // OJO, cambiado para devolver original
    }

    public String toStringT(T obj) {

        String json = gson.toJson(obj);
        return reFormat5(json);
    }

    public String toStringO(O obj) {

        String json = gson.toJson(obj);
        return reFormat5(json);
    }

    private boolean equivalent(T testedObj, O okOBj) throws IllegalArgumentException, IllegalAccessException {

        Field f = null;
        Field ok_f = null;

        for (String n : ok_fields.keySet()) {

            f = tested_fields.get(n);
            ok_f = ok_fields.get(n);

            int ok_mod = ok_f.getModifiers();

            // Solo comprobamos atributos de instancia

            if (!Modifier.isStatic(ok_mod)) {

                String g1 = gson.toJson(f.get(testedObj));
                String g2 = gson.toJson(ok_f.get(okOBj));

                // Quito la comparación con null para que puedan compararse
                // objetos con atributos igual a null
                // En su momento la puse por algún motivo, así que cautela

                // if (f.get(testedObj) == null ||

                if (!g1.equals(g2)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static String python3Path() {

        String path;
        File f;

        path = "c:/windows/py.exe";
        f = new File(path);
        if (f.exists()) {
            return path;
        }

        path = "/bin/python3";
        f = new File(path);
        if (f.exists()) {
            return path;
        }

        path = "/usr/local/bin/python3";
        f = new File(path);
        if (f.exists()) {
            return path;
        }

        return "";

    }

    private static void createHtmlDiffTable(String fileName1, String fileName2, String outFile, String textoIzquierdo,
            String textoDerecho) {

        if (python3Path().equals("")) {
            String content = "";

            try {
                FileOutputStream fout;
                fout = new FileOutputStream(outFile);

                content += "<table class=\"diff\" > <tr> <th class=\"diff_header\">";
                content += textoIzquierdo;

                content += "</th> <th class=\"diff_header\">";

                content += textoDerecho;

                content += "</th> </tr> <tr> <td>";

                List<String> lines;
                lines = Files.readAllLines(Paths.get(fileName1), StandardCharsets.UTF_8);

                for (String line : lines) {
                    content += line;
                }

                content += "</td> <td>";

                lines = Files.readAllLines(Paths.get(fileName2), StandardCharsets.UTF_8);

                for (String line : lines) {
                    content += line;
                }

                content += "</td> </tr> </table>";
                fout.write(content.getBytes());
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            String prg = "";

            prg += "import difflib\n";
            prg += "import os\n";

            prg += "fromfile = r'" + fileName1 + "'\n";
            prg += "tofile = r'" + fileName2 + "'\n";

            prg += "fromlines = open(fromfile, 'r', encoding='UTF-8').readlines()\n";
            prg += "tolines = open(tofile, 'r', encoding='UTF-8').readlines()\n";
            prg += "diff = difflib.HtmlDiff().make_table(fromlines,tolines,'" + textoIzquierdo + "','" + textoDerecho
                    + "')\n";
            prg += "path = r'" + outFile + "'\n";
            prg += "f = open(path, 'w', encoding='UTF-8')\n";
            prg += "f.write(diff)\n";
            prg += "f.close()\n";

            String fileName = Log.getLogDir() + "prog.py";
            FileOutputStream f;
            try {
                f = new FileOutputStream(fileName);
                f.write(prg.getBytes());
                f.close();

                ProcessBuilder pb = new ProcessBuilder(python3Path(), fileName);
                Process p = pb.start();
                int exitCode = p.waitFor();
                String s = Integer.toString(exitCode);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String htmlHeader(String title) {
        String contents = "";
        contents += "<!DOCTYPE html>" + "\n";
        contents += "<html>" + "\n";
        contents += "<head>" + "\n";
        contents += "<title>" + title + "</title>" + "\n";
        contents += "<style type=\"text/css\">" + "\n";
        contents += "table.diff {font-family:Courier; border:medium;}" + "\n";
        contents += ".diff_header {background-color:#e0e0e0}" + "\n";
        contents += "td.diff_header {text-align:right}" + "\n";
        contents += ".diff_next {background-color:#c0c0c0}" + "\n";
        contents += ".diff_add {background-color:#ffff77}" + "\n";
        contents += ".diff_chg {background-color:#ffff77}" + "\n";
        contents += ".diff_sub {background-color:#ffff77}" + "\n";
        contents += "</style>" + "\n";
        contents += "</head>" + "\n";
        contents += "<body>" + "\n";
        return contents;
    }

    private static String normalizedMethodName(String m) {
        return m.replace("()", "").replace("(", "-").replace(")", "").replace(",", "-").replace(" ", "");
    }

    public UnitTestsHelper(T dT, O dO) {

        T_Class = dT.getClass();
        O_Class = dO.getClass();

        testedClassName = T_Class.getSimpleName();

        Field[] allOkFields = O_Class.getDeclaredFields();

        for (Field ok_field : allOkFields) {
            String n = ok_field.getName();
            ok_fields.put(n, ok_field);
            ok_field.setAccessible(true);
            Field tested_field;
            try {
                tested_field = T_Class.getDeclaredField(ok_field.getName());
                tested_fields.put(n, tested_field);
                tested_field.setAccessible(true);
            } catch (NoSuchFieldException | SecurityException e) {
                // e.printStackTrace();
            }
        }

    }

    public void setUp() {

        Field f = null;
        Field ok_f = null;

        for (String n : ok_fields.keySet()) {
            try {

                String msg;

                f = tested_fields.get(n);
                msg = Utils.addHeaderAndFooter("EL ATRIBUTO " + n + " NO EXISTE");
                assertNotNull(msg, f);

                ok_f = ok_fields.get(n);

                msg = Utils.addHeaderAndFooter("EL TIPO DEL ATRIBUTO " + n + " NO ES EL CORRECTO\n");
                assertTrue(msg, f.getType() == ok_f.getType());

            } catch (IllegalArgumentException e) {
                String msg = "";
                Utils.excepcionInesperada(e, msg);
            }
        }

    }

    public void testModificadoresAtributos() {

        Field f = null;
        Field ok_f = null;

        int mod = 0;
        int ok_mod = 0;

        for (String n : ok_fields.keySet()) {
            try {

                String msg;

                f = tested_fields.get(n);
                ok_f = ok_fields.get(n);

                mod = f.getModifiers();
                ok_mod = ok_f.getModifiers();

                if (!Modifier.isStatic(ok_mod)) {
                    msg = Utils.addHeaderAndFooter("EL ATRIBUTO " + n + " NO PUEDE SER 'static'\n");
                    assertTrue(msg, !Modifier.isStatic(mod));
                }

                if (Modifier.isStatic(ok_mod)) {
                    msg = Utils.addHeaderAndFooter("EL ATRIBUTO " + n + " DEBE SER 'static'\n");
                    assertTrue(msg, Modifier.isStatic(mod));

                    boolean res = f.get(null).equals(ok_f.get(null));

                    msg = Utils.addHeaderAndFooter("EL VALOR INICIAL DEL ATRIBUTO " + n + " NO ES CORRECTO'\n");
                    assertTrue(msg, res);

                }

                if (!Modifier.isFinal(ok_mod)) {
                    msg = Utils.addHeaderAndFooter("EL ATRIBUTO " + n + " NO PUEDE SER 'final'\n");
                    assertTrue(msg, !Modifier.isFinal(mod));
                }

                if (Modifier.isFinal(ok_mod)) {
                    msg = Utils.addHeaderAndFooter("EL ATRIBUTO " + n + " DEBE SER 'final'\n");
                    assertTrue(msg, Modifier.isFinal(mod));
                }

                if (Modifier.isPublic(ok_mod)) {
                    msg = Utils.addHeaderAndFooter("EL ATRIBUTO " + n + " DEBE SER 'public'\n");
                    assertTrue(msg, Modifier.isPublic(mod));
                }

                if (Modifier.isPrivate(ok_mod)) {
                    msg = Utils.addHeaderAndFooter("EL ATRIBUTO " + n + " DEBE SER 'private'\n");
                    assertTrue(msg, Modifier.isPrivate(mod));
                }

            } catch (IllegalArgumentException | IllegalAccessException e) {
                String msg = "";
                Utils.excepcionInesperada(e, msg);
            }
        }

    }

    public void checkMethodExists(String methodName) {

        Method[] allMethods = T_Class.getDeclaredMethods();
        for (Method m : allMethods) {
            if (methodName.equals(m.getName())) {
                return;
            }
        }

        Constructor[] allConstructors = T_Class.getDeclaredConstructors();
        for (Constructor m : allConstructors) {
            if (methodName.equals(m.getName())) {
                return;
            }
        }

        String s = "NO SE LOCALIZA EL MÉTODO " + methodName;
        s += Utils.addHeaderAndFooter(s);
        fail(s);
    }

    public void testModificadoresClaseMetodos() {

        String s;

        int mod = 0;
        int ok_mod = 0;

        mod = T_Class.getModifiers();

        s = Utils.addHeaderAndFooter("LA CLASE DEBE SER DECLARADA 'public'\n");
        assertTrue(s, Modifier.isPublic(mod));

        Method[] allMethods = T_Class.getDeclaredMethods();

        for (Method m : allMethods) {

            mod = m.getModifiers();

            Class<?>[] cArg = m.getParameterTypes();

            Class<?>[] cArg2 = new Class<?>[cArg.length];

            int i = 0;
            for (Class<?> c : cArg) {
                if (c.equals(T_Class)) {
                    cArg2[i] = O_Class;
                } else {
                    cArg2[i] = cArg[i];
                }
            }

            String methodName = m.getName();

            Method ok_m;
            try {
                ok_m = O_Class.getDeclaredMethod(methodName, cArg2);
            } catch (NoSuchMethodException | SecurityException e) {
                // El método del alumno no es parte de la especificación,
                break;
            }

            ok_mod = ok_m.getModifiers();

            if (Modifier.isPublic(ok_mod)) {
                s = Utils.addHeaderAndFooter("EL MÉTODO " + m.getName() + " DEBE SER 'public'\n");
                assertTrue(s, Modifier.isPublic(mod));
            }

            if (Modifier.isPrivate(ok_mod)) {
                s = Utils.addHeaderAndFooter("EL MÉTODO " + m.getName() + " DEBE SER 'private'\n");
                assertTrue(s, Modifier.isPrivate(mod));
            }

            if (!Modifier.isStatic(ok_mod)) {
                s = Utils.addHeaderAndFooter("EL MÉTODO " + m.getName() + " NO PUEDE SER 'static'\n");
                assertTrue(s, !Modifier.isStatic(mod));
            }

            if (Modifier.isStatic(ok_mod)) {
                s = Utils.addHeaderAndFooter("EL MÉTODO " + m.getName() + " DEBE SER 'static'\n");
                assertTrue(s, Modifier.isStatic(mod));
            }

        }

        for (Constructor<?> c : T_Class.getDeclaredConstructors()) {
            s = Utils.addHeaderAndFooter("EL CONSTRUCTOR " + c + " DEBE SER 'public'\n");
            assertTrue(s, Modifier.isPublic(c.getModifiers()));
        }
    }

    public void generalTest_Constructor(String methodCall, String arguments, O ok_obj, T tested_obj, Exception ok_ex,
            Exception tested_ex) {

        String s = "";
        boolean equiv = false;

        String methodNormalized = normalizedMethodName(methodCall);

        Log.initMethodLog(methodNormalized);

        try {
            FileOutputStream f;
            f = new FileOutputStream(getLogDir() + "method-call.txt");
            f.write(methodCall.getBytes());
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream f;
            f = new FileOutputStream(getLogDir() + "arguments.txt");
            f.write(arguments.getBytes());
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ok_ex != null) {
            // Esto no debería haber sucedido, algo está mal implementado
            // en esta clase o en la solución correcta

            s = Utils.addHeaderAndFooter(s);
            Utils.excepcionInesperada(ok_ex, s);
        } else if (tested_ex != null) {

            s += "ATENCIÓN! SE HA PRODUCIDO UNA EXCEPCIÓN. SUS DATOS SON:\n";
            s += Utils.exceptionDesc(tested_ex);
            s += "\n\n";
            s = Utils.addHeaderAndFooter(s);
            fail(s); // Aquí termina el test
        }

        try {
            equiv = equivalent(tested_obj, ok_obj);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        String sThisOK = toStringO(ok_obj) + "\n";
        String sThisTested = toStringT(tested_obj) + "\n";

        try {
            FileOutputStream f;
            f = new FileOutputStream(getLogDir() + "this-expected.txt");
            f.write(sThisOK.getBytes());
            f.close();
            f = new FileOutputStream(getLogDir() + "this-obtained.txt");
            f.write(sThisTested.getBytes());
            f.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!equiv) {
            String reportFile = Log.getBaseLogDir() + methodNormalized + ".html";

            try {

                String contents = htmlHeader(methodCall);

                contents += "<h1>El test ha fallado</h1>" + "\n";
                contents += "<p>Para realizar la prueba, se ha creado un nuevo objeto de la clase ";
                contents += testedClassName;
                contents += ", ejecutado:</p>" + "\n";

                contents += "<pre>" + "\n";

                contents += "new " + methodCall;
                contents += "</pre>" + "\n";

                if (!arguments.equals("")) {
                    contents += "<p>con los argumentos:</p>" + "\n";

                    contents += "<pre>" + "\n";
                    contents += arguments;
                    contents += "</pre>" + "\n";
                }

                contents += "<p>El test ha fallado porque el objeto creado no es igual al esperado, tal como puedes comprobar en la siguiente tabla:</p>"
                        + "\n";

                String logDir = Log.getLogDir();

                createHtmlDiffTable(logDir + "this-obtained.txt", logDir + "this-expected.txt", logDir + "diff.html",
                        "Objeto creado en tu programa", "Objeto esperado");

                contents += new String(Files.readAllBytes(Paths.get(logDir + "diff.html")));

                contents += "</body>" + "\n";
                contents += "</html>" + "\n";

                FileOutputStream f;
                f = new FileOutputStream(reportFile);
                f.write(contents.getBytes());

                f.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        s = "\n  ABRE EL SIGUIENTE FICHERO EN EL NAVEGADOR PARA VER LA CAUSA:\n";
        s += "  " + Log.getBaseLogDir() + methodNormalized + ".html\n";
        s = Utils.addHeaderAndFooter(s);

        assertTrue(s, equiv); // Si falla la prueba el test termina aquí

    }

    public void generalTest_Method(String methodCall, String arguments, O ok_obj, T tested_obj_before,
            T tested_obj_after, String ok_res, String tested_res, Exception ok_ex, Exception tested_ex) {

        String s = "";
        boolean equiv = false;

        String methodNormalized = normalizedMethodName(methodCall);

        Log.initMethodLog(methodNormalized);

        try {
            FileOutputStream f;
            f = new FileOutputStream(getLogDir() + "method-call.txt");
            f.write(methodCall.getBytes());
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream f;
            f = new FileOutputStream(getLogDir() + "arguments.txt");
            f.write(arguments.getBytes());
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream f;
            f = new FileOutputStream(getLogDir() + "this-before.txt");
            f.write(toStringT(tested_obj_before).getBytes());
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ok_ex != null) {
            // Esto no debería haber sucedido, algo está mal implementado
            // en esta clase o en la solución correcta

            s = Utils.addHeaderAndFooter(s);
            Utils.excepcionInesperada(ok_ex, s);
        } else if (tested_ex != null) {

            if (tested_ex.getClass().getSimpleName().equals("NoSuchMethodError")) {
                s += "NO SE HA ENCONTRADO EL MÉTODO:\n";
                s += methodCall;

            } else {
                s += "ATENCIÓN! SE HA PRODUCIDO UNA EXCEPCIÓN. SUS DATOS SON:\n";
                s += Utils.exceptionDesc(tested_ex);
                s += "\n\n";
            }

            s = Utils.addHeaderAndFooter(s);
            fail(s); // Aquí termina el test
        }

        try {
            equiv = equivalent(tested_obj_after, ok_obj);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream f;
            f = new FileOutputStream(getLogDir() + "this-expected.txt");
            f.write(toStringO(ok_obj).getBytes());
            f.close();
            f = new FileOutputStream(getLogDir() + "this-obtained.txt");
            f.write(toStringT(tested_obj_after).getBytes());
            f.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        String contentsPrev = htmlHeader(methodCall);
        contentsPrev += "<h1>El test ha fallado</h1>" + "\n";
        contentsPrev += "<p>Para realizar la prueba, se ha preparado un objeto llamado \"x\" de la clase "
                + T_Class.getSimpleName() + ", con los siguientes valores para sus atributos:</p>" + "\n";

        contentsPrev += "<pre>" + "\n";
        contentsPrev += toStringT(tested_obj_before);
        contentsPrev += "</pre>" + "\n";

        contentsPrev += "<p>y a continuación, se ha ejecutado el método:</p>" + "\n";

        contentsPrev += "<pre>" + "\n";

        String logDir = Log.getLogDir();

        if (!equiv) {
            String reportFile = Log.getBaseLogDir() + methodNormalized + ".html";

            try {

                String contents = contentsPrev;
                contents += "x." + methodCall;
                contents += "</pre>" + "\n";

                if (!arguments.equals("")) {
                    contents += "<p>con los argumentos:</p>" + "\n";

                    contents += "<pre>" + "\n";
                    contents += arguments;
                    contents += "</pre>" + "\n";
                }

                contents += "<p>El test ha fallado porque algún atributo del objeto \"x\" no tiene el valor esperado, tal como puedes comprobar en la siguiente tabla:</p>"
                        + "\n";

                createHtmlDiffTable(logDir + "this-obtained.txt", logDir + "this-expected.txt", logDir + "diff.html",
                        "Estado final de x en tu programa", "Estado final de x esperado");

                contents += new String(Files.readAllBytes(Paths.get(logDir + "diff.html")));

                contents += "</body>" + "\n";
                contents += "</html>" + "\n";

                FileOutputStream f;
                f = new FileOutputStream(reportFile);
                f.write(contents.getBytes());

                f.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        s = "\n  ABRE EL SIGUIENTE FICHERO EN EL NAVEGADOR PARA VER LA CAUSA:\n";
        s += "  " + Log.getBaseLogDir() + methodNormalized + ".html\n";
        s = Utils.addHeaderAndFooter(s);

        assertTrue(s, equiv); // Si falla la prueba el test termina aquí

        try {
            String ok_resFormatted = reFormat5(ok_res);
            String tested_resFormatted = reFormat5(tested_res);

            FileOutputStream f;
            f = new FileOutputStream(getLogDir() + "result-expected.txt");
            f.write(ok_resFormatted.getBytes());
            f.close();
            f = new FileOutputStream(getLogDir() + "result-obtained.txt");
            f.write(tested_resFormatted.getBytes());
            f.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!ok_res.equals(tested_res)) {
            String reportFile = Log.getBaseLogDir() + methodNormalized + ".html";

            try {

                String contents = contentsPrev;

                contents += "res = x." + methodCall;
                contents += "</pre>" + "\n";

                if (!arguments.equals("")) {
                    contents += "<p>con los argumentos:</p>" + "\n";

                    contents += "<pre>" + "\n";
                    contents += arguments;
                    contents += "</pre>" + "\n";
                }

                contents += "<p>El test ha fallado porque el valor que ha devuelto el método no es el esperado, tal como puedes comprobar en la siguiente tabla:</p>"
                        + "\n";

                createHtmlDiffTable(logDir + "result-obtained.txt", logDir + "result-expected.txt",
                        logDir + "diff.html", "Valor de res en tu programa", "Valor de res esperado");

                contents += new String(Files.readAllBytes(Paths.get(logDir + "diff.html")));

                contents += "</body>" + "\n";
                contents += "</html>" + "\n";

                FileOutputStream f;
                f = new FileOutputStream(reportFile);
                f.write(contents.getBytes());

                f.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        s = "\n  ABRE EL SIGUIENTE FICHERO EN EL NAVEGADOR PARA VER LA CAUSA:\n";
        s += "  " + Log.getBaseLogDir() + methodNormalized + ".html\n";
        s = Utils.addHeaderAndFooter(s);

        assertTrue(s, ok_res.equals(tested_res)); // Si falla la prueba el test termina aquí

        // assertEquals(s, ok_res, tested_res);
    }

    public static void generalTest_StaticMethod(String methodCall, String arguments, String ok_res, String tested_res,
            Exception ok_ex, Exception tested_ex, String targetParamName) {

        String sPrev = "";
        String s = "";

        String methodNormalized = normalizedMethodName(methodCall);

        Log.initMethodLog(methodNormalized);

        try {
            FileOutputStream f;
            f = new FileOutputStream(getLogDir() + "method-call.txt");
            f.write(methodCall.getBytes());
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream f;
            f = new FileOutputStream(getLogDir() + "arguments.txt");
            f.write(arguments.getBytes());
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ok_ex != null) {
            // Esto no debería haber sucedido, algo está mal implementado
            // en esta clase o en la solución correcta

            sPrev = Utils.addHeaderAndFooter(sPrev);
            Utils.excepcionInesperada(ok_ex, sPrev);
        } else if (tested_ex != null) {

            s = sPrev;
            s += "ATENCIÓN! SE HA PRODUCIDO UNA EXCEPCIÓN. SUS DATOS SON:\n";
            s += Utils.exceptionDesc(tested_ex);
            s += "\n\n";
            s = Utils.addHeaderAndFooter(s);
            fail(s); // Aquí termina el test
        }

        try {
            String ok_resFormatted = reFormat5(ok_res);
            String tested_resFormatted = reFormat5(tested_res);

            FileOutputStream f;
            f = new FileOutputStream(getLogDir() + "result-expected.txt");
            f.write(ok_resFormatted.getBytes());
            f.close();
            f = new FileOutputStream(getLogDir() + "result-obtained.txt");
            f.write(tested_resFormatted.getBytes());
            f.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!ok_res.equals(tested_res)) {
            String reportFile = Log.getBaseLogDir() + methodNormalized + ".html";

            try {

                String contents = htmlHeader(methodCall);

                contents += "<h1>El test ha fallado</h1>" + "\n";
                contents += "<p>Para realizar la prueba, se ha ejecutado el método:</p>" + "\n";

                contents += "<pre>" + "\n";

                if (targetParamName.equals("")) {
                    contents += "res = " +  methodCall;
                } else {
                    contents +=  methodCall;

                }

                contents += "</pre>" + "\n";

                if (!arguments.equals("")) {
                    contents += "<p>con los argumentos:</p>" + "\n";

                    contents += "<pre>" + "\n";
                    contents += arguments;
                    contents += "</pre>" + "\n";
                }

                String detail;
                if (targetParamName.equals("")) {
                    detail = "que ha devuelvo el método";
                } else {
                    detail = "de " + targetParamName;
                }
                contents += "<p>El test ha fallado porque el valor " + detail + " no es el esperado, tal como puedes comprobar en la siguiente tabla:</p>"
                        + "\n";

                String logDir = Log.getLogDir();

                createHtmlDiffTable(logDir + "result-obtained.txt", logDir + "result-expected.txt",
                        logDir + "diff.html", "Valor de res en tu programa", "Valor de res esperado");

                contents += new String(Files.readAllBytes(Paths.get(logDir + "diff.html")));

                contents += "</body>" + "\n";
                contents += "</html>" + "\n";

                FileOutputStream f;
                f = new FileOutputStream(reportFile);
                f.write(contents.getBytes());

                f.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        s = "\n  ABRE EL SIGUIENTE FICHERO EN EL NAVEGADOR PARA VER LA CAUSA:\n";
        s += "  " + Log.getBaseLogDir() + methodNormalized + ".html\n";
        s = Utils.addHeaderAndFooter(s);

        assertTrue(s, ok_res.equals(tested_res)); // Si falla la prueba el test termina aquí

        // assertEquals(s, ok_res, tested_res);
    }

    // private static Log log;
    private static Redir redir;
    private static String testedClass;
    private static String okClass;

    public static void setUpTestMain(String tC, String oC) {
        redir = new Redir();
        redir.saveInOutErr();
        testedClass = tC;
        okClass = oC;
    }

    public static void tearDownTestMain() {
        redir.restoreInOutErr();
        Log.close();
    }

    public static void main_test_internal(String inData) {

        Log.deleteBaseLogDir();
        Log.intMainLog("main");

        // Ejecución correcta
        String args[] = new String[0];
        redir.doOKRedirection(inData);
        Log.setCurrentTraceExpected();

        int seed = Utils.valorEnteroRandomEn(0, 10000);

        try {
            Utils.resetRandomGenerator(seed);
            Class<?> cls = Class.forName(okClass);
            Method meth = cls.getMethod("main", String[].class);
            meth.invoke(null, (Object) args);
        } catch (NoSuchMethodException | ClassNotFoundException | SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            String s = "";
            s += "ATENCIÓN! SE HA PRODUCIDO UNA EXCEPCIÓN. SUS DATOS SON:\n";
            s += Utils.exceptionDesc(e);
            s += "\n\n";
            s = Utils.addHeaderAndFooter(s);
            fail(s);
        }

        // Ejecución a comprobar
        redir.doRedirection(inData);
        Log.setCurrentTraceWas();

        try {
            Utils.resetRandomGenerator(seed);
            Class<?> cls = Class.forName(testedClass);
            Method meth = cls.getMethod("main", String[].class);
            meth.invoke(null, (Object) args);
        } catch (NoSuchMethodException | ClassNotFoundException | SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {

            e.printStackTrace();
        }

        // Test

        Log.eoWrite(redir.outStringOKraw());
        Log.woWrite(redir.outStringRaw());
        Log.wiWrite(inData);

        String logDir = Log.getLogDir();

        if (!(redir.outStringOKclean()).equals(redir.outStringClean())) {
            String reportFile = Log.getBaseLogDir() + "main.html";

            try {

                String contents = htmlHeader("main()");
                contents += "<h1>El test ha fallado</h1>" + "\n";
                contents += "<p>La prueba ha consistido en ejecutar tu programa con los siguientes datos de entrada:</p>"
                        + "\n";
                contents += "<pre>" + "\n";

                contents += new String(Files.readAllBytes(Paths.get(logDir + "input.txt")));

                contents += "</pre>" + "\n";

                contents += "<p>El test ha fallado porque la salida de tu programa no ha sido igual a la salida que se esperaba, como puedes comprobar en la siguiente tabla:</p>"
                        + "\n";

                createHtmlDiffTable(logDir + "output-obtained.txt", logDir + "output-expected.txt",
                        logDir + "diff.html", "Salida de tu programa", "Salida esperada");

                contents += new String(Files.readAllBytes(Paths.get(logDir + "diff.html")));

                contents += "<em>Aunque en la tabla aparezcan como diferencias, el test no tiene en cuenta acentos, espacios en blanco, líneas en blanco, ni líneas que comiencen por &gt;</em>";

                contents += "</body>" + "\n";
                contents += "</html>" + "\n";

                FileOutputStream f;
                f = new FileOutputStream(reportFile);
                f.write(contents.getBytes());

                f.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        String msg = "\n  ABRE EL SIGUIENTE FICHERO EN EL NAVEGADOR PARA VER LA CAUSA:\n";
        msg += "  " + Log.getBaseLogDir() + "main.html\n";
        msg = Utils.addHeaderAndFooter(msg);

        assertTrue(msg, (redir.outStringOKclean()).equals(redir.outStringClean()));

        if (!Log.tracesAreSimilar()) {
            String reportFile = Log.getBaseLogDir() + "main.html";

            try {

                String contents = htmlHeader("main()");
                contents += "<h1>El test ha fallado</h1>" + "\n";
                contents += "<p>La prueba ha consistido en ejecutar tu programa con los siguientes datos de entrada:</p>"
                        + "\n";
                contents += "<pre>" + "\n";

                contents += new String(Files.readAllBytes(Paths.get(logDir + "input.txt")));

                contents += "</pre>" + "\n";

                contents += "<p>Tu programa ha funcionado correctamente, pero no ha utilizado correctamente todos los métodos que se requería en el enunciado de la tarea</p>";
                contents += "<p>En la columna de la izquierda puedes ver qué métodos ha llamado tu programa y en la de la derecha qué métodos debería haber llamado</p>";

                createHtmlDiffTable(logDir + "trace-obtained.txt", logDir + "trace-expected.txt", logDir + "diff.html",
                        "Traza de tu programa", "Traza esperada");

                contents += new String(Files.readAllBytes(Paths.get(logDir + "diff.html")));

                contents += "<em>Este tipo de prueba es experimental, pide ayuda a tu profesor/a para interpretar la tabla anterior</em>";

                contents += "</body>" + "\n";
                contents += "</html>" + "\n";

                FileOutputStream f;
                f = new FileOutputStream(reportFile);
                f.write(contents.getBytes());

                f.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            fail(msg);
        }

    }

    public T clonDe(O origen) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(origen);
        T destino = (T) gson.fromJson(jsonString, T_Class);
        return destino;
    }

}
