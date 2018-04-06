package com.schulze.desenvolvimento.cromos.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.schulze.desenvolvimento.cromos.entity.Cromo;

import java.util.ArrayList;
import java.util.List;

public class CromoDAO implements ICromoDAO {

    private SQLiteDatabase escreve;
    private SQLiteDatabase recupera;

    public CromoDAO(Context context) {
        DataBase db = new DataBase(context);
        escreve = db.getWritableDatabase();
        recupera = db.getReadableDatabase();
    }

    @Override
    public boolean atualizarPossui(Cromo cromo, int valor) {

        ContentValues cv = new ContentValues();
        cv.put("possui", valor);

        try{
            String[] args = {String.valueOf(valor), String.valueOf(cromo.getNumero() + 1 )};
            escreve.update(DataBase.TABELA_CROMOS, cv, "possui != ? AND numero = ?", args);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizarRepetidas(Cromo cromo, int valor) {

        ContentValues cv = new ContentValues();
        cv.put("repetida", valor);

        try{
            String[] args = {String.valueOf(valor), String.valueOf(cromo.getNumero() + 1)};
            escreve.update(DataBase.TABELA_CROMOS, cv, "repetida != ? AND numero = ?", args);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public List<Cromo> listarTodas() {
        List<Cromo> cromos = new ArrayList<>();

        String sql = "SELECT numero, nome, possui, repetida FROM " + DataBase.TABELA_CROMOS + ";";
        Cursor c = recupera.rawQuery(sql, null);

        while(c.moveToNext()){
            Cromo cromo = new Cromo();

            Integer numero = c.getInt( c.getColumnIndex("numero") );
            String nome = c.getString( c.getColumnIndex("nome") );
            Integer possui = c.getInt( c.getColumnIndex("possui") );
            Integer repetida = c.getInt( c.getColumnIndex("repetida") );

            cromo.setNumero(numero - 1);
            cromo.setNome(nome);
            cromo.setPossui(possui != 0);
            cromo.setRepetida(repetida != 0);

            cromos.add(cromo);
        }

        return cromos;
    }

    @Override
    public List<Cromo> listarPossui() {
        List<Cromo> cromos = new ArrayList<>();

        String sql = "SELECT numero, nome, possui, repetida FROM " + DataBase.TABELA_CROMOS + " WHERE possui = 1 ORDER BY numero;";
        Cursor c = recupera.rawQuery(sql, null);

        while(c.moveToNext()){
            Cromo cromo = new Cromo();

            Integer numero = c.getInt( c.getColumnIndex("numero") );
            String nome = c.getString( c.getColumnIndex("nome") );
            Integer possui = c.getInt( c.getColumnIndex("possui") );
            Integer repetida = c.getInt( c.getColumnIndex("repetida") );

            cromo.setNumero(numero - 1);
            cromo.setNome(nome);
            cromo.setPossui(possui != 0);
            cromo.setRepetida(repetida != 0);

            cromos.add(cromo);
        }

        return cromos;
    }

    @Override
    public List<Cromo> listarFalta() {
        List<Cromo> cromos = new ArrayList<>();

        String sql = "SELECT numero, nome, possui, repetida FROM " + DataBase.TABELA_CROMOS + " WHERE possui != 1 ORDER BY numero;";
        Cursor c = recupera.rawQuery(sql, null);

        while(c.moveToNext()){
            Cromo cromo = new Cromo();

            Integer numero = c.getInt( c.getColumnIndex("numero") );
            String nome = c.getString( c.getColumnIndex("nome") );
            Integer possui = c.getInt( c.getColumnIndex("possui") );
            Integer repetida = c.getInt( c.getColumnIndex("repetida") );

            cromo.setNumero(numero - 1);
            cromo.setNome(nome);
            cromo.setPossui(possui != 0);
            cromo.setRepetida(repetida != 0);

            cromos.add(cromo);
        }

        return cromos;
    }

    @Override
    public List<Cromo> listarRepetidas() {
        List<Cromo> cromos = new ArrayList<>();

        String sql = "SELECT numero, nome, possui, repetida FROM " + DataBase.TABELA_CROMOS + " WHERE repetida = 1 ORDER BY numero;";
        Cursor c = recupera.rawQuery(sql, null);

        while(c.moveToNext()){
            Cromo cromo = new Cromo();

            Integer numero = c.getInt( c.getColumnIndex("numero") );
            String nome = c.getString( c.getColumnIndex("nome") );
            Integer possui = c.getInt( c.getColumnIndex("possui") );
            Integer repetida = c.getInt( c.getColumnIndex("repetida") );

            cromo.setNumero(numero - 1);
            cromo.setNome(nome);
            cromo.setPossui(possui != 0);
            cromo.setRepetida(repetida != 0);

            cromos.add(cromo);
        }

        return cromos;
    }
}
