package info.lotharschulz.item.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Test
@ContextConfiguration(locations = {"classpath:**/*exceptionDispatcher-servlet.xml"})
@WebAppConfiguration
public class ItemServiceIT extends AbstractTestNGSpringContextTests {

    @Autowired private WebApplicationContext ctx;

    private MockMvc mockMvc;
    private static final Logger log = Logger.getLogger(ItemServiceIT.class);
    private final String urlhttpPrefix = "http://localhost";
    private final String basePath = "/rest/v1/items/";
    private final String descriptionLabel = "description";
    private final String labelLabel = "label";

    @BeforeClass(dependsOnMethods={"springTestContextPrepareTestInstance"})
    public void setUp() {
        log.info("starting up IT tests");
        this.mockMvc = webAppContextSetup(ctx).alwaysDo(print()).build();
    }

    @Test
    public void testServletContext() throws Exception {
        Assert.assertNotNull(mockMvc);
    }

    @Test
    public void testItem123() throws Exception {
        String itemID = "id123";
        this.testGet(itemID, descriptionLabel,  "description", labelLabel, "label", "externalID");
    }

    @Test
    public void testItem1234() throws Exception {
        String itemID = "id1234";
        this.testGet(itemID, descriptionLabel, "description_2", labelLabel, "another label", "externalID");
    }

    @Test
    public void testItems() throws Exception {
        mockMvc.perform(get(basePath).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$..description[0]").value("description"))
                .andExpect(jsonPath("$..label[0]").value("label"))
                .andExpect(jsonPath("$..externalID[0]").value("id123"))
                .andExpect(jsonPath("$..description[1]").value("description_2"))
                .andExpect(jsonPath("$..label[1]").value("another label"))
                .andExpect(jsonPath("$..externalID[1]").value("id1234"))
        ;
    }

    @Test
    public void testNotExistingItem() throws Exception {
        String itemID = "whatever";
        mockMvc.perform(get(basePath + "{itemID}", itemID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().
                        string("Resource '" + itemID + "' does not exist?"))
        ;
    }

    @Test
    public void testSeeOther() throws Exception {
        String itemID = "id123";
                mockMvc.perform(put(basePath + "{itemID}", itemID).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"label\":\"label_neu\",\"description\":\"lallallallalla neue description\"}"))
                        .andExpect(status().isSeeOther())
                        .andExpect(redirectedUrl(urlhttpPrefix + basePath + itemID))
                        .andReturn()
                ;
    }

    @Test
    public void testCreateNewResource() throws Exception {
        String itemID = "id1234a";
        String description = "lallallallalla neue description";
        String label = "label_neu";
        mockMvc.perform(put(basePath + "{itemID}", itemID).contentType(MediaType.APPLICATION_JSON)
                .content("{\"label\":\"" + label + "\",\"description\":\"" + description + "\"}"))
                .andExpect(status().isCreated())
        ;
        this.testGet(itemID, descriptionLabel, description, labelLabel, label, "externalID");
    }

    @Test
    public void testUpdateExistingResource() throws Exception {
        String itemID = "id123";

        String newDescription = "new description XXX";
        String newLabel = "new_label";

        String existingDescription = "description";
        String existingLabel = "label";

        mockMvc.perform(post(basePath + "{itemID}", itemID).contentType(MediaType.APPLICATION_JSON)
                .content("{\"label\":\"" + newLabel + "\",\"description\":\"" + newDescription + "\"}"))
                .andExpect(status().isOk())
        ;

        this.testGet(itemID, descriptionLabel, newDescription, labelLabel, newLabel, "externalID");

        mockMvc.perform(post(basePath + "{itemID}", itemID).contentType(MediaType.APPLICATION_JSON)
                .content("{\"label\":\"" + existingLabel + "\",\"description\":\"" + existingDescription + "\"}"))
                .andExpect(status().isOk())
        ;

        this.testGet(itemID, descriptionLabel, existingDescription, labelLabel, existingLabel, "externalID");
    }


    @Test
    public void testBadRequestException() throws Exception {
        String itemID = "id12";

        String newDescription = "a";
        String newLabel = "b";

        mockMvc.perform(post(basePath + "{itemID}", itemID).contentType(MediaType.APPLICATION_JSON)
                .content("{\"label\":\"" + newLabel + "\",\"description\":\"" + newDescription + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().
                        string("Please request to an existing resource.\n" +
                                "Resource with id '" + itemID + "' does not exist."))
        ;
    }

    private void testGet(String itemID, String descriptionLabel, String description, String labelLabel,
                         String label, String externalIDLabel) throws Exception {
        mockMvc.perform(get(basePath + "{itemID}", itemID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"" + descriptionLabel + "\":\"" + description + "\",\"" +
                        labelLabel + "\":\"" + label + "\",\"" + externalIDLabel + "\":\"" + itemID + "\"}"))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.label").value(label))
                .andExpect(jsonPath("$.externalID").value(itemID))
        ;

    }

}
