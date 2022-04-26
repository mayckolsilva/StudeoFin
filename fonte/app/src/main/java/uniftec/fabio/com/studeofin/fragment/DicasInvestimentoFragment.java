package uniftec.fabio.com.studeofin.fragment;

import android.content.Intent;
import android.net.Uri;
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



        binding.dica1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = "https://economia.uol.com.br/";
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(acesso);
            }
        });
        binding.dica2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = "https://mepoupe.com/";
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(acesso);
            }
        });
        binding.dica3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = "https://www.oprimorico.com.br/";
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(acesso);
            }
        });
        binding.dica4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = "https://investidorsardinha.r7.com/";
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(acesso);
            }
        });
        binding.dica5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = "https://fontedafortuna.com/";
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(acesso);
            }
        });
        binding.dica6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = "https://hackeandofinancas.com.br/";
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(acesso);
            }
        });
        return root;
    }
}