package br.com.apptransescolar.Adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.apptransescolar.Classes.Escolas;
import br.com.apptransescolar.R;


public class EscolaAdapter extends RecyclerView.Adapter<EscolaAdapter.MyViewHolder>{

    private List<Escolas> contacts;
    private Context context;

    public EscolaAdapter(List<Escolas> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linha_escola_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(contacts.get(position).getNome());
        holder.end.setText(contacts.get(position).getEndereco());
        holder.tell.setText(contacts.get(position).getTell());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,end,tell;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nomeText);
            end = itemView.findViewById(R.id.endeText);
            tell = itemView.findViewById(R.id.tellText);
        }
    }
}
