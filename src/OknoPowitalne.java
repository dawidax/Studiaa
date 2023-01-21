import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;


public class OknoPowitalne extends JFrame{
    private JButton zalogujSieButton;
    private JButton wyjdźButton;
    private JButton zarejestrujSieButton;
    private JPanel Main;

    public OknoPowitalne (){

        zalogujSieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logowanie logowanie = new Logowanie(null);
            }
        });

        zarejestrujSieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Rejestracja rejestracja = new Rejestracja(null);
            }
        });
        wyjdźButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }


    public static void main(String[] args) {
        JFrame okno = new JFrame("Wypożyczalnia filmów");

        okno.setTitle("Wypożyczalnia filmów");
        okno.setContentPane(new OknoPowitalne().Main);
        okno.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        okno.setMinimumSize(new Dimension(520,450));
        okno.pack();
        okno.setVisible(true);
    }
}
