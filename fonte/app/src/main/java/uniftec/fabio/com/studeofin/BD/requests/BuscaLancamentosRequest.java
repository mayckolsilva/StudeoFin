package uniftec.fabio.com.studeofin.BD.requests;

public class BuscaLancamentosRequest {

    private Integer codCategoria;
    private String dtaInicial;
    private String dtaFinal;

    public Integer getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(Integer codCategoria) {
        this.codCategoria = codCategoria;
    }

    public String getDtaInicial() {
        return dtaInicial;
    }

    public void setDtaInicial(String dtaInicial) {
        this.dtaInicial = dtaInicial;
    }

    public String getDtaFinal() {
        return dtaFinal;
    }

    public void setDtaFinal(String dtaFinal) {
        this.dtaFinal = dtaFinal;
    }
}
