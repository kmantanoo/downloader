package model.exception;

public class IllegalFileTypeException extends Exception{
   
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   public IllegalFileTypeException() {
      super();
   }
   
   public IllegalFileTypeException(String msg) {
      super(msg);
   }
}
