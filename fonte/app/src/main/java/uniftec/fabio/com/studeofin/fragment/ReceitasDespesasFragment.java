package uniftec.fabio.com.studeofin.fragment;

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
import java.util.ArrayList;

import uniftec.fabio.com.studeofin.BD.DB;
import uniftec.fabio.com.studeofin.BD.requests.BuscaCategoriasRequest;
import uniftec.fabio.com.studeofin.adapter.LancamentosAdapter;
import uniftec.fabio.com.studeofin.databinding.FragmentReceitasDespesasBinding;
import uniftec.fabio.com.studeofin.vo.CategoriasVO;
import uniftec.fabio.com.studeofin.vo.LancamentosVO;


public class ReceitasDespesasFragment extends Fragment {
    private FragmentReceitasDespesasBinding binding;

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

                    DB db = new DB(getContext());
                    LancamentosVO lancamento = new LancamentosVO();
                    lancamento.setCodLancamento(lancamentoSelecionado.getCodLancamento());
                    lancamento.setCodCategoria(codCategoria);
                    lancamento.setDesLancamento(binding.edtDescLancamento.getText().toString().trim());
                    lancamento.setVlrLancamento(new BigDecimal(binding.edtValor.getText().toString().trim()));

                    db.insereLancamentos(lancamento);
                    db.close();

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

                DB db = new DB(getContext());
                db.removeLancamento(lancamentoSelecionado.getCodLancamento());
                db.close();

                limparTela();
                buscarLancamentos();
            }
        });
        carregarSpinnerCategoria();
        buscarLancamentos();
        limparTela();
        return root;
    }

    private void buscarLancamentos(){

        try{
            DB db = new DB(getContext());
            binding.listaLancamentos.setAdapter(new LancamentosAdapter(getActivity(),lancamentos = db.buscaLancamentos()));
        } catch (Exception e) {
            Log.println(Log.ERROR,"Lançamentos","Erro na busca dos lançamentos" );
        }
    }
    private void carregarSpinnerCategoria(){
        try{

            DB db = new DB(getContext());
            BuscaCategoriasRequest req = new BuscaCategoriasRequest();
            req.setbVerificaMeta(false);


            ArrayAdapter<CategoriasVO> adapter =  new ArrayAdapter<CategoriasVO>(getContext(), android.R.layout.simple_spinner_item, categorias = db.buscaCategorias(req));
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