package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServicesImplTest {

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    private Registration testRegistration;
    private Skier testSkier;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testRegistration = new Registration();
        testRegistration.setNumRegistration(1L);
        testRegistration.setNumWeek(1);

        testSkier = new Skier();
        testSkier.setNumSkier(1L);
        testSkier.setFirstName("John");
        testSkier.setLastName("Doe");
        testSkier.setDateOfBirth(LocalDate.of(1990, 1, 1));

        testCourse = new Course();
        testCourse.setNumCourse(1L);
        testCourse.setLevel(1);
        testCourse.setTypeCourse(TypeCourse.INDIVIDUAL);
    }

    @Test
    void testAddRegistrationAndAssignToSkier() {
        // Given
        when(skierRepository.findById(1L)).thenReturn(Optional.of(testSkier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // When
        Registration result = registrationServices.addRegistrationAndAssignToSkier(testRegistration, 1L);

        // Then
        assertNotNull(result);
        assertEquals(testRegistration.getNumRegistration(), result.getNumRegistration());
        verify(skierRepository, times(1)).findById(1L);
        verify(registrationRepository, times(1)).save(testRegistration);
    }

    @Test
    void testAddRegistrationAndAssignToSkierNotFound() {
        // Given
        when(skierRepository.findById(999L)).thenReturn(Optional.empty());
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // When
        Registration result = registrationServices.addRegistrationAndAssignToSkier(testRegistration, 999L);

        // Then
        assertNotNull(result);
        assertEquals(testRegistration.getNumRegistration(), result.getNumRegistration());
        verify(skierRepository, times(1)).findById(999L);
        verify(registrationRepository, times(1)).save(testRegistration);
    }

    @Test
    void testAssignRegistrationToCourse() {
        // Given
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(testRegistration));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // When
        Registration result = registrationServices.assignRegistrationToCourse(1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(testRegistration.getNumRegistration(), result.getNumRegistration());
        verify(registrationRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(registrationRepository, times(1)).save(testRegistration);
    }

    @Test
    void testAssignRegistrationToCourseNotFound() {
        // Given
        when(registrationRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            registrationServices.assignRegistrationToCourse(999L, 1L);
        });
        
        verify(registrationRepository, times(1)).findById(999L);
        verify(courseRepository, never()).findById(any());
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourseIndividual() {
        // Given
        when(skierRepository.findById(1L)).thenReturn(Optional.of(testSkier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong()))
                .thenReturn(0L);
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // When
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(testRegistration, 1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(testRegistration.getNumRegistration(), result.getNumRegistration());
        verify(skierRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(registrationRepository, times(1)).save(testRegistration);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourseCollectiveChildren() {
        // Given
        testSkier.setDateOfBirth(LocalDate.of(2010, 1, 1)); // Child
        testCourse.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);

        when(skierRepository.findById(1L)).thenReturn(Optional.of(testSkier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong()))
                .thenReturn(0L);
        when(registrationRepository.countByCourseAndNumWeek(any(Course.class), anyInt())).thenReturn(3L);
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // When
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(testRegistration, 1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(testRegistration.getNumRegistration(), result.getNumRegistration());
        verify(skierRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(registrationRepository, times(1)).save(testRegistration);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourseCollectiveAdults() {
        // Given
        testSkier.setDateOfBirth(LocalDate.of(1990, 1, 1)); // Adult
        testCourse.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);

        when(skierRepository.findById(1L)).thenReturn(Optional.of(testSkier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong()))
                .thenReturn(0L);
        when(registrationRepository.countByCourseAndNumWeek(any(Course.class), anyInt())).thenReturn(3L);
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // When
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(testRegistration, 1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(testRegistration.getNumRegistration(), result.getNumRegistration());
        verify(skierRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(registrationRepository, times(1)).save(testRegistration);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourseWithNullSkier() {
        // Given
        when(skierRepository.findById(999L)).thenReturn(Optional.empty());
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        // When
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(testRegistration, 999L, 1L);

        // Then
        assertNull(result);
        verify(skierRepository, times(1)).findById(999L);
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourseWithNullCourse() {
        // Given
        when(skierRepository.findById(1L)).thenReturn(Optional.of(testSkier));
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(testRegistration, 1L, 999L);

        // Then
        assertNull(result);
        verify(skierRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(999L);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourseAlreadyRegistered() {
        // Given
        when(skierRepository.findById(1L)).thenReturn(Optional.of(testSkier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong()))
                .thenReturn(1L); // Already registered

        // When
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(testRegistration, 1L, 1L);

        // Then
        assertNull(result);
        verify(skierRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourseFullCourse() {
        // Given
        testSkier.setDateOfBirth(LocalDate.of(2010, 1, 1)); // Child
        testCourse.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);

        when(skierRepository.findById(1L)).thenReturn(Optional.of(testSkier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong()))
                .thenReturn(0L);
        when(registrationRepository.countByCourseAndNumWeek(any(Course.class), anyInt())).thenReturn(6L); // Full course

        // When
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(testRegistration, 1L, 1L);

        // Then
        assertNull(result);
        verify(skierRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void testAddRegistrationAndAssignToSkierWithNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            registrationServices.addRegistrationAndAssignToSkier(null, 1L);
        });

        assertThrows(NullPointerException.class, () -> {
            registrationServices.addRegistrationAndAssignToSkier(testRegistration, null);
        });
    }

    @Test
    void testAssignRegistrationToCourseWithNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            registrationServices.assignRegistrationToCourse(null, 1L);
        });

        assertThrows(NullPointerException.class, () -> {
            registrationServices.assignRegistrationToCourse(1L, null);
        });
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourseWithNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            registrationServices.addRegistrationAndAssignToSkierAndCourse(null, 1L, 1L);
        });

        assertThrows(NullPointerException.class, () -> {
            registrationServices.addRegistrationAndAssignToSkierAndCourse(testRegistration, null, 1L);
        });

        assertThrows(NullPointerException.class, () -> {
            registrationServices.addRegistrationAndAssignToSkierAndCourse(testRegistration, 1L, null);
        });
    }

    @Test
    void testNumWeeksCourseOfInstructorBySupport() {
        // Given
        List<Integer> expectedWeeks = Arrays.asList(1, 2, 3);
        when(registrationRepository.numWeeksCourseOfInstructorBySupport(1L, Support.SKI))
                .thenReturn(expectedWeeks);

        // When
        List<Integer> result = registrationServices.numWeeksCourseOfInstructorBySupport(1L, Support.SKI);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Arrays.asList(1, 2, 3), result);
        verify(registrationRepository, times(1)).numWeeksCourseOfInstructorBySupport(1L, Support.SKI);
    }

    @Test
    void testNumWeeksCourseOfInstructorBySupportWithNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            registrationServices.numWeeksCourseOfInstructorBySupport(null, Support.SKI);
        });

        assertThrows(NullPointerException.class, () -> {
            registrationServices.numWeeksCourseOfInstructorBySupport(1L, null);
        });
    }
}
