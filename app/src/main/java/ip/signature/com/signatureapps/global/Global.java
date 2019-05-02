package ip.signature.com.signatureapps.global;

public class Global {
    public static int id;
    public static String nip;
    public static String name;
    public static int step;

    public static void set(int id, String nip, String name, int step) {
        Global.id = id;
        Global.nip = nip;
        Global.name = name;
        Global.step = step;
    }

    public static void reset() {
        Global.id = -1;
        Global.nip = "-1";
        Global.name = "";
        Global.step = -1;
    }
}
