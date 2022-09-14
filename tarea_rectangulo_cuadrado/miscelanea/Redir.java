package miscelanea;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class Redir {

    private PrintStream originalOut;
    private PrintStream originalErr;
    private InputStream originalIn;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private ByteArrayOutputStream outOKContent;
    private ByteArrayOutputStream errOKContent;


    public void saveInOutErr() {

        originalOut = System.out;
        originalErr = System.err;
        originalIn = System.in;
    }

    public void restoreInOutErr() {

        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
    }

    public void doRedirection(String inData) {
        // Redirección de salida estándar

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Redirección de salida de error

        errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        // Redirección de entrada

        System.setIn(new ByteArrayInputStream(inData.getBytes()));
    }

    public void doOKRedirection(String inData) {
        // Redirección de salida estándar

        outOKContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outOKContent));

        // Redirección de salida de error

        errOKContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errOKContent));

        // Redirección de entrada

        System.setIn(new ByteArrayInputStream(inData.getBytes()));
    }


    public String outStringOKraw() {
        // return Utils.removeSpecialLines(outOKContent.toString());
        return outOKContent.toString();
    }

    public String outStringOKclean() {
        return Utils.cleanString(outStringOKraw());
    }

    public String errStringOKraw() {
        return Utils.removeSpecialLines(errOKContent.toString());
    }

    public String errStringOKclean() {
        return Utils.cleanString(errStringOKraw());
    }

    public String outStringRaw() {
        // return Utils.removeSpecialLines(outContent.toString());
        return outContent.toString();
    }

    public String outStringClean() {
        return Utils.cleanString(outStringRaw());
    }

    public String errStringRaw() {
        return Utils.removeSpecialLines(errContent.toString());
    }

    public String errStringClean() {
        return Utils.cleanString(errStringRaw());
    }


 
}