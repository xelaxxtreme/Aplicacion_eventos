package my.wab.festejaper;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import my.wab.festejaper.db.ObtenerEventos;
import my.wab.festejaper.db.cEventos;

public class ListaEventos extends RecyclerView.Adapter<ListaEventos.ViewHolder> implements View.OnClickListener{
    List<cEventos> ListaEventos;
    Context context;
    private View.OnClickListener listener;

    public ListaEventos(List<cEventos> lEventos,Context context){
        this.context = context;
        this.ListaEventos = lEventos;

    }
    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }
    @NonNull
    @Override
    public ListaEventos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventos_detalles,parent,false);
        RecyclerView.LayoutParams layoutParams =new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaEventos.ViewHolder holder, int position) {
        //vamos hacer que se vea bonito nuestro sistema
        cEventos e = ListaEventos.get(position);
        String[] artistas = e.getArtistas().split(",");
        holder.tvArtistasA.setText(artistas[0]);
        holder.tvProvA.setText(e.getProvincia());
        holder.tvFechaA.setText(e.getFecha());
        Glide.with(context)
                .load(e.getImagen())
                .centerCrop()
                .placeholder(R.drawable.esperando)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.ivImagenA);
    }

    @Override
    public int getItemCount() {
        return ListaEventos.size();
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImagenA;
        TextView tvArtistasA,tvProvA,tvFechaA;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImagenA = (ImageView)itemView.findViewById(R.id.ivImagenA);
            tvArtistasA = (TextView)itemView.findViewById(R.id.tvArtistasA);
            tvProvA = (TextView)itemView.findViewById(R.id.tvProvA);
            tvFechaA = (TextView)itemView.findViewById(R.id.tvFechaA);
        }
    }
}
