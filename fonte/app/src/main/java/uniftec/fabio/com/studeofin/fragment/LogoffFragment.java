package uniftec.fabio.com.studeofin.fragment;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import uniftec.fabio.com.studeofin.LoginActivity;
import uniftec.fabio.com.studeofin.MainActivity;
import uniftec.fabio.com.studeofin.R;
import uniftec.fabio.com.studeofin.databinding.FragmentLogoffBinding;

public class LogoffFragment extends Fragment {

    FragmentLogoffBinding binding;
    Button btnSair;
    Button btnCancelar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLogoffBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnSair = binding.btnSalvar;
        btnCancelar = binding.btnCancelar;

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),getString(R.string.msg_logoff_efetuado), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), LoginActivity.class );
                startActivity(i);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainActivity.class );
                startActivity(i);
            }
        });
        return root;
    }
}