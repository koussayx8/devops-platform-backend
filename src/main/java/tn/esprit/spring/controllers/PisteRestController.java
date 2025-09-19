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
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.services.IPisteServices;

import java.util.List;

@Tag(name = "\uD83C\uDFBF Piste Management")
@RestController
@RequestMapping("/piste")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PisteRestController {

    private final IPisteServices pisteServices;

    @Operation(
        summary = "Add a new piste",
        description = "Creates a new piste (ski slope) with specified name, color, length, and slope information.",
        tags = {"Pistes"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Piste created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Piste.class),
                examples = @ExampleObject(
                    name = "Created Piste",
                    value = """
                        {
                            "numPiste": 1,
                            "namePiste": "La Combe",
                            "color": "BLUE",
                            "length": 1200,
                            "slope": 15,
                            "skiers": []
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
    public ResponseEntity<Piste> addPiste(
        @Parameter(description = "Piste object containing all necessary information", required = true)
        @RequestBody Piste piste
    ) {
        Piste createdPiste = pisteServices.addPiste(piste);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPiste);
    }
    @Operation(
        summary = "Get all pistes",
        description = "Retrieves a list of all available pistes (ski slopes) in the system.",
        tags = {"Pistes"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of all pistes retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Piste.class)
            )
        )
    })
    @GetMapping("/all")
    public ResponseEntity<List<Piste>> getAllPistes() {
        List<Piste> pistes = pisteServices.retrieveAllPistes();
        return ResponseEntity.ok(pistes);
    }

    @Operation(
        summary = "Get piste by ID",
        description = "Retrieves a specific piste by its unique identifier.",
        tags = {"Pistes"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Piste found and returned successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Piste.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Piste not found",
            content = @Content(mediaType = "application/json")
        )
    })
    @GetMapping("/get/{id-piste}")
    public ResponseEntity<Piste> getById(
        @Parameter(description = "Unique identifier of the piste", required = true, example = "1")
        @PathVariable("id-piste") Long numPiste
    ) {
        Piste piste = pisteServices.retrievePiste(numPiste);
        return ResponseEntity.ok(piste);
    }

    @Operation(
        summary = "Delete piste by ID",
        description = "Permanently deletes a piste from the system. This action cannot be undone.",
        tags = {"Pistes"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Piste deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Piste not found",
            content = @Content(mediaType = "application/json")
        )
    })
    @DeleteMapping("/delete/{id-piste}")
    public ResponseEntity<Void> deleteById(
        @Parameter(description = "Unique identifier of the piste to delete", required = true, example = "1")
        @PathVariable("id-piste") Long numPiste
    ) {
        pisteServices.removePiste(numPiste);
        return ResponseEntity.noContent().build();
    }
    

}
