import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Rejestracja extends JDialog {
    private JTextField tfImie;
    private JTextField tfNazwisko;
    private JTextField tfLogin;
    private JPasswordField pfHaslo;
    private JPasswordField pfPotwierdzHaslo;
    private JButton btnZarejestruj;
    private JButton btnAnuluj;
    private JPanel rejPanel;
    private JTextField emailtf;



    public Rejestracja(JFrame p){
        super(p);
        setTitle("Rejestracja");
        setContentPane(rejPanel);
        setMinimumSize(new Dimension(620, 450));
        setModal(true);
        setLocationRelativeTo(p);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnZarejestruj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodajUzytkownika();
            }
        });
        btnAnuluj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
        if(uzytkownik != null) {
            System.out.println("Udana rejestracja: " + uzytkownik.imie);
            JOptionPane.showMessageDialog(null, "Witaj "+uzytkownik.imie);
        }
        else{
            System.out.println("Rejestracja anulowana");
            JOptionPane.showMessageDialog(null,  "Rejestracja anulowana");
        }
    }

    private void dodajUzytkownika() {
        String imie = tfImie.getText();
        String nazwisko = tfNazwisko.getText();
        String email = emailtf.getText();
        String login = tfLogin.getText();
        String haslo = String.valueOf(pfHaslo.getPassword());
        String potwierdzHaslo = String.valueOf(pfPotwierdzHaslo.getPassword());

        if (imie.isEmpty() || nazwisko.isEmpty() ||email.isEmpty() || login.isEmpty() || haslo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Wypelnij wszystkie pola",
                    "Jeszcze raz",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!haslo.equals(potwierdzHaslo)){
            JOptionPane.showMessageDialog(this,
                    "Hasla roznia sie",
                    "Jeszcze raz",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        uzytkownik = dodajUzytkownikaDoBazyDanych(imie, nazwisko, email, login, haslo);
        if (uzytkownik != null){
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Nie udalo sie",
                    "Jeszcze raz",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    public Uzytkownik uzytkownik;
    private Uzytkownik dodajUzytkownikaDoBazyDanych(String imie, String nazwisko, String email, String login, String haslo) {
        Uzytkownik uzytkownik= null;
        final String DB_url = "jdbc:mysql://localhost/wypozyczalnia_filmow?serverTimezone=UTC";
        final String DB_login = "root";
        final String DB_haslo = "root";

        try {
            Connection conn = DriverManager.getConnection(DB_url, DB_login, DB_haslo);
            Statement st = conn.createStatement();
            String sql = "INSERT INTO klienci(imie, nazwisko, email, login, haslo)" +
                    "VALUES(?,?,?,?,?)";
            PreparedStatement pSt = conn.prepareStatement(sql);
            pSt.setString(1, imie);
            pSt.setString(2, nazwisko);
            pSt.setString(3, email);
            pSt.setString(4, login);
            pSt.setString(5, haslo);

            int wiersz = pSt.executeUpdate();
            if(wiersz > 0) {
                uzytkownik = new Uzytkownik();
                uzytkownik.imie = imie;
                uzytkownik.nazwisko = nazwisko;
                uzytkownik.email = email;
                uzytkownik.login = login;
                uzytkownik.haslo = haslo;
            }

            st.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return uzytkownik;
    }

    public static void main(String[] args) {
        Rejestracja rejestracja = new Rejestracja(null);
        Uzytkownik uzytkownik = rejestracja.uzytkownik;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}