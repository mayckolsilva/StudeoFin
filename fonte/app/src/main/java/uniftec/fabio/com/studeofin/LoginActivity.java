package uniftec.fabio.com.studeofin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    private EditText desUsuario;
    private EditText desSenha;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        desUsuario = (EditText) findViewById(R.id.input_email);
        desSenha = (EditText) findViewById(R.id.input_senha);

        btnLogin = (Button) findViewById(R.id.btn_login);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validaLogin()){
                    Intent i = new Intent(LoginActivity.this,PrincipalActivity.class );
                    startActivity(i);
                };

            }
        });






    }



    private boolean validaLogin() {
        if(desSenha == null || desSenha == null){

            Toast.makeText(LoginActivity.this,"Campos usuários e senha são obrigatórios", Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

}