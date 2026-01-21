package pacman;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class Acceuil extends JFrame {
    public Acceuil() {
        setTitle("Pacman - Sélection du niveau");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Titre
        JLabel titre = new JLabel("Choisissez votre niveau", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titre, BorderLayout.NORTH);

        // Panneau des boutons
        JPanel panelNiveaux = new JPanel();
        panelNiveaux.setLayout(new FlowLayout(FlowLayout.CENTER,10, 10)); 
        panelNiveaux.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Dynamisation du nombre de niveaux
        File dossierNiveaux = new File("niveaux");
        File[] fichiers = dossierNiveaux.listFiles((dir, name) -> name.startsWith("niveau") && name.endsWith(".txt"));


        if(fichiers != null && fichiers.length > 0) {
            Dimension tailleBouton = new Dimension(110, 60);

            for (int i = 1; i <= fichiers.length; i ++) {
                final int indexNiveau = i;
                JButton btn = new JButton("Niveau " + i);

                btn.setPreferredSize(tailleBouton);
                btn.addActionListener((ActionEvent e)-> {
                    lancerJeu(indexNiveau);
                });

                panelNiveaux.add(btn);
            }
        } else  {
            panelNiveaux.add(new JLabel("Aucun niveau trouvé"));
        }

        JScrollPane scrollPane = new JScrollPane(panelNiveaux);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panelNiveaux.setPreferredSize(new Dimension(680, 600));
        
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.getViewport().addChangeListener(e -> panelNiveaux.revalidate());
        setVisible(true);
    }

    private void lancerJeu(int indexNiveau) {
        this.dispose();

        // On lance l'interface principale en passant le niveau choisi
        SwingUtilities.invokeLater(() -> {
            new Interface(indexNiveau);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Acceuil::new);

    }
}
