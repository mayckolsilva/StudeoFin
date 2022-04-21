package uniftec.fabio.com.studeofin.vo;

import java.math.BigDecimal;
import java.util.Date;

public class LancamentosVO {

    private Integer codLancamento;
    private String desLancamento;
    private BigDecimal vlrLancamento;
    private Integer codCategoria;
    private Integer codUsuario;
    private Date dtaLancamento;
    private String desCategoria;

    public Integer getCodLancamento() {
        return codLancamento;
    }

    public void setCodLancamento(Integer codLancamento) {
        this.codLancamento = codLancamento;
    }

    public String getDesLancamento() {
        return desLancamento;
    }

    public void setDesLancamento(String desLancamento) {
        this.desLancamento = desLancamento;
    }

    public BigDecimal getVlrLancamento() {
        return vlrLancamento;
    }

    public void setVlrLancamento(BigDecimal vlrLancamento) {
        this.vlrLancamento = vlrLancamento;
    }

    public Integer getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(Integer codCategoria) {
        this.codCategoria = codCategoria;
    }

    public Integer getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Integer codUsuario) {
        this.codUsuario = codUsuario;
    }

    public Date getDtaLancamento() {
        return dtaLancamento;
    }

    public void setDtaLancamento(Date dtaLancamento) {
        this.dtaLancamento = dtaLancamento;
    }

    public String getDesCategoria() {
        return desCategoria;
    }

    public void setDesCategoria(String desCategoria) {
        this.desCategoria = desCategoria;
    }
}
