package info.lotharschulz.item.model.data.exception;

public class ItemAlreadyExistsException extends Exception{
    private String exceptionMsg;
    private String existingItemID;

    private static final long serialVersionUID = -5562012974937511643L;


    public ItemAlreadyExistsException() {
        super();
    }

    public ItemAlreadyExistsException(String exceptionMsg, String existingItemID) {
        super(exceptionMsg);
        this.exceptionMsg = exceptionMsg;
        this.existingItemID = existingItemID;
    }

    public String getExceptionMsg(){
        return this.exceptionMsg;
    }

    public String getExistingItemID() {
        return existingItemID;
    }

    public void setExistingItemID(String existingItemID) {
        this.existingItemID = existingItemID;
    }
}
