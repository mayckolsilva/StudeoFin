package uniftec.fabio.com.studeofin.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import uniftec.fabio.com.studeofin.CancelarContaActivity;
import uniftec.fabio.com.studeofin.databinding.FragmentPerfilBinding;
import uniftec.fabio.com.studeofin.global.Global;

public class PerfilFragment extends Fragment {

    private SQLiteDatabase query;
    private static final int SELECAO_CAMERA = 1;
    private static final int SELECAO_GALEIRA = 2;
    private Intent data;
    FragmentPerfilBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.edtNome.setText(Global.getDesNome());
        binding.edtEmail.setText(Global.getDesEmail());
        binding.edtSobrenome.setText(Global.getDesSobreNome());

        binding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(intent.resolveActivity(getActivity().getPackageManager())!= null) {
                    //someActivityResultLauncher.launch(intent);
                    startActivityForResult(intent, SELECAO_CAMERA);


                }
            }
        });


        binding.linkCancelarConta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                query = getActivity().openOrCreateDatabase("studeofin", android.content.Context.MODE_PRIVATE,
                        null);

                Cursor busca = query.rawQuery("SELECT id_usuario, des_email, des_nome, des_sobrenome FROM usuarios WHERE des_email = '" +
                                binding.edtEmail.getText().toString().trim() + "' AND des_senha = '" + binding.edtSenha.getText().toString().trim() + "'",
                        null);

                busca.moveToFirst();
                if (busca.getCount() > 0) {
                    Intent i = new Intent(getActivity(), CancelarContaActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "Senha não confere, exclusão de conta cancelada!", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;

    }
}