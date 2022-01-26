package my.wab.festejaper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import my.wab.festejaper.db.Interface;
import my.wab.festejaper.db.cEventos;
import my.wab.festejaper.db.conexionSQL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class bienvenida extends AppCompatActivity {
    SpinKitView spin;
    Retrofit retrofit;
    List<cEventos> EventosAPI;
    Date recuperarFecha= Calendar.getInstance().getTime();

    conexionSQL con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        spin = (SpinKitView) findViewById(R.id.spin);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://us-central1-festeja-peru-aec5e.cloudfunctions.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        con = new conexionSQL(getApplicationContext(),"db_FestejaPeru",null,1);

        boolean conexion = isNetworkAvailable(getApplicationContext());
        if(conexion){
            ObtenerDatos();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(bienvenida.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2500);
    }

    public void ObtenerDatos(){
        EventosAPI = new ArrayList<>();
        Interface service = retrofit.create(Interface.class);
        Call<List<cEventos>> respuesta = service.obtener();
        respuesta.enqueue(new Callback<List<cEventos>>() {
            @Override
            public void onResponse(Call<List<cEventos>> call, Response<List<cEventos>> response) {
                if(response.isSuccessful()){
                    EventosAPI = response.body();
                    for(int i=0;i<EventosAPI.size();i++){
                        cEventos e = EventosAPI.get(i);
                        String[] c = Comparar(e.getId());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String auxDate = sdf.format(recuperarFecha);
                        Date fechaFB = new Date();
                        try {
                            fechaFB = sdf.parse(e.getFecha());
                            recuperarFecha = sdf.parse(auxDate);
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        if(!e.getId().equals(c[0]) && (fechaFB.after(recuperarFecha) )){
                            ReistrarEventos(e.getId(),e.getArtistas(),e.getComprar(),e.getDepartamento(),e.getDireccion(),e.getFecha(),e.getHora(),e.getImagen(),e.getLocal(),e.getNombre(),e.getProvincia(),e.getTipoEvento());
                        }
                        if(e.getId().equals(c[0])&&(!e.getArtistas().equals(c[1])||
                                !e.getComprar().equals(c[2])||
                                !e.getDepartamento().equals(c[3])||
                                !e.getDireccion().equals(c[4])||
                                !e.getFecha().equals(c[5])||
                                !e.getHora().equals(c[6])||
                                !e.getImagen().equals(c[7])||
                                !e.getLocal().equals(c[8])||
                                !e.getNombre().equals(c[9])||
                                !e.getProvincia().equals(c[10])||
                                !e.getTipoEvento().equals(c[11]))){
                            ActualizarEventos(e.getId(),e.getArtistas(),e.getComprar(),e.getDepartamento(),e.getDireccion(),e.getFecha(),e.getHora(),e.getImagen(),e.getLocal(),e.getNombre(),e.getProvincia(),e.getTipoEvento());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<cEventos>> call, Throwable t) {

            }

        });
    }

    public void ActualizarEventos(String Codigo,String artistas,String comprar,String departamento,String dierccion,String fecha,String hora,String imagen,String local,String nombre,String provincia,String tipoEvento )
    {
        SQLiteDatabase dbSQLITE =con.getReadableDatabase();
        String consulta ="UPDATE tEventos SET Artistas='"+artistas+"',Comprar='"+comprar+"',Departamento='"+departamento+"',Direccion='"+dierccion+"',Fecha='"+fecha+"',Hora='"+hora+"',Imagen='"+imagen+"',Local='"+local+"',Nombre='"+nombre+"',Provincia='"+provincia+"',TipoEvento='"+tipoEvento+"' WHERE ID ='"+Codigo+"'";
        dbSQLITE.execSQL(consulta);
    }

    public void ReistrarEventos(String Id,String artistas,String comprar,String departamento,String dierccion,String fecha,String hora,String imagen,String local,String nombre,String provincia,String tipoEvento )
    {
        SQLiteDatabase dbSQLITE =con.getReadableDatabase();
        String consulta ="INSERT INTO tEventos(ID,Artistas,Comprar,Departamento,Direccion,Fecha,Hora,Imagen,Local,Nombre,Provincia,TipoEvento) values('"+Id+"','"+artistas+"','"+comprar+"','"+departamento+"','"+dierccion+"','"+fecha+"','"+hora+"','"+imagen+"','"+local+"','"+nombre+"','"+provincia+"','"+tipoEvento+"')";
        dbSQLITE.execSQL(consulta);
    }

    public String[] Comparar(String ID){
        SQLiteDatabase dbSqlite = con.getReadableDatabase();
        Cursor cursor = dbSqlite.rawQuery("SELECT ID,Artistas,Comprar,Departamento,Direccion,Fecha,Hora,Imagen,Local,Nombre,Provincia,TipoEvento FROM tEventos WHERE ID=+'"+ID+"'",null);
        String[] datos = new String[12];
        while (cursor.moveToNext()) {
            datos[0]= cursor.getString(0);
            datos[1]= cursor.getString(1);
            datos[2]= cursor.getString(2);
            datos[3]= cursor.getString(3);
            datos[4]= cursor.getString(4);
            datos[5]= cursor.getString(5);
            datos[6]= cursor.getString(6);
            datos[7]= cursor.getString(7);
            datos[8]= cursor.getString(8);
            datos[9]= cursor.getString(9);
            datos[10]= cursor.getString(10);
            datos[11]= cursor.getString(11);
        }
        return datos;
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
}