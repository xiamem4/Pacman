package pacman;

public class EntiteMouvante {
    protected int x, y;
    protected int dx, dy;
    protected int directionActuelle = -1;

    public EntiteMouvante(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters et Setters de position
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Définir une direction
    public void setDirection(int direction) {
        this.directionActuelle = direction;
        switch (direction) {
            case 0 -> {
                this.dx = 0;
                this.dy = -1;
            } // Haut
            case 1 -> {
                this.dx = 0;
                this.dy = 1;
            } // Bas
            case 2 -> {
                this.dx = -1;
                this.dy = 0;
            } // Gauche
            case 3 -> {
                this.dx = 1;
                this.dy = 0;
            } // Droite
        }
    }

    // Faire un demi tour
    public void demiTour() {
        this.dx = -this.dx;
        this.dy = -this.dy;
    }

    /**
     * Méthode de déplacement
     * 
     * @param terrain : labyrinthe du niveau
     */
    public void bouger(Terrain terrain) {
        int futurX = x + dx;
        int futurY = y + dy;

        // Vérifier si mur
        if (!terrain.estMur(futurX, futurY)) {
            this.x = futurX;
            this.y = futurY;
        }
    }

}
