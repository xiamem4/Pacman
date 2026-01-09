import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Acceuil extends JFrame {
    public Acceuil() {
        setTitle("Pacman - Sélection du niveau");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Titre
        JLabel titre = new JLabel("Choisissez votre niveau", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titre, BorderLayout.NORTH);

        // Panneau des boutons
        JPanel panelNiveaux = new JPanel();
        panelNiveaux.setLayout(new GridLayout(0, 3, 10, 10)); 
        panelNiveaux.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        int nombreDeNiveaux = 12;

        for (int i = 1; i < nombreDeNiveaux; i++) {
            final int indexNiveau = i; // Variable finale pour l'action
            JButton btn = new JButton("Niveau " + i);
            
            btn.addActionListener((ActionEvent e) -> {
                lancerJeu(indexNiveau);
            });
            
            panelNiveaux.add(btn);
        }

        add(new JScrollPane(panelNiveaux), BorderLayout.CENTER);
        setVisible(true);
    }

    private void lancerJeu(int indexNiveau) {
        // On ferme l'accueil
        this.dispose();

        // On lance l'interface principale en lui passant le niveau choisi
        SwingUtilities.invokeLater(() -> {
            new Interface(indexNiveau);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Acceuil::new);

    }
}
