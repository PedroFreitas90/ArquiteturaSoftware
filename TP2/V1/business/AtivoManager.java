package business;

import business.ativos.Ativo;
import persistence.AtivoDAO;
import persistence.AtivoTipoDAO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AtivoManager implements Runnable {

    private Map<String, Ativo> ativosPorId;
    private Map<String, List<Ativo>> ativosPorTipo;

    public AtivoManager() {
        this.ativosPorId = new AtivoDAO();
        this.ativosPorTipo = new AtivoTipoDAO();
        new Thread(this).start();
    }

    /**
     * @return todos os ativos no sistema
     */
    public Collection<Ativo> getAll() {
        return this.ativosPorId.values();
    }

    /**
     * @param id id do ativo
     * @return Ativo (ou null caso o ID não exista)
     */
    public Ativo get(String id) {
        return this.ativosPorId.get(id);
    }

    /**
     * @param tipo tipo de ativo
     * @return Lista de ativos com esse tipo
     */
    public List<Ativo> getPorTipo(String tipo) {
        return this.ativosPorTipo.get(tipo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Updating the assets values!");
            Collection<Ativo> ativos = this.ativosPorId.values();
            ativos.forEach(Runnable::run);
            ativos.forEach(a -> this.ativosPorId.put(a.getId(), a));
            System.out.println("Finished updating the assets values!");
        }
    }
}
