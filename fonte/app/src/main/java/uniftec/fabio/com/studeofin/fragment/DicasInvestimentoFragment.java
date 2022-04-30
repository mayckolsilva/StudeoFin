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

        String[] pagina = {
                "https://economia.uol.com.br/",
                "https://mepoupe.com/",
                "https://www.oprimorico.com.br/",
                "https://investidorsardinha.r7.com/",
                "https://fontedafortuna.com/",
                "https://hackeandofinancas.com.br/"
        };

        //páginas
        binding.dica1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(pagina[0]));
                startActivity(acesso);
            }
        });
        binding.dica2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(pagina[1]));
                startActivity(acesso);
            }
        });
        binding.dica3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(pagina[2]));
                startActivity(acesso);
            }
        });
        binding.dica4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(pagina[3]));
                startActivity(acesso);
            }
        });
        binding.dica5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(pagina[4]));
                startActivity(acesso);
            }
        });
        binding.dica6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(pagina[5]));
                startActivity(acesso);
            }
        });

        String[] youtube = {
                "https://www.youtube.com/user/uol",
                "https://www.youtube.com/c/Mepoupenaweb",
                "https://www.youtube.com/c/ThiagoNigro",
                "https://www.youtube.com/c/investidorsardinha",
                "https://www.youtube.com/c/FontedaFortuna",
                "https://www.youtube.com/channel/UC-ezuc6BSkMhdPIoaEU8S1g"
        };

        //youtube
        binding.imageUol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube[0]));
                startActivity(acesso);
            }
        });
        binding.imageMePoupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube[1]));
                startActivity(acesso);
            }
        });
        binding.imagePrimoRico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube[2]));
                startActivity(acesso);
            }
        });
        binding.imageSardinha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube[3]));
                startActivity(acesso);
            }
        });
        binding.imageFonte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube[4]));
                startActivity(acesso);
            }
        });
        binding.imageFui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acesso = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube[5]));
                startActivity(acesso);
            }
        });

        binding.infoTela.setText("Esta tela refere-se à busca de conhecimento e receber dicas sobre a educação financeira e investimentos." + '\n' +
        "Clicando em cima do nome, abrirá a página web de cada parceiro, clicando no ícone do vídeo, será encaminhado para o canal do Youtube do parceiro.");

        return root;
    }
}