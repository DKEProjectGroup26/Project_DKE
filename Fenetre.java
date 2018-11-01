import java.awt.Color; 
import javax.swing.JFrame;
import javax.swing.JPanel;
 
public class Fenetre extends JFrame {
  public Fenetre(){             
    this.setTitle("Ma première fenêtre Java");

    this.setSize(800, 800);

    //Put the windows in the middle of the screen
    this.setLocationRelativeTo(null);               
 
    //Instanciation d'un objet JPanel
    JPanel pan = new JPanel();
    //Définition de sa couleur de fond -- Background color
    pan.setBackground(Color.ORANGE);        
    //On prévient notre JFrame que notre JPanel sera son content pane
    this.setContentPane(pan);  
    this.setContentPane(new Panneau());
             
    this.setVisible(true);
  }


  public static void main(String[] args) {
            //
             Fenetre fen = new Fenetre();
         }       
}