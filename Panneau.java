import java.awt.Graphics;
import javax.swing.JPanel;
 
public class Panneau extends JPanel { 
  public void paintComponent(Graphics g){
    //Vous verrez cette phrase chaque fois que la méthode sera invoquée
    System.out.println("Je suis exécutée !"); 
    g.drawOval(20, 20, 50, 50);
    g.drawOval(100, 20, 50, 50);
    g.drawOval(180, 20, 50, 50);
    g.drawOval(300, 20, 50, 50);
    g.drawOval(400, 80, 50, 50);
    g.drawOval(30, 170, 50, 50);
    g.drawOval(20, 300, 50, 50);
    g.drawOval(20, 700, 50, 50);
    g.drawLine(45,45,50,200);

  }               
}