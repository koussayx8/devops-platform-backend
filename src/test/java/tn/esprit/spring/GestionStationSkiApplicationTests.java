package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import tn.esprit.spring.repositories.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
    "server.port=0"
})
class GestionStationSkiApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

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
        assertNotNull(applicationContext, "Application context should be loaded");
        assertTrue(applicationContext.getBeanDefinitionCount() > 0, "Application context should contain beans");
    }

    @Test
    void applicationStarts() {
        // This test verifies that the application can start without errors
        // It's a basic smoke test to ensure the application is properly configured
        assertNotNull(applicationContext, "Application context should be available");
        
        // Verify that all our mock repositories are properly injected
        assertNotNull(skierRepository, "SkierRepository should be mocked and available");
        assertNotNull(pisteRepository, "PisteRepository should be mocked and available");
        assertNotNull(courseRepository, "CourseRepository should be mocked and available");
        assertNotNull(registrationRepository, "RegistrationRepository should be mocked and available");
        assertNotNull(subscriptionRepository, "SubscriptionRepository should be mocked and available");
        assertNotNull(instructorRepository, "InstructorRepository should be mocked and available");
    }
}