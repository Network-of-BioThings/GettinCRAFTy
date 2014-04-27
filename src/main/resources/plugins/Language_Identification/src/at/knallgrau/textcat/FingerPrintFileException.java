package at.knallgrau.textcat;

public class FingerPrintFileException extends Exception {
    private static final long serialVersionUID = -9000607002978507668L;

    public FingerPrintFileException() {
    }

    public FingerPrintFileException(String message, Throwable e) {
	super(message, e);
    }

    public FingerPrintFileException(Throwable e) {
	super(e);
    }

}
