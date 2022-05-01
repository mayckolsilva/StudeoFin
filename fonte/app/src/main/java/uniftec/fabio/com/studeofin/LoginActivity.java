package uniftec.fabio.com.studeofin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import uniftec.fabio.com.studeofin.BD.DB;
import uniftec.fabio.com.studeofin.BD.requests.BuscaUsuarioRequest;

public class LoginActivity extends Activity {
    private EditText desEmail;
    private EditText desSenha;
    private TextView btnCriarConta;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        desEmail = (EditText) findViewById(R.id.input_email);
        desSenha = (EditText) findViewById(R.id.input_senha);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnCriarConta = (TextView) findViewById(R.id.link_criar_conta);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validaLogin()){
                    Intent i = new Intent(LoginActivity.this, MainActivity.class );
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "Usuário inválido!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,CadastroUsuarioActivity.class );
                startActivity(i);
            }
        });
    }

    private boolean validaLogin(){

        BuscaUsuarioRequest req = new BuscaUsuarioRequest();
        req.setDesEmail(desEmail.getText().toString().trim());
        req.setDesSenha(desSenha.getText().toString().trim());

        DB db = new DB(getApplicationContext());

        return db.buscaUsuario(req);

    }

}