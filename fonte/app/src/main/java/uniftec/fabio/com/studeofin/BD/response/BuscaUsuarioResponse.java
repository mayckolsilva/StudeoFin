package uniftec.fabio.com.studeofin.BD.response;

import java.util.ArrayList;

import uniftec.fabio.com.studeofin.vo.UsuariosVO;

public class BuscaUsuarioResponse {

    private UsuariosVO usuario;

    public UsuariosVO getUsuario() {
        if (this.usuario == null)
            this.usuario = new UsuariosVO();
        return usuario;
    }

    public void setUsuario(UsuariosVO usuario) {
        this.usuario = usuario;
    }
}
