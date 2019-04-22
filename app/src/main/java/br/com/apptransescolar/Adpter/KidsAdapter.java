package br.com.apptransescolar.Adpter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import br.com.apptransescolar.Activies.InfFilhosActivity;
import br.com.apptransescolar.Classes.Kids;
import br.com.apptransescolar.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class KidsAdapter  extends RecyclerView.Adapter<KidsAdapter.MyViewHolder>{

    RequestOptions options;
    private final Context context;
    private final List<Kids> nData;
    String imgK, id;

    public KidsAdapter(List<Kids> filhos, Context context) {
        this.context = context;
        this.nData = filhos;
        options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.kids)
                .error(R.drawable.kids);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.linha_passageiro_list,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Kids kids = this.nData.get(position);
        holder.nome.setText(nData.get(position).getNome());
        holder.periodo.setText(nData.get(position).getTio());
        holder.escola.setText(nData.get(position).getPeriodo());
        Glide.with(context).load(kids.getImg()).apply(options).into(holder.img);
        Log.e("Foto",  kids.getImg());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(KidsAdapter.this.context, InfFilhosActivity.class);
                it.putExtra("kids", kids);
                KidsAdapter.this.context.startActivity(it);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("Foto",  kids.getImg());
                return false;
            }
        });
        holder.itemView.setLongClickable(true);
    }


    @Override
    public int getItemCount() {
        return nData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView nome, escola, periodo, end, dtnas;
        CircleImageView img;

        private final Context context;

        public MyViewHolder(View itemView){
            super(itemView);

            context = itemView.getContext();

            cardView = (CardView) itemView;
            nome = itemView.findViewById(R.id.textNome);
            periodo = itemView.findViewById(R.id.textApelido);
            escola = itemView.findViewById(R.id.textEmail);
            img = itemView.findViewById(R.id.imgK);


        }

    }
}
