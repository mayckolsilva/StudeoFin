package uniftec.fabio.com.studeofin.vo;

import java.math.BigDecimal;

public class AlertaGastosVO {

    private Integer codAlerta;
    private Integer id_usuario;
    private BigDecimal vlr_alerta;

    public Integer getCodAlerta() {
        return codAlerta;
    }

    public void setCodAlerta(Integer codAlerta) {
        this.codAlerta = codAlerta;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public BigDecimal getVlr_alerta() {
        return vlr_alerta;
    }

    public void setVlr_alerta(BigDecimal vlr_alerta) {
        this.vlr_alerta = vlr_alerta;
    }

}
