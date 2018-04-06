package com.schulze.desenvolvimento.cromos.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class DataBase extends SQLiteOpenHelper{

    public static int VERSION = 1;
    public static String NOME_DB = "DB_CROMOS";
    public static String TABELA_CROMOS = "cromos";
    public Context context;

    public DataBase(Context context) {
        super(context, NOME_DB, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABELA_CROMOS
                        + " (numero INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL,"
                        + " possui INTEGER DEFAULT 0, repetida INTEGER DEFAULT 0);";
        try{
            db.execSQL(sql);

            String json = null;
            InputStream is = context.getAssets().open("cromos.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONArray jArray = new JSONArray(json);

            for (int i=0; i < jArray.length(); i++)
            {
                try {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array
                    String nomeJson = oneObject.getString("nome");
                    int numeroJson = oneObject.getInt("numero");
                    db.execSQL("INSERT INTO cromos(nome, possui, repetida) VALUES ('" + nomeJson + "', 0, 0)");
                } catch (JSONException e) {
                    Log.i("INFO DB", "Erro ao ler Json " + e.getMessage());
                }
            }

            Log.i("INFO DB", "Sucesso ao criar a tabela");
        }catch(Exception e){
            Log.i("INFO DB", "Erro ao criar a tabela " + e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // É chamado quando uma nova versão do aplicativo for lançada

    }
}
