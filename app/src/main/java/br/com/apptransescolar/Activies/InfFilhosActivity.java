package br.com.apptransescolar.Activies;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.apptransescolar.Classes.Kids;
import br.com.apptransescolar.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class InfFilhosActivity extends AppCompatActivity {

    TextView nomeK, periodoK, escolaK, endK, dt_nasc, toolbartext;
    String id;
    CircleImageView imgInfKids;
    Spinner spinnerPeriodo, spinnerTios, spinnerEscola;

    Kids kids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_filhos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        kids = (Kids) getIntent().getExtras().get("kids");
        nomeK = findViewById(R.id.nomeK);
        periodoK = findViewById(R.id.emailT);
        escolaK = findViewById(R.id.escolaK);
        endK = findViewById(R.id.tellT);
        dt_nasc = findViewById(R.id.dt_nasc);
        imgInfKids = findViewById(R.id.imgInfKids);
        toolbartext = findViewById(R.id.toolbartext);

        toolbartext.setText(kids.getNome());
        nomeK.setText(kids.getNome());
        escolaK.setText(kids.getNm_escola());
        periodoK.setText(kids.getPeriodo().toUpperCase());
        endK.setText(kids.getEnd_principal());
        dt_nasc.setText(kids.getDt_nas());

        Picasso.get().load(R.drawable.kids).into(imgInfKids);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfFilhosActivity.this, EditarFilhoActivity.class);
                intent.putExtra("kids", kids);
                InfFilhosActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



