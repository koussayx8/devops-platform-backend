package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.services.ICourseServices;

import java.util.List;

@Tag(name = "\uD83D\uDCDA Course Management")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CourseRestController {
    
    private final ICourseServices courseServices;

    @Operation(
        summary = "Add a new course",
        description = "Creates a new skiing course with specified level, type, support, price, and time slot.",
        tags = {"Courses"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Course created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Course.class),
                examples = @ExampleObject(
                    name = "Created Course",
                    value = """
                        {
                            "numCourse": 1,
                            "level": 2,
                            "typeCourse": "COLLECTIVE_ADULT",
                            "support": "SKI",
                            "price": 150.0,
                            "timeSlot": 2,
                            "registrations": []
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/add")
    public ResponseEntity<Course> addCourse(
        @Parameter(description = "Course object containing all necessary information", required = true)
        @RequestBody Course course
    ) {
        Course createdCourse = courseServices.addCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @Operation(
        summary = "Get all courses",
        description = "Retrieves a list of all available courses in the system.",
        tags = {"Courses"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of all courses retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Course.class)
            )
        )
    })
    @GetMapping("/all")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseServices.retrieveAllCourses();
        return ResponseEntity.ok(courses);
    }

    @Operation(
        summary = "Update course",
        description = "Updates an existing course with new information. The course must exist in the system.",
        tags = {"Courses"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Course updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Course.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Course not found",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json")
        )
    })
    @PutMapping("/update")
    public ResponseEntity<Course> updateCourse(
        @Parameter(description = "Course object with updated information", required = true)
        @RequestBody Course course
    ) {
        Course updatedCourse = courseServices.updateCourse(course);
        return ResponseEntity.ok(updatedCourse);
    }

    @Operation(
        summary = "Get course by ID",
        description = "Retrieves a specific course by its unique identifier.",
        tags = {"Courses"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Course found and returned successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Course.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Course not found",
            content = @Content(mediaType = "application/json")
        )
    })
    @GetMapping("/get/{id-course}")
    public ResponseEntity<Course> getById(
        @Parameter(description = "Unique identifier of the course", required = true, example = "1")
        @PathVariable("id-course") Long numCourse
    ) {
        Course course = courseServices.retrieveCourse(numCourse);
        return ResponseEntity.ok(course);
    }

}
