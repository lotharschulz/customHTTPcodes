package info.lotharschulz.item.service;

import info.lotharschulz.item.model.data.exception.ItemAlreadyExistsException;
import info.lotharschulz.item.model.Item;
import info.lotharschulz.item.model.RESTItem;

import java.util.List;

public interface ItemService {

    public RESTItem getItemByExternalId(String externalID);

    public RESTItem updateItemByExternalID(String externalID, String description, String label);

    public void deleteItemByExternalID(String externalID);

    public RESTItem insertItem(RESTItem RESTItem) throws ItemAlreadyExistsException;

    public RESTItem insertItem(String externalID, Item item) throws ItemAlreadyExistsException;

    public List<RESTItem> getAllItems();
}
