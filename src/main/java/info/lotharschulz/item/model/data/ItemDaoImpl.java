package info.lotharschulz.item.model.data;

import info.lotharschulz.item.model.Item;
import info.lotharschulz.item.model.RESTItem;
import info.lotharschulz.item.model.data.exception.ItemAlreadyExistsException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
*/

@Repository("ItemDao")
public class ItemDaoImpl implements ItemDao {

    private static final Logger log = Logger.getLogger(ItemDaoImpl.class);
    private List<RESTItem> myRESTItems = new ArrayList<RESTItem>(2);

    private HashMap<String, RESTItem> items = new HashMap<String, RESTItem>();

    public ItemDaoImpl() {
        this.clearItems();
        this.setup();
    }

    public void setup(){
        RESTItem RESTItem2 = new RESTItem("id123", "description", "label");
        this.items.put(RESTItem2.getExternalID(), RESTItem2);
        RESTItem2 = new RESTItem("id1234", "description_2", "another label");
        this.items.put(RESTItem2.getExternalID(), RESTItem2);
        log.debug("items: " + items.toString());
    }

    public void clearItems(){
        this.items.clear();
    }

    public Map<String, RESTItem> selectAll() {
        return this.items;
    }

    @Override
    public RESTItem getItemByExternalId(String externalID) {
        if(!this.items.containsKey(externalID)){
            return null;
        }
        return this.items.get(externalID);
    }

    @Override
    public boolean updateItemByExternalID(RESTItem RESTItem) {
        if(!this.items.containsKey(RESTItem.getExternalID())){
            return false;
        }
        this.items.put(RESTItem.getExternalID(), RESTItem);
        return true;
    }

    @Override
    public boolean deleteItemByExternalID(String externalID) {
        if(!this.items.containsKey(externalID)){
             return false;
        }
        this.items.remove(externalID);
        return true;
    }

    @Override
    public RESTItem insertItem(RESTItem RESTItem) throws ItemAlreadyExistsException{
        if(!this.items.containsKey(RESTItem.getExternalID())){
            this.items.put(RESTItem.getExternalID(), RESTItem);
            return this.getItemByExternalId(RESTItem.getExternalID());
        }
        throw new ItemAlreadyExistsException("RESTItem already exists.", RESTItem.getExternalID());
    }

    @Override
    public RESTItem insertItem(String externalID, Item item) throws ItemAlreadyExistsException{
        if(!this.items.containsKey(externalID)){
            this.items.put(externalID, new RESTItem(externalID,item));
            return this.getItemByExternalId(externalID);
        }
        throw new ItemAlreadyExistsException("RESTItem already exists.", externalID);
    }

}