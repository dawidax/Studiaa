import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Menu extends JFrame {

    private JPanel mainPanel;
    private JButton btnDodaj;
    private JTextField tfTytul;
    private JTextField tfRok;
    private JTextField tfRezyser;
    private JTextField tfGatunek;
    private JButton btnWyloguj;
    private JTextField tfId;
    private JButton btnUsun;
    private JButton btnSzukaj;

    public Menu() {
        setTitle("Admin Menu");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnDodaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tytul = tfTytul.getText();
                Date rok = Date.valueOf((tfRok.getText()));
                String rezyser = tfRezyser.getText();
                String gatunek = tfGatunek.getText();
                addMovie(tytul, rok, rezyser, gatunek);
            }
        });
        btnUsun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(tfId.getText());
                deleteMovie(id);
                tfTytul.setText("Title: ");
                tfRok.setText("Year: ");
                tfRezyser.setText("Director: ");
                tfGatunek.setText("Genre: ");
            }
        });
        btnSzukaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(tfId.getText());
                Film film = getMovie(id);
                if (film != null) {
                    tfTytul.setText(film.tytul);
                    tfRok.setText(String.valueOf(film.rok));
                    tfRezyser.setText(film.rezyser);
                    tfGatunek.setText(film.gatunek);
                } else {
                    JOptionPane.showMessageDialog(Menu.this, "Film o id " + id + " nie istnieje");
                }
            }
        });


        btnWyloguj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Logowanie login = new Logowanie(null);
                login.setVisible(true);
            }
        });
    }
    private void addMovie(String tytul, Date rok, String rezyser, String gatunek) {
        final String DB_url = "jdbc:mysql://localhost/wypozyczalnia_filmow?serverTimezone=UTC";
        final String DB_login = "root";
        final String DB_haslo = "root";
        try {
            Connection conn = DriverManager.getConnection(DB_url, DB_login, DB_haslo);
            String sql = "INSERT INTO filmy (tytul,rok_produkcji,rezyser,gatunek) VALUES (?,?,?,?)";
            PreparedStatement pSt = conn.prepareStatement(sql);

            pSt.setString(1, tytul);
            pSt.setDate(2, rok);
            pSt.setString(3, rezyser);
            pSt.setString(4, gatunek);
            pSt.executeUpdate();
            JOptionPane.showMessageDialog(Menu.this, "Pomyślnie dodano!");
            pSt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void deleteMovie(int id) {
        final String DB_url = "jdbc:mysql://localhost/wypozyczalnia_filmow?serverTimezone=UTC";
        final String DB_login = "root";
        final String DB_haslo = "";
        try {
            Connection conn = DriverManager.getConnection(DB_url, DB_login, DB_haslo);
            PreparedStatement pSt = conn.prepareStatement("DELETE FROM filmy WHERE id_filmu = ?");
            pSt.setInt(1, id);
            int result = pSt.executeUpdate();
            if(result > 0)
                JOptionPane.showMessageDialog(Menu.this, "Film usunięty!");
            else
                JOptionPane.showMessageDialog(Menu.this, "Nie znaleziono!");
            pSt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Film getMovie(int id) {
        final String DB_url = "jdbc:mysql://localhost/wypozyczalnia_filmow?serverTimezone=UTC";
        final String DB_login = "root";
        final String DB_haslo = "";
        Film film = null;
        try {
            Connection conn = DriverManager.getConnection(DB_url, DB_login, DB_haslo);
            PreparedStatement pSt = conn.prepareStatement("SELECT * FROM filmy WHERE id_filmu = ?");
            pSt.setInt(1, id);
            ResultSet rs = pSt.executeQuery();
            if(rs.next()) {
                film = new Film();
                film.id_filmu = rs.getInt("id_filmu");
                film.tytul = rs.getString("tytul");
                film.rok = rs.getDate("rok_produkcji");
                film.rezyser = rs.getString("rezyser");
                film.gatunek = rs.getString("gatunek");
            }
            pSt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return film;
    }
}


