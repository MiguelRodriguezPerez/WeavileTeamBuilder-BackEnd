package com.example.demo.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.apiResponses.ApiNotFoundResponse;
import com.example.demo.domain.team.NatureData;
import com.example.demo.dto.NatureDto;
import com.example.demo.services.implementations.NatureDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "Nature",
    description = "Endpoints related to pokemon natures"
)
@RequestMapping("/natureData")
@RestController
public class NatureDataController {

    @Autowired
    NatureDataService natureDataService;

    @Operation(
        operationId = "getNatureByName",
        summary = "Get a nature by name parameter",
        description = "Returns a nature based on string parameter"
    )
    @ApiResponses(
        @ApiResponse(
            responseCode = "200",
            description = "Nature successfully retrieved",
            content = @Content(
                schema = @Schema(implementation = NatureData.class)
            )
        )
    )
    @ApiNotFoundResponse
    @GetMapping("/getNatureByName/{name}")
    public ResponseEntity<NatureDto> getNatureByNameEndpoint(@PathVariable String name) {
        return new ResponseEntity<>(
            natureDataService.convertNatureDataToDto(natureDataService.getNatureByName(name)), HttpStatus.OK);
    }

    @Operation(
        operationId = "getAllNatures",
        summary = "Get all natures",
        description = "Get a list with all pokemon natures"
    )
    @ApiResponses({
      @ApiResponse(
        responseCode = "200",
        description = "Nature list successfully retrieved",
        content = @Content(
            array = @ArraySchema(schema = @Schema(implementation = NatureData.class))
        )
      )  
    })
    @GetMapping("/getAllNatures")
    public ResponseEntity<Set<NatureDto>> getAllNaturesEndpoint() {
        return new ResponseEntity<Set<NatureDto>>(natureDataService.getAllNaturesAsDto(), HttpStatus.OK);
    }

}
