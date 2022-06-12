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
    private static String desSenha;
    private static String desNome;
    private static String desSobreNome;
    private static String imgFoto;

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

    public static void setDesSobreNome(String desSobreNome) { Global.desSobreNome = desSobreNome;  }

    public static String getDesSenha() { return desSenha; }

    public static void setDesSenha(String desSenha) { Global.desSenha = desSenha; }

    public static String getImgFoto() { return imgFoto; }

    public static void setImgFoto(String imgFoto) { Global.imgFoto = imgFoto; }

    public ArrayList<CategoriasVO> getListCategorias() {
        if(this.listCategorias == null)
            this.listCategorias = new ArrayList<CategoriasVO>();
        return listCategorias;
    }

    public void setListCategorias(ArrayList<CategoriasVO> listCategorias) {
        this.listCategorias = listCategorias;
    }
}

