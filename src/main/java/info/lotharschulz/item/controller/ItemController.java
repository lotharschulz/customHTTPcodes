package info.lotharschulz.item.controller;


import info.lotharschulz.item.controller.exception.BadRequestException;
import info.lotharschulz.item.controller.exception.ResourceNotFoundException;
import info.lotharschulz.item.model.Item;
import info.lotharschulz.item.model.RESTItem;
import info.lotharschulz.item.model.data.exception.ItemAlreadyExistsException;
import info.lotharschulz.item.service.ItemService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/rest/v1/items")
public class ItemController{

    @Autowired
    private ItemService itemService;
    private static final Logger log = Logger.getLogger(ItemController.class);

    @RequestMapping(value = "/{itemID}", method = RequestMethod.GET, produces="application/json")
    public
    @ResponseBody
    RESTItem getItem(@PathVariable String itemID) {
        RESTItem RESTItem = itemService.getItemByExternalId(itemID);
        log.debug(RESTItem);
        if (null == RESTItem) {
            throw new ResourceNotFoundException(itemID);
        }
        return RESTItem;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces="application/json")
    public
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    List<RESTItem> getAllItems() {
        List<RESTItem> RESTItemList = itemService.getAllItems();
        return RESTItemList;
    }

    @RequestMapping(value = "/{itemID}", method = RequestMethod.PUT, produces="application/json")
    public
    @ResponseBody
    ResponseEntity addItem(@PathVariable final String itemID, @RequestBody final Item item, HttpServletRequest request  ) {
        log.debug("itemID: " + itemID + " \nitem: " + item + " \n");
        RESTItem inserted = null;
        try{
            inserted = itemService.insertItem(itemID, item);
        }catch(ItemAlreadyExistsException iaee){
            log.debug("e: " + iaee.toString() + " \n");
            HttpHeaders responseHeaders = new HttpHeaders();
            ResponseEntity re;
            try{
                responseHeaders.setLocation(new URI(request.getRequestURL().toString()));
                re = new ResponseEntity(new HashMap<String, String>().put("Location", "/" + iaee.getExistingItemID()), HttpStatus.SEE_OTHER);
            }catch (URISyntaxException urise){
                log.error("URISyntaxException: " + urise + " \n");
                re = new ResponseEntity(HttpStatus.SEE_OTHER);
            }
            log.debug("re: " + re + " \n");
            return re;
        }
        log.debug("inserted RESTItem: " + inserted + " \n");
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{itemID}", method = RequestMethod.POST, produces="application/json")
    public
    @ResponseBody
    RESTItem udpateItem(@PathVariable final String itemID, @RequestBody final Item item) {
        log.debug("itemID: " + itemID + " \nitem: " + item + " \n");
        RESTItem updated = itemService.updateItemByExternalID(itemID, item.getDescription(), item.getLabel());
        log.debug("RESTItem updated: " + updated + " \n");
        if (null == updated) {
            throw new BadRequestException(itemID);
        }
        return updated;
    }

}
