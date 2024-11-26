package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.RestControllers.FoyerRestController;
import tn.esprit.spring.Services.Foyer.IFoyerService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FoyerRestController.class)
class FoyerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFoyerService foyerService;

    @Test
    void testAddOrUpdateFoyer() throws Exception {
        Foyer foyer = new Foyer(1L, "Main Foyer", 100, null, Collections.emptyList());
        when(foyerService.addOrUpdate(any(Foyer.class))).thenReturn(foyer);

        mockMvc.perform(post("/foyer/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomFoyer\":\"Main Foyer\",\"capaciteFoyer\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFoyer").value(foyer.getIdFoyer()))
                .andExpect(jsonPath("$.nomFoyer").value(foyer.getNomFoyer()))
                .andExpect(jsonPath("$.capaciteFoyer").value(foyer.getCapaciteFoyer()));
    }

    @Test
    void testFindAllFoyers() throws Exception {
        Foyer foyer = new Foyer(1L, "Main Foyer", 100, null, Collections.emptyList());
        when(foyerService.findAll()).thenReturn(Collections.singletonList(foyer));

        mockMvc.perform(get("/foyer/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idFoyer").value(foyer.getIdFoyer()))
                .andExpect(jsonPath("$[0].nomFoyer").value(foyer.getNomFoyer()))
                .andExpect(jsonPath("$[0].capaciteFoyer").value(foyer.getCapaciteFoyer()));
    }

    @Test
    void testFindFoyerById() throws Exception {
        Foyer foyer = new Foyer(1L, "Main Foyer", 100, null, Collections.emptyList());
        when(foyerService.findById(1L)).thenReturn(foyer);

        mockMvc.perform(get("/foyer/findById?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFoyer").value(foyer.getIdFoyer()))
                .andExpect(jsonPath("$.nomFoyer").value(foyer.getNomFoyer()))
                .andExpect(jsonPath("$.capaciteFoyer").value(foyer.getCapaciteFoyer()));
    }

    @Test
    void testDeleteFoyer() throws Exception {
        doNothing().when(foyerService).delete(any(Foyer.class));

        mockMvc.perform(delete("/foyer/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idFoyer\":1}"))
                .andExpect(status().isOk());

        // Verify the deletion method was called
    }

    @Test
    void testDeleteFoyerById() throws Exception {
        doNothing().when(foyerService).deleteById(1L);

        mockMvc.perform(delete("/foyer/deleteById?id=1"))
                .andExpect(status().isOk());

        // Verify the deleteById method was called
    }

    @Test
    void testAffecterFoyerAUniversite() throws Exception {
        Universite universite = new Universite(); // Mock or create your Universite as needed
        when(foyerService.affecterFoyerAUniversite(1L, "My University")).thenReturn(universite);

        mockMvc.perform(put("/foyer/affecterFoyerAUniversite?idFoyer=1&nomUniversite=My University"))
                .andExpect(status().isOk());

        // Further assertions can be made based on the expected Universite structure
    }

    @Test
    void testDesaffecterFoyerAUniversite() throws Exception {
        Universite universite = new Universite(); // Mock or create your Universite as needed
        when(foyerService.desaffecterFoyerAUniversite(1L)).thenReturn(universite);

        mockMvc.perform(put("/foyer/desaffecterFoyerAUniversite?idUniversite=1"))
                .andExpect(status().isOk());

        // Further assertions can be made based on the expected Universite structure
    }

    @Test
    void testAjouterFoyerEtAffecterAUniversite() throws Exception {
        Foyer foyer = new Foyer(1L, "Main Foyer", 100, null, Collections.emptyList());
        when(foyerService.ajouterFoyerEtAffecterAUniversite(any(Foyer.class), any(Long.class))).thenReturn(foyer);

        mockMvc.perform(post("/foyer/ajouterFoyerEtAffecterAUniversite?idUniversite=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomFoyer\":\"Main Foyer\",\"capaciteFoyer\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFoyer").value(foyer.getIdFoyer()))
                .andExpect(jsonPath("$.nomFoyer").value(foyer.getNomFoyer()))
                .andExpect(jsonPath("$.capaciteFoyer").value(foyer.getCapaciteFoyer()));
    }
}
