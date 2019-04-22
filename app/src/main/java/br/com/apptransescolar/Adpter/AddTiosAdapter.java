package br.com.apptransescolar.Adpter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import br.com.apptransescolar.Activies.AddTiosActivity;
import br.com.apptransescolar.Activies.InfTioActivity;
import br.com.apptransescolar.Activies.PerfilActivity;
import br.com.apptransescolar.Activies.TiosActivity;
import br.com.apptransescolar.Classes.Tio;
import br.com.apptransescolar.Classes.Tios;
import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static br.com.apptransescolar.API.URLs.INSERIR_TIO;
import static br.com.apptransescolar.API.URLs.URL_EDIT;
import static com.android.volley.VolleyLog.TAG;

public class AddTiosAdapter  extends RecyclerView.Adapter<AddTiosAdapter.MyViewHolder>{
    private final Context context;
    private final List<Tio> nData;
    RequestOptions options;

    SessionManager sessionManager;
    String getIdP, getIdT;
    Tio tios;

    public AddTiosAdapter(List<Tio> tios, Context context) {
        this.context = context;
        this.nData = tios;
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.kids)
                .error(R.drawable.kids);
    }

    @NonNull
    @Override
    public AddTiosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.linha_add_tio,parent,false);
        sessionManager = new SessionManager(context);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getIdP = user.get(sessionManager.ID);

        return new AddTiosAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddTiosAdapter.MyViewHolder holder, final int position) {
        tios = this.nData.get(position);
        holder.nome.setText(nData.get(position).getNome());
        holder.apelido.setText(nData.get(position).getApelido());
        holder.placa.setText(nData.get(position).getPlaca());
        Glide.with(context).load(nData.get(position).getImg()).apply(options).into(holder.img);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                addTio();
            }

            private void addTio(){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                View mView = View.inflate(context,R.layout.dialog_add_tio, null);
                Button mSim = mView.findViewById(R.id.btnSim);
                Button mNao = mView.findViewById(R.id.btnNao);
                getIdT = String.valueOf(nData.get(position).getId());
                alertDialog.setView(mView);

                final AlertDialog dialog = alertDialog.create();

                mNao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                mSim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      StringRequest stringRequest = new StringRequest(Request.Method.POST, INSERIR_TIO,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //progess.setVisibility(View.GONE);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            //boolean success = jsonObject.getBoolean("success");
                                            String success = jsonObject.getString("success");

                                            if (success.equals("1")){
                                                Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(context, TiosActivity.class);
                                                context.startActivity(intent);
                                            }else {
                                                Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                            }

                                        } catch (JSONException e1) {
                                            Toast.makeText(context, "Opss! Algo deu errado!", Toast.LENGTH_SHORT).show();
                                            Log.e("JSON", "Error parsing JSON", e1);
                                            dialog.dismiss();
                                        }
                                        Log.e(TAG, "response: " + response);
                                    }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(context, "Opss! Tente novamente mais tarde!", Toast.LENGTH_SHORT).show();
                                            Log.e("JSON", "Error Response", error);
                                            dialog.dismiss();
                                        }
                                    }){

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("idPais", getIdP);
                                params.put("idTios", getIdT);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);
                    }
                });
                dialog.show();
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
        TextView nome, apelido, placa;
        CircleImageView img;

        //private Context contextT;


        public MyViewHolder(View itemView){
            super(itemView);

            //contextT = itemView.getContext();

            cardView = (CardView) itemView;
            nome = itemView.findViewById(R.id.textNome);
            apelido = itemView.findViewById(R.id.textApelido);
            placa = itemView.findViewById(R.id.textPlaca);
            img = itemView.findViewById(R.id.imgTios);


        }

    }
}
