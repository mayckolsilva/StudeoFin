package uniftec.fabio.com.studeofin;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import uniftec.fabio.com.studeofin.global.Global;

public class CancelarContaActivity extends AppCompatActivity {

    private SQLiteDatabase query;
    Button btnOk, btnCancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancelar_conta);

        btnOk = findViewById(R.id.btn_OK);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try{
                    query = openOrCreateDatabase("studeofin", MODE_PRIVATE,
                            null);

                    query.delete("usuarios","des_email = ?", new String[]{Global.getDesEmail()});
                    Intent i = new Intent(CancelarContaActivity.this, LoginActivity.class );
                    startActivity(i);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btnCancelar = findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CancelarContaActivity.this,  MainActivity.class );
                startActivity(i);
            }
        });
    }
}
