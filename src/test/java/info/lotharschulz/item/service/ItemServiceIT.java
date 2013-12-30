package info.lotharschulz.item.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

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

    @Test(groups = "a")
    public void testItems() throws Exception {
        mockMvc.perform(get(basePath).accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[{\"description\":\"description_2\",\"label\":\"another label\",\"externalID\":\"id1234\"}," +
                        "{\"description\":\"description\",\"label\":\"label\",\"externalID\":\"id123\"}]"))
                .andExpect(jsonPath("$..description[0]").value("description_2"))
                .andExpect(jsonPath("$..label[0]").value("another label"))
                .andExpect(jsonPath("$..externalID[0]").value("id1234"))
                .andExpect(jsonPath("$..description[1]").value("description"))
                .andExpect(jsonPath("$..label[1]").value("label"))
                .andExpect(jsonPath("$..externalID[1]").value("id123"))
        ;
    }

    @Test
    public void testNotExistingItem() throws Exception {
        String itemID = "whatever";
        mockMvc.perform(get(basePath + "{itemID}", itemID).accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().
                        string("Resource '" + itemID + "' does not exist?"))
        ;
    }

    @Test
    public void testSeeOther() throws Exception {
        String itemID = "id123";
        MvcResult result =
                mockMvc.perform(put(basePath + "{itemID}", itemID).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"label\":\"label_neu\",\"description\":\"lallallallalla neue description\"}"))
                        //.andDo(print())
                        .andExpect(status().isSeeOther())
                        .andReturn()
        ;

        // double check implementation below once spring 3.2.4 release is out
        // urls
        //   https://jira.springsource.org/browse/SPR-10789?page=com.atlassian.jira.plugin.system.issuetabpanels:changehistory-tabpanel
        //   https://github.com/spring-projects/spring-test-mvc/issues/82
        //   http://stackoverflow.com/questions/17834034/spring-mockmvc-redirectedurl-with-pattern

        MockHttpServletResponse response = result.getResponse();
        String location = response.getHeader("Location");
        Pattern pattern = Pattern.compile(basePath + itemID);
        Assert.assertTrue(pattern.matcher(location).find());
    }

    @Test(groups = "b", dependsOnGroups = "a")
    public void testCreateNewResource() throws Exception {
        String itemID = "id12345678";
        String description = "lallallallalla neue description";
        String label = "label_neu";
        mockMvc.perform(put(basePath + "{itemID}", itemID).contentType(MediaType.APPLICATION_JSON)
                .content("{\"label\":\"" + label + "\",\"description\":\"" + description + "\"}"))
                //.andDo(print())
                .andExpect(status().isCreated())
        ;

        this.testGet(itemID, descriptionLabel, description, labelLabel, label, "externalID");
    }

    @Test(groups = "b", dependsOnGroups = "a")
    public void testUpdateExistingResource() throws Exception {
        String itemID = "id123";

        String newDescription = "new description XXX";
        String newLabel = "new_label";

        String existingDescription = "description";
        String existingLabel = "label";

        mockMvc.perform(post(basePath + "{itemID}", itemID).contentType(MediaType.APPLICATION_JSON)
                .content("{\"label\":\"" + newLabel + "\",\"description\":\"" + newDescription + "\"}"))
                //.andDo(print())
                .andExpect(status().isOk())
        ;

        this.testGet(itemID, descriptionLabel, newDescription, labelLabel, newLabel, "externalID");

        mockMvc.perform(post(basePath + "{itemID}", itemID).contentType(MediaType.APPLICATION_JSON)
                .content("{\"label\":\"" + existingLabel + "\",\"description\":\"" + existingDescription + "\"}"))
                //.andDo(print())
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
                //.andDo(print())
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
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"" + descriptionLabel + "\":\"" + description + "\",\"" +
                        labelLabel + "\":\"" + label + "\",\"" + externalIDLabel + "\":\"" + itemID + "\"}"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(description))
                .andExpect(MockMvcResultMatchers.jsonPath("$.label").value(label))
                .andExpect(MockMvcResultMatchers.jsonPath("$.externalID").value(itemID))
        ;

    }

    /*
    @Configuration
    public static class TestConfiguration {

        @Bean
        public ItemController itemController() {
            return new ItemController();
        }

    }
    */

}
