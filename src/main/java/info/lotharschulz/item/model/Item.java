package info.lotharschulz.item.model;

public class Item {

    private String description;
    private String label;

    public Item() {
    }

    public Item(String description, String label) {
        this.description = description;
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Item{" +
                "description=" + description +
                ",label=" + label +
                '}';
    }
}
