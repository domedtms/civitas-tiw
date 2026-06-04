package it.polimi.tiw.civitas.service;

import it.polimi.tiw.civitas.dao.UserDAO;
import it.polimi.tiw.civitas.model.User;
import it.polimi.tiw.civitas.util.PasswordUtil;

import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Pattern;

public class AuthService {

    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MIN_PASSWORD_LENGTH = 8;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User register(String username, String email, String plainPassword)
            throws SQLException, AuthException {

        String normalizedUsername = normalize(username);
        String normalizedEmail = normalize(email).toLowerCase();

        validateRegistrationInput(normalizedUsername, normalizedEmail, plainPassword);

        if (userDAO.existsByUsername(normalizedUsername)) {
            throw new AuthException("Nome utente già in uso.");
        }

        if (userDAO.existsByEmail(normalizedEmail)) {
            throw new AuthException("Email già in uso.");
        }

        String passwordHash = PasswordUtil.hashPassword(plainPassword);

        User user = new User();
        user.setUsername(normalizedUsername);
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordHash);

        int generatedId = userDAO.create(user);

        return userDAO.findById(generatedId)
                .orElseThrow(() -> new AuthException("Registrazione utente non riuscita."));
    }

    public User login(String email, String plainPassword)
            throws SQLException, AuthException {

        String normalizedEmail = normalize(email).toLowerCase();

        if (isBlank(normalizedEmail) || isBlank(plainPassword)) {
            throw new AuthException("Email o password non validi.");
        }

        Optional<User> userOptional = userDAO.findByEmail(normalizedEmail);

        if (userOptional.isEmpty()) {
            throw new AuthException("Email o password non validi.");
        }

        User user = userOptional.get();

        if (!PasswordUtil.verifyPassword(plainPassword, user.getPasswordHash())) {
            throw new AuthException("Email o password non validi.");
        }

        return user;
    }

    private void validateRegistrationInput(String username, String email, String plainPassword)
            throws AuthException {

        if (isBlank(username) || username.length() < MIN_USERNAME_LENGTH) {
            throw new AuthException("Il nome utente deve contenere almeno " + MIN_USERNAME_LENGTH + " caratteri.");
        }

        if (isBlank(email) || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new AuthException("Formato email non valido.");
        }

        if (isBlank(plainPassword) || plainPassword.length() < MIN_PASSWORD_LENGTH) {
            throw new AuthException("La password deve contenere almeno " + MIN_PASSWORD_LENGTH + " caratteri.");
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
