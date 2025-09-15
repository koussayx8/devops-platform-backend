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
import java.util.Set;

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
        testSubscription.setNumSub(1L);
        testSubscription.setStartDate(LocalDate.now());
        testSubscription.setEndDate(LocalDate.now().plusMonths(1));
        testSubscription.setPrice(100.0f);
        testSubscription.setTypeSub(TypeSubscription.MONTHLY);
    }

    @Test
    void testAddSubscription() {
        // Given
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(testSubscription);

        // When
        Subscription result = subscriptionServices.addSubscription(testSubscription);

        // Then
        assertNotNull(result);
        assertEquals(testSubscription.getNumSub(), result.getNumSub());
        assertEquals(testSubscription.getTypeSub(), result.getTypeSub());
        verify(subscriptionRepository, times(1)).save(testSubscription);
    }

    @Test
    void testUpdateSubscription() {
        // Given
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(testSubscription);

        // When
        Subscription result = subscriptionServices.updateSubscription(testSubscription);

        // Then
        assertNotNull(result);
        assertEquals(testSubscription.getNumSub(), result.getNumSub());
        verify(subscriptionRepository, times(1)).save(testSubscription);
    }

    @Test
    void testRetrieveSubscriptionById() {
        // Given
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(testSubscription));

        // When
        Subscription result = subscriptionServices.retrieveSubscriptionById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testSubscription.getNumSub(), result.getNumSub());
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveSubscriptionByIdNotFound() {
        // Given
        when(subscriptionRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Subscription result = subscriptionServices.retrieveSubscriptionById(999L);

        // Then
        assertNull(result);
        verify(subscriptionRepository, times(1)).findById(999L);
    }

    @Test
    void testGetSubscriptionByType() {
        // Given
        Set<Subscription> subscriptions = Set.of(testSubscription);
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(TypeSubscription.MONTHLY))
                .thenReturn(subscriptions);

        // When
        Set<Subscription> result = subscriptionServices.getSubscriptionByType(TypeSubscription.MONTHLY);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(testSubscription));
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(TypeSubscription.MONTHLY);
    }

    @Test
    void testRetrieveSubscriptionsByDates() {
        // Given
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();
        List<Subscription> subscriptions = Arrays.asList(testSubscription);
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate))
                .thenReturn(subscriptions);

        // When
        List<Subscription> result = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSubscription, result.get(0));
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(startDate, endDate);
    }

    @Test
    void testAddSubscriptionWithAnnualType() {
        // Given
        Subscription annualSubscription = new Subscription();
        annualSubscription.setNumSub(2L);
        annualSubscription.setStartDate(LocalDate.now());
        annualSubscription.setPrice(500.0f);
        annualSubscription.setTypeSub(TypeSubscription.ANNUAL);

        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> {
            Subscription sub = invocation.getArgument(0);
            sub.setEndDate(sub.getStartDate().plusYears(1));
            return sub;
        });

        // When
        Subscription result = subscriptionServices.addSubscription(annualSubscription);

        // Then
        assertNotNull(result);
        assertEquals(LocalDate.now().plusYears(1), result.getEndDate());
        verify(subscriptionRepository, times(1)).save(annualSubscription);
    }

    @Test
    void testAddSubscriptionWithSemestrielType() {
        // Given
        Subscription semestrielSubscription = new Subscription();
        semestrielSubscription.setNumSub(3L);
        semestrielSubscription.setStartDate(LocalDate.now());
        semestrielSubscription.setPrice(300.0f);
        semestrielSubscription.setTypeSub(TypeSubscription.SEMESTRIEL);

        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> {
            Subscription sub = invocation.getArgument(0);
            sub.setEndDate(sub.getStartDate().plusMonths(6));
            return sub;
        });

        // When
        Subscription result = subscriptionServices.addSubscription(semestrielSubscription);

        // Then
        assertNotNull(result);
        assertEquals(LocalDate.now().plusMonths(6), result.getEndDate());
        verify(subscriptionRepository, times(1)).save(semestrielSubscription);
    }

    @Test
    void testAddSubscriptionWithMonthlyType() {
        // Given
        Subscription monthlySubscription = new Subscription();
        monthlySubscription.setNumSub(4L);
        monthlySubscription.setStartDate(LocalDate.now());
        monthlySubscription.setPrice(100.0f);
        monthlySubscription.setTypeSub(TypeSubscription.MONTHLY);

        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> {
            Subscription sub = invocation.getArgument(0);
            sub.setEndDate(sub.getStartDate().plusMonths(1));
            return sub;
        });

        // When
        Subscription result = subscriptionServices.addSubscription(monthlySubscription);

        // Then
        assertNotNull(result);
        assertEquals(LocalDate.now().plusMonths(1), result.getEndDate());
        verify(subscriptionRepository, times(1)).save(monthlySubscription);
    }

    @Test
    void testUpdateSubscriptionWithNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            subscriptionServices.updateSubscription(null);
        });
    }

    @Test
    void testRetrieveSubscriptionByIdWithNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            subscriptionServices.retrieveSubscriptionById(null);
        });
    }

    @Test
    void testGetSubscriptionByTypeWithNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            subscriptionServices.getSubscriptionByType(null);
        });
    }

    @Test
    void testRetrieveSubscriptionsByDatesWithNullDates() {
        // Test null start date
        assertThrows(NullPointerException.class, () -> 
            subscriptionServices.retrieveSubscriptionsByDates(null, LocalDate.now())
        );

        // Test null end date  
        assertThrows(NullPointerException.class, () -> 
            subscriptionServices.retrieveSubscriptionsByDates(LocalDate.now(), null)
        );
    }

    @Test
    void testAddSubscriptionWithNullType() {
        // Given
        Subscription subscriptionWithNullType = new Subscription();
        subscriptionWithNullType.setNumSub(5L);
        subscriptionWithNullType.setStartDate(LocalDate.now());
        subscriptionWithNullType.setPrice(100.0f);
        subscriptionWithNullType.setTypeSub(null);

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            subscriptionServices.addSubscription(subscriptionWithNullType);
        });
        
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    void testMultipleSubscriptionsByType() {
        // Given
        Subscription subscription1 = new Subscription();
        subscription1.setNumSub(1L);
        subscription1.setTypeSub(TypeSubscription.MONTHLY);

        Subscription subscription2 = new Subscription();
        subscription2.setNumSub(2L);
        subscription2.setTypeSub(TypeSubscription.MONTHLY);

        Set<Subscription> subscriptions = Set.of(subscription1, subscription2);
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(TypeSubscription.MONTHLY))
                .thenReturn(subscriptions);

        // When
        Set<Subscription> result = subscriptionServices.getSubscriptionByType(TypeSubscription.MONTHLY);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(sub -> sub.getTypeSub() == TypeSubscription.MONTHLY));
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(TypeSubscription.MONTHLY);
    }

    @Test
    void testUpdateSubscriptionWithDifferentData() {
        // Given
        Subscription originalSubscription = new Subscription();
        originalSubscription.setNumSub(1L);
        originalSubscription.setPrice(100.0f);

        Subscription updatedSubscription = new Subscription();
        updatedSubscription.setNumSub(1L);
        updatedSubscription.setPrice(150.0f);

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(updatedSubscription);

        // When
        Subscription result = subscriptionServices.updateSubscription(updatedSubscription);

        // Then
        assertNotNull(result);
        assertEquals(150.0f, result.getPrice());
        verify(subscriptionRepository, times(1)).save(updatedSubscription);
    }
}
