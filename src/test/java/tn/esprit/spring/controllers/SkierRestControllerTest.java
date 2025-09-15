package tn.esprit.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.services.ISkierServices;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SkierRestController.class)
class SkierRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISkierServices skierServices;

    @Autowired
    private ObjectMapper objectMapper;

    private Skier testSkier;
    private Subscription testSubscription;

    @BeforeEach
    void setUp() {
        // Create test subscription
        testSubscription = new Subscription();
        testSubscription.setNumSubscription(1L);
        testSubscription.setTypeSub(TypeSubscription.ANNUAL);
        testSubscription.setStartDate(LocalDate.now());
        testSubscription.setEndDate(LocalDate.now().plusYears(1));

        // Create test skier
        testSkier = new Skier();
        testSkier.setNumSkier(1L);
        testSkier.setFirstName("John");
        testSkier.setLastName("Doe");
        testSkier.setDateOfBirth(LocalDate.of(1990, 5, 15));
        testSkier.setCity("Chamonix");
        testSkier.setSubscription(testSubscription);
    }

    @Test
    void testAddSkier() throws Exception {
        // Given
        when(skierServices.addSkier(any(Skier.class))).thenReturn(testSkier);

        // When & Then
        mockMvc.perform(post("/api/skier/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSkier)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numSkier").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.city").value("Chamonix"));
    }

    @Test
    void testAddSkierAndAssignToCourse() throws Exception {
        // Given
        Long courseId = 1L;
        when(skierServices.addSkierAndAssignToCourse(any(Skier.class), eq(courseId))).thenReturn(testSkier);

        // When & Then
        mockMvc.perform(post("/api/skier/addAndAssign/{numCourse}", courseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSkier)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numSkier").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testAssignToSubscription() throws Exception {
        // Given
        Long skierId = 1L;
        Long subscriptionId = 2L;
        when(skierServices.assignSkierToSubscription(skierId, subscriptionId)).thenReturn(testSkier);

        // When & Then
        mockMvc.perform(put("/api/skier/assignToSub/{numSkier}/{numSub}", skierId, subscriptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSkier").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testAssignToPiste() throws Exception {
        // Given
        Long skierId = 1L;
        Long pisteId = 1L;
        when(skierServices.assignSkierToPiste(skierId, pisteId)).thenReturn(testSkier);

        // When & Then
        mockMvc.perform(put("/api/skier/assignToPiste/{numSkier}/{numPiste}", skierId, pisteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSkier").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testRetrieveSkiersBySubscriptionType() throws Exception {
        // Given
        TypeSubscription typeSubscription = TypeSubscription.ANNUAL;
        List<Skier> skiers = Arrays.asList(testSkier);
        when(skierServices.retrieveSkiersBySubscriptionType(typeSubscription)).thenReturn(skiers);

        // When & Then
        mockMvc.perform(get("/api/skier/getSkiersBySubscription")
                .param("typeSubscription", "ANNUAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].numSkier").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void testGetSkierById() throws Exception {
        // Given
        Long skierId = 1L;
        when(skierServices.retrieveSkier(skierId)).thenReturn(testSkier);

        // When & Then
        mockMvc.perform(get("/api/skier/get/{id-skier}", skierId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSkier").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testDeleteSkierById() throws Exception {
        // Given
        Long skierId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/skier/delete/{id-skier}", skierId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllSkiers() throws Exception {
        // Given
        List<Skier> skiers = Arrays.asList(testSkier);
        when(skierServices.retrieveAllSkiers()).thenReturn(skiers);

        // When & Then
        mockMvc.perform(get("/api/skier/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].numSkier").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void testAddSkierWithInvalidData() throws Exception {
        // Given
        Skier invalidSkier = new Skier();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/skier/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidSkier)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetSkierByIdNotFound() throws Exception {
        // Given
        Long skierId = 999L;
        when(skierServices.retrieveSkier(skierId)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/skier/get/{id-skier}", skierId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
