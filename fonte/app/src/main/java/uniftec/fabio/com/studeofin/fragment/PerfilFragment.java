package uniftec.fabio.com.studeofin.fragment;

import static uniftec.fabio.com.studeofin.MainActivity.drawableToBitmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import uniftec.fabio.com.studeofin.BD.DB;
import uniftec.fabio.com.studeofin.BD.requests.BuscaUsuarioRequest;
import uniftec.fabio.com.studeofin.CadastroUsuarioActivity;
import uniftec.fabio.com.studeofin.CancelarContaActivity;
import uniftec.fabio.com.studeofin.LoginActivity;
import uniftec.fabio.com.studeofin.MainActivity;
import uniftec.fabio.com.studeofin.R;
import uniftec.fabio.com.studeofin.RoundImage;
import uniftec.fabio.com.studeofin.databinding.FragmentPerfilBinding;
import uniftec.fabio.com.studeofin.global.Global;
import uniftec.fabio.com.studeofin.vo.UsuariosVO;

public class PerfilFragment extends Fragment {

    private SQLiteDatabase query;
    private static final int SELECAO_CAMERA = 1;
    private static final int SELECAO_GALEIRA = 2;
    private Intent data;
    FragmentPerfilBinding binding;

    public static ImageView imageUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.edtNome.setText(Global.getDesNome());
        binding.edtEmail.setText(Global.getDesEmail());
        binding.edtSobrenome.setText(Global.getDesSobreNome());

        if (Global.getImgFoto() != null) {
            byte[] bytes = Base64.decode(Global.getImgFoto().getBytes(), Base64.DEFAULT);
            ByteArrayInputStream imageStream = new ByteArrayInputStream(
                    bytes);
            Bitmap bmp_foto = BitmapFactory
                    .decodeStream(imageStream);
            RoundImage ri = new RoundImage(bmp_foto);
            binding.imagemUsuario.setImageDrawable(ri);
        }

        imageUsuario = binding.imagemUsuario;

        binding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View view) {


                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
                } else {
                    MainActivity.PROCESSID = MainActivity.CAMERA;

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, SELECAO_CAMERA);
                }
            }
        });

        binding.btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.PROCESSID = MainActivity.GALLERY;

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 99);
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

        binding.btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaCampos()) {
                    salvarUsuario();
                }
            }
        });

        return root;
    }

    private void salvarUsuario() {
        try {

            DB db = new DB(getContext());

            UsuariosVO usuario = new UsuariosVO();
            usuario.setIdUsuario(Global.getIdUsuario());
            usuario.setDesNome(binding.edtNome.getText().toString().trim());
            usuario.setDesSobreNome(binding.edtSobrenome.getText().toString().trim());
            usuario.setDesEmail(binding.edtEmail.getText().toString().trim());
            usuario.setDesSenha(binding.edtSenha.getText().toString().trim());

            if (binding.imagemUsuario != null) {
                Bitmap bitmap = drawableToBitmap(binding.imagemUsuario.getDrawable());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                usuario.setImgFoto(new String(android.util.Base64.encodeToString(byteArray, Base64.DEFAULT)));
            }

            db.insereUsuario(usuario);

            Toast.makeText(getContext(), getString(R.string.msg_cadastro_efetuado), Toast.LENGTH_LONG).show();

            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean verificaCampos() {
        if (!TextUtils.isEmpty(binding.edtSenha.getText())) {
            BuscaUsuarioRequest req = new BuscaUsuarioRequest();
            req.setDesEmail(binding.edtEmail.getText().toString().trim());
            req.setDesSenha(binding.edtSenha.getText().toString());

            DB db = new DB(getContext());

            return db.buscaUsuario(req);

        } else {
            Toast.makeText(getContext(), getString(R.string.msg_falha_autenticacao), Toast.LENGTH_LONG).show();
            return false;
        }
    }

}