package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorServicesImplTest {

    @Mock
    private IInstructorRepository instructorRepository;

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    private Instructor testInstructor;

    @BeforeEach
    void setUp() {
        testInstructor = new Instructor();
        testInstructor.setNumInstructor(1L);
        testInstructor.setFirstName("John");
        testInstructor.setLastName("Doe");
        testInstructor.setDateOfHire(LocalDate.of(2020, 1, 15));
        testInstructor.setSupport(Support.SKI);
    }

    // CREATE Tests
    @Test
    void testAddInstructor_Success() {
        // Given
        when(instructorRepository.save(any(Instructor.class))).thenReturn(testInstructor);

        // When
        Instructor result = instructorServices.addInstructor(testInstructor);

        // Then
        assertNotNull(result);
        assertEquals(testInstructor.getNumInstructor(), result.getNumInstructor());
        assertEquals(testInstructor.getFirstName(), result.getFirstName());
        assertEquals(testInstructor.getLastName(), result.getLastName());
        assertEquals(testInstructor.getSupport(), result.getSupport());
        verify(instructorRepository).save(testInstructor);
    }

    @Test
    void testAddInstructor_WithNullInstructor() {
        // When & Then
        assertThrows(Exception.class, () -> {
            instructorServices.addInstructor(null);
        });
    }

    // READ Tests
    @Test
    void testRetrieveAllInstructors_Success() {
        // Given
        List<Instructor> expectedInstructors = Arrays.asList(testInstructor);
        when(instructorRepository.findAll()).thenReturn(expectedInstructors);

        // When
        List<Instructor> result = instructorServices.retrieveAllInstructors();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testInstructor.getNumInstructor(), result.get(0).getNumInstructor());
        verify(instructorRepository).findAll();
    }

    @Test
    void testRetrieveAllInstructors_EmptyList() {
        // Given
        when(instructorRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Instructor> result = instructorServices.retrieveAllInstructors();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(instructorRepository).findAll();
    }

    @Test
    void testRetrieveInstructor_Success() {
        // Given
        Long instructorId = 1L;
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(testInstructor));

        // When
        Instructor result = instructorServices.retrieveInstructor(instructorId);

        // Then
        assertNotNull(result);
        assertEquals(testInstructor.getNumInstructor(), result.getNumInstructor());
        assertEquals(testInstructor.getFirstName(), result.getFirstName());
        verify(instructorRepository).findById(instructorId);
    }

    @Test
    void testRetrieveInstructor_NotFound() {
        // Given
        Long instructorId = 999L;
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        // When
        Instructor result = instructorServices.retrieveInstructor(instructorId);

        // Then
        assertNull(result);
        verify(instructorRepository).findById(instructorId);
    }

    @Test
    void testRetrieveInstructor_WithNullId() {
        // When & Then
        assertThrows(Exception.class, () -> {
            instructorServices.retrieveInstructor(null);
        });
    }

    // UPDATE Tests
    @Test
    void testUpdateInstructor_Success() {
        // Given
        Instructor updatedInstructor = new Instructor();
        updatedInstructor.setNumInstructor(1L);
        updatedInstructor.setFirstName("Jane");
        updatedInstructor.setLastName("Smith");
        updatedInstructor.setSupport(Support.SNOWBOARD);

        when(instructorRepository.save(any(Instructor.class))).thenReturn(updatedInstructor);

        // When
        Instructor result = instructorServices.updateInstructor(updatedInstructor);

        // Then
        assertNotNull(result);
        assertEquals(updatedInstructor.getNumInstructor(), result.getNumInstructor());
        assertEquals(updatedInstructor.getFirstName(), result.getFirstName());
        assertEquals(updatedInstructor.getLastName(), result.getLastName());
        assertEquals(updatedInstructor.getSupport(), result.getSupport());
        verify(instructorRepository).save(updatedInstructor);
    }

    @Test
    void testUpdateInstructor_WithNullInstructor() {
        // When & Then
        assertThrows(Exception.class, () -> {
            instructorServices.updateInstructor(null);
        });
    }

    // DELETE Tests
    @Test
    void testRemoveInstructor_Success() {
        // Given
        Long instructorId = 1L;

        // When
        instructorServices.removeInstructor(instructorId);

        // Then
        verify(instructorRepository).deleteById(instructorId);
    }

    @Test
    void testRemoveInstructor_WithNullId() {
        // When & Then
        assertThrows(Exception.class, () -> {
            instructorServices.removeInstructor(null);
        });
    }

    // Edge Cases and Business Logic Tests
    @Test
    void testAddInstructor_WithDifferentSupports() {
        // Given
        Instructor skiInstructor = new Instructor();
        skiInstructor.setSupport(Support.SKI);
        skiInstructor.setFirstName("Ski");

        Instructor snowboardInstructor = new Instructor();
        snowboardInstructor.setSupport(Support.SNOWBOARD);
        snowboardInstructor.setFirstName("Snowboard");

        when(instructorRepository.save(any(Instructor.class))).thenReturn(skiInstructor, snowboardInstructor);

        // When
        Instructor result1 = instructorServices.addInstructor(skiInstructor);
        Instructor result2 = instructorServices.addInstructor(snowboardInstructor);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(Support.SKI, result1.getSupport());
        assertEquals(Support.SNOWBOARD, result2.getSupport());
        verify(instructorRepository, times(2)).save(any(Instructor.class));
    }

    @Test
    void testRetrieveAllInstructors_MultipleInstructors() {
        // Given
        Instructor instructor1 = new Instructor();
        instructor1.setNumInstructor(1L);
        instructor1.setFirstName("John");

        Instructor instructor2 = new Instructor();
        instructor2.setNumInstructor(2L);
        instructor2.setFirstName("Jane");

        List<Instructor> expectedInstructors = Arrays.asList(instructor1, instructor2);
        when(instructorRepository.findAll()).thenReturn(expectedInstructors);

        // When
        List<Instructor> result = instructorServices.retrieveAllInstructors();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(instructor1.getNumInstructor(), result.get(0).getNumInstructor());
        assertEquals(instructor2.getNumInstructor(), result.get(1).getNumInstructor());
        verify(instructorRepository).findAll();
    }

    @Test
    void testAddInstructor_WithDateOfHire() {
        // Given
        LocalDate hireDate = LocalDate.of(2023, 6, 1);
        testInstructor.setDateOfHire(hireDate);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(testInstructor);

        // When
        Instructor result = instructorServices.addInstructor(testInstructor);

        // Then
        assertNotNull(result);
        assertEquals(hireDate, result.getDateOfHire());
        verify(instructorRepository).save(testInstructor);
    }
}
