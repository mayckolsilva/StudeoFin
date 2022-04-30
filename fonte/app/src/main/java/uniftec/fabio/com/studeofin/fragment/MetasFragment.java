package uniftec.fabio.com.studeofin.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import uniftec.fabio.com.studeofin.adapter.MetasAdapter;
import uniftec.fabio.com.studeofin.databinding.FragmentMetasBinding;
import uniftec.fabio.com.studeofin.global.Global;
import uniftec.fabio.com.studeofin.vo.CategoriasVO;
import uniftec.fabio.com.studeofin.vo.MetasVO;


public class MetasFragment extends Fragment {

    private static FragmentMetasBinding binding;
    private SQLiteDatabase query;
    private CategoriasVO categoria;
    private ArrayList<MetasVO> metas;
    private Integer codMeta;
    private MetasVO metaSelecionada;
    private Integer codCategoria;
    public static Date dtaMeta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMetasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.listaMetas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickMeta(metas.get(i));
            }
        });

        binding.btnSalvarMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(verificaCampos()){
                    salvarMeta();
                    limparTela();
                    buscarMetas();
                }

            }
        });

        binding.btnExcluirMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query.delete("categorias","id_categoria = ?",new String[]{String.valueOf(metaSelecionada.getCodCategoria())});
                query.delete("metas","id_meta = ?", new String[]{String.valueOf(metaSelecionada.getCodMeta())});
                limparTela();
                buscarMetas();
            }
        });

        binding.btnAddMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limparTela();
            }
        });


        binding.btnCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragmentFinal();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });



        query = getActivity().openOrCreateDatabase("studeofin", android.content.Context.MODE_PRIVATE,
                null);

        limparTela();
        return root;

    }


    private void buscarMetas(){
        try{

            Cursor busca = query.rawQuery(" SELECT id_meta, des_meta, vlr_meta, cod_categoria " +
                    "FROM metas " +
                    "WHERE id_usuario =  " + Global.getIdUsuario(),null);

            metas = new ArrayList<MetasVO>();
            busca.moveToFirst();
            if(busca.getCount()>0){
                while(!busca.isAfterLast()){
                    MetasVO meta = new MetasVO();
                    meta.setCodMeta(busca.getInt(0));
                    meta.setDesMeta(busca.getString(1));
                    meta.setVlrMeta(BigDecimal.valueOf(busca.getDouble(2)));
                    meta.setCodCategoria(busca.getInt(3));
                    this.getMetas().add(meta);
                    busca.moveToNext();
                }
            }

            if(metas!=null){
                binding.listaMetas.setAdapter(new MetasAdapter(getActivity(),metas));
            }
        } catch(Exception e){
            Log.println(Log.ERROR,"Metas","Erro na busca das metas" );
        }
    }

    private void salvarMeta(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        String data = sdf.format(binding.edtDtaMeta.getText().toString().trim());

        if(metaSelecionada.getCodMeta()!=null){
            ContentValues values = new ContentValues();
            values.put("des_meta", binding.edtDescMeta.getText().toString().trim());
            values.put("id_meta", metaSelecionada.getCodMeta());
            values.put("vlr_meta", binding.edtVlrMeta.getText().toString());
            values.put("dta_meta", data);
            query.update("metas", values,"id_meta = ?", new String[]{String.valueOf(metaSelecionada.getCodMeta())} );
        }else {

            try {
                query.execSQL("INSERT INTO categorias (des_categoria, ind_tipo_categoria, id_usuario) VALUES " +
                        "( '" + binding.edtDescMeta.getText().toString().trim() + "'," + 2 + ","+ Global.getIdUsuario() + ")");


                Cursor busca = query.rawQuery("SELECT id_categoria FROM categorias ORDER BY id_categoria DESC", null);

                busca.moveToFirst();
                if(busca.getCount()>0){
                    codCategoria = busca.getInt(0);
                }

                query.execSQL("INSERT INTO metas (des_meta, cod_categoria, id_usuario, vlr_meta, dta_meta) VALUES " +
                        "( '" + binding.edtDescMeta.getText().toString().trim() + "'," + codCategoria + "," + Global.getIdUsuario() + "," + binding.edtVlrMeta.getText() + ", '" +  data + "')");
                limparTela();
                buscarMetas();
            } catch (Exception e ){
                Log.println(Log.ERROR,"Lancamento","Erro ao salvar lançamento!");
            }

        }


    }

    private void limparTela(){
        binding.edtDescMeta.setText(null);
        binding.edtDtaMeta.setText(null);
        binding.edtVlrMeta.setText(null);
        metaSelecionada = new MetasVO();
    }

    private void clickMeta(final MetasVO meta){
        metaSelecionada = meta;
        binding.edtDescMeta.setText(meta.getDesMeta());
        binding.edtVlrMeta.setText(String.format("%.2f",meta.getVlrMeta()));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String data = sdf.format(meta.getDtaMeta());
        binding.edtDtaMeta.setText(data);
    }

    private boolean verificaCampos(){
        if(TextUtils.isEmpty(binding.edtDescMeta.getText())){
            binding.edtDescMeta.requestFocus();
            Toast.makeText(getContext(), "Preencher descrição da meta!", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(binding.edtVlrMeta.getText())){
            binding.edtVlrMeta.requestFocus();
            Toast.makeText(getContext(), "Preencher valor da meta!", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(binding.edtDtaMeta.getText())){
            binding.edtDtaMeta.requestFocus();
            Toast.makeText(getContext(), "Preencher a data estipulada para atingir a meta!", Toast.LENGTH_LONG).show();
            return false;
        } else{
            return true;
        }
    }

    public static class DatePickerFragmentFinal extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog a = new DatePickerDialog(getActivity(), this, year, month, day);
            a.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return a;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            if (view.isShown()) {
                String data = ((day < 10) ? "0" : "") + day + "/" + ((month + 1) < 10 ? "0" + (month + 1) : "" + (month + 1)) + "/" + year;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date a = sdf.parse(data);

                    dtaMeta = a;

                    Calendar c = Calendar.getInstance();
                    c.setTime(dtaMeta);

                    binding.edtDtaMeta.setText(sdf.format(c.getTime()));

                } catch (Exception e) {

                }
            }
        }
    }


    public ArrayList<MetasVO> getMetas() {
        if (this.metas == null)
            this.metas = new ArrayList<MetasVO>();
        return metas;
    }

    public void setMetas(ArrayList<MetasVO> metas) {
        this.metas = metas;
    }

    public Integer getCodMeta() {
        return codMeta;
    }

    public void setCodMeta(Integer codMeta) {
        this.codMeta = codMeta;
    }

    public MetasVO getMetaSelecionada() {
        return metaSelecionada;
    }

    public void setMetaSelecionada(MetasVO metaSelecionada) {
        this.metaSelecionada = metaSelecionada;
    }
}