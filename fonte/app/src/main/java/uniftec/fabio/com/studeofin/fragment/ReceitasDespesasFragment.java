package uniftec.fabio.com.studeofin.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uniftec.fabio.com.studeofin.adapter.LancamentosAdapter;
import uniftec.fabio.com.studeofin.databinding.FragmentReceitasDespesasBinding;
import uniftec.fabio.com.studeofin.global.Global;
import uniftec.fabio.com.studeofin.vo.CategoriasVO;
import uniftec.fabio.com.studeofin.vo.LancamentosVO;


public class ReceitasDespesasFragment extends Fragment {
    private FragmentReceitasDespesasBinding binding;

    private SQLiteDatabase query;
    private ArrayList<CategoriasVO> categorias;
    private ArrayList<LancamentosVO> lancamentos;
    private Integer codCategoria;
    private LancamentosVO lancamentoSelecionado;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReceitasDespesasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.btnAddLancamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                limparTela();

            }
        });

        binding.btnSalvarLancamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(verificaCampos()){

                    query = getActivity().openOrCreateDatabase("studeofin", android.content.Context.MODE_PRIVATE,
                            null);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date dtaLanc = new Date();
                    String data = sdf.format(dtaLanc);

                    if(lancamentoSelecionado.getCodLancamento()!=null){

                        ContentValues values = new ContentValues();
                        values.put("des_lancamento", binding.edtDescLancamento.getText().toString().trim());
                        values.put("cod_categoria", codCategoria);
                        values.put("vlr_lancamento", binding.edtValor.getText().toString());
                        values.put("dta_lancamento",data);
                        query.update("lancamentos", values,"id_lancamento = ?", new String[]{String.valueOf(lancamentoSelecionado.getCodLancamento())} );
                    } else {
                        try {
                            query.execSQL("INSERT INTO lancamentos (des_lancamento, cod_categoria, id_usuario, vlr_lancamento, dta_lancamento) VALUES " +
                                    "( '" + binding.edtDescLancamento.getText().toString().trim() + "'," + codCategoria + "," + Global.getIdUsuario() + "," + binding.edtValor.getText() + ", '" +  data + "')");
                        } catch (Exception e ){
                            Log.println(Log.ERROR,"Lancamento","Erro ao salvar lançamento!");
                        }

                    }
                    limparTela();
                    buscarLancamentos();
                }
            }
        });

        binding.listaLancamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickLancamento(lancamentos.get(i));
            }
        });

        binding.btnExcluirLancamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query.delete("lancamentos","id_lancamento = ?", new String[]{String.valueOf(lancamentoSelecionado.getCodLancamento())});
                limparTela();
                buscarLancamentos();

            }
        });


        query = getActivity().openOrCreateDatabase("studeofin", android.content.Context.MODE_PRIVATE,
                null);


        carregarSpinnerCategoria();
        buscarLancamentos();
        limparTela();
        return root;
    }

    private void buscarLancamentos(){

        try{

            Cursor buscalanc = query.rawQuery(" SELECT l.id_lancamento, l.des_lancamento, l.cod_categoria, l.dta_lancamento, l.vlr_lancamento, c.des_categoria " +
                    " FROM lancamentos l" +
                    "   JOIN categorias c ON (c.id_categoria = l.cod_categoria) " +
                    " WHERE l.id_usuario =  " + Global.getIdUsuario() ,null);

            lancamentos = new ArrayList<LancamentosVO>();

            buscalanc.moveToFirst();

            if(buscalanc.getCount()>0) {
                while (!buscalanc.isAfterLast()) {
                    LancamentosVO lancamento = new LancamentosVO();
                    lancamento.setCodLancamento(buscalanc.getInt(0));
                    lancamento.setDesLancamento(buscalanc.getString(1));
                    lancamento.setCodCategoria(buscalanc.getInt(2));
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(buscalanc.getString(3));
                    lancamento.setDtaLancamento(date);
                    lancamento.setVlrLancamento(BigDecimal.valueOf(buscalanc.getDouble(4)));
                    lancamento.setDesCategoria(buscalanc.getString(5));
                    this.getLancamentos().add(lancamento);
                    buscalanc.moveToNext();
                }
            }

            if(lancamentos!=null){
                binding.listaLancamentos.setAdapter(new LancamentosAdapter(getActivity(),lancamentos));
            }

        } catch (Exception e) {
            Log.println(Log.ERROR,"Lançamentos","Erro na busca dos lançamentos" );

        }

    }
    private void carregarSpinnerCategoria(){
        try{

            Cursor busca = query.rawQuery(" SELECT id_categoria, des_categoria, ind_tipo_categoria, id_meta " +
                    " FROM categorias " +
                    " WHERE id_usuario =  " + Global.getIdUsuario() +
                    " ORDER BY des_categoria",null);

            categorias = new ArrayList<CategoriasVO>();

            busca.moveToFirst();
            if(busca.getCount()>0){
                while(!busca.isAfterLast()){
                    CategoriasVO categoria = new CategoriasVO();
                    categoria.setCodCategoria(busca.getInt(0));
                    categoria.setDesCategoria(busca.getString(1));
                    categoria.setIndTipo(busca.getInt(2));
                    categoria.setIdMeta(busca.getInt(3));
                    this.getCategorias().add(categoria);
                    busca.moveToNext();
                }
            }

            if(categorias!=null){
                ArrayAdapter<CategoriasVO> adapter =  new ArrayAdapter<CategoriasVO>(getContext(), android.R.layout.simple_spinner_item, getCategorias());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerLancamento.setAdapter(adapter);

                binding.spinnerLancamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        CategoriasVO categorias = (CategoriasVO) adapterView.getSelectedItem();
                        codCategoria = categorias.getCodCategoria();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }

                });
            }
        } catch(Exception e){
            Log.println(Log.ERROR,"Categoria","Erro na busca da categoria" );
        }

    }

    private void limparTela(){
        binding.edtDescLancamento.setText(null);
        binding.edtValor.setText(null);
        lancamentoSelecionado = new LancamentosVO();
    }

    private void clickLancamento(final LancamentosVO lanc){
        lancamentoSelecionado = lanc;
        binding.edtDescLancamento.setText(lanc.getDesLancamento());
        binding.edtValor.setText(String.format("%.2f",lanc.getVlrLancamento()));

        for(Integer i=0;i<categorias.size();i++){
            if(categorias.get(i).getCodCategoria().equals(lanc.getCodCategoria() )){
                binding.spinnerLancamento.setSelection(i);
                break;
            }
        }
    }

    private boolean verificaCampos(){

        if(TextUtils.isEmpty(binding.edtDescLancamento.getText())){
            binding.edtDescLancamento.requestFocus();
            Toast.makeText(getContext(), "Preencher descrição do lançamento!", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(binding.edtValor.getText())){
            binding.edtValor.requestFocus();
            Toast.makeText(getContext(), "Preencher valor do lançamento!", Toast.LENGTH_LONG).show();
            return false;
        } else{
            return true;
        }

    }

    public ArrayList<CategoriasVO> getCategorias() {
        if(this.categorias == null)
            this.categorias = new ArrayList<CategoriasVO>();
        return categorias;
    }

    public void setCategorias(ArrayList<CategoriasVO> categorias) {
        this.categorias = categorias;
    }

    public LancamentosVO getLancamentoSelecionado() {
        if(this.lancamentoSelecionado== null)
            this.lancamentoSelecionado = new LancamentosVO();
        return lancamentoSelecionado;
    }

    public void setLancamentoSelecionado(LancamentosVO lancamentoSelecionado) {
        this.lancamentoSelecionado = lancamentoSelecionado;
    }

    public ArrayList<LancamentosVO> getLancamentos() {
        if(this.lancamentos == null)
            this.lancamentos = new ArrayList<LancamentosVO>();
        return lancamentos;
    }

    public void setLancamentos(ArrayList<LancamentosVO> lancamentos) {
        this.lancamentos = lancamentos;
    }
}