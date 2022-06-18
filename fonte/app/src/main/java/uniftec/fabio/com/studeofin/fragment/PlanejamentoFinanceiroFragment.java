package uniftec.fabio.com.studeofin.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import uniftec.fabio.com.studeofin.BD.DB;
import uniftec.fabio.com.studeofin.BD.requests.BuscaLancamentosRequest;
import uniftec.fabio.com.studeofin.adapter.MetasAdapter;
import uniftec.fabio.com.studeofin.adapter.PlanejamentosAdapter;
import uniftec.fabio.com.studeofin.databinding.FragmentPlanejamentoFinanceiroBinding;
import uniftec.fabio.com.studeofin.vo.MetasVO;

public class PlanejamentoFinanceiroFragment extends Fragment {

    private FragmentPlanejamentoFinanceiroBinding binding;

    private ArrayList<MetasVO> listMetas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPlanejamentoFinanceiroBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        buscarMetas();

        return root;
    }


    private void buscarMetas(){

        try{
            DB db = new DB(getContext());
            BuscaLancamentosRequest req = new BuscaLancamentosRequest();
            binding.listaPlanejamentos.setAdapter(new PlanejamentosAdapter(getActivity(), db.calculoPlanejamentoFinanceiro()));
        } catch (Exception e) {
            Log.println(Log.ERROR,"Lançamentos","Erro na busca dos lançamentos" );
        }
    }
}