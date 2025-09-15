package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.repositories.IRegistrationRepository;

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

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    private Registration testRegistration;

    @BeforeEach
    void setUp() {
        testRegistration = new Registration();
        testRegistration.setNumRegistration(1L);
        testRegistration.setNumWeek(1);
    }

    // CREATE Tests
    @Test
    void testAddRegistration_Success() {
        // Given
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // When
        Registration result = registrationServices.addRegistration(testRegistration);

        // Then
        assertNotNull(result);
        assertEquals(testRegistration.getNumRegistration(), result.getNumRegistration());
        assertEquals(testRegistration.getNumWeek(), result.getNumWeek());
        verify(registrationRepository).save(testRegistration);
    }

    @Test
    void testAddRegistration_WithNullRegistration() {
        // When & Then
        assertThrows(Exception.class, () -> {
            registrationServices.addRegistration(null);
        });
    }

    // READ Tests
    @Test
    void testRetrieveAllRegistrations_Success() {
        // Given
        List<Registration> expectedRegistrations = Arrays.asList(testRegistration);
        when(registrationRepository.findAll()).thenReturn(expectedRegistrations);

        // When
        List<Registration> result = registrationServices.retrieveAllRegistrations();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testRegistration.getNumRegistration(), result.get(0).getNumRegistration());
        verify(registrationRepository).findAll();
    }

    @Test
    void testRetrieveAllRegistrations_EmptyList() {
        // Given
        when(registrationRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Registration> result = registrationServices.retrieveAllRegistrations();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(registrationRepository).findAll();
    }

    @Test
    void testRetrieveRegistration_Success() {
        // Given
        Long registrationId = 1L;
        when(registrationRepository.findById(registrationId)).thenReturn(Optional.of(testRegistration));

        // When
        Registration result = registrationServices.retrieveRegistration(registrationId);

        // Then
        assertNotNull(result);
        assertEquals(testRegistration.getNumRegistration(), result.getNumRegistration());
        assertEquals(testRegistration.getNumWeek(), result.getNumWeek());
        verify(registrationRepository).findById(registrationId);
    }

    @Test
    void testRetrieveRegistration_NotFound() {
        // Given
        Long registrationId = 999L;
        when(registrationRepository.findById(registrationId)).thenReturn(Optional.empty());

        // When
        Registration result = registrationServices.retrieveRegistration(registrationId);

        // Then
        assertNull(result);
        verify(registrationRepository).findById(registrationId);
    }

    @Test
    void testRetrieveRegistration_WithNullId() {
        // When & Then
        assertThrows(Exception.class, () -> {
            registrationServices.retrieveRegistration(null);
        });
    }

    // UPDATE Tests
    @Test
    void testUpdateRegistration_Success() {
        // Given
        Registration updatedRegistration = new Registration();
        updatedRegistration.setNumRegistration(1L);
        updatedRegistration.setNumWeek(2);

        when(registrationRepository.save(any(Registration.class))).thenReturn(updatedRegistration);

        // When
        Registration result = registrationServices.updateRegistration(updatedRegistration);

        // Then
        assertNotNull(result);
        assertEquals(updatedRegistration.getNumRegistration(), result.getNumRegistration());
        assertEquals(updatedRegistration.getNumWeek(), result.getNumWeek());
        verify(registrationRepository).save(updatedRegistration);
    }

    @Test
    void testUpdateRegistration_WithNullRegistration() {
        // When & Then
        assertThrows(Exception.class, () -> {
            registrationServices.updateRegistration(null);
        });
    }

    // DELETE Tests
    @Test
    void testRemoveRegistration_Success() {
        // Given
        Long registrationId = 1L;

        // When
        registrationServices.removeRegistration(registrationId);

        // Then
        verify(registrationRepository).deleteById(registrationId);
    }

    @Test
    void testRemoveRegistration_WithNullId() {
        // When & Then
        assertThrows(Exception.class, () -> {
            registrationServices.removeRegistration(null);
        });
    }

    // Edge Cases and Business Logic Tests
    @Test
    void testAddRegistration_WithDifferentWeeks() {
        // Given
        Registration week1Registration = new Registration();
        week1Registration.setNumWeek(1);

        Registration week2Registration = new Registration();
        week2Registration.setNumWeek(2);

        Registration week3Registration = new Registration();
        week3Registration.setNumWeek(3);

        when(registrationRepository.save(any(Registration.class))).thenReturn(week1Registration, week2Registration, week3Registration);

        // When
        Registration result1 = registrationServices.addRegistration(week1Registration);
        Registration result2 = registrationServices.addRegistration(week2Registration);
        Registration result3 = registrationServices.addRegistration(week3Registration);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertEquals(1, result1.getNumWeek());
        assertEquals(2, result2.getNumWeek());
        assertEquals(3, result3.getNumWeek());
        verify(registrationRepository, times(3)).save(any(Registration.class));
    }

    @Test
    void testRetrieveAllRegistrations_MultipleRegistrations() {
        // Given
        Registration registration1 = new Registration();
        registration1.setNumRegistration(1L);
        registration1.setNumWeek(1);

        Registration registration2 = new Registration();
        registration2.setNumRegistration(2L);
        registration2.setNumWeek(2);

        List<Registration> expectedRegistrations = Arrays.asList(registration1, registration2);
        when(registrationRepository.findAll()).thenReturn(expectedRegistrations);

        // When
        List<Registration> result = registrationServices.retrieveAllRegistrations();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(registration1.getNumRegistration(), result.get(0).getNumRegistration());
        assertEquals(registration2.getNumRegistration(), result.get(1).getNumRegistration());
        verify(registrationRepository).findAll();
    }

    @Test
    void testAddRegistration_WithValidWeekNumbers() {
        // Given
        Registration validRegistration = new Registration();
        validRegistration.setNumWeek(5); // Valid week number

        when(registrationRepository.save(any(Registration.class))).thenReturn(validRegistration);

        // When
        Registration result = registrationServices.addRegistration(validRegistration);

        // Then
        assertNotNull(result);
        assertEquals(5, result.getNumWeek());
        assertTrue(result.getNumWeek() > 0);
        verify(registrationRepository).save(validRegistration);
    }

    @Test
    void testUpdateRegistration_ChangeWeek() {
        // Given
        Registration originalRegistration = new Registration();
        originalRegistration.setNumRegistration(1L);
        originalRegistration.setNumWeek(1);

        Registration updatedRegistration = new Registration();
        updatedRegistration.setNumRegistration(1L);
        updatedRegistration.setNumWeek(3);

        when(registrationRepository.save(any(Registration.class))).thenReturn(updatedRegistration);

        // When
        Registration result = registrationServices.updateRegistration(updatedRegistration);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getNumWeek());
        verify(registrationRepository).save(updatedRegistration);
    }

    @Test
    void testAddRegistration_WithZeroWeek() {
        // Given
        Registration zeroWeekRegistration = new Registration();
        zeroWeekRegistration.setNumWeek(0);

        when(registrationRepository.save(any(Registration.class))).thenReturn(zeroWeekRegistration);

        // When
        Registration result = registrationServices.addRegistration(zeroWeekRegistration);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getNumWeek());
        verify(registrationRepository).save(zeroWeekRegistration);
    }

    @Test
    void testAddRegistration_WithNegativeWeek() {
        // Given
        Registration negativeWeekRegistration = new Registration();
        negativeWeekRegistration.setNumWeek(-1);

        when(registrationRepository.save(any(Registration.class))).thenReturn(negativeWeekRegistration);

        // When
        Registration result = registrationServices.addRegistration(negativeWeekRegistration);

        // Then
        assertNotNull(result);
        assertEquals(-1, result.getNumWeek());
        verify(registrationRepository).save(negativeWeekRegistration);
    }
}
