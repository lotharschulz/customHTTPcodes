package info.lotharschulz.item.model.data;

import info.lotharschulz.item.model.RESTItem;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;
import java.util.TreeMap;

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

    @BeforeMethod
    public void before() {
        imageDAO.setup();
    }

    @AfterMethod
    public void after() {
        imageDAO.clearItems();
    }

    @Test
    public void selectAll() throws Exception {
        Map<String,RESTItem> itemMap = imageDAO.selectAll();
        log.debug("itemMap: " + itemMap);
        TreeMap itemTreeMap = new TreeMap();
        itemTreeMap.putAll(itemMap);
        log.debug("itemTreeMap: " + itemTreeMap);
        String expected = "{id123=RESTItem{externalID=id123,description=description,label=label}, id1234=RESTItem{externalID=id1234,description=description_2,label=another label}}";
        Assert.assertEquals(itemTreeMap.toString().trim(), expected.trim());
    }

    @Test
    public void selectbyExtID() throws Exception {
        RESTItem RESTItem = imageDAO.getItemByExternalId("id1234");
        log.debug("RESTItem: " + RESTItem);
        String expected = "RESTItem{externalID=id1234,description=description_2,label=another label}";
        Assert.assertEquals(RESTItem.toString().trim(), expected.trim());
    }

    @Test
    public void updateByExternalID() throws Exception {
        boolean result = imageDAO.updateItemByExternalID(new RESTItem("id123", "description_changed", "label_changed"));
        log.debug("result: " + result);
        RESTItem RESTItem = imageDAO.getItemByExternalId("id123");
        log.debug("RESTItem: " + RESTItem);
        String expected = "RESTItem{externalID=id123,description=description_changed,label=label_changed}";
        Assert.assertEquals(RESTItem.toString().trim(), expected.trim());
    }

    @Test
    public void deleteByExternalID() throws Exception {
        boolean result = imageDAO.deleteItemByExternalID("id123");
        log.debug("result: " + result);
        RESTItem RESTItem = imageDAO.getItemByExternalId("id123");
        log.debug("RESTItem: " + RESTItem);
        Assert.assertNull(RESTItem);
    }

    @Test
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
    }
}