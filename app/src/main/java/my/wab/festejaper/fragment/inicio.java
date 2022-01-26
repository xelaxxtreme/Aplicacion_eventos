package my.wab.festejaper.fragment;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import my.wab.festejaper.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class inicio extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    // TODO: Rename and change types of parameters
     TextView tvTitulo;
     TextView tvTipoEvento;
     TextView tvLocal;
     TextView tvDireccion;
     TextView tvFecha;
     TextView tvHora;
     TextView tvDepartamento;
     TextView tvProvincia;
     TextView tvArtistas;
     ImageView ivImagen;
     String datos[] = new String [11];
     String comprar = "";
     Button buComprar;
     private AdView adViewInicio;



    public inicio() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static inicio newInstance(String param1, String param2) {
        inicio fragment = new inicio();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            datos = getArguments().getStringArray("datos");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_inicio, container, false);
        tvTitulo = (TextView)vista.findViewById(R.id.tvinicioTitulo);
        tvTipoEvento = (TextView)vista.findViewById(R.id.tvinicioTipoEvento);
        tvLocal=(TextView)vista.findViewById(R.id.tvinicioLocal);
        tvDireccion=(TextView)vista.findViewById(R.id.tvinicioDireccion);
        tvFecha=(TextView) vista.findViewById(R.id.tvinicioFecha);
        tvHora=(TextView) vista.findViewById(R.id.tvinicioHora);
        tvDepartamento=(TextView)vista.findViewById(R.id.tvinicioDepartamento);
        tvProvincia=(TextView) vista.findViewById(R.id.tvinicioProvincia);
        tvArtistas=(TextView)vista.findViewById(R.id.tvinicioArtistas);
        ivImagen=(ImageView)vista.findViewById(R.id.ivinicioImagen);
        buComprar=(Button)vista.findViewById(R.id.buComprar);
        adViewInicio=(AdView)vista.findViewById(R.id.adViewInicio);
        comprar = datos[1];
        buComprar.setVisibility(View.INVISIBLE);
        if(!comprar.equals("no")){
            buComprar.setVisibility(View.VISIBLE);
            buComprar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri link = Uri.parse(comprar);
                    Intent intent = new Intent(Intent.ACTION_VIEW, link);
                    startActivity(intent);
                }
            });
        }

        MostrarEvento();
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewInicio.loadAd(adRequest);
        return vista;
    }
    public void MostrarEvento(){

        tvArtistas.setText(datos[0]);
        tvDepartamento.setText(datos[2]);
        tvDireccion.setText(datos[3]);
        tvFecha.setText(datos[4]);
        tvHora.setText(datos[5]);
        tvLocal.setText(datos[7]);
        tvTitulo.setText(datos[8]);
        tvProvincia.setText(datos[9]);
        tvTipoEvento.setText(datos[10]);
        TituloMatizado(tvTipoEvento,
                getResources().getColor(R.color.verde1),
                getResources().getColor(R.color.naranja1),
                getResources().getColor(R.color.rosa1));
        TituloMatizado(tvTitulo,
                getResources().getColor(R.color.rosa1),
                getResources().getColor(R.color.naranja1),
                getResources().getColor(R.color.verde1));
        ArtistasMatizado(tvArtistas,
                getResources().getColor(R.color.morado),
                getResources().getColor(R.color.rosado));

        Glide.with(getContext())
                .load(datos[6])
                .placeholder(R.drawable.esperando)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImagen);
    }

    public void TituloMatizado(TextView tv,int...color){
        TextPaint paint = tv.getPaint();
        float with = paint.measureText(tv.getText().toString());
        Shader shader = new LinearGradient(0,0,with,tv.getTextSize(),color,null,Shader.TileMode.CLAMP);
        tv.getPaint().setShader(shader);
        tv.setTextColor(color[0]);
    }
    public void ArtistasMatizado(TextView tv,int...color){
        TextPaint paint = tv.getPaint();
        float with = paint.measureText(tv.getText().toString());
        Shader shader = new LinearGradient(0,50,0  ,0,color,null,Shader.TileMode.CLAMP);
        tv.getPaint().setShader(shader);
        tv.setTextColor(color[0]);
    }
}