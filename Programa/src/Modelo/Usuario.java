package Modelo;

public abstract class Usuario {
    private String username;
    private String password;

    public Usuario(String username, String password) {
        this.username = username;
        this.password = password; //Implementar Hash
    }

    public String getUsername() {
        return username;
    }

    public boolean verificarPassword(String IntroPassword) {
        boolean verificacion = false; //Variable para poner en True si la contraseña introducida coincide

        return verificacion;
    }
}
