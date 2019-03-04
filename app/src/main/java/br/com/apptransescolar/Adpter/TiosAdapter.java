package br.com.apptransescolar.Adpter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import br.com.apptransescolar.Activies.InfTioActivity;
import br.com.apptransescolar.Classes.Tios;
import br.com.apptransescolar.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class TiosAdapter extends RecyclerView.Adapter<TiosAdapter.MyViewHolder>{

    private final Context context;
    private final List<Tios> nData;

    public TiosAdapter(List<Tios> tios, Context context) {
        this.context = context;
        this.nData = tios;
    }

    @NonNull
    @Override
    public TiosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.linha_pais_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TiosAdapter.MyViewHolder holder, int position) {
        //final Tios tios = this.nData.get(position);
        holder.nome.setText(nData.get(position).getNome());
        holder.tell.setText(nData.get(position).getTell());
        holder.email.setText(nData.get(position).getEmail());
//        Glide.with(context).load(nData.get(position).getImg()).apply(options).into(holder.img);
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent it = new Intent(TiosAdapter.this.context, InfTioActivity.class);
//                it.putExtra("tios", tios);
//                TiosAdapter.this.context.startActivity(it);
//            }
//        });
        //holder.itemView.setLongClickable(true);
    }

    @Override
    public int getItemCount() {
        return nData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        //CardView cardView;
        TextView nome, tell, email;
        //CircleImageView img;

        //private final Context context;

        public MyViewHolder(View itemView){
            super(itemView);

            //context = itemView.getContext();

            //cardView = (CardView) itemView;
            nome = itemView.findViewById(R.id.textView14);
            tell = itemView.findViewById(R.id.textEscola);
            email = itemView.findViewById(R.id.textView16);
            //img = itemView.findViewById(R.id.imgPass);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "Vc Clicou!", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    Toast.makeText(context, "Vc precionou!", Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//            });
        }

    }
}
