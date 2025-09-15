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

    // CREATE Tests
    @Test
    void testAddPiste_Success() {
        // Given
        when(pisteRepository.save(any(Piste.class))).thenReturn(testPiste);

        // When
        Piste result = pisteServices.addPiste(testPiste);

        // Then
        assertNotNull(result);
        assertEquals(testPiste.getNumPiste(), result.getNumPiste());
        assertEquals(testPiste.getNamePiste(), result.getNamePiste());
        assertEquals(testPiste.getColor(), result.getColor());
        assertEquals(testPiste.getLength(), result.getLength());
        assertEquals(testPiste.getSlope(), result.getSlope());
        verify(pisteRepository).save(testPiste);
    }

    @Test
    void testAddPiste_WithNullPiste() {
        // When & Then
        assertThrows(Exception.class, () -> {
            pisteServices.addPiste(null);
        });
    }

    // READ Tests
    @Test
    void testRetrieveAllPistes_Success() {
        // Given
        List<Piste> expectedPistes = Arrays.asList(testPiste);
        when(pisteRepository.findAll()).thenReturn(expectedPistes);

        // When
        List<Piste> result = pisteServices.retrieveAllPistes();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPiste.getNumPiste(), result.get(0).getNumPiste());
        verify(pisteRepository).findAll();
    }

    @Test
    void testRetrieveAllPistes_EmptyList() {
        // Given
        when(pisteRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Piste> result = pisteServices.retrieveAllPistes();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(pisteRepository).findAll();
    }

    @Test
    void testRetrievePiste_Success() {
        // Given
        Long pisteId = 1L;
        when(pisteRepository.findById(pisteId)).thenReturn(Optional.of(testPiste));

        // When
        Piste result = pisteServices.retrievePiste(pisteId);

        // Then
        assertNotNull(result);
        assertEquals(testPiste.getNumPiste(), result.getNumPiste());
        assertEquals(testPiste.getNamePiste(), result.getNamePiste());
        verify(pisteRepository).findById(pisteId);
    }

    @Test
    void testRetrievePiste_NotFound() {
        // Given
        Long pisteId = 999L;
        when(pisteRepository.findById(pisteId)).thenReturn(Optional.empty());

        // When
        Piste result = pisteServices.retrievePiste(pisteId);

        // Then
        assertNull(result);
        verify(pisteRepository).findById(pisteId);
    }

    @Test
    void testRetrievePiste_WithNullId() {
        // When & Then
        assertThrows(Exception.class, () -> {
            pisteServices.retrievePiste(null);
        });
    }

    // UPDATE Tests
    @Test
    void testUpdatePiste_Success() {
        // Given
        Piste updatedPiste = new Piste();
        updatedPiste.setNumPiste(1L);
        updatedPiste.setNamePiste("Red Piste");
        updatedPiste.setColor(Color.RED);
        updatedPiste.setLength(1500);
        updatedPiste.setSlope(25);

        when(pisteRepository.save(any(Piste.class))).thenReturn(updatedPiste);

        // When
        Piste result = pisteServices.updatePiste(updatedPiste);

        // Then
        assertNotNull(result);
        assertEquals(updatedPiste.getNumPiste(), result.getNumPiste());
        assertEquals(updatedPiste.getNamePiste(), result.getNamePiste());
        assertEquals(updatedPiste.getColor(), result.getColor());
        assertEquals(updatedPiste.getLength(), result.getLength());
        assertEquals(updatedPiste.getSlope(), result.getSlope());
        verify(pisteRepository).save(updatedPiste);
    }

    @Test
    void testUpdatePiste_WithNullPiste() {
        // When & Then
        assertThrows(Exception.class, () -> {
            pisteServices.updatePiste(null);
        });
    }

    // DELETE Tests
    @Test
    void testRemovePiste_Success() {
        // Given
        Long pisteId = 1L;

        // When
        pisteServices.removePiste(pisteId);

        // Then
        verify(pisteRepository).deleteById(pisteId);
    }

    @Test
    void testRemovePiste_WithNullId() {
        // When & Then
        assertThrows(Exception.class, () -> {
            pisteServices.removePiste(null);
        });
    }

    // Edge Cases and Business Logic Tests
    @Test
    void testAddPiste_WithDifferentColors() {
        // Given
        Piste bluePiste = new Piste();
        bluePiste.setColor(Color.BLUE);
        bluePiste.setNamePiste("Blue Piste");

        Piste redPiste = new Piste();
        redPiste.setColor(Color.RED);
        redPiste.setNamePiste("Red Piste");

        Piste greenPiste = new Piste();
        greenPiste.setColor(Color.GREEN);
        greenPiste.setNamePiste("Green Piste");

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
    void testRetrieveAllPistes_MultiplePistes() {
        // Given
        Piste piste1 = new Piste();
        piste1.setNumPiste(1L);
        piste1.setNamePiste("Piste 1");

        Piste piste2 = new Piste();
        piste2.setNumPiste(2L);
        piste2.setNamePiste("Piste 2");

        List<Piste> expectedPistes = Arrays.asList(piste1, piste2);
        when(pisteRepository.findAll()).thenReturn(expectedPistes);

        // When
        List<Piste> result = pisteServices.retrieveAllPistes();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(piste1.getNumPiste(), result.get(0).getNumPiste());
        assertEquals(piste2.getNumPiste(), result.get(1).getNumPiste());
        verify(pisteRepository).findAll();
    }

    @Test
    void testAddPiste_WithDifferentSlopes() {
        // Given
        Piste beginnerPiste = new Piste();
        beginnerPiste.setSlope(10);
        beginnerPiste.setColor(Color.GREEN);

        Piste intermediatePiste = new Piste();
        intermediatePiste.setSlope(20);
        intermediatePiste.setColor(Color.BLUE);

        Piste advancedPiste = new Piste();
        advancedPiste.setSlope(35);
        advancedPiste.setColor(Color.RED);

        when(pisteRepository.save(any(Piste.class))).thenReturn(beginnerPiste, intermediatePiste, advancedPiste);

        // When
        Piste result1 = pisteServices.addPiste(beginnerPiste);
        Piste result2 = pisteServices.addPiste(intermediatePiste);
        Piste result3 = pisteServices.addPiste(advancedPiste);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertEquals(10, result1.getSlope());
        assertEquals(20, result2.getSlope());
        assertEquals(35, result3.getSlope());
        verify(pisteRepository, times(3)).save(any(Piste.class));
    }

    @Test
    void testAddPiste_WithDifferentLengths() {
        // Given
        Piste shortPiste = new Piste();
        shortPiste.setLength(500);
        shortPiste.setNamePiste("Short Piste");

        Piste mediumPiste = new Piste();
        mediumPiste.setLength(1000);
        mediumPiste.setNamePiste("Medium Piste");

        Piste longPiste = new Piste();
        longPiste.setLength(2000);
        longPiste.setNamePiste("Long Piste");

        when(pisteRepository.save(any(Piste.class))).thenReturn(shortPiste, mediumPiste, longPiste);

        // When
        Piste result1 = pisteServices.addPiste(shortPiste);
        Piste result2 = pisteServices.addPiste(mediumPiste);
        Piste result3 = pisteServices.addPiste(longPiste);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertEquals(500, result1.getLength());
        assertEquals(1000, result2.getLength());
        assertEquals(2000, result3.getLength());
        verify(pisteRepository, times(3)).save(any(Piste.class));
    }
}
