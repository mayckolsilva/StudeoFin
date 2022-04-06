package uniftec.fabio.com.studeofin;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

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
    private SQLiteDatabase query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_usuario);

        desNome = (EditText) findViewById(R.id.edtNome);
        desSobrenome = (EditText) findViewById(R.id.edtSobrenome);
        desEmail = (EditText) findViewById(R.id.edtEmail);
        desSenha = (EditText) findViewById(R.id.edtSenha);
        btnSalvar = (Button) findViewById(R.id.btn_salvar);


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verificaCampos()){
                    try{
                        query = openOrCreateDatabase("studeofin",
                                MODE_PRIVATE,
                                null);
                        String str = "INSERT INTO usuarios (des_email, des_senha, des_nome, des_sobrenome) VALUES " +
                                "( '" + desEmail.getText().toString().trim() + "','" + desSenha.getText().toString().trim() + "','" + desNome.getText().toString().trim() + "','" + desSobrenome.getText().toString().trim() +"')";
                        query.execSQL("INSERT INTO usuarios (des_email, des_senha, des_nome, des_sobrenome) VALUES " +
                                "( '" + desEmail.getText().toString().trim() + "','" + desSenha.getText().toString().trim() + "','" + desNome.getText().toString().trim() + "','" + desSobrenome.getText().toString().trim() +"')");
                        Toast.makeText(CadastroUsuarioActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(CadastroUsuarioActivity.this,LoginActivity.class );
                        startActivity(i);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

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