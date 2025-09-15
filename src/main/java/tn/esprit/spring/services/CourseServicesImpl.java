package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;

import java.util.List;
@AllArgsConstructor
@Service
public class CourseServicesImpl implements  ICourseServices{

    private ICourseRepository courseRepository;

    @Override
    public List<Course> retrieveAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course addCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (course.getNumCourse() == null) {
            throw new IllegalArgumentException("Course ID cannot be null for update");
        }
        // Verify course exists before updating
        if (!courseRepository.existsById(course.getNumCourse())) {
            throw new IllegalArgumentException("Course with ID " + course.getNumCourse() + " does not exist");
        }
        return courseRepository.save(course);
    }

    @Override
    public Course retrieveCourse(Long numCourse) {
        if (numCourse == null) {
            throw new IllegalArgumentException("Course ID cannot be null");
        }
        return courseRepository.findById(numCourse).orElse(null);
    }


}
