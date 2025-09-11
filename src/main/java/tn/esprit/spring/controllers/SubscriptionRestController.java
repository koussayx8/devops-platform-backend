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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISubscriptionServices;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Tag(name = "\uD83D\uDC65 Subscription Management")
@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionRestController {

    private final ISubscriptionServices subscriptionServices;

    @Operation(
        summary = "Add a new subscription",
        description = "Creates a new subscription with specified type, dates, and price.",
        tags = {"Subscriptions"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Subscription created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Subscription.class),
                examples = @ExampleObject(
                    name = "Created Subscription",
                    value = """
                        {
                            "numSub": 1,
                            "startDate": "2024-01-01",
                            "endDate": "2024-12-31",
                            "price": 500.0,
                            "typeSub": "ANNUAL"
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
    public ResponseEntity<Subscription> addSubscription(
        @Parameter(description = "Subscription object containing all necessary information", required = true)
        @RequestBody Subscription subscription
    ) {
        Subscription createdSubscription = subscriptionServices.addSubscription(subscription);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubscription);
    }
    @Operation(
        summary = "Get subscription by ID",
        description = "Retrieves a specific subscription by its unique identifier.",
        tags = {"Subscriptions"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Subscription found and returned successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Subscription.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Subscription not found",
            content = @Content(mediaType = "application/json")
        )
    })
    @GetMapping("/get/{id-subscription}")
    public ResponseEntity<Subscription> getById(
        @Parameter(description = "Unique identifier of the subscription", required = true, example = "1")
        @PathVariable("id-subscription") Long numSubscription
    ) {
        Subscription subscription = subscriptionServices.retrieveSubscriptionById(numSubscription);
        return ResponseEntity.ok(subscription);
    }
    
    @Operation(
        summary = "Get subscriptions by type",
        description = "Retrieves all subscriptions of a specific type (ANNUAL, MONTHLY, or SEMESTRIEL).",
        tags = {"Subscriptions"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of subscriptions retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Subscription.class)
            )
        )
    })
    @GetMapping("/all/{typeSub}")
    public ResponseEntity<Set<Subscription>> getSubscriptionsByType(
        @Parameter(description = "Type of subscription to filter by", required = true, example = "ANNUAL")
        @PathVariable("typeSub") TypeSubscription typeSubscription
    ) {
        Set<Subscription> subscriptions = subscriptionServices.getSubscriptionByType(typeSubscription);
        return ResponseEntity.ok(subscriptions);
    }
    @Operation(
        summary = "Update subscription",
        description = "Updates an existing subscription with new information. The subscription must exist in the system.",
        tags = {"Subscriptions"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Subscription updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Subscription.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Subscription not found",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json")
        )
    })
    @PutMapping("/update")
    public ResponseEntity<Subscription> updateSubscription(
        @Parameter(description = "Subscription object with updated information", required = true)
        @RequestBody Subscription subscription
    ) {
        Subscription updatedSubscription = subscriptionServices.updateSubscription(subscription);
        return ResponseEntity.ok(updatedSubscription);
    }
    @Operation(
        summary = "Get subscriptions by date range",
        description = "Retrieves all subscriptions created between two specific dates (inclusive).",
        tags = {"Subscriptions"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of subscriptions retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Subscription.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid date format or range",
            content = @Content(mediaType = "application/json")
        )
    })
    @GetMapping("/all/{date1}/{date2}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByDates(
        @Parameter(description = "Start date (YYYY-MM-DD)", required = true, example = "2024-01-01")
        @PathVariable("date1") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @Parameter(description = "End date (YYYY-MM-DD)", required = true, example = "2024-12-31")
        @PathVariable("date2") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Subscription> subscriptions = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);
        return ResponseEntity.ok(subscriptions);
    }

}
