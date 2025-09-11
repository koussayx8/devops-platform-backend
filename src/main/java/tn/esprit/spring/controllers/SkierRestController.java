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
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISkierServices;

import java.util.List;

@Tag(name = "\uD83C\uDFC2 Skier Management")
@RestController
@RequestMapping("/skier")
@RequiredArgsConstructor
public class SkierRestController {

    private final ISkierServices skierServices;

    @Operation(
        summary = "Add a new skier",
        description = "Creates a new skier profile in the system. The skier will be assigned a unique ID automatically.",
        tags = {"Skiers"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Skier created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Skier.class),
                examples = @ExampleObject(
                    name = "Created Skier",
                    value = """
                        {
                            "numSkier": 1,
                            "firstName": "John",
                            "lastName": "Doe",
                            "dateOfBirth": "1990-05-15",
                            "city": "Chamonix",
                            "subscription": null,
                            "pistes": [],
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
    public ResponseEntity<Skier> addSkier(
        @Parameter(description = "Skier object containing all necessary information", required = true)
        @RequestBody Skier skier
    ) {
        Skier createdSkier = skierServices.addSkier(skier);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSkier);
    }

    @Operation(
        summary = "Add skier and assign to course",
        description = "Creates a new skier and automatically assigns them to a specific course in one operation.",
        tags = {"Skiers"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Skier created and assigned to course successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Skier.class)
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
    @PostMapping("/addAndAssign/{numCourse}")
    public ResponseEntity<Skier> addSkierAndAssignToCourse(
        @Parameter(description = "Skier object containing all necessary information", required = true)
        @RequestBody Skier skier,
        @Parameter(description = "ID of the course to assign the skier to", required = true, example = "1")
        @PathVariable("numCourse") Long numCourse
    ) {
        Skier createdSkier = skierServices.addSkierAndAssignToCourse(skier, numCourse);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSkier);
    }
    @Operation(
        summary = "Assign skier to subscription",
        description = "Assigns an existing skier to a subscription. This creates a relationship between the skier and their subscription.",
        tags = {"Skiers"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Skier assigned to subscription successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Skier.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Skier or subscription not found",
            content = @Content(mediaType = "application/json")
        )
    })
    @PutMapping("/assignToSub/{numSkier}/{numSub}")
    public ResponseEntity<Skier> assignToSubscription(
        @Parameter(description = "ID of the skier", required = true, example = "1")
        @PathVariable("numSkier") Long numSkier,
        @Parameter(description = "ID of the subscription", required = true, example = "1")
        @PathVariable("numSub") Long numSub
    ) {
        Skier updatedSkier = skierServices.assignSkierToSubscription(numSkier, numSub);
        return ResponseEntity.ok(updatedSkier);
    }

    @Operation(
        summary = "Assign skier to piste",
        description = "Assigns an existing skier to a piste (ski slope). This allows the skier to access the specific piste.",
        tags = {"Skiers"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Skier assigned to piste successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Skier.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Skier or piste not found",
            content = @Content(mediaType = "application/json")
        )
    })
    @PutMapping("/assignToPiste/{numSkier}/{numPiste}")
    public ResponseEntity<Skier> assignToPiste(
        @Parameter(description = "ID of the skier", required = true, example = "1")
        @PathVariable("numSkier") Long numSkier,
        @Parameter(description = "ID of the piste", required = true, example = "1")
        @PathVariable("numPiste") Long numPiste
    ) {
        Skier updatedSkier = skierServices.assignSkierToPiste(numSkier, numPiste);
        return ResponseEntity.ok(updatedSkier);
    }
    @Operation(
        summary = "Get skiers by subscription type",
        description = "Retrieves all skiers who have a specific type of subscription (ANNUAL, MONTHLY, or SEMESTRIEL).",
        tags = {"Skiers"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of skiers retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Skier.class)
            )
        )
    })
    @GetMapping("/getSkiersBySubscription")
    public ResponseEntity<List<Skier>> retrieveSkiersBySubscriptionType(
        @Parameter(description = "Type of subscription to filter by", required = true, example = "ANNUAL")
        @RequestParam TypeSubscription typeSubscription
    ) {
        List<Skier> skiers = skierServices.retrieveSkiersBySubscriptionType(typeSubscription);
        return ResponseEntity.ok(skiers);
    }
    @Operation(
        summary = "Get skier by ID",
        description = "Retrieves a specific skier by their unique identifier.",
        tags = {"Skiers"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Skier found and returned successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Skier.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Skier not found",
            content = @Content(mediaType = "application/json")
        )
    })
    @GetMapping("/get/{id-skier}")
    public ResponseEntity<Skier> getById(
        @Parameter(description = "Unique identifier of the skier", required = true, example = "1")
        @PathVariable("id-skier") Long numSkier
    ) {
        Skier skier = skierServices.retrieveSkier(numSkier);
        return ResponseEntity.ok(skier);
    }

    @Operation(
        summary = "Delete skier by ID",
        description = "Permanently deletes a skier from the system. This action cannot be undone.",
        tags = {"Skiers"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Skier deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Skier not found",
            content = @Content(mediaType = "application/json")
        )
    })
    @DeleteMapping("/delete/{id-skier}")
    public ResponseEntity<Void> deleteById(
        @Parameter(description = "Unique identifier of the skier to delete", required = true, example = "1")
        @PathVariable("id-skier") Long numSkier
    ) {
        skierServices.removeSkier(numSkier);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get all skiers",
        description = "Retrieves a list of all skiers in the system with their complete information.",
        tags = {"Skiers"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of all skiers retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Skier.class)
            )
        )
    })
    @GetMapping("/all")
    public ResponseEntity<List<Skier>> getAllSkiers() {
        List<Skier> skiers = skierServices.retrieveAllSkiers();
        return ResponseEntity.ok(skiers);
    }

}
