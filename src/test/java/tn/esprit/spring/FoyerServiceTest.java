package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.Services.Foyer.FoyerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FoyerServiceTest {

    @Mock
    private FoyerRepository foyerRepository;

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private FoyerService foyerService;

    private Foyer foyer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        foyer = new Foyer(1L, "Main Foyer", 100, null, Arrays.asList(new Bloc(), new Bloc()));
    }

    @Test
    void testAddOrUpdateFoyer() {
        when(foyerRepository.save(foyer)).thenReturn(foyer);
        Foyer savedFoyer = foyerService.addOrUpdate(foyer);
        assertEquals(foyer, savedFoyer);
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    void testFindAllFoyers() {
        when(foyerRepository.findAll()).thenReturn(Arrays.asList(foyer));
        List<Foyer> foyers = foyerService.findAll();
        assertEquals(1, foyers.size());
        assertEquals(foyer, foyers.get(0));
        verify(foyerRepository, times(1)).findAll();
    }

    @Test
    void testFindFoyerById() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));
        Foyer foundFoyer = foyerService.findById(1L);
        assertEquals(foyer, foundFoyer);
        verify(foyerRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteFoyerById() {
        doNothing().when(foyerRepository).deleteById(1L);
        foyerService.deleteById(1L);
        verify(foyerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteFoyer() {
        doNothing().when(foyerRepository).delete(foyer);
        foyerService.delete(foyer);
        verify(foyerRepository, times(1)).delete(foyer);
    }

    @Test
    void testAffecterFoyerAUniversite() {
        Universite universite = new Universite(); // Create or mock Universite as needed
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));
        when(universiteRepository.findByNomUniversite("My University")).thenReturn(universite);
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite updatedUniversite = foyerService.affecterFoyerAUniversite(1L, "My University");
        assertEquals(universite, updatedUniversite);
        verify(universiteRepository, times(1)).findByNomUniversite("My University");
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    void testDesaffecterFoyerAUniversite() {
        Universite universite = new Universite();
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite updatedUniversite = foyerService.desaffecterFoyerAUniversite(1L);
        assertEquals(universite, updatedUniversite);
        verify(universiteRepository, times(1)).findById(1L);
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    void testAjouterFoyerEtAffecterAUniversite() {
        Universite universite = new Universite(); // Create or mock Universite as needed
        when(foyerRepository.save(any(Foyer.class))).thenReturn(foyer);
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Foyer addedFoyer = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, 1L);
        assertEquals(foyer, addedFoyer);
        verify(foyerRepository, times(1)).save(any(Foyer.class));
        verify(universiteRepository, times(1)).findById(1L);
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    void testAjoutFoyerEtBlocs() {
        when(foyerRepository.save(any(Foyer.class))).thenReturn(foyer);
        Foyer addedFoyer = foyerService.ajoutFoyerEtBlocs(foyer);
        assertEquals(foyer, addedFoyer);
        verify(foyerRepository, times(1)).save(any(Foyer.class));
    }
}
