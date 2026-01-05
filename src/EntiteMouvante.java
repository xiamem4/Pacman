public class EntiteMouvante {
    protected int x, y;
    protected int dx, dy;

    public EntiteMouvante(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters de position
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Définir une direction
    public void setDirection(int direction) {
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

    // Dans EntiteMouvante.java
    public void bouger(Terrain terrain) {
        int futurX = x + dx;
        int futurY = y + dy;

        // Le fantôme vérifie juste s'il y a un mur
        if (!terrain.estMur(futurX, futurY)) {
            this.x = futurX;
            this.y = futurY;
        }
    }

}
