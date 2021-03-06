package uniftec.fabio.com.studeofin;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import uniftec.fabio.com.studeofin.BD.DB;
import uniftec.fabio.com.studeofin.BD.requests.BuscaUsuarioRequest;
import uniftec.fabio.com.studeofin.vo.UsuariosVO;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText desNome, desSobrenome, desEmail, desSenha;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_usuario);

        desNome = (EditText) findViewById(R.id.edtNome);
        desSobrenome = (EditText) findViewById(R.id.edtSobrenome);
        desEmail = (EditText) findViewById(R.id.edtEmail);
        desSenha = (EditText) findViewById(R.id.edtSenha);
        btnSalvar = (Button) findViewById(R.id.btn_salvar);

        btnSalvar.setOnClickListener(view -> {
            if(verificaCampos()){
                try{

                    DB db = new DB(getApplicationContext());

                    BuscaUsuarioRequest req = new BuscaUsuarioRequest();
                    req.setDesEmail(desEmail.getText().toString().trim());

                    if(!db.buscaUsuario(req)) {

                        UsuariosVO usuario = new UsuariosVO();
                        usuario.setDesNome(desNome.getText().toString().trim());
                        usuario.setDesSobreNome(desSobrenome.getText().toString().trim());
                        usuario.setDesEmail(desEmail.getText().toString().trim());
                        usuario.setDesSenha(desSenha.getText().toString().trim());
                        db.insereUsuario(usuario);

                        Toast.makeText(CadastroUsuarioActivity.this, getString(R.string.msg_cadastro_efetuado), Toast.LENGTH_LONG).show();

                        Intent i = new Intent(CadastroUsuarioActivity.this,LoginActivity.class );
                        startActivity(i);

                    } else {
                        Toast.makeText(CadastroUsuarioActivity.this, getString(R.string.msg_cadastro_erro), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean verificaCampos() {
        if(desEmail == null || desSobrenome == null || desEmail == null || desSenha == null){
            Toast.makeText(CadastroUsuarioActivity.this,"Preencha todos os dados!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }
}