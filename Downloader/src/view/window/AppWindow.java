package view.window;

import java.nio.file.Path;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import controller.DownloadManager;
import model.CredentialInformation;
import model.enumeration.DownloadState;
import view.panel.ControlPanel;
import view.panel.DownloadList;

public class AppWindow extends JFrame {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private JPanel contentPane;
   private ControlPanel controlPanel;
   private DownloadList downloadList;
   private DownloadManager dlManager;
   private static AppWindow window;


   /**
    * Create the frame.
    */
   public AppWindow() {

      setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 639, 466);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);

      controlPanel = new ControlPanel(this);
      controlPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
      controlPanel.setBounds(10, 11, 613, 96);
      contentPane.add(controlPanel);

      downloadList = new DownloadList(this);
      downloadList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
      downloadList.setBounds(10, 118, 613, 308);
      contentPane.add(downloadList);

   }
   
   public void setDownloadManager(DownloadManager dlManager) {
      this.dlManager = dlManager;
      controlPanel.setDestination(dlManager.getDestinationFolder());
   }

   public static AppWindow getInstance() {
      if (window == null)
         window = new AppWindow();
      return window;
   }

   public void addRow(Object[] objects) {
      downloadList.addRow(objects);
   }

   public void setValueAt(Object obj, int row, int column) {
      downloadList.setValueAt(obj, row, column);
   }

   public void downloadFiles(String sources) {
      dlManager.setSources(sources);
      dlManager.downloadFiles();
   }

   public void clearDataTable() {
      downloadList.clearDataTable();
   }

   public void changeDestination(String destination) {
      dlManager.setDestinationFolder(destination);
   }

   public String getSourceAtIndex(int index) {
      return dlManager.getSourceAtIndex(index);
   }

   public Path getSavedPathAtIndex(int index) {
      return dlManager.getSavedFile(index);
   }

   public DownloadState getDownloadState(int downloadSeq) {
      return dlManager.getDownloadState(downloadSeq);
   }

   public void cancelDownload(int downloadSeq) {
      dlManager.cancelDownload(downloadSeq);
   }

   public void cancelDownload() {
      dlManager.cancelDownload();
   }

   public void makeButtonClickableAllRow(boolean clickable) {
      downloadList.makeButtonClickableAllRow(clickable);
   }

   public void restartWorker(int downloadSeq) {
      dlManager.restartWorker(downloadSeq);
   }

   public CredentialInformation getCredentialInfo(String requester) {
      String title = String.format("Username/password for %s", requester);
      UserPasswordDialog userPasswordDialog = new UserPasswordDialog(this, title);
      userPasswordDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      userPasswordDialog.setVisible(true);
      
      String uname, password;
      uname = userPasswordDialog.getUsername();
      password = userPasswordDialog.getPassword();
      
      if ("".equals(uname) && "".equals(password)) {
         return null;
      }
      return new CredentialInformation(userPasswordDialog.getUsername(), userPasswordDialog.getPassword());
   }
}
