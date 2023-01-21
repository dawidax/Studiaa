import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class OknoFilmow extends JFrame {
    private JTable table;
    private JPanel panel;
    private JComboBox gatunek;
    private DefaultTableModel tableModel;

    public OknoFilmow() {
        super("Lista Filmów");

        String[] columnNames = {"Tytul", "Rok produkcji", "Rezyser", "Gatunek"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(panel);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] genres = {"Wszystko", "Dramat", "Thriller", "Horror", "SF"};
        gatunek = new JComboBox(genres);
        gatunek.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedGenre = (String) gatunek.getSelectedItem();
                pobierzFilmy(selectedGenre);
            }

        });


        panel.add(gatunek, BorderLayout.NORTH);

        pobierzFilmy("Wszystko");
    }

    private void pobierzFilmy(String gatunki) {
        tableModel.setRowCount(0);

        final String DB_url = "jdbc:mysql://localhost/wypozyczalnia_filmow?serverTimezone=UTC";
        final String DB_login = "root";
        final String DB_haslo = "";

        try {
            Connection conn = DriverManager.getConnection(DB_url, DB_login, DB_haslo);

            Statement st = conn.createStatement();
            String sql;
            if(gatunki.equals("Wszystko")) {
                sql = "SELECT * FROM filmy";
            } else {
                sql = "SELECT * FROM filmy WHERE gatunek = '" + gatunki + "'";
            }
            ResultSet rS = st.executeQuery(sql);

            while (rS.next()) {
                String tytul = rS.getString("tytul");
                String rok = rS.getString("rok_produkcji");
                String rezyser = rS.getString("rezyser");
                String gatunek1 = rS.getString("gatunek");

                Object[] data = {tytul, rok, rezyser, gatunek1};
                tableModel.addRow(data);
            }

            st.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            OknoFilmow oknoFilmow = new OknoFilmow();
            oknoFilmow.setVisible(true);
        });
    }
}