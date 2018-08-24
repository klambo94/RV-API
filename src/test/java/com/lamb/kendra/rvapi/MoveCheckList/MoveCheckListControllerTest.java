package com.lamb.kendra.rvapi.MoveCheckList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MoveCheckListControllerTest {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private MoveCheckListRepository moveCheckListRepository;


    private Item antenna = new Item();
    private Item fridge = new Item();
    private Item shower = new Item();

    private String uri = "/moveLocations";

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }


    @Before
    public void setUp() throws Exception {
        moveCheckListRepository.deleteAll();
        shower.setDescription("Shower doors are locked");
        antenna.setDescription("Antenna is down");
        fridge.setDescription("Fridge doors are locked");
    }

    @Test
    public void moveLocationsIsValid() throws Exception {
        mockMvc.perform(get(uri))
                .andExpect(status().isOk());
    }

    @Test
    public void checkMoveLocationsIsEmpty() throws Exception {
       mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testAddItemToCheckList() throws Exception {
        mockMvc.perform(post(uri)
                        .content(this.json(antenna))
                        .contentType(contentType))
                .andExpect(status().isOk());


        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].name", is(antenna.getId())))
                .andExpect(jsonPath("$[0].description", is(antenna.getDescription())));
    }

    @Test
    public void testAddMultipleItemsToCheckList() throws Exception {
        List<Item> items = new ArrayList<>();
        items.add(fridge);
        items.add(antenna);
        items.add(shower);

        items.forEach(item -> {
            try {
                mockMvc.perform(post(uri)
                        .content(this.json(item))
                        .contentType(contentType))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is(fridge.getId())))
                .andExpect(jsonPath("$[0].description", is(fridge.getDescription())))
                .andExpect(jsonPath("$[1].name", is(antenna.getId())))
                .andExpect(jsonPath("$[1].description", is(antenna.getDescription())))
                .andExpect(jsonPath("$[2].name", is(shower.getId())))
                .andExpect(jsonPath("$[2].description", is(shower.getDescription())));

    }

    @Test
    public void testUpdateItem() throws Exception {
        mockMvc.perform(post(uri)
                        .content(this.json(antenna))
                        .contentType(contentType))
                .andExpect(status().isOk());

        antenna.setDescription(shower.getDescription());
        mockMvc.perform(put(uri)
                        .content(this.json(antenna))
                        .contentType(contentType))
                .andExpect(status().isOk());

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(antenna.getId())))
                .andExpect(jsonPath("$[0].description", is(shower.getDescription())));

    }

    @Test
    public void testDeleteItem() throws Exception {
        mockMvc.perform(post(uri)
                .content(this.json(antenna))
                .contentType(contentType))
                .andExpect(status().isOk());

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(antenna.getId())));

        mockMvc.perform(delete(uri)
                        .contentType(contentType)
                        .content(String.valueOf(antenna.getId())))
                .andExpect(status().isOk());

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(0)));

    }
    //TODO: create tests that handle bad responses

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
