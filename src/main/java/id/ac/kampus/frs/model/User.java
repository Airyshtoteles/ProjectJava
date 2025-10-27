package id.ac.kampus.frs.model;

public class User {
    private int idUser;
    private String username;
    private String passwordHash;
    private Role role;

    public enum Role { ADMIN, DOSEN, MAHASISWA }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
