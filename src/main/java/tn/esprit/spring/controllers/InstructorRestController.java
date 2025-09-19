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
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;

import java.util.List;

@Tag(name = "\uD83D\uDC69\u200D\uD83C\uDFEB Instructor Management")
@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InstructorRestController {

    private final IInstructorServices instructorServices;

    @Operation(
        summary = "Add a new instructor",
        description = "Creates a new instructor profile in the system with their personal information and hire date.",
        tags = {"Instructors"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Instructor created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Instructor.class),
                examples = @ExampleObject(
                    name = "Created Instructor",
                    value = """
                        {
                            "numInstructor": 1,
                            "firstName": "Marie",
                            "lastName": "Dubois",
                            "dateOfHire": "2020-01-15",
                            "courses": []
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
    public ResponseEntity<Instructor> addInstructor(
        @Parameter(description = "Instructor object containing all necessary information", required = true)
        @RequestBody Instructor instructor
    ) {
        Instructor createdInstructor = instructorServices.addInstructor(instructor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInstructor);
    }
    @Operation(
        summary = "Add instructor and assign to course",
        description = "Creates a new instructor and automatically assigns them to a specific course in one operation.",
        tags = {"Instructors"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Instructor created and assigned to course successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Instructor.class)
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
    @PutMapping("/addAndAssignToCourse/{numCourse}")
    public ResponseEntity<Instructor> addAndAssignToInstructor(
        @Parameter(description = "Instructor object containing all necessary information", required = true)
        @RequestBody Instructor instructor,
        @Parameter(description = "ID of the course to assign the instructor to", required = true, example = "1")
        @PathVariable("numCourse") Long numCourse
    ) {
        Instructor createdInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, numCourse);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInstructor);
    }
    @Operation(
        summary = "Get all instructors",
        description = "Retrieves a list of all instructors in the system with their complete information.",
        tags = {"Instructors"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of all instructors retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Instructor.class)
            )
        )
    })
    @GetMapping("/all")
    public ResponseEntity<List<Instructor>> getAllInstructors() {
        List<Instructor> instructors = instructorServices.retrieveAllInstructors();
        return ResponseEntity.ok(instructors);
    }

    @Operation(
        summary = "Update instructor",
        description = "Updates an existing instructor with new information. The instructor must exist in the system.",
        tags = {"Instructors"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Instructor updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Instructor.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Instructor not found",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json")
        )
    })
    @PutMapping("/update")
    public ResponseEntity<Instructor> updateInstructor(
        @Parameter(description = "Instructor object with updated information", required = true)
        @RequestBody Instructor instructor
    ) {
        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);
        return ResponseEntity.ok(updatedInstructor);
    }

    @Operation(
        summary = "Get instructor by ID",
        description = "Retrieves a specific instructor by their unique identifier.",
        tags = {"Instructors"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Instructor found and returned successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Instructor.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Instructor not found",
            content = @Content(mediaType = "application/json")
        )
    })
    @GetMapping("/get/{id-instructor}")
    public ResponseEntity<Instructor> getById(
        @Parameter(description = "Unique identifier of the instructor", required = true, example = "1")
        @PathVariable("id-instructor") Long numInstructor
    ) {
        Instructor instructor = instructorServices.retrieveInstructor(numInstructor);
        return ResponseEntity.ok(instructor);
    }

}
