package test.model;

import static org.junit.Assert.*;

import org.junit.Test;

import model.CredentialInformation;

public class TestCredentialInformation {

   @Test
   public void testConstructWithoutParams() {
      CredentialInformation info = new CredentialInformation();
      assertEquals(null, info.getUsername());
      assertEquals(null, info.getPassword());
   }
   
   @Test
   public void testConstructWithParams() {
      CredentialInformation info = new CredentialInformation("username", "password");
      assertEquals("username", info.getUsername());
      assertEquals("password", info.getPassword());
   }
   
   @Test
   public void testGetAndGet() {
      CredentialInformation info = new CredentialInformation();

      assertEquals(null, info.getUsername());
      assertEquals(null, info.getPassword());
      
      info.setUsername("username");
      assertEquals("username", info.getUsername());
      assertEquals(null, info.getPassword());
      
      info.setUsername(null);
      assertEquals(null, info.getUsername());
      assertEquals(null, info.getPassword());
      
      info.setPassword("password");
      assertEquals(null, info.getUsername());
      assertEquals("password", info.getPassword());
      
      info.setUsername("username");
      assertEquals("username", info.getUsername());
      assertEquals("password", info.getPassword());
   }

}
