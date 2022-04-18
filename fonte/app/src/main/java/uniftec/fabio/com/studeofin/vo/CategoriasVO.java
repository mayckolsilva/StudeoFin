package uniftec.fabio.com.studeofin.vo;

import java.util.Objects;

public class CategoriasVO {

    private Integer codCategoria;
    private String desCategoria;
    private Integer codUduario;
    private Integer indTipo;
    private Integer idMeta;

    public Integer getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(Integer codCategoria) {
        this.codCategoria = codCategoria;
    }

    public String getDesCategoria() {
        return desCategoria;
    }

    public void setDesCategoria(String desCategoria) {
        this.desCategoria = desCategoria;
    }

    public Integer getCodUduario() {
        return codUduario;
    }

    public void setCodUduario(Integer codUduario) {
        this.codUduario = codUduario;
    }

    public Integer getIndTipo() { return indTipo; }

    public void setIndTipo(Integer indTipo) {
        this.indTipo = indTipo;
    }

    public Integer getIdMeta() { return idMeta; }

    public void setIdMeta(Integer idMeta) { this.idMeta = idMeta; }

    @Override
    public String toString(){ return desCategoria;  }
}
