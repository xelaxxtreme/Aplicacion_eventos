package my.wab.festejaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import my.wab.festejaper.fragment.inicio;
import my.wab.festejaper.fragment.ubicacion;

public class eventos_detalles extends AppCompatActivity {

    BottomNavigationView nvBarra;
    String[] cadena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_detalles);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        nvBarra = findViewById(R.id.nvBarra);

        Bundle extras =getIntent().getExtras();
        cadena = extras.getStringArray("datos");

        Fragment inicio = new inicio();
        Bundle bundle =new Bundle();
        bundle.putStringArray("datos",cadena);
        inicio.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.body_eventos,inicio).commit();
        nvBarra.setSelectedItemId(R.id.nav_inicio);

        nvBarra.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_inicio:
                        fragment = new inicio();
                        break;
                    case R.id.nav_ubicacion:
                        fragment = new ubicacion();
                        break;
                }
                Bundle bundle = new Bundle();
                bundle.putStringArray("datos",cadena);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.body_eventos, fragment).commit();
                return true;
            }
        });



    }

}