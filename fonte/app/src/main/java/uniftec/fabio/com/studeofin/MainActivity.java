package uniftec.fabio.com.studeofin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_planejamento_financeiro: {
                Toast.makeText(this, "Planejamento Financeiro", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_gerir_receitas_despesas: {
                Toast.makeText(this, "Gerir Receitas e Despesas", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_controle_saldo: {
                Toast.makeText(this, "Controle de Saldo", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_alerta_gastos: {
                Toast.makeText(this, "Alerta de Gastos", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_categorias: {
                Toast.makeText(this, "Categorias", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_dicas_investimentos: {
                Toast.makeText(this, "Dicas de Investimento", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_metas: {
                Toast.makeText(this, "Metas", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_perfil: {
                Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_sair: {
                Toast.makeText(this, "Sair", Toast.LENGTH_SHORT).show();
                break;
            }
            default: {
                Toast.makeText(this, "Menu", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

}

