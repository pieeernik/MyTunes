package dk.igor.mytunes.exceptions;

import java.sql.SQLException;

public class PlayerException extends RuntimeException {
    public PlayerException(SQLException message) {super(message);}
    public PlayerException(String message) {super(message);}
    }
