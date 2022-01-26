package my.wab.festejaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import my.wab.festejaper.db.cEventos;
import my.wab.festejaper.db.conexionSQL;


public class MainActivity extends AppCompatActivity {

    RecyclerView rvEventos;


    ArrayList<cEventos> lEventos;

    ListaEventos adapter;
    TextView tvDepartamento;
    Spinner spDepatamento;
    String Departamento;
    TextView tvMenuTitulo;

    conexionSQL con;

    private InterstitialAd mInterstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvEventos = (RecyclerView)findViewById(R.id.rvEventos);
        tvDepartamento = (TextView) findViewById(R.id.tvMenuDepartamentos);
        spDepatamento=(Spinner)findViewById(R.id.spDepartamento);
        tvMenuTitulo = (TextView)findViewById(R.id.tvMenuTitulo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TituloMatizado(tvMenuTitulo,
                getResources().getColor(R.color.verde1),
                getResources().getColor(R.color.naranja1),
                getResources().getColor(R.color.rosa1));

        lEventos = new ArrayList<>();
        rvEventos.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        rvEventos.setLayoutManager(gridLayoutManager);
        adapter = new ListaEventos(lEventos,this);
        rvEventos.setAdapter(adapter);

        /****************** inicio de publicidad ******************************/
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-5582861426500490/5523437225", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });

        /****************** final de publicidad ****************************/
        con = new conexionSQL(getApplicationContext(), "db_FestejaPeru", null, 1);


        String cantidadDatos = CantidadRegistros();
        if(cantidadDatos.equals("0")){
            DialogBDVacia("Para el correcto funcionamiento de la aplicación, se necesita conexión a INTERNET, por favor inténtelo más tarde.");
        }else {
            //cargar base de datos sin conexion
            ConsultarEventos("Todos");
        }

        //inicio spinner departamentos
        ArrayAdapter spAdapter = ArrayAdapter.createFromResource(this,
                R.array.Departamentos,
                R.layout.spinner_tecto);
        spAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spDepatamento.setAdapter(spAdapter);
        //fin spinner departamentos

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                }
                boolean estado = isNetworkAvailable(getApplicationContext());
                if(estado) {
                    String artistas = lEventos.get(rvEventos.getChildAdapterPosition(view)).getArtistas();
                    String comprar = lEventos.get(rvEventos.getChildAdapterPosition(view)).getComprar();
                    String departamento = lEventos.get(rvEventos.getChildAdapterPosition(view)).getDepartamento();
                    String direccion = lEventos.get(rvEventos.getChildAdapterPosition(view)).getDireccion();
                    String fecha = lEventos.get(rvEventos.getChildAdapterPosition(view)).getFecha();
                    String hora = lEventos.get(rvEventos.getChildAdapterPosition(view)).getHora();
                    String imagen = lEventos.get(rvEventos.getChildAdapterPosition(view)).getImagen();
                    String local = lEventos.get(rvEventos.getChildAdapterPosition(view)).getLocal();
                    String nombre = lEventos.get(rvEventos.getChildAdapterPosition(view)).getNombre();
                    String provincia = lEventos.get(rvEventos.getChildAdapterPosition(view)).getProvincia();
                    String tipoEvento = lEventos.get(rvEventos.getChildAdapterPosition(view)).getTipoEvento();
                    String completo[] = {artistas,comprar, departamento, direccion, fecha, hora, imagen, local, nombre, provincia, tipoEvento};
                    Intent intent = new Intent(view.getContext(), eventos_detalles.class);
                    intent.putExtra("datos", completo);
                    startActivityForResult(intent, 0);
                }
                else {
                    DialogEstadoInternet("Para ver mas detalles del evento y la localización exacta necesita INTERNET, verifique su conexión.");
                }
            }
        });
        spDepatamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Departamento = parent.getItemAtPosition(position).toString();
                lEventos.clear();
                ConsultarEventos(Departamento);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        EliminarEventos();
    }

    public void ConsultarEventos(String departamento){
        SQLiteDatabase dbSqlite = con.getReadableDatabase();
        Cursor cursor = null;
        if(departamento.equals("Todos")) {
            cursor = dbSqlite.rawQuery("SELECT Artistas,Comprar,Departamento,Direccion,Fecha,Hora,Imagen,Local,Nombre,Provincia,TipoEvento FROM tEventos ORDER BY Fecha", null);
        }
        else {
            cursor = dbSqlite.rawQuery("SELECT Artistas,Comprar,Departamento,Direccion,Fecha,Hora,Imagen,Local,Nombre,Provincia,TipoEvento FROM tEventos WHERE Departamento='"+departamento+"' ORDER BY Fecha", null);
        }
        cEventos e = null;
        while(cursor.moveToNext()){
            e = new cEventos();
            e.setArtistas(cursor.getString(0));
            e.setComprar(cursor.getString(1));
            e.setDepartamento(cursor.getString(2));
            e.setDireccion(cursor.getString(3));
            e.setFecha(cursor.getString(4));
            e.setHora(cursor.getString(5));
            e.setImagen(cursor.getString(6));
            e.setLocal(cursor.getString(7));
            e.setNombre(cursor.getString(8));
            e.setProvincia(cursor.getString(9));
            e.setTipoEvento(cursor.getString(10));
            lEventos.add(e);
        }
        adapter.notifyDataSetChanged();

    }

    public void EliminarEventos(){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String fecha = sdf.format(date);
        SQLiteDatabase dbSQLITE =con.getReadableDatabase();
        String consultaEliminar= "DELETE FROM tEventos WHERE Fecha < '"+fecha+"'";
        dbSQLITE.execSQL(consultaEliminar);
    }

    public void TituloMatizado(TextView tv,int...color){
        TextPaint paint = tv.getPaint();
        float with = paint.measureText(tv.getText().toString());
        Shader shader = new LinearGradient(0,80,0  ,0,color,null,Shader.TileMode.CLAMP);
        tv.getPaint().setShader(shader);
        tv.setTextColor(color[0]);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true;
                }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                    return true;
                }
            }
        }
        return false;
    }
    public void DialogEstadoInternet(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Advertencia");
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void DialogBDVacia(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Advertencia");
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public String CantidadRegistros() {
        SQLiteDatabase dbSqlite = con.getReadableDatabase();
        Cursor cursor = dbSqlite.rawQuery("SELECT * FROM tEventos",null);
        String numeroRegistros =  cursor.getCount()+"";
        return numeroRegistros.trim();
    }
}