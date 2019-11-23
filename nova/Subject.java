package Servidor;

public interface Subject {
    public void registerObserverUtilizador(Observer o);
    public void registerObserverVenda(Observer o);
    public void registerObserverCompra(Observer o);
    public void removeObserverCompra(Observer o);
    public void removeObserverVenda(Observer o);
    public void removeObserverUtilizador(Observer o);
    public void notifyObserverUtilizador();
    public void notifyObserverCompra();
    public void notifyObserverVenda();

}
