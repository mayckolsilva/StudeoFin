package uniftec.fabio.com.studeofin.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import uniftec.fabio.com.studeofin.BD.DB;
import uniftec.fabio.com.studeofin.R;
import uniftec.fabio.com.studeofin.databinding.FragmentAlertaGastosBinding;
import uniftec.fabio.com.studeofin.global.Global;
import uniftec.fabio.com.studeofin.vo.AlertaGastosVO;

public class AlertaGastosFragment extends Fragment {

    private FragmentAlertaGastosBinding binding;
    private AlertaGastosVO alerta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAlertaGastosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnSalvarAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(binding.edtVlrAlerta.getText())){
                    salvarAlertaGastos();
                } else {
                    excluirAlertaGastos();
                    limparTela();
                }
                buscarAlertaGastos();
            }
        });

        buscarAlertaGastos();
        return root;
    }

    private void buscarAlertaGastos(){

        try{
            DB db = new DB(getContext());
            alerta = db.buscaAlertaGastos();
            if(alerta.getVlr_alerta() != null){

                binding.edtVlrAlerta.setText(String.format("%.2f",alerta.getVlr_alerta()));
            }
            db.close();
        } catch (Exception e){
            Log.println(Log.ERROR,"Alerta de Gastos","Erro na busca do alerta de Gastos" );
        }

    }

    private void salvarAlertaGastos(){

        DB db = new DB(getContext());
        alerta.setVlr_alerta(new BigDecimal(binding.edtVlrAlerta.getText().toString()));
        db.salvarAlertaGastos(alerta);
        db.close();

        if(alerta.getCodAlerta() != null){
            Toast.makeText(getContext(),getString(R.string.msg_atualizar),Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(),getString(R.string.msg_salvar),Toast.LENGTH_LONG).show();
        }
    }

    private void excluirAlertaGastos(){

        DB db = new DB(getContext());
        db.removeALertaGastos(alerta);
        db.close();

        Toast.makeText(getContext(),getString(R.string.msg_excluir),Toast.LENGTH_LONG).show();
    }

    private void  limparTela(){

        binding.edtVlrAlerta.setText(null);
        alerta = new AlertaGastosVO();
        alerta.setId_usuario(Global.getIdUsuario());

    }

    public AlertaGastosVO getAlerta() {
        return alerta;
    }

    public void setAlerta(AlertaGastosVO alerta) {
        this.alerta = alerta;
    }
}