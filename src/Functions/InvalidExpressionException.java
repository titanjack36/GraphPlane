//---------------------INVALID EXPRESSION EXCEPTION CLASS---------------------//
//The exception is thrown when an expression inputted by the user cannot be
//parsed by the Function Parser class.

package Functions;

public class InvalidExpressionException extends Exception {

    public InvalidExpressionException() {
    }

    public InvalidExpressionException(String message) {
        super(message);
    }
}
