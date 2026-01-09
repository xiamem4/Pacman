import java.awt.*;


public class Pacman extends EntiteMouvante{

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

    // Dessiner le Pacamn
    public void dessiner(Graphics g, int tailleCase) {
        g.setColor(Color.YELLOW);
        g.fillOval(x * tailleCase, y * tailleCase, tailleCase, tailleCase);
    }

}