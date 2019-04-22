package br.com.apptransescolar.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

import br.com.apptransescolar.Classes.Tio;
import br.com.apptransescolar.R;

public class SpinnerTiosAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Tio> listData;
    private Context context;

    public SpinnerTiosAdapter(Context context, List<Tio> listData) {
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
        return (Tio)listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpinnerTiosAdapter.ViewHolder spinnerHolder;
        if (convertView == null) {
            spinnerHolder = new SpinnerTiosAdapter.ViewHolder();
            convertView = layoutInflater.inflate(R.layout.spinner_list, parent, false);
            spinnerHolder.spinnerItemList = convertView.findViewById(R.id.spinner_list_item);
            convertView.setTag(spinnerHolder);
        } else {
            spinnerHolder = (SpinnerTiosAdapter.ViewHolder) convertView.getTag();
        }
        spinnerHolder.spinnerItemList.setText(listData.get(position).getNome());
        return convertView;

    }


    class ViewHolder{
            TextView spinnerItemList;
        }

}
