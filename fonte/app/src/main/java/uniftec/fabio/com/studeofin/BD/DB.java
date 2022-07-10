package uniftec.fabio.com.studeofin.BD;

import static java.math.BigDecimal.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import uniftec.fabio.com.studeofin.BD.requests.BuscaCategoriasRequest;
import uniftec.fabio.com.studeofin.BD.requests.BuscaUsuarioRequest;
import uniftec.fabio.com.studeofin.BD.requests.RemoveMetaRequest;
import uniftec.fabio.com.studeofin.global.Global;
import uniftec.fabio.com.studeofin.vo.AlertaGastosVO;
import uniftec.fabio.com.studeofin.vo.CategoriasVO;
import uniftec.fabio.com.studeofin.vo.LancamentosVO;
import uniftec.fabio.com.studeofin.vo.MetasVO;
import uniftec.fabio.com.studeofin.vo.UsuariosVO;

public class DB extends SQLiteOpenHelper {

    public DB(Context context) {

        super(context, "studeofin",null,4);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS usuarios (id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, des_email VARCHAR(50) NOT NULL, des_senha VARCHAR(20) NOT NULL, des_nome VARCHAR(20) NOT NULL, des_sobrenome VARCHAR(30) NOT NULL, img_foto text )");
        db.execSQL("CREATE TABLE IF NOT EXISTS metas (id_meta INTEGER PRIMARY KEY AUTOINCREMENT, des_meta VARCHAR(100) NOT NULL, vlr_meta REAL, id_usuario INTEGER, dta_meta VARCHAR(20), cod_categoria INTEGER, FOREIGN KEY (id_usuario) references usuarios (id_usuario), FOREIGN KEY (cod_categoria) REFERENCES categorias(id_categoria) )");
        //0 - RECEITA 1- DESPESA 2-META
        db.execSQL("CREATE TABLE IF NOT EXISTS categorias (id_categoria INTEGER PRIMARY KEY AUTOINCREMENT, des_categoria VARCHAR(100) NOT NULL, ind_tipo_categoria INTEGER, id_meta INTEGER, id_usuario INTEGER, FOREIGN KEY (id_meta) REFERENCES metas (id_meta), FOREIGN KEY (id_usuario) REFERENCES usuarios (id_usuario) )");
        db.execSQL("CREATE TABLE IF NOT EXISTS lancamentos (id_lancamento INTEGER PRIMARY KEY AUTOINCREMENT, des_lancamento VARCHAR(100) NOT NULL, cod_categoria INTEGER, id_usuario INTEGER, dta_lancamento VARCHAR(20), vlr_lancamento REAL,FOREIGN KEY (cod_categoria) REFERENCES categorias (id_categoria), FOREIGN KEY (id_usuario) REFERENCES usuarios (id_usuario) )");
        db.execSQL("CREATE TABLE IF NOT EXISTS alerta_gastos (id_alerta INTEGER PRIMARY KEY AUTOINCREMENT, id_usuario INTEGER, vlr_alerta REAL, FOREIGN KEY (id_usuario) REFERENCES usuarios (id_usuario))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS metas");
        db.execSQL("DROP TABLE IF EXISTS categorias");
        db.execSQL("DROP TABLE IF EXISTS lancamentos");
        db.execSQL("DROP TABLE IF EXISTS alerta_gastos");

    }

    public void insereUsuario(UsuariosVO usuario){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();

        dados.put("id_usuario", usuario.getIdUsuario());
        dados.put("des_nome", usuario.getDesNome());
        dados.put("des_sobrenome", usuario.getDesSobreNome());
        dados.put("des_senha", usuario.getDesSenha());
        dados.put("des_email", usuario.getDesEmail());
        dados.put("img_foto", usuario.getImgFoto());

        if(usuario.getIdUsuario() != null) {
            db.update("usuarios",dados,"id_usuario = ?",new String[]{String.valueOf(usuario.getIdUsuario())});
        } else {
            db.insert("usuarios",null,dados);
        }

        db.close();
    }

    public void removeUsuario(Integer codUsuario){

        SQLiteDatabase db = getWritableDatabase();
        db.delete("usuarios","id_usuario = ?", new String[]{String.valueOf(codUsuario)});
        db.delete("metas","id_usuario = ?", new String[]{String.valueOf(codUsuario)});
        db.delete("categorias","id_usuario = ?", new String[]{String.valueOf(codUsuario)});
        db.delete("lancamentos","id_usuario = ?", new String[]{String.valueOf(codUsuario)});
        db.delete("alerta_gastos","id_usuario = ?", new String[]{String.valueOf(codUsuario)});
        db.close();

    }

    public boolean buscaUsuario(BuscaUsuarioRequest req){

        SQLiteDatabase db = getReadableDatabase();
        String sql = " SELECT id_usuario, des_nome, des_sobrenome, des_email, img_foto, des_senha" +
                     " FROM usuarios " +
                     " WHERE des_email = '" + req.getDesEmail() + "' ";

        if(req.getDesSenha()!= null){
            sql +=  " AND  des_senha = '" + req.getDesSenha() + "'";
        }


        Cursor busca = db.rawQuery(sql,null);

        busca.moveToFirst();
        if(busca.getCount()>0){
            Global.setIdUsuario(busca.getInt(0));
            Global.setDesNome(busca.getString(1));
            Global.setDesSobreNome(busca.getString(2));
            Global.setDesEmail(busca.getString(3));
            Global.setImgFoto(busca.getString(4));
            Global.setDesSenha(busca.getString(5));
            db.close();
            return true;
        } else {
            return false;
        }
    }


    public void insereCategoria(CategoriasVO categoria){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();

        dados.put("id_categoria", categoria.getCodCategoria());
        dados.put("des_categoria", categoria.getDesCategoria());
        dados.put("ind_tipo_categoria", categoria.getIndTipo());
        dados.put("id_usuario", Global.getIdUsuario());

        if(categoria.getCodCategoria() != null) {
            db.update("categorias",dados,"id_categoria = ?",new String[]{String.valueOf(categoria.getCodCategoria())});
        } else {
            db.insert("categorias", null, dados);
        }

        db.close();
    }

    public void removeCategoria(Integer codCategoria){

        SQLiteDatabase db = getWritableDatabase();
        db.delete("categorias","id_categoria = ?", new String[]{String.valueOf(codCategoria)});
        db.close();

    }

    public ArrayList<CategoriasVO> buscaCategorias (BuscaCategoriasRequest req){

        ArrayList<CategoriasVO> categorias = new ArrayList<CategoriasVO>();

        SQLiteDatabase db = getReadableDatabase();

        String sql = " SELECT id_categoria, des_categoria, ind_tipo_categoria, id_meta " +
                     " FROM categorias " +
                     " WHERE id_usuario =  " + Global.getIdUsuario();


        if (req.isbVerificaMeta()){
            sql += " ORDER BY id_categoria desc limit 1";
        } else {
            sql += " ORDER BY des_categoria";
        }

        Cursor busca = db.rawQuery(sql,null);

        busca.moveToFirst();
        if(busca.getCount()>0){
            while(!busca.isAfterLast()){
                CategoriasVO categoria = new CategoriasVO();
                categoria.setCodCategoria(busca.getInt(0));
                categoria.setDesCategoria(busca.getString(1));
                categoria.setIndTipo(busca.getInt(2));
                categoria.setIdMeta(busca.getInt(3));

                categorias.add(categoria);

                busca.moveToNext();
            }
        }

        db.close();

        return categorias;
    }


    public void  insereLancamentos(LancamentosVO lancamento) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dtaLanc = new Date();
        String data = sdf.format(dtaLanc);

        dados.put("id_lancamento", lancamento.getCodLancamento());
        dados.put("des_lancamento", lancamento.getDesLancamento());
        dados.put("dta_lancamento", data);
        dados.put("id_usuario", Global.getIdUsuario());
        dados.put("vlr_lancamento", String.valueOf(lancamento.getVlrLancamento()));
        dados.put("cod_categoria", lancamento.getCodCategoria());

        if(lancamento.getCodLancamento() != null){
            db.update("lancamentos",dados,"id_lancamento = ?",new String[]{String.valueOf(lancamento.getCodLancamento())});
        } else {
            db.insert("lancamentos", null, dados);
        }

        db.close();
    }

    public void removeLancamento(Integer codLancamento){

        SQLiteDatabase db = getWritableDatabase();
        db.delete("lancamentos","id_lancamento = ?", new String[]{String.valueOf(codLancamento)});
        db.close();

    }

    public ArrayList<LancamentosVO> buscaLancamentos(){
        ArrayList<LancamentosVO> lancamentos = new ArrayList<LancamentosVO>();

        SQLiteDatabase db = getReadableDatabase();

        String sql = " SELECT l.id_lancamento, l.des_lancamento, l.cod_categoria, l.dta_lancamento, l.vlr_lancamento, c.des_categoria " +
                     " FROM lancamentos l" +
                     "  LEFT JOIN categorias c ON (c.id_categoria = l.cod_categoria)" +
                     " WHERE l.id_usuario =  " + Global.getIdUsuario();

        Cursor busca = db.rawQuery(sql,null);

        busca.moveToFirst();
        if(busca.getCount()>0){
            while(!busca.isAfterLast()){
                LancamentosVO lancamento = new LancamentosVO();
                lancamento.setCodLancamento(busca.getInt(0));
                lancamento.setDesLancamento(busca.getString(1));
                lancamento.setCodCategoria(busca.getInt(2));
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(busca.getString(3));
                    lancamento.setDtaLancamento(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                lancamento.setVlrLancamento(valueOf(busca.getDouble(4)));
                lancamento.setDesCategoria(busca.getString(5));
                lancamentos.add(lancamento);
                busca.moveToNext();
            }
        }

        db.close();

        return lancamentos;
    }


    public void insereMeta(MetasVO meta) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String data = sdf.format(meta.getDtaMeta());

        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();

        dados.put("id_meta", meta.getCodMeta());
        dados.put("des_meta", meta.getDesMeta());
        dados.put("vlr_meta", String.valueOf(meta.getVlrMeta()));
        dados.put("dta_meta", data);
        dados.put("cod_categoria", meta.getCodCategoria());
        dados.put("id_usuario", Global.getIdUsuario());

        if(meta.getCodMeta()!=null){
            db.update("metas",dados,"id_meta= ?",new String[]{String.valueOf(meta.getCodMeta())});
        } else {
            db.insert("metas", null, dados);
        }
        db.close();
    }

    public void removeMeta(RemoveMetaRequest req){

        SQLiteDatabase db = getWritableDatabase();
        db.delete("categorias","id_categoria = ?", new String[]{String.valueOf(req.getCodCategoria())});
        db.delete("metas","id_meta = ?", new String[]{String.valueOf(req.getCodMeta())});
        db.close();

    }

    public ArrayList<MetasVO> buscaMetas(){

        ArrayList<MetasVO> metas = new ArrayList<MetasVO>();

        SQLiteDatabase db = getReadableDatabase();

        String sql = " SELECT id_meta, des_meta, cod_categoria, dta_meta, vlr_meta " +
                     " FROM metas " +
                     " WHERE id_usuario =  " + Global.getIdUsuario();

        Cursor busca = db.rawQuery(sql,null);

        busca.moveToFirst();
        if(busca.getCount()>0) {
            while (!busca.isAfterLast()) {
                MetasVO meta = new MetasVO();
                meta.setCodMeta(busca.getInt(0));
                meta.setDesMeta(busca.getString(1));
                meta.setCodCategoria(busca.getInt(2));
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(busca.getString(3));
                    meta.setDtaMeta(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                meta.setVlrMeta(valueOf(busca.getDouble(4)));
                metas.add(meta);
                busca.moveToNext();
            }
        }
        return metas;
    }


    public ArrayList<MetasVO> calculoPlanejamentoFinanceiro(){
        ArrayList<MetasVO> metas = new ArrayList<MetasVO>();

        SQLiteDatabase db = getReadableDatabase();

        String sql = " SELECT id_meta, des_meta, cod_categoria, dta_meta, vlr_meta " +
                     " FROM metas " +
                     " WHERE id_usuario =  " + Global.getIdUsuario();

        Cursor busca = db.rawQuery(sql,null);

        busca.moveToFirst();
        if(busca.getCount()>0) {
            while (!busca.isAfterLast()) {
                MetasVO meta = new MetasVO();
                meta.setCodMeta(busca.getInt(0));
                meta.setDesMeta(busca.getString(1));
                meta.setCodCategoria(busca.getInt(2));
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(busca.getString(3));
                    meta.setDtaMeta(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                meta.setVlrMeta(valueOf(busca.getDouble(4)));

                //Verifica meses restantes para atingir o objetivo na data prevista

                Calendar dtaMeta = Calendar.getInstance();
                dtaMeta.setTime(meta.getDtaMeta());
                Calendar dtaAtual = Calendar.getInstance();
                int mesesRestantes = (dtaMeta.get(Calendar.YEAR) * 12 + dtaMeta.get(Calendar.MONTH)) -
                        (dtaAtual.get(Calendar.YEAR) * 12 + dtaAtual.get(Calendar.MONTH));

                //Verifica a soma dos lancamentos  da meta
                SQLiteDatabase db2 = getReadableDatabase();
                String sql2 = " SELECT SUM(vlr_lancamento) " +
                        " FROM lancamentos " +
                        " WHERE cod_categoria =  " + meta.getCodCategoria();
                Cursor busca2 = db2.rawQuery(sql2,null);

                busca2.moveToFirst();
                if(busca2.getCount()>0) {
                    while (!busca2.isAfterLast()) {
                        BigDecimal vlrAtingido = (valueOf(busca2.getDouble(0)));
                        meta.setVlrMetaAtingida(vlrAtingido);
                        BigDecimal vlrLancMensal = meta.getVlrMeta().subtract(vlrAtingido);

                        if(mesesRestantes > 0) {
                            vlrLancMensal = vlrLancMensal.divide(new BigDecimal(mesesRestantes), 2, RoundingMode.HALF_EVEN);
                        }
                        meta.setVlrMetaMensal(vlrLancMensal);
                        busca2.moveToNext();
                    }
                }
                metas.add(meta);
                db2.close();
                busca.moveToNext();
            }

        }
        db.close();

        return metas;

    }


    public AlertaGastosVO buscaAlertaGastos (){

        AlertaGastosVO alerta = new AlertaGastosVO();
        SQLiteDatabase db = getReadableDatabase();

        String sql = " SELECT id_alerta, vlr_alerta " +
                     " FROM alerta_gastos " +
                     " WHERE id_usuario =  " + Global.getIdUsuario();

        Cursor busca = db.rawQuery(sql,null);

        busca.moveToFirst();
        if(busca.getCount()>0) {
            while (!busca.isAfterLast()) {
                alerta.setCodAlerta(busca.getInt(0));
                alerta.setVlr_alerta(valueOf(busca.getDouble(1)));
                busca.moveToNext();
            }
        }
        alerta.setId_usuario(Global.getIdUsuario());
        return alerta;
    }
    public void salvarAlertaGastos(AlertaGastosVO req){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = new ContentValues();
        dados.put("id_alerta", req.getCodAlerta());
        dados.put("id_usuario",Global.getIdUsuario());
        dados.put("vlr_alerta", String.valueOf(req.getVlr_alerta()));

        if(req.getCodAlerta()!=null){
            db.update("alerta_gastos", dados,"id_alerta = ?", new String[]{String.valueOf(req.getCodAlerta())});
        } else{
            db.insert("alerta_gastos",null,dados);
        }

        db.close();
    }

    public void removeALertaGastos(AlertaGastosVO req){

        SQLiteDatabase db = getWritableDatabase();
        db.delete("alerta_gastos","id_alerta = ?", new String[]{String.valueOf(req.getCodAlerta())});
        db.close();

    }

    public boolean alertaGastos (BigDecimal alerta) {

        Calendar dtaAtual = Calendar.getInstance();
        String ano = String.valueOf(dtaAtual.get(Calendar.YEAR));
        String mes = String.valueOf(dtaAtual.get(Calendar.MONTH) + 1);
        if(dtaAtual.get(Calendar.MONTH) + 1 <10){
            mes = "0" + mes;
        }
        String dtaInicial = "'" + ano + "-" + mes + "-01" + "'";

        SQLiteDatabase db = getReadableDatabase();
        String sql = " SELECT SUM(l.vlr_lancamento) " +
                     " FROM lancamentos l" +
                     "   LEFT JOIN categorias c ON (c.id_categoria = l.cod_categoria)" +
                     " WHERE l.id_usuario =  " + Global.getIdUsuario() +
                     " AND c.ind_tipo_categoria = 1" +
                     " AND l.dta_lancamento >= " + dtaInicial ;

        Cursor busca = db.rawQuery(sql,null);

        busca.moveToFirst();
        if(busca.getCount()>0) {
            while (!busca.isAfterLast()) {
                BigDecimal vlrLancamentos = (valueOf(busca.getDouble(0)));
                if(vlrLancamentos.compareTo(alerta) == 1){
                    return true;
                } else{
                    return false;
                }
            }
        }
        return false;
    }

}
