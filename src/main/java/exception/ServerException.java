package exception;

/**
 * @author bsun
 */
public class ServerException extends Exception {
    protected ServerException() {
        super();
    }

    public ServerException(Throwable cause) {
        super(cause);
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
