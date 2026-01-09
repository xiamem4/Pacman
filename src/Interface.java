import javax.swing.*;
import java.awt.*;

public class Interface extends JFrame {
    private Jeu jeuPanel;
    private JLabel vieLabel;

    public Interface(int niveauChoisi) {
        setTitle("Pacman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Le HUD
        JPanel hudPanel = new JPanel();
        vieLabel = new JLabel("Vies : 3");
        hudPanel.add(vieLabel);
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

    public void updateVie(int v) {
        vieLabel.setText("Vies : " + v);
    }

    public Jeu getJeu() {
        return this.jeuPanel;
    }

}