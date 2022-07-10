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
import uniftec.fabio.com.studeofin.R;
import uniftec.fabio.com.studeofin.adapter.LancamentosAdapter;
import uniftec.fabio.com.studeofin.databinding.FragmentReceitasDespesasBinding;
import uniftec.fabio.com.studeofin.vo.AlertaGastosVO;
import uniftec.fabio.com.studeofin.vo.CategoriasVO;
import uniftec.fabio.com.studeofin.vo.LancamentosVO;


public class ReceitasDespesasFragment extends Fragment {
    private FragmentReceitasDespesasBinding binding;

    private ArrayList<CategoriasVO> categorias;
    private ArrayList<LancamentosVO> lancamentos;
    private Integer codCategoria;
    private Integer indTipoCategoria;
    private LancamentosVO lancamentoSelecionado;
    private AlertaGastosVO alertaGastos;

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
                    salvarReceitaDespesa();
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

                ExcluirReceitaDespesa();
                limparTela();
                buscarLancamentos();

            }
        });

        carregarSpinnerCategoria();
        buscarLancamentos();
        buscarAlertaGastos();
        limparTela();
        binding.edtDescLancamento.requestFocus();
        return root;
    }

    private void salvarReceitaDespesa(){

        DB db = new DB(getContext());
        LancamentosVO lancamento = new LancamentosVO();
        lancamento.setCodLancamento(lancamentoSelecionado.getCodLancamento());
        lancamento.setCodCategoria(codCategoria);
        lancamento.setDesLancamento(binding.edtDescLancamento.getText().toString().trim());
        lancamento.setVlrLancamento(new BigDecimal(binding.edtValor.getText().toString().trim()));
        db.insereLancamentos(lancamento);
        db.close();

        if(indTipoCategoria == 1 && alertaGastos.getVlr_alerta() != null){
            DB db2 = new DB(getContext());
            if(db2.alertaGastos(alertaGastos.getVlr_alerta())){
                Toast.makeText(getContext(),"Atenção!! Lançamentos de despesas excederam o valor do alerta de gastos.",Toast.LENGTH_LONG).show();
            };
        }

        if(lancamento.getCodLancamento() != null){
            Toast.makeText(getContext(),getString(R.string.msg_atualizar),Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(),getString(R.string.msg_salvar),Toast.LENGTH_LONG).show();
        }

    }
    private void ExcluirReceitaDespesa(){

        DB db = new DB(getContext());
        db.removeLancamento(lancamentoSelecionado.getCodLancamento());
        db.close();

        Toast.makeText(getContext(),getString(R.string.msg_excluir),Toast.LENGTH_LONG).show();

    }

    private void buscarLancamentos(){

        try{
            DB db = new DB(getContext());
            binding.listaLancamentos.setAdapter(new LancamentosAdapter(getActivity(),lancamentos = db.buscaLancamentos()));
            db.close();
        } catch (Exception e) {
            Log.println(Log.ERROR,"Lançamentos","Erro na busca dos lançamentos" );
        }
    }

    private  void buscarAlertaGastos(){
        try{
            DB db = new DB(getContext());
            alertaGastos = db.buscaAlertaGastos();
            db.close();
        } catch (Exception e) {
            Log.println(Log.ERROR,"Alerta de Gastos","Erro na busca do alerta de gastos" );
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
                    indTipoCategoria = categorias.getIndTipo();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }

            });

            db.close();
        } catch(Exception e){
            Log.println(Log.ERROR,"Categoria","Erro na busca da categoria" );
            e.printStackTrace();
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

    public AlertaGastosVO getAlertaGastos() {
        if(this.alertaGastos == null)
            this.alertaGastos = new AlertaGastosVO();
        return alertaGastos;
    }

    public void setAlertaGastos(AlertaGastosVO alertaGastos) {
        this.alertaGastos = alertaGastos;
    }
}