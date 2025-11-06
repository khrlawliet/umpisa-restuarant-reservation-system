package com.umpisa.restaurant.shared.exceptions;

/**
 * Exception thrown when reservation data is invalid.
 */
public class InvalidReservationException extends RuntimeException {

    public InvalidReservationException(String message) {
        super(message);
    }
}
