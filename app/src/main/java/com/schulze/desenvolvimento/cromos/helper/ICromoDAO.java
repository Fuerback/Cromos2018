package com.schulze.desenvolvimento.cromos.helper;

import com.schulze.desenvolvimento.cromos.entity.Cromo;

import java.util.List;

public interface ICromoDAO {

    public boolean atualizarPossui(Cromo cromo, int valor);
    public boolean atualizarRepetidas(Cromo cromo, int valor);
    public List<Cromo> listarTodas();
    public List<Cromo> listarPossui();
    public List<Cromo> listarFalta();
    public List<Cromo> listarRepetidas();
}
