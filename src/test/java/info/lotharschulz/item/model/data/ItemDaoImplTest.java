package info.lotharschulz.item.model.data;

import info.lotharschulz.item.model.RESTItem;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class ItemDaoImplTest {
    private static final Logger log = Logger.getLogger(ItemDaoImplTest.class);
    private static ItemDaoImpl imageDAO;

    @BeforeClass
    public static void setUp() throws Exception {
        log.info("starting up unit tests");
        imageDAO = new ItemDaoImpl();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        log.info("closing down myBatis tests");
    }

    @Test(groups = "a")
    public void selectAll() throws Exception {
        Map<String,RESTItem> itemMap = imageDAO.selectAll();
        log.debug("itemMap: " + itemMap);
        String expected = "{id1234=RESTItem{externalID=id1234,description=description_2,label=another label}, id123=RESTItem{externalID=id123,description=description,label=label}}";
        Assert.assertEquals(itemMap.toString().trim(), expected.trim());
    }

    @Test(groups = "a")
    public void selectbyExtID() throws Exception {
        RESTItem RESTItem = imageDAO.getItemByExternalId("id1234");
        log.debug("RESTItem: " + RESTItem);
        String expected = "RESTItem{externalID=id1234,description=description_2,label=another label}";
        Assert.assertEquals(RESTItem.toString().trim(), expected.trim());
    }

    @Test(groups = "b", dependsOnGroups = "a")
    public void updateByExternalID() throws Exception {
        boolean result = imageDAO.updateItemByExternalID(new RESTItem("id123", "description_changed", "label_changed"));
        log.debug("result: " + result);
        RESTItem RESTItem = imageDAO.getItemByExternalId("id123");
        log.debug("RESTItem: " + RESTItem);
        String expected = "RESTItem{externalID=id123,description=description_changed,label=label_changed}";
        Assert.assertEquals(RESTItem.toString().trim(), expected.trim());
    }

    @Test(groups = "c", dependsOnGroups = "b")
    public void deleteByExternalID() throws Exception {
        boolean result = imageDAO.deleteItemByExternalID("id123");
        log.debug("result: " + result);
        RESTItem RESTItem = imageDAO.getItemByExternalId("id123");
        log.debug("RESTItem: " + RESTItem);
        Assert.assertNull(RESTItem);
    }

    @Test(groups = "d", dependsOnGroups = "c")
    public void insertItem() throws Exception {
        RESTItem RESTItem = new RESTItem("345externalid", "new description", "another new label");
        RESTItem inserted = imageDAO.insertItem(RESTItem);
        RESTItem getJustCreatedRESTItem = imageDAO.getItemByExternalId("345externalid");
        log.debug("item2: " + getJustCreatedRESTItem);
        String expected = "RESTItem{externalID=345externalid,description=new description,label=another new label}";
        Assert.assertEquals(getJustCreatedRESTItem.toString().trim(), expected.trim());
        Assert.assertEquals(inserted.toString().trim(), expected.trim());
        Assert.assertEquals(inserted.toString(), getJustCreatedRESTItem.toString());
        Assert.assertEquals(inserted, getJustCreatedRESTItem);

        Map<String,RESTItem> itemMap = imageDAO.selectAll();
        log.debug("itemMap: " + itemMap);
        imageDAO.deleteItemByExternalID("345externalid");
        RESTItem = imageDAO.getItemByExternalId("345externalid");
        log.debug("RESTItem: " + RESTItem);
        Assert.assertNull(RESTItem);

        itemMap = imageDAO.selectAll();
        log.debug("itemList: " + itemMap);
        RESTItem = new RESTItem("id123","description","label");
        inserted = imageDAO.insertItem(RESTItem);
        getJustCreatedRESTItem = imageDAO.getItemByExternalId("id123");
        log.debug("item2: " + getJustCreatedRESTItem + "\ninserted: " + inserted);
        expected = "RESTItem{externalID=id123,description=description,label=label}";
        Assert.assertEquals(getJustCreatedRESTItem.toString().trim(), expected.trim());
        Assert.assertEquals(inserted.toString().trim(), expected.trim());
        Assert.assertEquals(inserted.toString(), getJustCreatedRESTItem.toString());
        Assert.assertEquals(inserted, getJustCreatedRESTItem);

    }
}
