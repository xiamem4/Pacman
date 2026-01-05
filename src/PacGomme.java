public class PacGomme {
    private int x;
    private int y;

    /**
     * Initisalise une pacgomme aux coordonnées données
     * @param x : coordonnée horizontal
     * @param y : coordonnée vertical
     */
    public PacGomme(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getter coordonnée x
    public int getX() { 
        return x; 
    }

    // Getter coordonnée y
    public int getY() { 
        return y; 
    }
}
