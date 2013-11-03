package info.lotharschulz.item.model.data;

import info.lotharschulz.item.model.RESTItem;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.Map;

@RunWith(Suite.class)
@Suite.SuiteClasses({ItemDaoImplTest.Group1.class, ItemDaoImplTest.Group2.class, ItemDaoImplTest.Group3.class, ItemDaoImplTest.Group4.class}) // })
public class ItemDaoImplTest {
    private static final Logger log = Logger.getLogger(ItemDaoImplTest.class);
    private static ItemDaoImpl imageDAO;

    @BeforeClass
    public static void setUp() throws Exception {
        log.info("start unit tests");
        imageDAO = new ItemDaoImpl();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        log.info("test finished");
    }

    public static class Group1 {
        @Test
        public void selectAll() throws Exception {
            Map<String, RESTItem> itemMap = imageDAO.selectAll();
            log.debug("itemMap: " + itemMap);
            String expected = "{id1234=RESTItem{externalID=id1234,description=description_2,label=another label}, id123=RESTItem{externalID=id123,description=description,label=label}}";
            Assert.assertEquals(itemMap.toString().trim(), expected.trim());
        }

        @Test
        public void selectbyExtID() throws Exception {
            RESTItem RESTItem = imageDAO.getItemByExternalId("id1234");
            log.debug("RESTItem: " + RESTItem);
            String expected = "RESTItem{externalID=id1234,description=description_2,label=another label}";
            Assert.assertEquals(RESTItem.toString().trim(), expected.trim());
        }
    }

    public static class Group2 {
        @Test
        public void updateByExternalID() throws Exception {
            boolean result = imageDAO.updateItemByExternalID(new RESTItem("id123", "description_changed", "label_changed"));
            log.debug("result: " + result);
            RESTItem RESTItem = imageDAO.getItemByExternalId("id123");
            log.debug("RESTItem: " + RESTItem);
            String expected = "RESTItem{externalID=id123,description=description_changed,label=label_changed}";
            Assert.assertEquals(RESTItem.toString().trim(), expected.trim());
        }
    }

    public static class Group3 {
        @Test
        public void deleteByExternalID() throws Exception {
            boolean result = imageDAO.deleteItemByExternalID("id123");
            log.debug("result: " + result);
            RESTItem RESTItem = imageDAO.getItemByExternalId("id123");
            log.debug("RESTItem: " + RESTItem);
            Assert.assertNull(RESTItem);
        }
    }

    public static class Group4 {
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

            Map<String, RESTItem> itemMap = imageDAO.selectAll();
            log.debug("itemMap: " + itemMap);
            imageDAO.deleteItemByExternalID("345externalid");
            RESTItem = imageDAO.getItemByExternalId("345externalid");
            log.debug("RESTItem: " + RESTItem);
            Assert.assertNull(RESTItem);

            itemMap = imageDAO.selectAll();
            log.debug("itemList: " + itemMap);
            RESTItem = new RESTItem("id123", "description", "label");
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

}
