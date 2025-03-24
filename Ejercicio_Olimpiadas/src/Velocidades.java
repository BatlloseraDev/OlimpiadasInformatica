public enum Velocidades {
    MEDIA(1),
    ALTA(2);


    private final int velocidad;

    Velocidades(int velocidad) {
        this.velocidad = velocidad;
    }

    public int getVelocidad() {
        return velocidad;
    }

}
