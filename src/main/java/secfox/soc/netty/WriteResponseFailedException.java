package secfox.soc.netty;

public class WriteResponseFailedException extends RuntimeException{

	 private static final long serialVersionUID = 2462223247762460301L;

	    public WriteResponseFailedException() { }

	    public WriteResponseFailedException(String s) {
	        super(s);
	    }

	    public WriteResponseFailedException(Throwable cause) {
	        super(cause);
	    }

	    public WriteResponseFailedException(String message, Throwable cause) {
	        super(message, cause);
	    }
}
