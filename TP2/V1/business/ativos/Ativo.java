package business.ativos;

import business.Observable;
import business.mercado.IntrinioAPI;
import business.mercado.Mercado;
import business.Observer;
import persistence.CFDAtivoDAO;

import java.util.ArrayList;
import java.util.List;

public abstract class Ativo implements Runnable, Observable {

    private String id;
    private String nome;
    private double valorPorUnidade;
    private List<Observer> observers;
    private Mercado mercado;

    public Ativo(String id, String nome, double vpu) {
        this.id = id;
        this.nome = nome;
        this.valorPorUnidade = vpu;
        this.observers = new CFDAtivoDAO(id);
        this.mercado = new IntrinioAPI();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public void setValorPorUnidade(double valorPorUnidade) {
        this.valorPorUnidade = valorPorUnidade;
    }

    public double getValorPorUnidade() {
        return this.valorPorUnidade;
    }

    public Mercado getMercado() {
        return this.mercado;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(id).append("\n");
        sb.append("Nome: ").append(nome).append("\n");
        sb.append("Valor por Unidade: ").append(valorPorUnidade).append("\n");
        return sb.toString();
    }

    public void setObservers(List<Observer> observers) {
        this.observers.clear();
        this.observers.addAll(observers);
    }

    public abstract double getValorPorUnidadeMaisRecente();

    public boolean registerObserver(Observer o) {
        return !(this.observers.add(o));
    }

    public void notifyObservers() {
        List<Observer> updatedObservers = new ArrayList<>();
        for (int i = 0; i < observers.size(); i++) {
            Observer o = observers.get(i);
            if (o.update(this.valorPorUnidade, this.getId()))
                updatedObservers.add(o);
        }
        this.setObservers(updatedObservers);
    }

    public void run() {
        double quotAntiga = this.getValorPorUnidade();
        double quot = this.getValorPorUnidadeMaisRecente(); // template method
        this.setValorPorUnidade(quot);

        if (quot != quotAntiga) {
            notifyObservers();
        }
    }
}