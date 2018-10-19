import java.awt.*;
import java.awt.event.*;
import java.io.*;

class ProgramForm extends Frame
   implements ActionListener, WindowListener
{  Label keyLabel;
   Label dataLabel;
   Label actionInfo;
   TextField keyText;
   TextField dataText;
   Button createButton;
   Button retrieveButton;
   Button updateButton;
   Button deleteButton;

   ProgramForm(String s)
   {  super(s);
      setSize(1200,100);
      setLayout(new FlowLayout(FlowLayout.LEADING));
      addWindowListener(this);

      // create the form labels and text boxes
      keyLabel = new Label("key: ");
      add(keyLabel);

      keyText = new TextField(25);
      add(keyText);

      dataLabel = new Label("data: ");
      add(dataLabel);

      dataText = new TextField(25);
      add(dataText);

      // create and add buttons
      createButton = new Button("create");
      add(createButton);
      createButton.addActionListener(this);

      retrieveButton = new Button("retrieve");
      add(retrieveButton);
      retrieveButton.addActionListener(this);

      updateButton = new Button("update");
      add(updateButton);
      updateButton.addActionListener(this);

      deleteButton = new Button("delete");
      add(deleteButton);
      deleteButton.addActionListener(this);

      actionInfo = new Label("CRUD - v0.2 copyright(c) 2018 Josh Roybal");
      add(actionInfo);

      setVisible(true);
   }

   public void windowClosed(WindowEvent event) {}
   public void windowDeiconified(WindowEvent event) {}
   public void windowIconified(WindowEvent event) {}
   public void windowActivated(WindowEvent event) {}
   public void windowDeactivated(WindowEvent event) {}
   public void windowOpened(WindowEvent event) {}

   public void windowClosing(WindowEvent event)
   {  System.exit(0);
   }

   public void actionPerformed(ActionEvent event)
   {  if (event.getActionCommand().equals("create"))
      {  String key = keyText.getText();
         String data = dataText.getText();
         String record = String.format("%40s%40s", key, data);
         boolean result = false;
         try { result = DirectFile.insert("records.dat", record); }
         catch (IOException ioe) { System.exit(1); }
         if (result)
            actionInfo.setText("record created");
         else
            actionInfo.setText("duplicate record not created");
         keyText.setText("");
         dataText.setText("");
      }

      if (event.getActionCommand().equals("retrieve"))
      {  String key = keyText.getText();
         String data = null;
         try { data = DirectFile.retrieve("records.dat", key); }
         catch (IOException ioe) { System.exit(1); }
         dataText.setText(data);
         if (data != null) actionInfo.setText("record retrieved");
         else actionInfo.setText("target not found");
      }

      if (event.getActionCommand().equals("update"))
      {  String key = keyText.getText();
         String data = dataText.getText();
         String record = String.format("%40s%40s", key, data);
         boolean result = false;
         try { result = DirectFile.update("records.dat", record); }
         catch ( IOException ioe ) { System.exit(1); }
         if (result)
            actionInfo.setText("record updated");
         else
            actionInfo.setText("target not found");
         keyText.setText("");
         dataText.setText("");
      }

      if (event.getActionCommand().equals("delete"))
      {  actionInfo.setText("under development");
         String key = keyText.getText();
         boolean result = false;
         try { result = DirectFile.delete("records.dat", key); }
         catch ( IOException ioe ) { System.exit(1); }
         if ( result )
            actionInfo.setText("record deleted");
         else
            actionInfo.setText("target not found");
         keyText.setText("");
         dataText.setText("");
      }
   }
}

class CRUD
{  static public void main(String[] args)
   {  new ProgramForm("CRUD");
   }
}
