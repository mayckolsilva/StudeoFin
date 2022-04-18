package uniftec.fabio.com.studeofin.fragment;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import uniftec.fabio.com.studeofin.R;
import uniftec.fabio.com.studeofin.adapter.CategoriasAdapter;
import uniftec.fabio.com.studeofin.databinding.FragmentCategoriasBinding;
import uniftec.fabio.com.studeofin.global.Global;
import uniftec.fabio.com.studeofin.vo.CategoriasVO;

public class CategoriasFragment extends Fragment {

    FragmentCategoriasBinding binding;
    private SQLiteDatabase query;
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

                    query = getActivity().openOrCreateDatabase("studeofin", android.content.Context.MODE_PRIVATE,
                            null);

                    if(catSelecionada.getCodCategoria()!=null){

                        ContentValues values = new ContentValues();
                        values.put("des_categoria", binding.edtDescCategoria.getText().toString().trim());
                        values.put("ind_tipo_categoria", indTipo);
                        query.update("categorias", values,"id_categoria = ?", new String[]{String.valueOf(catSelecionada.getCodCategoria())} );
                    } else {
                        query.execSQL("INSERT INTO categorias (des_categoria, ind_tipo_categoria, id_usuario) VALUES " +
                                "( '" + binding.edtDescCategoria.getText().toString().trim() + "'," + indTipo + ","+ Global.getIdUsuario() + ")");

                    }
                    limpaTela();
                    buscaCategorias();
                }
            }
        });

        binding.btnExcluirCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query.delete("categorias","id_categoria = ?", new String[]{String.valueOf(catSelecionada.getCodCategoria())});
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
        return root;
    }

    private void limpaTela(){
        binding.edtDescCategoria.setText(null);
        binding.rdgCategoriaTipo.clearCheck();
        catSelecionada = new CategoriasVO();
    }

    private boolean verificaCampos(){

        if(binding.txtDescCategoria == null){
            return false;
        } else if(binding.rdbDespesa.isChecked() == false && binding.rdbMeta.isChecked() == false && binding.rdbReceita.isChecked()==false){
            return false;
        } else {
            return true;
        }
    }

    private void buscaCategorias(){
        try{
            query = getActivity().openOrCreateDatabase("studeofin", android.content.Context.MODE_PRIVATE,
                    null);

            Cursor busca = query.rawQuery(" SELECT id_categoria, des_categoria, ind_tipo_categoria, id_meta " +
                                                "FROM categorias " +
                                                "WHERE id_usuario =  " + Global.getIdUsuario(),null);

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
                binding.listaCategorias.setAdapter(new CategoriasAdapter(getActivity(),categorias));
            }
        } catch(Exception e){
            Log.println(Log.ERROR,"Categoria","Erro na busca da categoria" );
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