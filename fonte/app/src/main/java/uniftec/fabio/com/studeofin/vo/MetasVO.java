package uniftec.fabio.com.studeofin.vo;

import java.math.BigDecimal;
import java.util.Date;

public class MetasVO {
    private Integer codMeta;
    private String desMeta;
    private BigDecimal vlrMeta;
    private BigDecimal vlrMetaAtingida;
    private BigDecimal vlrMetaMensal;
    private Date dtaMeta;
    private Integer codCategoria;

    public Integer getCodMeta() {
        return codMeta;
    }

    public void setCodMeta(Integer codMeta) {
        this.codMeta = codMeta;
    }

    public String getDesMeta() {
        return desMeta;
    }

    public void setDesMeta(String desMeta) {
        this.desMeta = desMeta;
    }

    public BigDecimal getVlrMeta() {
        return vlrMeta;
    }

    public void setVlrMeta(BigDecimal vlrMeta) {
        this.vlrMeta = vlrMeta;
    }

    public BigDecimal getVlrMetaAtingida() {
        return vlrMetaAtingida;
    }

    public BigDecimal getVlrMetaMensal() {
        return vlrMetaMensal;
    }

    public void setVlrMetaMensal(BigDecimal vlrMetaMensal) {
        this.vlrMetaMensal = vlrMetaMensal;
    }

    public void setVlrMetaAtingida(BigDecimal vlrMetaAtingida) {
        this.vlrMetaAtingida = vlrMetaAtingida;
    }

    public Date getDtaMeta() {
        return dtaMeta;
    }

    public void setDtaMeta(Date dtaMeta) {
        this.dtaMeta = dtaMeta;
    }

    public Integer getCodCategoria() { return codCategoria; }

    public void setCodCategoria(Integer codCategoria) {  this.codCategoria = codCategoria;    }
}
