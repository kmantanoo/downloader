package test.model.table;

import static org.junit.Assert.*;

import org.junit.Test;

import model.table.DownloadTableModel;

public class TestDownloadTableModel {
   private static Object[][] data = { {"R0C0", "R0C1"}
                                    , {"R1C0", "R2C1"}
                                    , {"R2C0", "R2C1"}
                                    , {"R3C0", "R3C1"}};

   @Test
   public void testGetColumnNames() {
      assertArrayEquals(new Object[]{"Progress",  ""}, DownloadTableModel.getColumnNames());
   }
   
   @Test
   public void testIsCellEditable() {
      DownloadTableModel model = new DownloadTableModel(data);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertTrue(model.isCellEditable(0, 1));
      assertTrue(model.isCellEditable(1, 1));
      assertTrue(model.isCellEditable(2, 1));
      assertTrue(model.isCellEditable(3, 1));
      
      model.addExceptRow(2);
      model.addExceptRow(0);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertFalse(model.isCellEditable(0, 1));
      assertTrue(model.isCellEditable(1, 1));
      assertFalse(model.isCellEditable(2, 1));
      assertTrue(model.isCellEditable(3, 1));
      
      model.clearExceptRows();
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertTrue(model.isCellEditable(0, 1));
      assertTrue(model.isCellEditable(1, 1));
      assertTrue(model.isCellEditable(2, 1));
      assertTrue(model.isCellEditable(3, 1));
      
      model.exceptAll(true);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertFalse(model.isCellEditable(0, 1));
      assertFalse(model.isCellEditable(1, 1));
      assertFalse(model.isCellEditable(2, 1));
      assertFalse(model.isCellEditable(3, 1));
      
      model.exceptAll(false);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertTrue(model.isCellEditable(0, 1));
      assertTrue(model.isCellEditable(1, 1));
      assertTrue(model.isCellEditable(2, 1));
      assertTrue(model.isCellEditable(3, 1));
   }
   
   @Test
   public void testExceptAllRow() {
      DownloadTableModel model = new DownloadTableModel(data);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertTrue(model.isCellEditable(0, 1));
      assertTrue(model.isCellEditable(1, 1));
      assertTrue(model.isCellEditable(2, 1));
      assertTrue(model.isCellEditable(3, 1));
      
      model.exceptAll(true);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertFalse(model.isCellEditable(0, 1));
      assertFalse(model.isCellEditable(1, 1));
      assertFalse(model.isCellEditable(2, 1));
      assertFalse(model.isCellEditable(3, 1));
      
      model.exceptAll(false);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertTrue(model.isCellEditable(0, 1));
      assertTrue(model.isCellEditable(1, 1));
      assertTrue(model.isCellEditable(2, 1));
      assertTrue(model.isCellEditable(3, 1));
   }
   
   @Test
   public void testAddExceptRow() {
      DownloadTableModel model = new DownloadTableModel(data);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertTrue(model.isCellEditable(0, 1));
      assertTrue(model.isCellEditable(1, 1));
      assertTrue(model.isCellEditable(2, 1));
      assertTrue(model.isCellEditable(3, 1));
      
      model.addExceptRow(0);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertFalse(model.isCellEditable(0, 1));
      assertTrue(model.isCellEditable(1, 1));
      assertTrue(model.isCellEditable(2, 1));
      assertTrue(model.isCellEditable(3, 1));
      
      model.addExceptRow(1);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertFalse(model.isCellEditable(0, 1));
      assertFalse(model.isCellEditable(1, 1));
      assertTrue(model.isCellEditable(2, 1));
      assertTrue(model.isCellEditable(3, 1));
      
      model.addExceptRow(2);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertFalse(model.isCellEditable(0, 1));
      assertFalse(model.isCellEditable(1, 1));
      assertFalse(model.isCellEditable(2, 1));
      assertTrue(model.isCellEditable(3, 1));
      
      model.addExceptRow(3);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertFalse(model.isCellEditable(0, 1));
      assertFalse(model.isCellEditable(1, 1));
      assertFalse(model.isCellEditable(2, 1));
      assertFalse(model.isCellEditable(3, 1));
   }

   @Test
   public void testClearExceptRows() {
      DownloadTableModel model = new DownloadTableModel(data);
      model.addExceptRow(1);
      model.addExceptRow(2);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertTrue(model.isCellEditable(0, 1));
      assertFalse(model.isCellEditable(1, 1));
      assertFalse(model.isCellEditable(2, 1));
      assertTrue(model.isCellEditable(3, 1));
      
      model.clearExceptRows();
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertTrue(model.isCellEditable(0, 1));
      assertTrue(model.isCellEditable(1, 1));
      assertTrue(model.isCellEditable(2, 1));
      assertTrue(model.isCellEditable(3, 1));
      
      model.exceptAll(true);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertFalse(model.isCellEditable(0, 1));
      assertFalse(model.isCellEditable(1, 1));
      assertFalse(model.isCellEditable(2, 1));
      assertFalse(model.isCellEditable(3, 1));
      
      model.clearExceptRows();
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertTrue(model.isCellEditable(0, 1));
      assertTrue(model.isCellEditable(1, 1));
      assertTrue(model.isCellEditable(2, 1));
      assertTrue(model.isCellEditable(3, 1));
   }
   
   @Test
   public void testRemoveExceptRow() {
      DownloadTableModel model = new DownloadTableModel(data);
      model.addExceptRow(0);
      model.addExceptRow(1);
      model.addExceptRow(3);
      model.addExceptRow(2);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertFalse(model.isCellEditable(0, 1));
      assertFalse(model.isCellEditable(1, 1));
      assertFalse(model.isCellEditable(2, 1));
      assertFalse(model.isCellEditable(3, 1));
      
      model.removeExceptRow(0);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertTrue(model.isCellEditable(0, 1));
      assertFalse(model.isCellEditable(1, 1));
      assertFalse(model.isCellEditable(2, 1));
      assertFalse(model.isCellEditable(3, 1));
      
      model.removeExceptRow(1);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertTrue(model.isCellEditable(0, 1));
      assertTrue(model.isCellEditable(1, 1));
      assertFalse(model.isCellEditable(2, 1));
      assertFalse(model.isCellEditable(3, 1));
      
      model.removeExceptRow(2);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertTrue(model.isCellEditable(0, 1));
      assertTrue(model.isCellEditable(1, 1));
      assertTrue(model.isCellEditable(2, 1));
      assertFalse(model.isCellEditable(3, 1));
      
      model.removeExceptRow(3);
      assertFalse(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(1, 0));
      assertFalse(model.isCellEditable(2, 0));
      assertFalse(model.isCellEditable(3, 0));
      assertTrue(model.isCellEditable(0, 1));
      assertTrue(model.isCellEditable(1, 1));
      assertTrue(model.isCellEditable(2, 1));
      assertTrue(model.isCellEditable(3, 1));
   }
}
