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

import uniftec.fabio.com.studeofin.BD.DB;
import uniftec.fabio.com.studeofin.BD.requests.BuscaCategoriasRequest;
import uniftec.fabio.com.studeofin.BD.requests.BuscaLancamentosRequest;
import uniftec.fabio.com.studeofin.BD.requests.RemoveMetaRequest;
import uniftec.fabio.com.studeofin.R;
import uniftec.fabio.com.studeofin.adapter.LancamentosAdapter;
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

                if(metaSelecionada != null && metaSelecionada.getCodMeta() != null) {
                    excluirMeta();
                    limparTela();
                    buscarMetas();
                } else {
                    Toast.makeText(getContext(),"Nenhuma meta selecionada para exclusão!", Toast.LENGTH_LONG).show();
                }

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
        buscarMetas();
        return root;
    }

    private void buscarMetas(){

        try{
            DB db = new DB(getContext());
            binding.listaMetas.setAdapter(new MetasAdapter(getActivity(),metas = db.buscaMetas()));
        } catch (Exception e) {
            Log.println(Log.ERROR,"Lançamentos","Erro na busca dos lançamentos" );
        }
    }

    private void salvarMeta(){

        DB db = new DB(getContext());

        //Cadastro da categoria que vai ser vinculado com a meta:

        CategoriasVO categoria = new CategoriasVO();
        categoria.setDesCategoria(binding.edtDescMeta.getText().toString().trim());
        categoria.setIndTipo(2);
        db.insereCategoria(categoria);

        //Verificação do código gerado da categoria

        BuscaCategoriasRequest req = new BuscaCategoriasRequest();
        req.setbVerificaMeta(true);
        db.buscaCategorias(req);
        ArrayList<CategoriasVO> buscaCategoria = new ArrayList<CategoriasVO>();
        buscaCategoria = db.buscaCategorias(req);
        codCategoria = buscaCategoria.get(0).getCodCategoria();

        //Cadastro da Meta
        MetasVO meta = new MetasVO();
        meta.setCodMeta(metaSelecionada.getCodMeta());
        meta.setDesMeta(binding.edtDescMeta.getText().toString().trim());
        meta.setVlrMeta(new BigDecimal(binding.edtVlrMeta.getText().toString()));
        meta.setDtaMeta(dtaMeta);
        meta.setCodCategoria(codCategoria);
        db.insereMeta(meta);

       if (metaSelecionada.getCodMeta() == null){
           Toast.makeText(getContext(), getString(R.string.msg_salvar), Toast.LENGTH_LONG).show();
       } else {
           Toast.makeText(getContext(), getString(R.string.msg_atualizar), Toast.LENGTH_LONG ).show();
       }

    }

    private void excluirMeta(){

        RemoveMetaRequest req = new RemoveMetaRequest();
        req.setCodCategoria(metaSelecionada.getCodCategoria());
        req.setCodMeta(metaSelecionada.getCodMeta());
        DB db = new DB(getContext());
        db.removeMeta(req);
        db.close();
        Toast.makeText(getContext(),getString(R.string.msg_excluir), Toast.LENGTH_LONG).show();

    }

    private void limparTela(){

        binding.edtDescMeta.setText(null);
        binding.edtDtaMeta.setText(null);
        binding.edtVlrMeta.setText(null);
        dtaMeta = null;
        metaSelecionada = new MetasVO();

    }

    private void clickMeta(final MetasVO meta){

        metaSelecionada = meta;
        dtaMeta = meta.getDtaMeta();
        binding.edtDescMeta.setText(meta.getDesMeta());
        binding.edtVlrMeta.setText(String.format("%.2f",meta.getVlrMeta()));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String data = sdf.format(meta.getDtaMeta());
        binding.edtDtaMeta.setText(data);

    }

    private boolean verificaCampos(){

        if(TextUtils.isEmpty(binding.edtDescMeta.getText())){
            binding.edtDescMeta.requestFocus();
            Toast.makeText(getContext(), "Preencher objetivo da meta!", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(binding.edtVlrMeta.getText())){
            binding.edtVlrMeta.requestFocus();
            Toast.makeText(getContext(), "Preencher valor da meta!", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(binding.edtDtaMeta.getText())){
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