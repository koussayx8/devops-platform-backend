package tn.esprit.spring.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tn.esprit.spring.entities.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ISkierRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ISkierRepository skierRepository;

    @Test
    void testFindBySubscription_TypeSub() {
        // Given
        Subscription subscription1 = new Subscription();
        subscription1.setTypeSub(TypeSubscription.ANNUAL);
        subscription1.setStartDate(LocalDate.now());
        subscription1.setEndDate(LocalDate.now().plusYears(1));
        entityManager.persistAndFlush(subscription1);

        Subscription subscription2 = new Subscription();
        subscription2.setTypeSub(TypeSubscription.MONTHLY);
        subscription2.setStartDate(LocalDate.now());
        subscription2.setEndDate(LocalDate.now().plusMonths(1));
        entityManager.persistAndFlush(subscription2);

        Skier skier1 = new Skier();
        skier1.setFirstName("John");
        skier1.setLastName("Doe");
        skier1.setDateOfBirth(LocalDate.of(1990, 5, 15));
        skier1.setCity("Chamonix");
        skier1.setSubscription(subscription1);
        entityManager.persistAndFlush(skier1);

        Skier skier2 = new Skier();
        skier2.setFirstName("Jane");
        skier2.setLastName("Smith");
        skier2.setDateOfBirth(LocalDate.of(1985, 8, 20));
        skier2.setCity("Courchevel");
        skier2.setSubscription(subscription2);
        entityManager.persistAndFlush(skier2);

        // When
        List<Skier> annualSkiers = skierRepository.findBySubscription_TypeSub(TypeSubscription.ANNUAL);
        List<Skier> monthlySkiers = skierRepository.findBySubscription_TypeSub(TypeSubscription.MONTHLY);

        // Then
        assertEquals(1, annualSkiers.size());
        assertEquals("John", annualSkiers.get(0).getFirstName());
        assertEquals(TypeSubscription.ANNUAL, annualSkiers.get(0).getSubscription().getTypeSub());

        assertEquals(1, monthlySkiers.size());
        assertEquals("Jane", monthlySkiers.get(0).getFirstName());
        assertEquals(TypeSubscription.MONTHLY, monthlySkiers.get(0).getSubscription().getTypeSub());
    }

    @Test
    void testFindById() {
        // Given
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusYears(1));
        entityManager.persistAndFlush(subscription);

        Skier skier = new Skier();
        skier.setFirstName("John");
        skier.setLastName("Doe");
        skier.setDateOfBirth(LocalDate.of(1990, 5, 15));
        skier.setCity("Chamonix");
        skier.setSubscription(subscription);
        entityManager.persistAndFlush(skier);

        // When
        Optional<Skier> foundSkier = skierRepository.findById(skier.getNumSkier());

        // Then
        assertTrue(foundSkier.isPresent());
        assertEquals("John", foundSkier.get().getFirstName());
        assertEquals("Doe", foundSkier.get().getLastName());
        assertEquals("Chamonix", foundSkier.get().getCity());
    }

    @Test
    void testFindByIdNotFound() {
        // When
        Optional<Skier> foundSkier = skierRepository.findById(999L);

        // Then
        assertFalse(foundSkier.isPresent());
    }

    @Test
    void testSaveSkier() {
        // Given
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusYears(1));
        entityManager.persistAndFlush(subscription);

        Skier skier = new Skier();
        skier.setFirstName("John");
        skier.setLastName("Doe");
        skier.setDateOfBirth(LocalDate.of(1990, 5, 15));
        skier.setCity("Chamonix");
        skier.setSubscription(subscription);

        // When
        Skier savedSkier = skierRepository.save(skier);

        // Then
        assertNotNull(savedSkier.getNumSkier());
        assertEquals("John", savedSkier.getFirstName());
        assertEquals("Doe", savedSkier.getLastName());
        assertEquals("Chamonix", savedSkier.getCity());
        assertEquals(subscription, savedSkier.getSubscription());
    }

    @Test
    void testDeleteById() {
        // Given
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusYears(1));
        entityManager.persistAndFlush(subscription);

        Skier skier = new Skier();
        skier.setFirstName("John");
        skier.setLastName("Doe");
        skier.setDateOfBirth(LocalDate.of(1990, 5, 15));
        skier.setCity("Chamonix");
        skier.setSubscription(subscription);
        entityManager.persistAndFlush(skier);

        Long skierId = skier.getNumSkier();

        // When
        skierRepository.deleteById(skierId);

        // Then
        Optional<Skier> deletedSkier = skierRepository.findById(skierId);
        assertFalse(deletedSkier.isPresent());
    }

    @Test
    void testFindAll() {
        // Given
        Subscription subscription1 = new Subscription();
        subscription1.setTypeSub(TypeSubscription.ANNUAL);
        subscription1.setStartDate(LocalDate.now());
        subscription1.setEndDate(LocalDate.now().plusYears(1));
        entityManager.persistAndFlush(subscription1);

        Subscription subscription2 = new Subscription();
        subscription2.setTypeSub(TypeSubscription.MONTHLY);
        subscription2.setStartDate(LocalDate.now());
        subscription2.setEndDate(LocalDate.now().plusMonths(1));
        entityManager.persistAndFlush(subscription2);

        Skier skier1 = new Skier();
        skier1.setFirstName("John");
        skier1.setLastName("Doe");
        skier1.setDateOfBirth(LocalDate.of(1990, 5, 15));
        skier1.setCity("Chamonix");
        skier1.setSubscription(subscription1);
        entityManager.persistAndFlush(skier1);

        Skier skier2 = new Skier();
        skier2.setFirstName("Jane");
        skier2.setLastName("Smith");
        skier2.setDateOfBirth(LocalDate.of(1985, 8, 20));
        skier2.setCity("Courchevel");
        skier2.setSubscription(subscription2);
        entityManager.persistAndFlush(skier2);

        // When
        List<Skier> allSkiers = skierRepository.findAll();

        // Then
        assertEquals(2, allSkiers.size());
        assertTrue(allSkiers.stream().anyMatch(s -> s.getFirstName().equals("John")));
        assertTrue(allSkiers.stream().anyMatch(s -> s.getFirstName().equals("Jane")));
    }
}
