package uniftec.fabio.com.studeofin;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import uniftec.fabio.com.studeofin.global.Global;

public class LoginActivity extends Activity {
    private EditText desEmail;
    private EditText desSenha;
    private TextView btnCriarConta;
    private Button btnLogin;
    private SQLiteDatabase query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        try{
            query = openOrCreateDatabase("studeofin",
                MODE_PRIVATE,
                null);


            query.execSQL("CREATE TABLE IF NOT EXISTS usuarios (id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, des_email VARCHAR(50) NOT NULL, des_senha VARCHAR(20) NOT NULL, des_nome VARCHAR(50) NOT NULL, des_sobrenome VARCHAR(50) NOT NULL )");
            query.execSQL("CREATE TABLE IF NOT EXISTS meta (id_meta INTEGER PRIMARY KEY AUTOINCREMENT, des_meta VARCHAR(100) NOT NULL, vlr_meta REAL )");

            System.out.println("databse has been creates.....");
        } catch (Exception e){
            e.printStackTrace();
        }

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
                };

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

    private boolean validaLogin() {

        try {
            Cursor busca = query.rawQuery("SELECT id_usuario, des_email, des_nome, des_sobrenome FROM usuarios WHERE des_email = '" +
                            desEmail.getText().toString().trim() + "' AND des_senha = '" + desSenha.getText().toString().trim() + "'",
                    null);

            int indiceIdUsuario = busca.getColumnIndex("id_usuario");
            int indiceDesEmail = busca.getColumnIndex("des_email");
            int indiceDesNome= busca.getColumnIndex("des_nome");
            int indiceSobreNome = busca.getColumnIndex("des_sobrenome");
            busca.moveToFirst();
            if(busca.getCount()>0){
                Global.setIdUsuario(busca.getInt(indiceIdUsuario));
                Global.setDesEmail(busca.getString(indiceDesEmail));
                Global.setDesNome(busca.getString(indiceDesNome));
                Global.setDesSobreNome(busca.getString(indiceSobreNome));
               // Log.i("Email: ", busca.getString(indiceEmail));
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }



}