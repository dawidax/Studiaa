import com.mysql.cj.x.protobuf.Mysqlx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Logowanie extends JDialog {
    private JTextField tfLogin;
    private JTextField tfHaslo;
    private JButton btnZaloguj;
    private JButton btnAnuluj;
    private JPanel logPanel;
    private JPasswordField pfHaslo;
    private JComboBox cbLoginType;

    public Logowanie(JFrame p) {
        super(p);
        setTitle("Login");
        setContentPane(logPanel);
        setMinimumSize(new Dimension(520, 450));
        setModal(true);
        setLocationRelativeTo(p);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnZaloguj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = tfLogin.getText();
                String haslo = String.valueOf(pfHaslo.getPassword());
                String loginType = (String) cbLoginType.getSelectedItem();

                uzytkownik = sprawdzDane(login, haslo,loginType);

                if (uzytkownik != null) {
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(Logowanie.this,
                            "Błędny login lub hasło",
                            "Spróbuj jeszcze raz",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnAnuluj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    public Uzytkownik uzytkownik;
    private Uzytkownik sprawdzDane(String login, String haslo, String loginType) {
        Uzytkownik uzytkownik = null;

        final String DB_url = "jdbc:mysql://localhost/wypozyczalnia_filmow?serverTimezone=UTC";
        final String DB_login = "root";
        final String DB_haslo = "root";

        try {
            Connection conn = DriverManager.getConnection(DB_url, DB_login, DB_haslo);

            Statement st = conn.createStatement();
            String sql = "SELECT * FROM klienci WHERE login=? AND haslo=?";
            PreparedStatement pSt = conn.prepareStatement(sql);
            pSt.setString(1, login);
            pSt.setString(2, haslo);

            ResultSet rS = pSt.executeQuery();

            if(rS.next()) {
                uzytkownik = new Uzytkownik();
                uzytkownik.imie = rS.getString("imie");
                uzytkownik.nazwisko = rS.getString("nazwisko");
                uzytkownik.login = rS.getString("login");
                uzytkownik.haslo = rS.getString("haslo");
                uzytkownik.email = rS.getString("email");
            }

            st.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        if(uzytkownik != null) {
            if (loginType.equals("Admin")) {
                dispose();
                Menu menu = new Menu();
                menu.setVisible(true);
            } else {
                dispose();
                OknoFilmow oknoFilmow = new OknoFilmow();
                oknoFilmow.setVisible(true);
            }
        }
        else{
            JOptionPane.showMessageDialog(Logowanie.this,
                    "Błędny login lub hasło",
                    "Spróbuj jeszcze raz",
                    JOptionPane.ERROR_MESSAGE);
        }
        return uzytkownik;
    }

    public static void main(String[] args) {
        Logowanie logowanie = new Logowanie(null);
        Uzytkownik uzytkownik = logowanie.uzytkownik;

    }
}
