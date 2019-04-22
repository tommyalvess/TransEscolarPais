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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import br.com.apptransescolar.Activies.InfTioActivity;
import br.com.apptransescolar.Classes.Tios;
import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class TiosAdapter extends RecyclerView.Adapter<TiosAdapter.MyViewHolder>{

    private final Context context;
    private final List<Tios> nData;
    RequestOptions options;

    final String[] items = {"Desculpe, ele não irá hoje!", "Está chegando?"};

    SessionManager sessionManager;
    String getNome;


    public TiosAdapter(List<Tios> tios, Context context) {
        this.context = context;
        this.nData = tios;
                options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.kids)
                .error(R.drawable.kids);
    }

    @NonNull
    @Override
    public TiosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.linha_tios_list,parent,false);
        sessionManager = new SessionManager(context);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getNome = user.get(sessionManager.NAME);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TiosAdapter.MyViewHolder holder, final int position) {
        final Tios tios = this.nData.get(position);
        holder.nome.setText(nData.get(position).getApelido());
        holder.apelido.setText(nData.get(position).getTell());
        Glide.with(context).load(nData.get(position).getImg()).apply(options).into(holder.img);

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Notificar o Tio!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        if (item == 0){
                            Snackbar.make(v, R.string.mensagem_enviada, Snackbar.LENGTH_LONG)
                                    .show();
                            sendNotificationFirst();
                        }else if (item == 1){
                            Snackbar.make(v, R.string.mensagem_enviada, Snackbar.LENGTH_LONG)
                                    .show();
                            sendNotificationSecond();
                        }
                    }
                });
                builder.show();
                return false;
            }

            private void sendNotificationFirst()
            {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        int SDK_INT = android.os.Build.VERSION.SDK_INT;
                        if (SDK_INT > 8) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            String send_email = nData.get(position).getCpf();

                            try {
                                String jsonResponse;

                                URL url = new URL("https://onesignal.com/api/v1/notifications");
                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setUseCaches(false);
                                con.setDoOutput(true);
                                con.setDoInput(true);

                                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                                con.setRequestProperty("Authorization", "Basic MzUzNzMxMjMtOTIxNy00ZTBlLTg2YjktMDRlOTg4YmEwNzFh");
                                con.setRequestMethod("POST");

                                String strJsonBody = "{"
                                        + "\"app_id\": \"ef54b0b1-d6b0-46e0-ad4f-4e15d9f7dfe6\","

                                        + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"

                                        + "\"data\": {\"foo\": \"bar\"},"
                                        + "\"contents\": {\"en\": \" "+ getNome +":"+"Desculpe, ele não irá hoje!"+"\"}"
                                        + "}";


                                System.out.println("strJsonBody:\n" + strJsonBody);

                                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                                con.setFixedLengthStreamingMode(sendBytes.length);

                                OutputStream outputStream = con.getOutputStream();
                                outputStream.write(sendBytes);

                                int httpResponse = con.getResponseCode();
                                System.out.println("httpResponse: " + httpResponse);

                                if (httpResponse >= HttpURLConnection.HTTP_OK
                                        && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                                    Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                    scanner.close();
                                } else {
                                    Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                    scanner.close();
                                }
                                System.out.println("jsonResponse:\n" + jsonResponse);

                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                    }
                });
            }

            private void sendNotificationSecond()
            {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        int SDK_INT = android.os.Build.VERSION.SDK_INT;
                        if (SDK_INT > 8) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            String send_email = nData.get(position).getCpf();

                            try {
                                String jsonResponse;

                                URL url = new URL("https://onesignal.com/api/v1/notifications");
                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setUseCaches(false);
                                con.setDoOutput(true);
                                con.setDoInput(true);

                                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                                con.setRequestProperty("Authorization", "Basic MzUzNzMxMjMtOTIxNy00ZTBlLTg2YjktMDRlOTg4YmEwNzFh");
                                con.setRequestMethod("POST");

                                String strJsonBody = "{"
                                        + "\"app_id\": \"ef54b0b1-d6b0-46e0-ad4f-4e15d9f7dfe6\","

                                        + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"

                                        + "\"data\": {\"foo\": \"bar\"},"
                                        + "\"contents\": {\"en\": \" "+ getNome +":"+"Está chegando?!"+"\"}"
                                        + "}";


                                System.out.println("strJsonBody:\n" + strJsonBody);

                                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                                con.setFixedLengthStreamingMode(sendBytes.length);

                                OutputStream outputStream = con.getOutputStream();
                                outputStream.write(sendBytes);

                                int httpResponse = con.getResponseCode();
                                System.out.println("httpResponse: " + httpResponse);

                                if (httpResponse >= HttpURLConnection.HTTP_OK
                                        && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                                    Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                    scanner.close();
                                } else {
                                    Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                    scanner.close();
                                }
                                System.out.println("jsonResponse:\n" + jsonResponse);

                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                    }
                });
            }

        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent it = new Intent(TiosAdapter.this.context, InfTioActivity.class);
                it.putExtra("tios", tios);
                TiosAdapter.this.context.startActivity(it);
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
        TextView nome, apelido;
        CircleImageView img;

        //private Context contextT;


        public MyViewHolder(View itemView){
            super(itemView);

            //contextT = itemView.getContext();

            cardView = (CardView) itemView;
            nome = itemView.findViewById(R.id.textNome);
            apelido = itemView.findViewById(R.id.textApelido);
            img = itemView.findViewById(R.id.imgTios);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
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
