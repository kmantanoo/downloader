package model.exception;

public class InvalidURLException extends Exception {

   /**
    * 
    */
   private static final long serialVersionUID = -6784094193580725618L;

   public InvalidURLException() {
      super();
   }

   public InvalidURLException(String invalidUrl) {
      super(invalidUrl);
   }
}
