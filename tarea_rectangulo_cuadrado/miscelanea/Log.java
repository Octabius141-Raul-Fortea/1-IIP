package miscelanea;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

public class Log {

    private static FileOutputStream eo;
    private static FileOutputStream wo;
    private static FileOutputStream et;
    private static FileOutputStream wt;
    private static FileOutputStream wi;

    private static ArrayList<String> currentList;
    private static FileOutputStream currentTrace;

    private static ArrayList<String> etList = new ArrayList<String>();
    private static ArrayList<String> wtList = new ArrayList<String>();

    private static String singleLogDirName;

    private static final String BASE_LOG_DIR = System.getProperty("user.home") + File.separator + "iiplog"
            + File.separator;

    public static String getLogDir() {
        return BASE_LOG_DIR + "data" + File.separator + singleLogDirName + File.separator;
    }

    public static String getBaseLogDir() {
        return BASE_LOG_DIR;
    }

    public static void deleteBaseLogDir() {

        Path pathToBeDeleted = Path.of(BASE_LOG_DIR);

        try {
            Files.walk(pathToBeDeleted).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File dir = new File(BASE_LOG_DIR);
        dir.mkdir();
    }

    public static void intMainLog(String subfolder) {

        try {
            singleLogDirName = subfolder;

            String logDirName = getLogDir();
            File dir = new File(logDirName);
            dir.mkdirs();

            eo = new FileOutputStream(logDirName + "output-expected.txt");
            wo = new FileOutputStream(logDirName + "output-obtained.txt");
            et = new FileOutputStream(logDirName + "trace-expected.txt");
            wt = new FileOutputStream(logDirName + "trace-obtained.txt");
            wi = new FileOutputStream(logDirName + "input.txt");

            currentTrace = et;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void initMethodLog(String subfolder) {
        singleLogDirName = subfolder;

        String logDirName = getLogDir();
        File dir = new File(logDirName);
        dir.mkdirs();
        String dummy = "";
    }


    public static void close() {
        try {
            eo.close();
            wo.close();
            et.close();
            wt.close();
            wi.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void eoWrite(String s) {
        try {
            eo.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void woWrite(String s) {
        try {
            wo.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] NL = "\n".getBytes();

    public static void tracePrintln(String s) {

        if (currentTrace == null) {
            return;
        }
        try {
            currentList.add(s);
            currentTrace.write(s.getBytes());
            currentTrace.write(NL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void tracePrintln() {
        if (currentTrace == null) {
            return;
        }

        try {
            currentTrace.write(NL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void wiWrite(String s) {
        try {
            wi.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setCurrentTraceExpected() {
        currentTrace = et;
        currentList = etList;
    }

    public static void setCurrentTraceWas() {
        currentTrace = wt;
        currentList = wtList;
    }

    public static String getTraceExpected() {

        String contents = "";
        String path = getLogDir() + "trace-expected.txt";
        try {
            contents = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }

    public static String getTraceWas() {

        String contents = "";
        String path = getLogDir() + "trace-obtained.txt";
        try {
            contents = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }

    public static boolean tracesAreEquals() {

        if (etList.size() != wtList.size()) {
            return false;
        }

        for (String s : etList) {
            if (!wtList.contains(s)) {
                return false;
            }
        }

        // for (String s : wtList) {
        // if (!etList.contains(s)) {
        // return false;
        // }
        // }

        return true;
    }

    public static boolean tracesAreSimilar() {

        for (String s : etList) {
            if (!wtList.contains(s)) {
                return false;
            }
        }

        return true;
    }

}