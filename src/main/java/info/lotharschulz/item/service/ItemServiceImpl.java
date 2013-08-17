package info.lotharschulz.item.service;

import info.lotharschulz.item.model.data.exception.ItemAlreadyExistsException;
import info.lotharschulz.item.model.Item;
import info.lotharschulz.item.model.RESTItem;
import info.lotharschulz.item.model.data.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("ItemService")
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDao itemDao;

    @Override
    public RESTItem getItemByExternalId(String externalID) {
        return this.itemDao.getItemByExternalId(externalID);
    }

    @Override
    public RESTItem updateItemByExternalID(String externalID, String description, String label) {
        RESTItem ri = new RESTItem(externalID, description, label);
        if(this.itemDao.updateItemByExternalID(ri)){
            return ri;
        }
        return null;
    }

    @Override
    public void deleteItemByExternalID(String externalID) {
        this.itemDao.deleteItemByExternalID(externalID);
    }

    @Override
    public RESTItem insertItem(RESTItem RESTItem) throws ItemAlreadyExistsException{
        return this.itemDao.insertItem(RESTItem);
    }

    @Override
    public RESTItem insertItem(String externalID, Item item) throws ItemAlreadyExistsException{
        return this.itemDao.insertItem(externalID, item);
    }

    @Override
    public List<RESTItem> getAllItems() {
        return new ArrayList<RESTItem>(this.itemDao.selectAll().values());
    }

}
