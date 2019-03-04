package br.com.apptransescolar.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.apptransescolar.Classes.Escolas;
import br.com.apptransescolar.Classes.Tios;
import br.com.apptransescolar.R;

public class SpinnerEscolaAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Escolas> listData;
    private Context context;

    public SpinnerEscolaAdapter(Context context, List<Escolas> listData) {
        this.context = context;
        layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return (Escolas)listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpinnerEscolaAdapter.ViewHolder spinnerHolder;
        if (convertView == null) {
            spinnerHolder = new SpinnerEscolaAdapter.ViewHolder();
            convertView = layoutInflater.inflate(R.layout.spinner_list, parent, false);
            spinnerHolder.spinnerItemList = convertView.findViewById(R.id.spinner_list_item);
            convertView.setTag(spinnerHolder);
        } else {
            spinnerHolder = (SpinnerEscolaAdapter.ViewHolder) convertView.getTag();
        }
        spinnerHolder.spinnerItemList.setText(listData.get(position).getNome());
        return convertView;

    }
    class ViewHolder{
        TextView spinnerItemList;
    }
}
