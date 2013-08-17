package info.lotharschulz.item.model;

public class RESTItem extends Item{

    private String externalID;

    public RESTItem() {
        super();
    }

    public RESTItem(String externalID, String description, String label) {
        super(description, label);
        this.externalID = externalID;
    }

    public RESTItem(String externalID, Item item) {
        super(item.getDescription(), item.getLabel());
        this.externalID = externalID;
    }


    public String getExternalID() {
        return externalID;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    @Override
    public String toString() {
        return "RESTItem{" +
                "externalID=" + externalID +
                ",description=" + super.getDescription() +
                ",label=" + super.getLabel() +
                '}';
    }

}
