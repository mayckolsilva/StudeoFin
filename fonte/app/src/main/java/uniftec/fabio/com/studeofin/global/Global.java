package uniftec.fabio.com.studeofin.global;

import java.io.Serializable;
import java.util.ArrayList;

import uniftec.fabio.com.studeofin.vo.CategoriasVO;

public class Global implements Serializable {

    private Global(){

    }

    private ArrayList<CategoriasVO> listCategorias;
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

    public ArrayList<CategoriasVO> getListCategorias() {
        if(this.listCategorias == null)
            this.listCategorias = new ArrayList<CategoriasVO>();
        return listCategorias;
    }

    public void setListCategorias(ArrayList<CategoriasVO> listCategorias) {
        this.listCategorias = listCategorias;
    }
}

