package uniftec.fabio.com.studeofin.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import uniftec.fabio.com.studeofin.BD.DB;
import uniftec.fabio.com.studeofin.BD.requests.BuscaCategoriasRequest;
import uniftec.fabio.com.studeofin.R;
import uniftec.fabio.com.studeofin.adapter.CategoriasAdapter;
import uniftec.fabio.com.studeofin.databinding.FragmentCategoriasBinding;
import uniftec.fabio.com.studeofin.vo.CategoriasVO;

public class CategoriasFragment extends Fragment {

    FragmentCategoriasBinding binding;
    ArrayList<CategoriasVO> categorias;
    private CategoriasVO catSelecionada;
    private Integer indTipo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCategoriasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.btnAddCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpaTela();
            }
        });

        binding.btnSalvarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(verificaCampos()){
                    salvarCategoria();
                    limpaTela();
                    buscaCategorias();
                }
            }
        });

        binding.btnExcluirCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ExcluirCategoria();
                limpaTela();
                buscaCategorias();
            }
        });

        binding.rdbReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indTipo = 0;
            }
        });

        binding.rdbDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indTipo=1;
            }
        });

        binding.rdbMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indTipo = 2;
            }
        });

        buscaCategorias();

        binding.listaCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickCategoria(categorias.get(i));
            }
        });

        catSelecionada = new CategoriasVO();
        return root;
    }

    private void salvarCategoria(){

        DB db = new DB(getContext());

        CategoriasVO categoria = new CategoriasVO();
        categoria.setCodCategoria(catSelecionada.getCodCategoria());
        categoria.setDesCategoria(binding.edtDescCategoria.getText().toString().trim());
        categoria.setIndTipo(indTipo);

        db.insereCategoria(categoria);
        db.close();

        if(categoria.getCodCategoria() != null){
            Toast.makeText(getContext(),getString(R.string.msg_atualizar),Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(),getString(R.string.msg_salvar),Toast.LENGTH_LONG).show();
        }

    }

    private void ExcluirCategoria(){

        DB db = new DB(getContext());
        db.removeCategoria(catSelecionada.getCodCategoria());
        db.close();

        Toast.makeText(getContext(),getString(R.string.msg_excluir),Toast.LENGTH_LONG).show();

    }
    private void limpaTela(){
        binding.edtDescCategoria.setText(null);
        binding.rdgCategoriaTipo.clearCheck();
        catSelecionada = new CategoriasVO();
    }

    private boolean verificaCampos(){

        if(TextUtils.isEmpty(binding.edtDescCategoria.getText())){
            binding.edtDescCategoria.requestFocus();
            Toast.makeText(getContext(),"Preencher a descrição da categoria!", Toast.LENGTH_LONG).show();
            return false;
        } else if(binding.rdbDespesa.isChecked() == false && binding.rdbMeta.isChecked() == false && binding.rdbReceita.isChecked()==false){

            Toast.makeText(getContext(),"Selecione o tipo da categoria!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void buscaCategorias(){
        try{

            DB db = new DB(getContext());

            BuscaCategoriasRequest req = new BuscaCategoriasRequest();
            req.setbVerificaMeta(false);

            binding.listaCategorias.setAdapter(new CategoriasAdapter(getActivity(), categorias = db.buscaCategorias(req)));

        } catch(Exception e){
            Log.println(Log.ERROR,"Categoria","Erro na busca das categorias" );
        }
    }

    private void clickCategoria(final CategoriasVO categoria) {

        binding.edtDescCategoria.setText(categoria.getDesCategoria());

        catSelecionada = categoria;

        if(categoria.getIndTipo() == 0){
            binding.rdgCategoriaTipo.check(R.id.rdb_receita);
            indTipo = 0;
        } else if (categoria.getIndTipo() == 1) {
            binding.rdgCategoriaTipo.check(R.id.rdb_despesa);
            indTipo=1;
        } else {
            binding.rdgCategoriaTipo.check(R.id.rdb_meta);
            indTipo=2;
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
}