import javax.swing.*;
import java.awt.*;

public class Interface extends JFrame {
    private Jeu jeuPanel;
    private JLabel vieLabel;

    public Interface() {
        setTitle("Pacman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Le HUD
        JPanel hudPanel = new JPanel();
        vieLabel = new JLabel("Vies : 3");
        hudPanel.add(vieLabel);
        add(hudPanel, BorderLayout.NORTH);

        // Le jeu
        jeuPanel = new Jeu(this);
        add(jeuPanel, BorderLayout.CENTER);

        setResizable(false);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        jeuPanel.requestFocusInWindow();
    }

    public void updateVie(int v) {
        vieLabel.setText("Vies : " + v);
    }

    public Jeu getJeu() {
        return this.jeuPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Interface fenetre = new Interface();
            Jeu jeu = fenetre.getJeu();
            Processus processus = new Processus(jeu);
            Thread thread = new Thread(processus);
            thread.start();
        });
    }
}