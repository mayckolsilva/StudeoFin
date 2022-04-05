package uniftec.fabio.com.studeofin;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //setContentView(R.layout.login);

        try{
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("studeofin",null, null);

            db.execSQL("DROP TABLE usuarios");
            db.execSQL("CREATE TABLE IF NOT EXISTS usuarios (id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, des_email VARCHAR(50) NOT NULL, des_senha VARCHAR(20) NOT NULL, desNome VARCHAR(50) NOT NULL, desSobrenome VARCHAR(50) NOT NULL )");
            db.execSQL("CREATE TABLE IF NOT EXISTS meta (id_meta INTEGER PRIMARY KEY AUTOINCREMENT, des_meta VARCHAR(100) NOT NULL, vlr_meta REAL )");

        } catch (Exception e){
            e.printStackTrace();
        }


    }
}