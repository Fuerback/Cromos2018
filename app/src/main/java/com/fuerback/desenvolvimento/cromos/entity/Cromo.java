package com.fuerback.desenvolvimento.cromos.entity;

public class Cromo {

    private int numero;
    private String nome;
    private boolean possui;
    private boolean repetida;

    public Cromo() {
    }

    public Cromo(int numero, String nome, boolean possui, boolean repetida) {
        this.numero = numero;
        this.nome = nome;
        this.possui = possui;
        this.repetida = repetida;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isPossui() {
        return possui;
    }

    public void setPossui(boolean possui) {
        this.possui = possui;
    }

    public boolean isRepetida() {
        return repetida;
    }

    public void setRepetida(boolean repetida) {
        this.repetida = repetida;
    }
}
