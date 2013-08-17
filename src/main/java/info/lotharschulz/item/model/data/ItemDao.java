package info.lotharschulz.item.model.data;


import info.lotharschulz.item.model.data.exception.ItemAlreadyExistsException;
import info.lotharschulz.item.model.Item;
import info.lotharschulz.item.model.RESTItem;

import java.util.Map;

public interface ItemDao {
    public Map selectAll();

    public RESTItem getItemByExternalId(String externalID);

    public boolean updateItemByExternalID(RESTItem RESTItem);

    public boolean deleteItemByExternalID(String externalID);

    public RESTItem insertItem(RESTItem RESTItem) throws ItemAlreadyExistsException;

    public RESTItem insertItem(String externalID, Item item) throws ItemAlreadyExistsException;
}
