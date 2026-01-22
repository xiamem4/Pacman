package pacman;
import java.awt.*;


public class Pacman extends EntiteMouvante{

    /** Intialisation du Pacman
     * @param x
     * @param y
     */
    public Pacman( int x, int y) {
        super(x, y);
    }

    // Méthode de déplacement
    public void bouger(Terrain terrain) {
        int futurX = x + dx;
        int futurY = y + dy;

        if (!terrain.estMur(futurX, futurY)) {
            terrain.deplacerPacman(x, y, futurX, futurY);
            this.x = futurX;
            this.y = futurY;

        }
    }

    // Vérifier je peux changer de direction
    public boolean peutTourner(Terrain terrain, int direction){
        int testDx = 0;
        int testDy = 0;

        switch (direction) {
            case 0 -> testDy = -1; // Haut
            case 1 -> testDy = 1; // Bas
            case 2 -> testDx = -1; // Gauche
            case 3 -> testDx = 1; // Droite
        }

        return !terrain.estMur(x + testDx, y +testDy);
    }

    // Dessiner le Pacamn
    public void dessiner(Graphics g, int tailleCase) {
        g.setColor(Color.YELLOW);
        g.fillOval(x * tailleCase, y * tailleCase, tailleCase, tailleCase);
    }

}