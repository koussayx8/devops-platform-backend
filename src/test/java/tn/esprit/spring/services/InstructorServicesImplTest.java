package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
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

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    private Instructor testInstructor;

    @BeforeEach
    void setUp() {
        testInstructor = new Instructor();
        testInstructor.setNumInstructor(1L);
        testInstructor.setFirstName("John");
        testInstructor.setLastName("Doe");
        testInstructor.setDateOfHire(LocalDate.now());
    }

    @Test
    void testAddInstructor() {
        // Given
        when(instructorRepository.save(any(Instructor.class))).thenReturn(testInstructor);

        // When
        Instructor result = instructorServices.addInstructor(testInstructor);

        // Then
        assertNotNull(result);
        assertEquals(testInstructor.getNumInstructor(), result.getNumInstructor());
        assertEquals(testInstructor.getFirstName(), result.getFirstName());
        assertEquals(testInstructor.getLastName(), result.getLastName());
        verify(instructorRepository, times(1)).save(testInstructor);
    }

    @Test
    void testRetrieveAllInstructors() {
        // Given
        List<Instructor> expectedInstructors = Arrays.asList(testInstructor);
        when(instructorRepository.findAll()).thenReturn(expectedInstructors);

        // When
        List<Instructor> result = instructorServices.retrieveAllInstructors();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testInstructor.getNumInstructor(), result.get(0).getNumInstructor());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateInstructor() {
        // Given
        when(instructorRepository.existsById(1L)).thenReturn(true);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(testInstructor);

        // When
        Instructor result = instructorServices.updateInstructor(testInstructor);

        // Then
        assertNotNull(result);
        assertEquals(testInstructor.getNumInstructor(), result.getNumInstructor());
        verify(instructorRepository, times(1)).existsById(1L);
        verify(instructorRepository, times(1)).save(testInstructor);
    }

    @Test
    void testRetrieveInstructor() {
        // Given
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(testInstructor));

        // When
        Instructor result = instructorServices.retrieveInstructor(1L);

        // Then
        assertNotNull(result);
        assertEquals(testInstructor.getNumInstructor(), result.getNumInstructor());
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveInstructorNotFound() {
        // Given
        when(instructorRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Instructor result = instructorServices.retrieveInstructor(999L);

        // Then
        assertNull(result);
        verify(instructorRepository, times(1)).findById(999L);
    }

    @Test
    void testAddInstructorAndAssignToCourse() {
        // Given
        Course testCourse = new Course();
        testCourse.setNumCourse(1L);
        testCourse.setLevel(1);
        testCourse.setTypeCourse(tn.esprit.spring.entities.TypeCourse.INDIVIDUAL);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(testInstructor);

        // When
        Instructor result = instructorServices.addInstructorAndAssignToCourse(testInstructor, 1L);

        // Then
        assertNotNull(result);
        assertEquals(testInstructor.getNumInstructor(), result.getNumInstructor());
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, times(1)).save(testInstructor);
    }

    @Test
    void testAddInstructorAndAssignToCourseNotFound() {
        // Given
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());
        when(instructorRepository.save(any(Instructor.class))).thenReturn(testInstructor);

        // When
        Instructor result = instructorServices.addInstructorAndAssignToCourse(testInstructor, 999L);

        // Then
        assertNotNull(result);
        assertEquals(testInstructor.getNumInstructor(), result.getNumInstructor());
        verify(courseRepository, times(1)).findById(999L);
        verify(instructorRepository, times(1)).save(testInstructor);
    }

    @Test
    void testAddInstructorWithNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            instructorServices.addInstructor(null);
        });
    }

    @Test
    void testUpdateInstructorWithNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            instructorServices.updateInstructor(null);
        });
    }

    @Test
    void testUpdateInstructor_InstructorNotExists() {
        // Given
        Instructor nonExistentInstructor = new Instructor();
        nonExistentInstructor.setNumInstructor(999L);
        nonExistentInstructor.setFirstName("Ghost");

        when(instructorRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            instructorServices.updateInstructor(nonExistentInstructor);
        });

        verify(instructorRepository).existsById(999L);
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void testUpdateInstructor_WithNullId() {
        // Given
        Instructor instructorWithNullId = new Instructor();
        instructorWithNullId.setNumInstructor(null);
        instructorWithNullId.setFirstName("NoId");

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            instructorServices.updateInstructor(instructorWithNullId);
        });

        verify(instructorRepository, never()).existsById(any());
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void testRetrieveInstructorWithNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            instructorServices.retrieveInstructor(null);
        });
    }

    @Test
    void testAddInstructorAndAssignToCourseWithNullInstructor() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            instructorServices.addInstructorAndAssignToCourse(null, 1L);
        });
    }

    @Test
    void testAddInstructorAndAssignToCourseWithNullCourseId() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            instructorServices.addInstructorAndAssignToCourse(testInstructor, null);
        });
    }

    @Test
    void testRetrieveAllInstructorsEmpty() {
        // Given
        when(instructorRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Instructor> result = instructorServices.retrieveAllInstructors();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testMultipleInstructors() {
        // Given
        Instructor instructor1 = new Instructor();
        instructor1.setNumInstructor(1L);
        instructor1.setFirstName("John");
        instructor1.setLastName("Doe");

        Instructor instructor2 = new Instructor();
        instructor2.setNumInstructor(2L);
        instructor2.setFirstName("Jane");
        instructor2.setLastName("Smith");

        List<Instructor> instructors = Arrays.asList(instructor1, instructor2);
        when(instructorRepository.findAll()).thenReturn(instructors);

        // When
        List<Instructor> result = instructorServices.retrieveAllInstructors();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateInstructorWithDifferentData() {
        // Given
        Instructor originalInstructor = new Instructor();
        originalInstructor.setNumInstructor(1L);
        originalInstructor.setFirstName("John");
        originalInstructor.setLastName("Doe");

        Instructor updatedInstructor = new Instructor();
        updatedInstructor.setNumInstructor(1L);
        updatedInstructor.setFirstName("Johnny");
        updatedInstructor.setLastName("Doe");

        when(instructorRepository.existsById(1L)).thenReturn(true);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(updatedInstructor);

        // When
        Instructor result = instructorServices.updateInstructor(updatedInstructor);

        // Then
        assertNotNull(result);
        assertEquals("Johnny", result.getFirstName());
        verify(instructorRepository, times(1)).save(updatedInstructor);
    }

    @Test
    void testAddInstructorWithDateOfHire() {
        // Given
        LocalDate hireDate = LocalDate.of(2020, 1, 1);
        testInstructor.setDateOfHire(hireDate);

        when(instructorRepository.save(any(Instructor.class))).thenReturn(testInstructor);

        // When
        Instructor result = instructorServices.addInstructor(testInstructor);

        // Then
        assertNotNull(result);
        assertEquals(hireDate, result.getDateOfHire());
        verify(instructorRepository, times(1)).save(testInstructor);
    }
}
