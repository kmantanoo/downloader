package test.model;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import model.CustomFilenameFilter;

public class TestCustomFilenameFilter {
   
   @Rule
   public ExpectedException exp = ExpectedException.none();

   @Test
   public void testCustomerFileFilter() {
      CustomFilenameFilter filter = new CustomFilenameFilter("test.txt");
      assertTrue(filter.accept(null, "test.txt"));
      assertFalse(filter.accept(null, "test1.txt"));
      assertTrue(filter.accept(null, "test(1).txt"));
      assertFalse(filter.accept(null, "test(x).txt"));
      assertFalse(filter.accept(null, "test"));
   }
   
   @Test
   public void testNullExpectedFileName() {
      exp.expect(NullPointerException.class);
      new CustomFilenameFilter(null);
   }
   
   @Test
   public void testNullFileToFilter() {
      exp.expect(NullPointerException.class);
      CustomFilenameFilter filter = new CustomFilenameFilter("test.txt");
      assertFalse(filter.accept(null, null));
   }
   
   @Test
   public void testNullDirectoryFile() {
      CustomFilenameFilter filter = new CustomFilenameFilter("test.txt");
      assertTrue(filter.accept(null, "test(1).txt"));
   }
}
