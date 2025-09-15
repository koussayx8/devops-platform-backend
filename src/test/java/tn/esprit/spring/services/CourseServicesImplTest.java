package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServicesImplTest {

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseServices;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setNumCourse(1L);
        testCourse.setLevel(1);
        testCourse.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        testCourse.setPrice(50.0f);
        testCourse.setTimeSlot(2);
    }

    // CREATE Tests
    @Test
    void testAddCourse_Success() {
        // Given
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // When
        Course result = courseServices.addCourse(testCourse);

        // Then
        assertNotNull(result);
        assertEquals(testCourse.getNumCourse(), result.getNumCourse());
        assertEquals(testCourse.getLevel(), result.getLevel());
        assertEquals(testCourse.getTypeCourse(), result.getTypeCourse());
        assertEquals(testCourse.getPrice(), result.getPrice());
        verify(courseRepository).save(testCourse);
    }

    @Test
    void testAddCourse_WithNullCourse() {
        // When & Then
        assertThrows(Exception.class, () -> {
            courseServices.addCourse(null);
        });
    }

    // READ Tests
    @Test
    void testRetrieveAllCourses_Success() {
        // Given
        List<Course> expectedCourses = Arrays.asList(testCourse);
        when(courseRepository.findAll()).thenReturn(expectedCourses);

        // When
        List<Course> result = courseServices.retrieveAllCourses();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCourse.getNumCourse(), result.get(0).getNumCourse());
        verify(courseRepository).findAll();
    }

    @Test
    void testRetrieveAllCourses_EmptyList() {
        // Given
        when(courseRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Course> result = courseServices.retrieveAllCourses();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courseRepository).findAll();
    }

    @Test
    void testRetrieveCourse_Success() {
        // Given
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));

        // When
        Course result = courseServices.retrieveCourse(courseId);

        // Then
        assertNotNull(result);
        assertEquals(testCourse.getNumCourse(), result.getNumCourse());
        assertEquals(testCourse.getLevel(), result.getLevel());
        verify(courseRepository).findById(courseId);
    }

    @Test
    void testRetrieveCourse_NotFound() {
        // Given
        Long courseId = 999L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When
        Course result = courseServices.retrieveCourse(courseId);

        // Then
        assertNull(result);
        verify(courseRepository).findById(courseId);
    }

    @Test
    void testRetrieveCourse_WithNullId() {
        // When & Then
        assertThrows(Exception.class, () -> {
            courseServices.retrieveCourse(null);
        });
    }

    // UPDATE Tests
    @Test
    void testUpdateCourse_Success() {
        // Given
        Course updatedCourse = new Course();
        updatedCourse.setNumCourse(1L);
        updatedCourse.setLevel(2);
        updatedCourse.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        updatedCourse.setPrice(75.0f);
        updatedCourse.setTimeSlot(3);

        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        // When
        Course result = courseServices.updateCourse(updatedCourse);

        // Then
        assertNotNull(result);
        assertEquals(updatedCourse.getNumCourse(), result.getNumCourse());
        assertEquals(updatedCourse.getLevel(), result.getLevel());
        assertEquals(updatedCourse.getTypeCourse(), result.getTypeCourse());
        assertEquals(updatedCourse.getPrice(), result.getPrice());
        verify(courseRepository).save(updatedCourse);
    }

    @Test
    void testUpdateCourse_WithNullCourse() {
        // When & Then
        assertThrows(Exception.class, () -> {
            courseServices.updateCourse(null);
        });
    }

    // Edge Cases and Business Logic Tests
    @Test
    void testAddCourse_WithDifferentTypes() {
        // Given
        Course childrenCourse = new Course();
        childrenCourse.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        childrenCourse.setLevel(1);
        childrenCourse.setPrice(50.0f);

        Course adultCourse = new Course();
        adultCourse.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        adultCourse.setLevel(2);
        adultCourse.setPrice(75.0f);

        when(courseRepository.save(any(Course.class))).thenReturn(childrenCourse, adultCourse);

        // When
        Course result1 = courseServices.addCourse(childrenCourse);
        Course result2 = courseServices.addCourse(adultCourse);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(TypeCourse.COLLECTIVE_CHILDREN, result1.getTypeCourse());
        assertEquals(TypeCourse.COLLECTIVE_ADULT, result2.getTypeCourse());
        verify(courseRepository, times(2)).save(any(Course.class));
    }

    @Test
    void testRetrieveAllCourses_MultipleCourses() {
        // Given
        Course course1 = new Course();
        course1.setNumCourse(1L);
        course1.setLevel(1);

        Course course2 = new Course();
        course2.setNumCourse(2L);
        course2.setLevel(2);

        List<Course> expectedCourses = Arrays.asList(course1, course2);
        when(courseRepository.findAll()).thenReturn(expectedCourses);

        // When
        List<Course> result = courseServices.retrieveAllCourses();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(course1.getNumCourse(), result.get(0).getNumCourse());
        assertEquals(course2.getNumCourse(), result.get(1).getNumCourse());
        verify(courseRepository).findAll();
    }
}
