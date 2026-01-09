import javax.swing.*;
import java.awt.*;

public class ZoneDeJeu extends JPanel {

    private Pacman pacman;
    private Fantome aleaFantome;

    private Terrain terrain;
    private final int TAILLE_CASE = 20;

    /** Constructeur de la zone de jeu
     * @param p : Pacman
     * @param terrain : paterne du terrain
     */
    public ZoneDeJeu(Terrain terrain, Pacman p, Fantome aF) {
        this.terrain = terrain;
        this.pacman = p;
        this.aleaFantome = aF;

        setPreferredSize(new Dimension(
                terrain.getNbColonnes() * TAILLE_CASE,
                terrain.getNbLignes() * TAILLE_CASE));
        setBackground(Color.BLACK);
    }

    // Setter Pacman
    public void setPacman(Pacman pacman) {
        this.pacman = pacman;
    }

    // Setter aleaFantome
    public void setAleaFantome(Fantome aleaFantome) {
        this.aleaFantome = aleaFantome;
    }

    // Affiche la grille
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        char[][] grille = terrain.getGrille();

        for (int ligne = 0; ligne < terrain.getNbLignes(); ligne++) {
            for (int colonne = 0; colonne < terrain.getNbColonnes(); colonne++) {

                int x = colonne * TAILLE_CASE;
                int y = ligne * TAILLE_CASE;

                switch (grille[ligne][colonne]) {

                    case 'M': // Mur
                        g.setColor(Color.BLUE);
                        g.fillRect(x, y, TAILLE_CASE, TAILLE_CASE);
                        break;

                    case '.': // PacGomme
                        g.setColor(Color.WHITE);
                        g.fillOval(
                                x + 8,
                                y + 8,
                                4,
                                4);
                        break;

                    case 'P': // Pacman
                        g.setColor(Color.YELLOW);
                        g.fillOval(x, y, TAILLE_CASE, TAILLE_CASE);
                        break;

                    case 'V': // Zone des fantomes
                        g.setColor(Color.GRAY);
                        g.fillRect(x, y, TAILLE_CASE, TAILLE_CASE);
                        break;
                }
            }
        }

        g.setColor(Color.WHITE);
        for (PacGomme gomme : terrain.getGommes()) {
            if (gomme instanceof SuperPacGomme) {
                g.setColor(Color.GREEN);
                g.fillOval(gomme.getX() * TAILLE_CASE + 4, gomme.getY() * TAILLE_CASE + 4, 12, 12);
            } else {
                g.setColor(Color.YELLOW);
                g.fillOval(gomme.getX() * TAILLE_CASE + 8, gomme.getY() * TAILLE_CASE + 8, 4, 4);
            }
        }

        if (pacman != null) {
            pacman.dessiner(g, TAILLE_CASE);
        }

        if (aleaFantome != null) {
            aleaFantome.dessiner(g, TAILLE_CASE);
        }
    }
}
