import javax.swing.*;
import java.awt.*;

public class Interface extends JFrame {
    private Jeu jeuPanel;
    private JLabel vieLabel;
    private JLabel scoreLabel;

    /** Initialise l'interface
     * @param niveauChoisi : labyrinthe du niveau
     */
    public Interface(int niveauChoisi) {
        setTitle("Pacman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Le HUD
        JPanel hudPanel = new JPanel();
        hudPanel.setLayout(new BorderLayout());
        hudPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        vieLabel = new JLabel("Vies : 3");
        hudPanel.add(vieLabel, BorderLayout.EAST);

        scoreLabel = new JLabel("Score : 0");
        hudPanel.add(scoreLabel, BorderLayout.WEST);

        add(hudPanel, BorderLayout.NORTH);

        // Le jeu
        jeuPanel = new Jeu(this, niveauChoisi);
        add(jeuPanel, BorderLayout.CENTER);

        setResizable(false);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        jeuPanel.requestFocusInWindow();

        Processus processus = new Processus(jeuPanel);
        Thread thread = new Thread(processus);
        thread.start();
    }

    // MaJ du nombre de vie
    public void updateVie(int v) {
        vieLabel.setText("Vies : " + v);
    }

    // MaJ du score
    public void updateScore (int s) {
        scoreLabel.setText("Score : " + s);
    }

    public Jeu getJeu() {
        return this.jeuPanel;
    }

}