package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISubscriptionRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServicesImplTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    private Subscription testSubscription;

    @BeforeEach
    void setUp() {
        testSubscription = new Subscription();
        testSubscription.setNumSubscription(1L);
        testSubscription.setTypeSub(TypeSubscription.ANNUAL);
        testSubscription.setStartDate(LocalDate.now());
        testSubscription.setEndDate(LocalDate.now().plusYears(1));
    }

    // CREATE Tests
    @Test
    void testAddSubscription_Success() {
        // Given
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(testSubscription);

        // When
        Subscription result = subscriptionServices.addSubscription(testSubscription);

        // Then
        assertNotNull(result);
        assertEquals(testSubscription.getNumSubscription(), result.getNumSubscription());
        assertEquals(testSubscription.getTypeSub(), result.getTypeSub());
        assertEquals(testSubscription.getStartDate(), result.getStartDate());
        assertEquals(testSubscription.getEndDate(), result.getEndDate());
        verify(subscriptionRepository).save(testSubscription);
    }

    @Test
    void testAddSubscription_WithNullSubscription() {
        // When & Then
        assertThrows(Exception.class, () -> {
            subscriptionServices.addSubscription(null);
        });
    }

    // READ Tests
    @Test
    void testRetrieveAllSubscriptions_Success() {
        // Given
        List<Subscription> expectedSubscriptions = Arrays.asList(testSubscription);
        when(subscriptionRepository.findAll()).thenReturn(expectedSubscriptions);

        // When
        List<Subscription> result = subscriptionServices.retrieveAllSubscriptions();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSubscription.getNumSubscription(), result.get(0).getNumSubscription());
        verify(subscriptionRepository).findAll();
    }

    @Test
    void testRetrieveAllSubscriptions_EmptyList() {
        // Given
        when(subscriptionRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Subscription> result = subscriptionServices.retrieveAllSubscriptions();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(subscriptionRepository).findAll();
    }

    @Test
    void testRetrieveSubscription_Success() {
        // Given
        Long subscriptionId = 1L;
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(testSubscription));

        // When
        Subscription result = subscriptionServices.retrieveSubscription(subscriptionId);

        // Then
        assertNotNull(result);
        assertEquals(testSubscription.getNumSubscription(), result.getNumSubscription());
        assertEquals(testSubscription.getTypeSub(), result.getTypeSub());
        verify(subscriptionRepository).findById(subscriptionId);
    }

    @Test
    void testRetrieveSubscription_NotFound() {
        // Given
        Long subscriptionId = 999L;
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.empty());

        // When
        Subscription result = subscriptionServices.retrieveSubscription(subscriptionId);

        // Then
        assertNull(result);
        verify(subscriptionRepository).findById(subscriptionId);
    }

    @Test
    void testRetrieveSubscription_WithNullId() {
        // When & Then
        assertThrows(Exception.class, () -> {
            subscriptionServices.retrieveSubscription(null);
        });
    }

    // UPDATE Tests
    @Test
    void testUpdateSubscription_Success() {
        // Given
        Subscription updatedSubscription = new Subscription();
        updatedSubscription.setNumSubscription(1L);
        updatedSubscription.setTypeSub(TypeSubscription.MONTHLY);
        updatedSubscription.setStartDate(LocalDate.now());
        updatedSubscription.setEndDate(LocalDate.now().plusMonths(1));

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(updatedSubscription);

        // When
        Subscription result = subscriptionServices.updateSubscription(updatedSubscription);

        // Then
        assertNotNull(result);
        assertEquals(updatedSubscription.getNumSubscription(), result.getNumSubscription());
        assertEquals(updatedSubscription.getTypeSub(), result.getTypeSub());
        assertEquals(updatedSubscription.getStartDate(), result.getStartDate());
        assertEquals(updatedSubscription.getEndDate(), result.getEndDate());
        verify(subscriptionRepository).save(updatedSubscription);
    }

    @Test
    void testUpdateSubscription_WithNullSubscription() {
        // When & Then
        assertThrows(Exception.class, () -> {
            subscriptionServices.updateSubscription(null);
        });
    }

    // DELETE Tests
    @Test
    void testRemoveSubscription_Success() {
        // Given
        Long subscriptionId = 1L;

        // When
        subscriptionServices.removeSubscription(subscriptionId);

        // Then
        verify(subscriptionRepository).deleteById(subscriptionId);
    }

    @Test
    void testRemoveSubscription_WithNullId() {
        // When & Then
        assertThrows(Exception.class, () -> {
            subscriptionServices.removeSubscription(null);
        });
    }

    // Edge Cases and Business Logic Tests
    @Test
    void testAddSubscription_WithDifferentTypes() {
        // Given
        Subscription annualSubscription = new Subscription();
        annualSubscription.setTypeSub(TypeSubscription.ANNUAL);
        annualSubscription.setStartDate(LocalDate.now());
        annualSubscription.setEndDate(LocalDate.now().plusYears(1));

        Subscription monthlySubscription = new Subscription();
        monthlySubscription.setTypeSub(TypeSubscription.MONTHLY);
        monthlySubscription.setStartDate(LocalDate.now());
        monthlySubscription.setEndDate(LocalDate.now().plusMonths(1));

        Subscription semestrielSubscription = new Subscription();
        semestrielSubscription.setTypeSub(TypeSubscription.SEMESTRIEL);
        semestrielSubscription.setStartDate(LocalDate.now());
        semestrielSubscription.setEndDate(LocalDate.now().plusMonths(6));

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(annualSubscription, monthlySubscription, semestrielSubscription);

        // When
        Subscription result1 = subscriptionServices.addSubscription(annualSubscription);
        Subscription result2 = subscriptionServices.addSubscription(monthlySubscription);
        Subscription result3 = subscriptionServices.addSubscription(semestrielSubscription);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertEquals(TypeSubscription.ANNUAL, result1.getTypeSub());
        assertEquals(TypeSubscription.MONTHLY, result2.getTypeSub());
        assertEquals(TypeSubscription.SEMESTRIEL, result3.getTypeSub());
        verify(subscriptionRepository, times(3)).save(any(Subscription.class));
    }

    @Test
    void testRetrieveAllSubscriptions_MultipleSubscriptions() {
        // Given
        Subscription subscription1 = new Subscription();
        subscription1.setNumSubscription(1L);
        subscription1.setTypeSub(TypeSubscription.ANNUAL);

        Subscription subscription2 = new Subscription();
        subscription2.setNumSubscription(2L);
        subscription2.setTypeSub(TypeSubscription.MONTHLY);

        List<Subscription> expectedSubscriptions = Arrays.asList(subscription1, subscription2);
        when(subscriptionRepository.findAll()).thenReturn(expectedSubscriptions);

        // When
        List<Subscription> result = subscriptionServices.retrieveAllSubscriptions();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(subscription1.getNumSubscription(), result.get(0).getNumSubscription());
        assertEquals(subscription2.getNumSubscription(), result.get(1).getNumSubscription());
        verify(subscriptionRepository).findAll();
    }

    @Test
    void testAddSubscription_WithDateValidation() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        
        testSubscription.setStartDate(startDate);
        testSubscription.setEndDate(endDate);
        
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(testSubscription);

        // When
        Subscription result = subscriptionServices.addSubscription(testSubscription);

        // Then
        assertNotNull(result);
        assertEquals(startDate, result.getStartDate());
        assertEquals(endDate, result.getEndDate());
        assertTrue(result.getEndDate().isAfter(result.getStartDate()));
        verify(subscriptionRepository).save(testSubscription);
    }

    @Test
    void testUpdateSubscription_ChangeType() {
        // Given
        Subscription originalSubscription = new Subscription();
        originalSubscription.setNumSubscription(1L);
        originalSubscription.setTypeSub(TypeSubscription.MONTHLY);

        Subscription updatedSubscription = new Subscription();
        updatedSubscription.setNumSubscription(1L);
        updatedSubscription.setTypeSub(TypeSubscription.ANNUAL);

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(updatedSubscription);

        // When
        Subscription result = subscriptionServices.updateSubscription(updatedSubscription);

        // Then
        assertNotNull(result);
        assertEquals(TypeSubscription.ANNUAL, result.getTypeSub());
        verify(subscriptionRepository).save(updatedSubscription);
    }
}
