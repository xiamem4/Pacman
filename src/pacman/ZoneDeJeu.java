package pacman;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ZoneDeJeu extends JPanel {

    private Pacman pacman;
    private List<Fantome> fantomes;
    private List<Fantome> fantomesEnAttente;
    private boolean fantomesVulnerables = false;
    private Terrain terrain;
    private final int TAILLE_CASE = 20;

    /**
     * Constructeur de la zone de jeu
     * 
     * @param p       : Pacman
     * @param terrain : paterne du terrain
     */
    public ZoneDeJeu(Terrain terrain, Pacman p, List<Fantome> fantomes) {
        this.terrain = terrain;
        this.pacman = p;
        this.fantomes = fantomes;

        setPreferredSize(new Dimension(
                terrain.getNbColonnes() * TAILLE_CASE,
                terrain.getNbLignes() * TAILLE_CASE));
        setBackground(Color.BLACK);
    }

    // Setter Pacman
    public void setPacman(Pacman pacman) {
        this.pacman = pacman;
    }

    // Setter fantome

    public void setFantomes(List<Fantome> fantomes) {
        this.fantomes = fantomes;
    }

    // Setter fantome en attente
    public void setFantomesEnAttente(List<Fantome> fEnAttente) {
        this.fantomesEnAttente = fEnAttente;
    }

    // Setter fantome vulnérable
    public void setFantomesVulnerables(boolean v) {
        this.fantomesVulnerables = v;
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

                    case 'F': // Zone des fantomes
                        g.setColor(Color.BLACK);
                        g.fillRect(x, y, TAILLE_CASE, TAILLE_CASE);
                        break;
                }
            }
        }

        g.setColor(Color.WHITE);
        for (PacGomme gomme : terrain.getGommes()) {
            if (gomme instanceof SuperPacGomme) {
                g.setColor(Color.ORANGE);
                g.fillOval(gomme.getX() * TAILLE_CASE + 4, gomme.getY() * TAILLE_CASE + 4, 12, 12);
            } else {
                g.setColor(Color.YELLOW);
                g.fillOval(gomme.getX() * TAILLE_CASE + 8, gomme.getY() * TAILLE_CASE + 8, 4, 4);
            }
        }

        if (pacman != null) {
            pacman.dessiner(g, TAILLE_CASE);
        }


        // Fantomes actifs
        if (fantomes != null) {
            for (Fantome f : fantomes) {
                if (fantomesVulnerables) {
                    f.dessiner(g, TAILLE_CASE, Color.BLUE);
                } else {
                    f.dessiner(g, TAILLE_CASE);
                }
            }
        }

        // Fantomes en attentes
        if (fantomesEnAttente != null) {
            for (Fantome f : fantomesEnAttente) {
                f.dessiner(g, TAILLE_CASE);
            }
        }
    }
}
