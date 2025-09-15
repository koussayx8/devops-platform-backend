package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PisteServicesImplTest {

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteServices;

    private Piste testPiste;

    @BeforeEach
    void setUp() {
        testPiste = new Piste();
        testPiste.setNumPiste(1L);
        testPiste.setNamePiste("Blue Piste");
        testPiste.setColor(Color.BLUE);
        testPiste.setLength(1000);
        testPiste.setSlope(15);
    }

    @Test
    void testRetrieveAllPistes() {
        // Given
        List<Piste> expectedPistes = Arrays.asList(testPiste);
        when(pisteRepository.findAll()).thenReturn(expectedPistes);

        // When
        List<Piste> result = pisteServices.retrieveAllPistes();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPiste.getNumPiste(), result.get(0).getNumPiste());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPiste() {
        // Given
        when(pisteRepository.save(any(Piste.class))).thenReturn(testPiste);

        // When
        Piste result = pisteServices.addPiste(testPiste);

        // Then
        assertNotNull(result);
        assertEquals(testPiste.getNumPiste(), result.getNumPiste());
        assertEquals(testPiste.getNamePiste(), result.getNamePiste());
        assertEquals(testPiste.getColor(), result.getColor());
        verify(pisteRepository, times(1)).save(testPiste);
    }

    @Test
    void testRemovePiste() {
        // Given
        Long pisteId = 1L;

        // When
        pisteServices.removePiste(pisteId);

        // Then
        verify(pisteRepository, times(1)).deleteById(pisteId);
    }

    @Test
    void testRetrievePiste() {
        // Given
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(testPiste));

        // When
        Piste result = pisteServices.retrievePiste(1L);

        // Then
        assertNotNull(result);
        assertEquals(testPiste.getNumPiste(), result.getNumPiste());
        assertEquals(testPiste.getNamePiste(), result.getNamePiste());
        verify(pisteRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrievePisteNotFound() {
        // Given
        when(pisteRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Piste result = pisteServices.retrievePiste(999L);

        // Then
        assertNull(result);
        verify(pisteRepository, times(1)).findById(999L);
    }

    @Test
    void testAddPisteWithNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            pisteServices.addPiste(null);
        });
    }

    @Test
    void testRemovePisteWithNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            pisteServices.removePiste(null);
        });
    }

    @Test
    void testRetrievePisteWithNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            pisteServices.retrievePiste(null);
        });
    }

    @Test
    void testRetrieveAllPistesEmpty() {
        // Given
        when(pisteRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Piste> result = pisteServices.retrieveAllPistes();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testMultiplePistes() {
        // Given
        Piste piste1 = new Piste();
        piste1.setNumPiste(1L);
        piste1.setNamePiste("Blue Piste");
        piste1.setColor(Color.BLUE);

        Piste piste2 = new Piste();
        piste2.setNumPiste(2L);
        piste2.setNamePiste("Red Piste");
        piste2.setColor(Color.RED);

        List<Piste> pistes = Arrays.asList(piste1, piste2);
        when(pisteRepository.findAll()).thenReturn(pistes);

        // When
        List<Piste> result = pisteServices.retrieveAllPistes();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Blue Piste", result.get(0).getNamePiste());
        assertEquals("Red Piste", result.get(1).getNamePiste());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPisteWithDifferentColors() {
        // Given
        Piste bluePiste = new Piste();
        bluePiste.setNamePiste("Blue Piste");
        bluePiste.setColor(Color.BLUE);

        Piste redPiste = new Piste();
        redPiste.setNamePiste("Red Piste");
        redPiste.setColor(Color.RED);

        Piste greenPiste = new Piste();
        greenPiste.setNamePiste("Green Piste");
        greenPiste.setColor(Color.GREEN);

        when(pisteRepository.save(any(Piste.class))).thenReturn(bluePiste, redPiste, greenPiste);

        // When
        Piste result1 = pisteServices.addPiste(bluePiste);
        Piste result2 = pisteServices.addPiste(redPiste);
        Piste result3 = pisteServices.addPiste(greenPiste);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertEquals(Color.BLUE, result1.getColor());
        assertEquals(Color.RED, result2.getColor());
        assertEquals(Color.GREEN, result3.getColor());
        verify(pisteRepository, times(3)).save(any(Piste.class));
    }

    @Test
    void testAddPisteWithLengthAndSlope() {
        // Given
        Piste piste = new Piste();
        piste.setNamePiste("Advanced Piste");
        piste.setColor(Color.RED);
        piste.setLength(2000);
        piste.setSlope(25);

        when(pisteRepository.save(any(Piste.class))).thenReturn(piste);

        // When
        Piste result = pisteServices.addPiste(piste);

        // Then
        assertNotNull(result);
        assertEquals(2000, result.getLength());
        assertEquals(25, result.getSlope());
        verify(pisteRepository, times(1)).save(piste);
    }

    @Test
    void testRetrievePisteWithSpecificId() {
        // Given
        Long specificId = 5L;
        Piste specificPiste = new Piste();
        specificPiste.setNumPiste(specificId);
        specificPiste.setNamePiste("Specific Piste");

        when(pisteRepository.findById(specificId)).thenReturn(Optional.of(specificPiste));

        // When
        Piste result = pisteServices.retrievePiste(specificId);

        // Then
        assertNotNull(result);
        assertEquals(specificId, result.getNumPiste());
        assertEquals("Specific Piste", result.getNamePiste());
        verify(pisteRepository, times(1)).findById(specificId);
    }
}
