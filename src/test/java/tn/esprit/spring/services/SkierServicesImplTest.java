package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkierServicesImplTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SkierServicesImpl skierServices;

    private Skier testSkier;
    private Subscription testSubscription;
    private Piste testPiste;
    private Course testCourse;
    private Registration testRegistration;

    @BeforeEach
    void setUp() {
        // Create test subscription
        testSubscription = new Subscription();
        testSubscription.setNumSub(1L);
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

        // Create test piste
        testPiste = new Piste();
        testPiste.setNumPiste(1L);
        testPiste.setNamePiste("Blue Piste");
        testPiste.setColor(Color.BLUE);
        testPiste.setLength(1000);
        testPiste.setSlope(15);

        // Create test course
        testCourse = new Course();
        testCourse.setNumCourse(1L);
        testCourse.setLevel(1);
        testCourse.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        testCourse.setPrice(50.0f);
        testCourse.setTimeSlot(2);

        // Create test registration
        testRegistration = new Registration();
        testRegistration.setNumRegistration(1L);
        testRegistration.setNumWeek(1);
        testSkier.setRegistrations(Set.of(testRegistration));
    }

    @Test
    void testRetrieveAllSkiers() {
        // Given
        List<Skier> expectedSkiers = Arrays.asList(testSkier);
        when(skierRepository.findAll()).thenReturn(expectedSkiers);

        // When
        List<Skier> result = skierServices.retrieveAllSkiers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSkier.getFirstName(), result.get(0).getFirstName());
        verify(skierRepository).findAll();
    }

    @Test
    void testAddSkierWithAnnualSubscription() {
        // Given
        testSkier.getSubscription().setTypeSub(TypeSubscription.ANNUAL);
        testSkier.getSubscription().setStartDate(LocalDate.now());
        when(skierRepository.save(any(Skier.class))).thenReturn(testSkier);

        // When
        Skier result = skierServices.addSkier(testSkier);

        // Then
        assertNotNull(result);
        assertEquals(LocalDate.now().plusYears(1), result.getSubscription().getEndDate());
        verify(skierRepository).save(testSkier);
    }

    @Test
    void testAddSkierWithMonthlySubscription() {
        // Given
        testSkier.getSubscription().setTypeSub(TypeSubscription.MONTHLY);
        testSkier.getSubscription().setStartDate(LocalDate.now());
        when(skierRepository.save(any(Skier.class))).thenReturn(testSkier);

        // When
        Skier result = skierServices.addSkier(testSkier);

        // Then
        assertNotNull(result);
        assertEquals(LocalDate.now().plusMonths(1), result.getSubscription().getEndDate());
        verify(skierRepository).save(testSkier);
    }

    @Test
    void testAddSkierWithSemestrielSubscription() {
        // Given
        testSkier.getSubscription().setTypeSub(TypeSubscription.SEMESTRIEL);
        testSkier.getSubscription().setStartDate(LocalDate.now());
        when(skierRepository.save(any(Skier.class))).thenReturn(testSkier);

        // When
        Skier result = skierServices.addSkier(testSkier);

        // Then
        assertNotNull(result);
        assertEquals(LocalDate.now().plusMonths(6), result.getSubscription().getEndDate());
        verify(skierRepository).save(testSkier);
    }

    @Test
    void testAssignSkierToSubscription() {
        // Given
        Long skierId = 1L;
        Long subscriptionId = 2L;
        Subscription newSubscription = new Subscription();
        newSubscription.setNumSub(subscriptionId);
        newSubscription.setTypeSub(TypeSubscription.MONTHLY);

        when(skierRepository.findById(skierId)).thenReturn(Optional.of(testSkier));
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(newSubscription));
        when(skierRepository.save(any(Skier.class))).thenReturn(testSkier);

        // When
        Skier result = skierServices.assignSkierToSubscription(skierId, subscriptionId);

        // Then
        assertNotNull(result);
        assertEquals(newSubscription, result.getSubscription());
        verify(skierRepository).findById(skierId);
        verify(subscriptionRepository).findById(subscriptionId);
        verify(skierRepository).save(testSkier);
    }

    @Test
    void testAssignSkierToSubscriptionWhenSkierNotFound() {
        // Given
        Long skierId = 999L;
        Long subscriptionId = 1L;
        when(skierRepository.findById(skierId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            skierServices.assignSkierToSubscription(skierId, subscriptionId);
        });
    }

    @Test
    void testAddSkierAndAssignToCourse() {
        // Given
        Long courseId = 1L;
        when(skierRepository.save(any(Skier.class))).thenReturn(testSkier);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // When
        Skier result = skierServices.addSkierAndAssignToCourse(testSkier, courseId);

        // Then
        assertNotNull(result);
        verify(skierRepository).save(testSkier);
        verify(courseRepository).findById(courseId);
        verify(registrationRepository).save(testRegistration);
    }

    @Test
    void testRemoveSkier() {
        // Given
        Long skierId = 1L;

        // When
        skierServices.removeSkier(skierId);

        // Then
        verify(skierRepository).deleteById(skierId);
    }

    @Test
    void testRetrieveSkier() {
        // Given
        Long skierId = 1L;
        when(skierRepository.findById(skierId)).thenReturn(Optional.of(testSkier));

        // When
        Skier result = skierServices.retrieveSkier(skierId);

        // Then
        assertNotNull(result);
        assertEquals(testSkier.getFirstName(), result.getFirstName());
        verify(skierRepository).findById(skierId);
    }

    @Test
    void testRetrieveSkierNotFound() {
        // Given
        Long skierId = 999L;
        when(skierRepository.findById(skierId)).thenReturn(Optional.empty());

        // When
        Skier result = skierServices.retrieveSkier(skierId);

        // Then
        assertNull(result);
        verify(skierRepository).findById(skierId);
    }

    @Test
    void testAssignSkierToPiste() {
        // Given
        Long skierId = 1L;
        Long pisteId = 1L;
        when(skierRepository.findById(skierId)).thenReturn(Optional.of(testSkier));
        when(pisteRepository.findById(pisteId)).thenReturn(Optional.of(testPiste));
        when(skierRepository.save(any(Skier.class))).thenReturn(testSkier);

        // When
        Skier result = skierServices.assignSkierToPiste(skierId, pisteId);

        // Then
        assertNotNull(result);
        assertTrue(result.getPistes().contains(testPiste));
        verify(skierRepository).findById(skierId);
        verify(pisteRepository).findById(pisteId);
        verify(skierRepository).save(testSkier);
    }

    @Test
    void testAssignSkierToPisteWithNullPistes() {
        // Given
        Long skierId = 1L;
        Long pisteId = 1L;
        testSkier.setPistes(null); // Simulate null pistes
        when(skierRepository.findById(skierId)).thenReturn(Optional.of(testSkier));
        when(pisteRepository.findById(pisteId)).thenReturn(Optional.of(testPiste));
        when(skierRepository.save(any(Skier.class))).thenReturn(testSkier);

        // When
        Skier result = skierServices.assignSkierToPiste(skierId, pisteId);

        // Then
        assertNotNull(result);
        assertNotNull(result.getPistes());
        assertTrue(result.getPistes().contains(testPiste));
        verify(skierRepository).save(testSkier);
    }

    @Test
    void testRetrieveSkiersBySubscriptionType() {
        // Given
        TypeSubscription typeSubscription = TypeSubscription.ANNUAL;
        List<Skier> expectedSkiers = Arrays.asList(testSkier);
        when(skierRepository.findBySubscription_TypeSub(typeSubscription)).thenReturn(expectedSkiers);

        // When
        List<Skier> result = skierServices.retrieveSkiersBySubscriptionType(typeSubscription);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSkier.getFirstName(), result.get(0).getFirstName());
        verify(skierRepository).findBySubscription_TypeSub(typeSubscription);
    }
}
