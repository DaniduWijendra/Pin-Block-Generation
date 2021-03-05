/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pinblockproject;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author danidu_o
 */
public class PinBlockProject extends Application {
    
    PinBlockData pinBlockData = new PinBlockData();
    Label lblcNum = new Label("Enter Card Number: ");
    Label lblPin = new Label("Enter Pin Number: ");
    Label lblTWk = new Label("Enter Terminal Working Key: ");
    Label lblTMK = new Label("Terminal Master Key: ");
    Label lblC1 = new Label("Component 1");
    Label lblC2 = new Label("Component 2");
    Label lblC3 = new Label("Component 3");
        
    TextField txtcNum = new TextField();
    PasswordField txtPin = new PasswordField();
    TextField txtcTWK = new TextField();
    TextField txtC1 = new TextField();
    TextField txtC2 = new TextField();
    TextField txtC3 = new TextField();
    String error="";
    String pinBlock = "";
    String encryptBlock = "";
    String decryptBlock = "";
    
    
    
    @Override
    public void start(Stage primaryStage) {
        pinBlockData.setCardNum("7774444455555555");
        pinBlockData.setPin("4671");
        pinBlockData.setTwk("11111111abc61111111a111f11457111");
        pinBlockData.setC1("11111111111111111111111111111111");
        pinBlockData.setC2("11111111111111111111111111111111");
        pinBlockData.setC3("11111111111111111111111111111111");
        
        txtcNum.setText(pinBlockData.getCardNum());
        txtPin.setText(pinBlockData.getPin());
        txtcTWK.setText(pinBlockData.getTwk());
        txtC1.setText(pinBlockData.getC1());
        txtC2.setText(pinBlockData.getC2());
        txtC3.setText(pinBlockData.getC3());
        
        blockGeneration();
        
       
        Button submit = new Button("Submit");
        Button reset = new Button("Reset");
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 400, 400);
        root.addRow(0, lblcNum,txtcNum);
        root.addRow(1, lblPin,txtPin);
        root.addRow(2, lblTWk,txtcTWK);
        root.addRow(3, lblTMK);
        root.addRow(4, lblC1,txtC1);
        root.addRow(5, lblC2,txtC2);
        root.addRow(6, lblC3,txtC3);      
        root.addRow(7, submit,reset);
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        
        Alert a = new Alert(Alert.AlertType.WARNING);
  
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                  checkData();
                  if(validate())
                  {
                      System.out.println("Success");
                     
                      a.setAlertType(Alert.AlertType.CONFIRMATION);
                      a.setContentText("Clear Pin Block: " + pinBlock + "\nEncrypted Pin Block: " + encryptBlock + "\nDecrypt Pin Block: " + decryptBlock);                       
                      a.show();
                  }
                  else
                  {
                      a.setAlertType(Alert.AlertType.ERROR);
                      a.setContentText(error);
                      a.show();
                      System.out.println("Failure");
                      
                  }
            }
        });
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                reset();
            }
        });
       
        primaryStage.setTitle("Pin Block Project");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void blockGeneration()
    {
         try
        {
            ThripleDES td = new ThripleDES(pinBlockData.getCardNum(), pinBlockData.getPin());
            pinBlock=td.generatePinBlock(pinBlockData.getCardNum(),pinBlockData.getPin());
            byte tmp[] = td.xorBytes3(ThripleDES.h2b(pinBlockData.getC1()), ThripleDES.h2b(pinBlockData.getC2()), ThripleDES.h2b(pinBlockData.getC3()));
            String tmkXor = ThripleDES.b2h(tmp);
            String decryptedKey = td.decrypt(pinBlockData.getTwk(), tmkXor);
                          
                          System.out.println("TMK after xor: " + tmkXor);
                          System.out.println("TWK after decrypt " + decryptedKey);
                          System.out.println("Generated Pin Block: " + pinBlock);
                          encryptBlock = td.encrypt(pinBlock,decryptedKey);
                          decryptBlock = td.decrypt(encryptBlock, decryptedKey);
                         
                          
                          System.out.println("Encrypted Pin Block: " + encryptBlock);
                          
                          System.out.println("Decrypted Pin Block: " + td.decrypt(encryptBlock, decryptedKey));
                          
                          
                         
                           
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
    }
    
    private void reset()
    {
        txtcNum.clear();
        txtPin.clear();
        txtcTWK.clear();
        txtC1.clear();
        txtC2.clear();
        txtC3.clear();
    }
    
    private void checkData()
    {

        System.out.println("Card Num: " + pinBlockData.getCardNum());
        System.out.println("Card Pin: " + pinBlockData.getPin());
        System.out.println("Card twk: " + pinBlockData.getTwk());
        System.out.println("Card c1: " + pinBlockData.getC1());
        System.out.println("Card c2: " + pinBlockData.getC2());
        System.out.println("Card c3: " + pinBlockData.getC3());
    }
    
   private boolean validate()
   {
      boolean ok = false;
      
      try
      {
       if(!Util.validateNUMBER(pinBlockData.getCardNum()))
       {
           error ="Card Length not valid";
           
       }
       else if(!Util.validatePin(pinBlockData.getPin()))
       {
           error ="Pin not valid";
          
       }
       else if(pinBlockData.getC1().trim().length()!=32)
       {
           error = "C1 Length not valid";
          
       }
       else if(pinBlockData.getC2().trim().length()!=32)
       {
           error = "C2 Length not valid";
           
       }
       else if(pinBlockData.getC3().trim().length()!=32)
       {
           error = "C3 Length not valid";
           
       }
       else if(pinBlockData.getCardNum().isEmpty() || pinBlockData.getCardNum().equals(""))
       {
           error = "Card Number is empty";
           
       }
       else if(pinBlockData.getPin().isEmpty() || pinBlockData.getPin().equals(""))
       {
           error = "Pin empty";
           
       }
       else if(pinBlockData.getTwk().isEmpty() || pinBlockData.getTwk().equals(""))
       {
           error = "TWK is empty";
           
       }
       else if(pinBlockData.getC1().isEmpty() || pinBlockData.getC1().equals(""))
       {
           error = "C1 is empty";
           
       }
       else if(pinBlockData.getC2().isEmpty() || pinBlockData.getC2().equals(""))
       {
           error = "C2 is empty";
           
       }
       else if(pinBlockData.getC3().isEmpty() || pinBlockData.getC3().equals(""))
       {
           error = "C3 is empty";
           
       }
       else
       {
           ok=true;
       }
      }
       catch(Exception e)
       {
           e.printStackTrace();
       }
       
       System.out.println(error);
       return ok;
   }
    
}

