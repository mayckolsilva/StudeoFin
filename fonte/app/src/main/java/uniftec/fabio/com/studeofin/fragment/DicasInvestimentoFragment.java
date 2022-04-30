package uniftec.fabio.com.studeofin.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import uniftec.fabio.com.studeofin.databinding.FragmentDicasInvestimentoBinding;


public class DicasInvestimentoFragment extends Fragment {

    private FragmentDicasInvestimentoBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDicasInvestimentoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        return root;


    }
}