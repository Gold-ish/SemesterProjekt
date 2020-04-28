/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package errorhandling;

/**
 *
 * @author allan
 */
public class MovieNotFoundException extends Exception {

    public MovieNotFoundException(String message) {
        super(message);
    }

    public MovieNotFoundException() {
        super("Requested item could not be found");
    }
}
