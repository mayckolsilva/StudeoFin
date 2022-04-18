package uniftec.fabio.com.studeofin.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import uniftec.fabio.com.studeofin.R;
import uniftec.fabio.com.studeofin.adapter.CategoriasAdapter;
import uniftec.fabio.com.studeofin.databinding.FragmentHomeBinding;
import uniftec.fabio.com.studeofin.databinding.FragmentReceitasDespesasBinding;
import uniftec.fabio.com.studeofin.global.Global;
import uniftec.fabio.com.studeofin.vo.CategoriasVO;


public class ReceitasDespesasFragment extends Fragment {
    private FragmentReceitasDespesasBinding binding;

    private SQLiteDatabase query;
    private ArrayList<CategoriasVO> categorias;
    private Integer codCategoria;


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

            }
        });

        binding.btnExcluirLancamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        carregarSpinnerCategoria();

        buscarLancamentos();
        return root;
    }

    private void buscarLancamentos(){


    }
    private void carregarSpinnerCategoria(){
        try{
            query = getActivity().openOrCreateDatabase("studeofin", android.content.Context.MODE_PRIVATE,
                    null);

            Cursor busca = query.rawQuery(" SELECT id_categoria, des_categoria, ind_tipo_categoria, id_meta " +
                    " FROM categorias " +
                    " WHERE id_usuario =  " + Global.getIdUsuario() +
                    " ORDER BY des_categoria",null);

            int indiceIdCategoria = busca.getColumnIndex("id_categoria");
            int indiceDesCategoria = busca.getColumnIndex("des_categoria");
            int indiceIndTipo= busca.getColumnIndex("ind_tipo_categoria");
            int indiceIdMeta = busca.getColumnIndex("id_meta");


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