package Servidor;







 public class Pedido {
        private Estado estado;


     public void set(Estado estado){
         this.estado= estado;
     }
     public void set (String pedido,boolean estado,int identificador,int idU){
         this.estado= new Estado(pedido,estado,identificador,idU);
     }



     public Memento saveToMemento()
     {
         System.out.println("Saving time to Memento");
         return new Memento(this.estado);
     }

     public void restoreFromMemento(Memento memento) {
         this.estado = memento.getSavedEstado();
     }
     public Estado getEstado(){
         return this.estado;
     }


     public static class Memento {

         private final Estado estado;

         public Memento(Estado estado)
         {
             this.estado = estado;
         }

         public Estado getSavedEstado()
         {
             return estado;
         }
     }

 }


