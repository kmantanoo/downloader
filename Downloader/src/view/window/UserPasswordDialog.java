package view.window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class UserPasswordDialog extends JDialog {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private final JPanel contentPanel = new JPanel();
   private JTextField txtUsername;
   private JPasswordField txtPassword;

   /**
    * Create the dialog.
    */
   public UserPasswordDialog(AppWindow window, String title) {

      super(window, title, true);


      setBounds(100, 100, 367, 234);
      getContentPane().setLayout(new BorderLayout());
      contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
      getContentPane().add(contentPanel, BorderLayout.CENTER);
      contentPanel.setLayout(null);

      JLabel lblNewLabel = new JLabel("Username");
      lblNewLabel.setBounds(47, 51, 63, 14);
      contentPanel.add(lblNewLabel);

      JLabel lblNewLabel_1 = new JLabel("Password");
      lblNewLabel_1.setBounds(47, 87, 63, 14);
      contentPanel.add(lblNewLabel_1);

      txtUsername = new JTextField();
      txtUsername.setBounds(120, 48, 167, 20);
      contentPanel.add(txtUsername);
      txtUsername.setColumns(10);

      txtPassword = new JPasswordField();
      txtPassword.setBounds(120, 84, 167, 20);
      contentPanel.add(txtPassword);
      {
         UserPasswordActionListener al = new UserPasswordActionListener(this);
         JPanel buttonPane = new JPanel();
         buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
         getContentPane().add(buttonPane, BorderLayout.SOUTH);
         {
            JButton okButton = new JButton("OK");
            okButton.setActionCommand("OK");
            okButton.addActionListener(al);
            buttonPane.add(okButton);
            getRootPane().setDefaultButton(okButton);
         }
         {
            JButton cancelButton = new JButton("Cancel");
            cancelButton.setActionCommand("Cancel");
            cancelButton.addActionListener(al);
            buttonPane.add(cancelButton);
         }
      }
   }
   
   public String getUsername() {
      return txtUsername.getText();
   }
   
   public String getPassword() {
      return new String(txtPassword.getPassword());
   }

   private class UserPasswordActionListener implements ActionListener {
      private JDialog dialog;

      public UserPasswordActionListener(JDialog dialog) {
         this.dialog = dialog;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         switch (e.getActionCommand()) {
         case "Cancel":
            doCancel();
            break;
         case "OK":
            doOK();
            break;
         }
      }

      private void doCancel() {
         dialog.setVisible(false);
      }

      private void doOK() {
         String username, password;
         username = txtUsername.getText();
         password = new String(txtPassword.getPassword());
         if ("".equals(username)) {
            JOptionPane.showMessageDialog(dialog, "Username cannot be null", "Null input for username",
                  JOptionPane.WARNING_MESSAGE);
         } else if ("".equals(password)) {
            JOptionPane.showMessageDialog(dialog, "Password cannot be null", "Null input for password",
                  JOptionPane.WARNING_MESSAGE);
         } else {
            dialog.setVisible(false);
         }
      }

   }
}
