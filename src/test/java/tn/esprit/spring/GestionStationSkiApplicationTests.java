package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import tn.esprit.spring.repositories.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
    "server.port=0"
})
class GestionStationSkiApplicationTests {

    @MockBean
    private ISkierRepository skierRepository;
    
    @MockBean
    private IPisteRepository pisteRepository;
    
    @MockBean
    private ICourseRepository courseRepository;
    
    @MockBean
    private IRegistrationRepository registrationRepository;
    
    @MockBean
    private ISubscriptionRepository subscriptionRepository;
    
    @MockBean
    private IInstructorRepository instructorRepository;

    @Test
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
        // If this test passes, it means all beans are properly configured
    }

    @Test
    void applicationStarts() {
        // This test verifies that the application can start without errors
        // It's a basic smoke test to ensure the application is properly configured
    }
}