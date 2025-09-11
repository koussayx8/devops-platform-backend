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
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.services.IRegistrationServices;

import java.util.List;

@Tag(name = "\uD83D\uDDD3️Registration Management")
@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationRestController {
    private final IRegistrationServices registrationServices;

    @Operation(
        summary = "Add registration and assign to skier",
        description = "Creates a new registration and automatically assigns it to a specific skier.",
        tags = {"Registrations"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Registration created and assigned to skier successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Registration.class),
                examples = @ExampleObject(
                    name = "Created Registration",
                    value = """
                        {
                            "numRegistration": 1,
                            "numWeek": 5,
                            "skier": null,
                            "course": null
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Skier not found",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json")
        )
    })
    @PutMapping("/addAndAssignToSkier/{numSkieur}")
    public ResponseEntity<Registration> addAndAssignToSkier(
        @Parameter(description = "Registration object containing week number", required = true)
        @RequestBody Registration registration,
        @Parameter(description = "ID of the skier to assign the registration to", required = true, example = "1")
        @PathVariable("numSkieur") Long numSkieur
    ) {
        Registration createdRegistration = registrationServices.addRegistrationAndAssignToSkier(registration, numSkieur);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRegistration);
    }
    @Operation(
        summary = "Assign registration to course",
        description = "Assigns an existing registration to a specific course.",
        tags = {"Registrations"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Registration assigned to course successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Registration.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Registration or course not found",
            content = @Content(mediaType = "application/json")
        )
    })
    @PutMapping("/assignToCourse/{numRegis}/{numSkieur}")
    public ResponseEntity<Registration> assignToCourse(
        @Parameter(description = "ID of the registration", required = true, example = "1")
        @PathVariable("numRegis") Long numRegistration,
        @Parameter(description = "ID of the course", required = true, example = "1")
        @PathVariable("numSkieur") Long numCourse
    ) {
        Registration updatedRegistration = registrationServices.assignRegistrationToCourse(numRegistration, numCourse);
        return ResponseEntity.ok(updatedRegistration);
    }


    @Operation(
        summary = "Add registration and assign to skier and course",
        description = "Creates a new registration and automatically assigns it to both a skier and a course in one operation.",
        tags = {"Registrations"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Registration created and assigned to skier and course successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Registration.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Skier or course not found",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json")
        )
    })
    @PutMapping("/addAndAssignToSkierAndCourse/{numSkieur}/{numCourse}")
    public ResponseEntity<Registration> addAndAssignToSkierAndCourse(
        @Parameter(description = "Registration object containing week number", required = true)
        @RequestBody Registration registration,
        @Parameter(description = "ID of the skier", required = true, example = "1")
        @PathVariable("numSkieur") Long numSkieur,
        @Parameter(description = "ID of the course", required = true, example = "1")
        @PathVariable("numCourse") Long numCourse
    ) {
        Registration createdRegistration = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, numSkieur, numCourse);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRegistration);
    }

    @Operation(
        summary = "Get instructor teaching weeks by support",
        description = "Retrieves the list of weeks when a specific instructor has given lessons for a particular support type (SKI or SNOWBOARD).",
        tags = {"Registrations"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of weeks retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "array", implementation = Integer.class),
                examples = @ExampleObject(
                    name = "Weeks List",
                    value = "[1, 3, 5, 7, 9]"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Instructor not found",
            content = @Content(mediaType = "application/json")
        )
    })
    @GetMapping("/numWeeks/{numInstructor}/{support}")
    public ResponseEntity<List<Integer>> numWeeksCourseOfInstructorBySupport(
        @Parameter(description = "ID of the instructor", required = true, example = "1")
        @PathVariable("numInstructor") Long numInstructor,
        @Parameter(description = "Support type (SKI or SNOWBOARD)", required = true, example = "SKI")
        @PathVariable("support") Support support
    ) {
        List<Integer> weeks = registrationServices.numWeeksCourseOfInstructorBySupport(numInstructor, support);
        return ResponseEntity.ok(weeks);
    }
}
