package pacman;

import java.awt.*;
import java.util.*;

public class Fantome extends EntiteMouvante {
    private Random rand = new Random();

    public Fantome(int x, int y) {
        super(x, y);
    }

    // Choisit une direction aléatoire
    public void choisirDirectionAleatoire() {
        int direction = rand.nextInt(4);
        this.setDirection(direction);
    }

    /**
     * Déplacement aléatoire du fantôme
     * @param terrain : labyrinthe du niveau
     */
    public void bougerAlea(Terrain terrain) {

        ArrayList<Integer> directionsPossibles = new ArrayList<>();

        if (!terrain.estMur(x, y - 1))
            directionsPossibles.add(0); // Haut
        if (!terrain.estMur(x, y + 1))
            directionsPossibles.add(1); // Bas
        if (!terrain.estMur(x - 1, y))
            directionsPossibles.add(2); // Gauche
        if (!terrain.estMur(x + 1, y))
            directionsPossibles.add(3); // Droite


        // Retire le demi-tour
        if (directionsPossibles.size() > 1) {
            directionsPossibles.remove(Integer.valueOf(getDirectionOpposee()));

        }

        // Si cul de sac ou non
        if (directionsPossibles.size() > 1) {
            boolean peutContinuer = directionsPossibles.contains(directionActuelle);
            
            // Si intersection
            if (directionsPossibles.size() >= 2 || !peutContinuer){
                setDirection(directionsPossibles.get(rand.nextInt(directionsPossibles.size())));
            }
        } else if (directionsPossibles.size() == 1) {
            setDirection(directionsPossibles.get(0));
        }

        super.bouger(terrain);
    }

    // Récupérer la direction opposée
    private int getDirectionOpposee() {
        int directionOpposee = -1;
        switch (directionActuelle) {
            case 0:
                directionOpposee = 1;
                break;
            case 1:
                directionOpposee = 0;
                break;
            case 2:
                directionOpposee = 3;
                break;
            case 3:
                directionOpposee = 2;
                break;
        }
        return directionOpposee;
    }

    // Dessiner le Fantome
    public void dessiner(Graphics g, int tailleCase) {
        g.setColor(Color.GREEN);
        g.fillOval(x * tailleCase, y * tailleCase, tailleCase, tailleCase);
    }

    // Dessiner le fantome vulnérable
    public void dessiner(Graphics g, int tailleCase, Color couleur) {
        g.setColor(couleur);
        g.fillOval(x * tailleCase, y * tailleCase, tailleCase, tailleCase);
    }

}
