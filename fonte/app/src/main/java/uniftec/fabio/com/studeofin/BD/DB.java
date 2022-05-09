package uniftec.fabio.com.studeofin.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uniftec.fabio.com.studeofin.BD.requests.BuscaCategoriasRequest;
import uniftec.fabio.com.studeofin.BD.requests.BuscaUsuarioRequest;
import uniftec.fabio.com.studeofin.BD.requests.RemoveMetaRequest;
import uniftec.fabio.com.studeofin.global.Global;
import uniftec.fabio.com.studeofin.vo.CategoriasVO;
import uniftec.fabio.com.studeofin.vo.LancamentosVO;
import uniftec.fabio.com.studeofin.vo.MetasVO;
import uniftec.fabio.com.studeofin.vo.UsuariosVO;

public class DB extends SQLiteOpenHelper {

    public DB(Context context) {

        super(context, "studeofin",null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS usuarios (id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, des_email VARCHAR(50) NOT NULL, des_senha VARCHAR(20) NOT NULL, des_nome VARCHAR(20) NOT NULL, des_sobrenome VARCHAR(30) NOT NULL )");
        db.execSQL("CREATE TABLE IF NOT EXISTS metas (id_meta INTEGER PRIMARY KEY AUTOINCREMENT, des_meta VARCHAR(100) NOT NULL, vlr_meta REAL, id_usuario INTEGER, dta_meta VARCHAR(20), cod_categoria INTEGER, FOREIGN KEY (id_usuario) references usuario(id_usuario), FOREIGN KEY (cod_categoria) REFERENCES categorias(id_categoria) )");
        //0 - RECEITA 1- DESPESA 2-META
        db.execSQL("CREATE TABLE IF NOT EXISTS categorias (id_categoria INTEGER PRIMARY KEY AUTOINCREMENT, des_categoria VARCHAR(100) NOT NULL, ind_tipo_categoria INTEGER, id_meta INTEGER, id_usuario INTEGER, FOREIGN KEY (id_meta) REFERENCES meta (id_meta), FOREIGN KEY (id_usuario) REFERENCES usuarios (is_usuario) )");
        db.execSQL("CREATE TABLE IF NOT EXISTS lancamentos (id_lancamento INTEGER PRIMARY KEY AUTOINCREMENT, des_lancamento VARCHAR(100) NOT NULL, cod_categoria INTEGER, id_usuario INTEGER, dta_lancamento VARCHAR(20), vlr_lancamento REAL,FOREIGN KEY (cod_categoria) REFERENCES categorias (id_categoria), FOREIGN KEY (id_usuario) REFERENCES usuarios (id_usuario) )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS metas");
        db.execSQL("DROP TABLE IF EXISTS categorias");
        db.execSQL("DROP TABLE IF EXISTS lancamentos");

    }

    public void insereUsuario(UsuariosVO usuario){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();

        dados.put("id_usuario", usuario.getIdUsuario());
        dados.put("des_nome", usuario.getDesNome());
        dados.put("des_sobrenome", usuario.getDesSobreNome());
        dados.put("des_senha", usuario.getDesSenha());
        dados.put("des_email", usuario.getDesEmail());

        if(usuario.getIdUsuario() != null) {
            db.update("usuarios",dados,"id_usuario = ?",new String[]{String.valueOf(usuario.getIdUsuario())});
        } else {
            db.insert("usuarios",null,dados);
        }

        db.close();
    }

    public void removeUsuario(Integer codUsuario){

        SQLiteDatabase db = getWritableDatabase();
        db.delete("usuarios","usuario = ?", new String[]{String.valueOf(codUsuario)});
        db.close();

    }

    public Boolean buscaUsuario(BuscaUsuarioRequest req){

        SQLiteDatabase db = getReadableDatabase();
        String sql = " SELECT id_usuario, des_nome, des_sobrenome, des_email" +
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
            db.update("lancamentos",dados,"id_lancamento= ?",new String[]{String.valueOf(lancamento.getCodLancamento())});
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

        String sql = " SELECT id_lancamento, des_lancamento, cod_categoria, dta_lancamento, vlr_lancamento " +
                     " FROM lancamentos " +
                     " WHERE id_usuario =  " + Global.getIdUsuario();

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
                lancamento.setVlrLancamento(BigDecimal.valueOf(busca.getDouble(4)));
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
                meta.setVlrMeta(BigDecimal.valueOf(busca.getDouble(4)));
                metas.add(meta);
                busca.moveToNext();
            }

        }
        return metas;
    }
}
