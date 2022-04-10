package uniftec.fabio.com.studeofin.global;

import java.io.Serializable;

public class Global implements Serializable {

    private Global(){

    }
    private static Integer idUsuario;
    private static String desEmail;
    private static String desNome;
    private static String desSobreNome;

    public static Integer getIdUsuario() {
        return idUsuario;
    }

    public static void setIdUsuario(Integer idUsuario) {
        Global.idUsuario = idUsuario;
    }

    public static String getDesEmail() {
        return desEmail;
    }

    public static void setDesEmail(String desEmail) {
        Global.desEmail = desEmail;
    }

    public static String getDesNome() {
        return desNome;
    }

    public static void setDesNome(String desNome) {
        Global.desNome = desNome;
    }

    public static String getDesSobreNome() {
        return desSobreNome;
    }

    public static void setDesSobreNome(String desSobreNome) {
        Global.desSobreNome = desSobreNome;
    }
}

