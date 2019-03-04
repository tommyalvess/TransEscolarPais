package br.com.apptransescolar.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class FilhosDAO extends SQLiteOpenHelper {

    public static final String DBNAME = "apptransescolar";
    public static final String TBKIDS = "kids";
    public static final int DBVERSION = 1;

    public FilhosDAO (Context context){
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TBKIDS + " (\n" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "`nome` TEXT," +
                "`escola` TEXT," +
                "`periodo` TEXT," +
                "`endereco` TEXT," +
                "`aniver` TEXT" +
                ")";

        sqLiteDatabase.execSQL(sql);
    }

    public void insert(Kids kids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", kids.getNome());
        values.put("escola", kids.getNm_escola());
        values.put("periodo", kids.getPeriodo());
        values.put("endereco", kids.getEnd_principal());
        values.put("aniver", kids.getDt_nas());

        db.insert(TBKIDS, null, values);
        db.close();
    }

    public List<Kids> all() {
        List<Kids> clientes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TBKIDS, new String[]{"id", "nome", "escola", "periodo", "endereco", "aniver"}, null, null, null, null, "nome ASC");

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nome  = cursor.getString(1);
            String escola = cursor.getString(2);
            String periodo = cursor.getString(3);
            String endereco = cursor.getString(4);
            String aniver = cursor.getString(5);

            clientes.add( new Kids(id, nome, escola, periodo, endereco, aniver) );
        }

        db.close();

        return clientes;
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBKIDS, "id = ?", new String[]{ String.valueOf(id) });
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
