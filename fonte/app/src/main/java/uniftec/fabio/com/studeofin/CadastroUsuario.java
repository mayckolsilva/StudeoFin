package uniftec.fabio.com.studeofin;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import android.os.Bundle;

public class CadastroUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_usuario);
    }
/*
    CircleImageView campoFoto = (CircleImageView) view.findViewById(R.id.item_foto);
    String caminhoFoto = aluno.getCaminhoFoto();
    if (caminhoFoto != null) {
        Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
        Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true); campoFoto.setImageBitmap(bitmapReduzido);
        campoFoto.setScaleType(ImageView.ScaleType.FIT_XY); }

return view; }

 */
}