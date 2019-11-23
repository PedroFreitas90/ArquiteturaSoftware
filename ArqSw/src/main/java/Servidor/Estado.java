package Servidor;

public class Estado {
    private String pedido;
    private boolean estado;
    private Integer identificador;
    private Integer idUtilizador;

    public Estado(String pedido, boolean estado,int id,int idU){
        this.pedido=pedido;
        this.estado=estado;
        this.identificador = id;
        this.idUtilizador= idU;
    }

    public String getPedido() {
        return pedido;
    }

    public boolean getEstado(){
        return estado;
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public Integer getIdUtilizador() {
        return idUtilizador;
    }

    public void setEstado(boolean estado){
        this.estado= estado;
    }

}
