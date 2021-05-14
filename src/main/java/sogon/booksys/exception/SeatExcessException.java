package sogon.booksys.exception;

public class SeatExcessException extends RuntimeException{
    public SeatExcessException() {
        super();
    }

    public SeatExcessException(String message) {
        super(message);
    }

    public SeatExcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeatExcessException(Throwable cause) {
        super(cause);
    }
}
