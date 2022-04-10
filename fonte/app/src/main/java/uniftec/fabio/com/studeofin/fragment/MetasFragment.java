package uniftec.fabio.com.studeofin.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uniftec.fabio.com.studeofin.R;
import uniftec.fabio.com.studeofin.databinding.FragmentDicasInvestimentoBinding;
import uniftec.fabio.com.studeofin.databinding.FragmentMetasBinding;


public class MetasFragment extends Fragment {

    private FragmentMetasBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMetasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;

    }
}