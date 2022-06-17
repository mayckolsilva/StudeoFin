package uniftec.fabio.com.studeofin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import uniftec.fabio.com.studeofin.BD.DB;
import uniftec.fabio.com.studeofin.BD.requests.BuscaUsuarioRequest;
import uniftec.fabio.com.studeofin.global.Global;
import uniftec.fabio.com.studeofin.global.TLSSocketFactory;

public class EsqueceuSenha extends AppCompatActivity {


    Button btnRecuperarSenha;
    EditText desEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esqueceu_senha);

        btnRecuperarSenha = (Button) findViewById(R.id.btn_recuperar_senha);

        desEmail = (EditText) findViewById(R.id.input_email);

        btnRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(desEmail.getText())){
                    if(validaUsuario()) {
                        recuperarSenha();
                        Toast.makeText(EsqueceuSenha.this, getString(R.string.msg_email_enviado), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(EsqueceuSenha.this, LoginActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(EsqueceuSenha.this, getString(R.string.msg_falha_autenticacao), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean validaUsuario(){

        BuscaUsuarioRequest req = new BuscaUsuarioRequest();
        req.setDesEmail(desEmail.getText().toString().trim());
        DB db = new DB(getApplicationContext());

        return db.buscaUsuario(req);

    }


    private boolean validarConexaoAInternet() {

        boolean ok = false;
        ConnectivityManager cm = (ConnectivityManager) EsqueceuSenha.this.getSystemService(Context.CONNECTIVITY_SERVICE);//Pego a conectividade do contexto o qual o metodo foi chamado
        NetworkInfo netInfo = cm.getActiveNetworkInfo();//Crio o objeto netInfo que recebe as informacoes da NEtwork
        if ((netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable())) //Se o objeto for nulo ou nao tem conectividade retorna false
            ok = true;

        return ok;
    }

    private void recuperarSenha(){

        if (!validarConexaoAInternet()) {
            Toast.makeText(EsqueceuSenha.this, "Verifique a sua conexãõ com a internet", Toast.LENGTH_LONG).show();
            return;
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        BufferedReader reader = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        OutputStreamWriter wr = null;
        String retornoErro = null;
        String conteudoRetorno = null;


        try {

            URL url = new URL("https://api.3wedesenv.com.br/MinhaEscolaTeen/rest/integracao/testeemail");

            String jsonEnvio = "{ \"assunto\": \"Conforme solicitação, segue a senha: " + Global.getDesSenha() + " \", \"mensagem\": \"StudeoFin - Recuperação de Senha\", \"destinatario\": \"" + desEmail.getText() + "\"}";


            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("x-api-token", "diego-teste");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {

                if (conn instanceof HttpsURLConnection) {
                    HttpsURLConnection a = (HttpsURLConnection) conn;
                    TLSSocketFactory myTlsSocketFactory = new TLSSocketFactory();
                    a.setSSLSocketFactory(myTlsSocketFactory);
                }
            }


            wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(jsonEnvio);
            wr.flush();

            boolean error = false;


            if (conn.getResponseCode() == 200 || conn.getResponseCode() == 204) {
                is = conn.getInputStream();
            } else {
                error = true;
                is = conn.getErrorStream();
            }


            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder sbResp = new StringBuilder();

            String line = reader.readLine();
            while (line != null) {
                sbResp.append(line);
                line = reader.readLine();
            }

            conteudoRetorno = sbResp.toString();

        } catch (Exception ex) {
            retornoErro = ex.getMessage();

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            sStackTrace.toString();


        } finally  {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex) {

            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception ex) {

            }
            try {
                if (wr != null) {
                    wr.close();
                }
            } catch (Exception ex) {
            }

            try {
                if (conn != null)
                    conn.disconnect();
            } catch (Exception e1) {

            }
        }

    }
}