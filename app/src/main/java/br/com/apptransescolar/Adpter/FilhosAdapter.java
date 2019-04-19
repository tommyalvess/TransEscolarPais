package br.com.apptransescolar.Adpter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.apptransescolar.Activies.InfFilhosActivity;
import br.com.apptransescolar.Classes.FilhosDAO;
import br.com.apptransescolar.Classes.Kids;
import br.com.apptransescolar.R;

public class FilhosAdapter extends BaseAdapter {

    private final List<Kids> kids;
    private final Context act;
    FilhosDAO kidsDAO;

    public FilhosAdapter(List<Kids> kids, Activity act) {
        this.kids = kids;
        this.act = act;
    }

    @Override
    public int getCount() {
        return kids.size();
    }

    @Override
    public Object getItem(int position) {
        return kids.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(act);
        view = mInflater.inflate(R.layout.linha_passageiro_list,parent,false);

        final Kids kid = kids.get(position);

        CardView cardView = view.findViewById(R.id.cardView);
        TextView nome =   view.findViewById(R.id.textNome);
        TextView escola = view.findViewById(R.id.textNumero);
        TextView periodo = view.findViewById(R.id.textEmail);
        //TextView endereco = view.findViewById(R.id.textEnd);
        //TextView aniver = view.findViewById(R.id.textDtnas);

        //ImageView imagem = view.findViewById(R.id.imageView2);
        //Glide.with(act).load(kids.get(position).getImg()).into(imagem);


        nome.setText(kid.getNome());
        escola.setText(kid.getNm_escola());
        periodo.setText(kid.getPeriodo());
        //endereco.setText(kid.getEnd_principal());
        //aniver.setText(kid.getDt_nas());

        //endereco.setVisibility(View.GONE);
        //aniver.setVisibility(View.GONE);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(FilhosAdapter.this.act, InfFilhosActivity.class);
                it.putExtra("kids", kid);
                FilhosAdapter.this.act.startActivity(it);
            }
        });

        return view;
    }
}
