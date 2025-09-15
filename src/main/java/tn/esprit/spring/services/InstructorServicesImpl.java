package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class InstructorServicesImpl implements IInstructorServices{

    private IInstructorRepository instructorRepository;
    private ICourseRepository courseRepository;

    @Override
    public Instructor addInstructor(Instructor instructor) {
        if (instructor == null) {
            throw new NullPointerException("Instructor cannot be null");
        }
        return instructorRepository.save(instructor);
    }

    @Override
    public List<Instructor> retrieveAllInstructors() {
        return instructorRepository.findAll();
    }

    @Override
    public Instructor updateInstructor(Instructor instructor) {
        if (instructor == null) {
            throw new NullPointerException("Instructor cannot be null");
        }
        if (instructor.getNumInstructor() == null) {
            throw new NullPointerException("Instructor ID cannot be null for update");
        }
        // Verify instructor exists before updating
        if (!instructorRepository.existsById(instructor.getNumInstructor())) {
            throw new NullPointerException("Instructor with ID " + instructor.getNumInstructor() + " does not exist");
        }
        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor retrieveInstructor(Long numInstructor) {
        if (numInstructor == null) {
            throw new NullPointerException("Instructor ID cannot be null");
        }
        return instructorRepository.findById(numInstructor).orElse(null);
    }

    @Override
    public Instructor addInstructorAndAssignToCourse(Instructor instructor, Long numCourse) {
        if (instructor == null || numCourse == null) {
            throw new NullPointerException("Instructor and course ID cannot be null");
        }
        Course course = courseRepository.findById(numCourse).orElse(null);
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        instructor.setCourses(courseSet);
        return instructorRepository.save(instructor);
    }


}
