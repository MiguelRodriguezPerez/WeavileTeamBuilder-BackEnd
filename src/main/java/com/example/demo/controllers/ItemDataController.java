package com.example.demo.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.WeavileErrorResponse;
import com.example.demo.dto.ItemDto;
import com.example.demo.services.implementations.ItemDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "Item",
    description = "Endpoints related to item data"
)
@RequestMapping("/itemData")
@RestController
public class ItemDataController {

    @Autowired
    ItemDataService itemDataService;

    @Operation(
        operationId = "getAllItems",
        summary = "Get all items",
        description = "Returns a list with all items"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Items successfully retrieved",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = ItemDto.class))
            )
        )
    })
    @GetMapping("/allItems")
    public ResponseEntity<Set<ItemDto>> getAllItemsEndpoint() {
        return new ResponseEntity<>(itemDataService.getAllItemsAsDto(), HttpStatus.OK);
    }

    @Operation(
        operationId = "getItemByName",
        summary = "Get item by name" ,
        description = "Returns an item using an string as parameter",
        parameters = {
            @Parameter(name = "itemName", description = "The item name to search", example = "Choice Band", required = true)
        }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Item succesfully obtained",
            content = @Content(
                schema = @Schema(implementation = ItemDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Item not found",
            content = @Content(
                schema = @Schema(implementation = WeavileErrorResponse.class)
            )
        )
    })
    @GetMapping("/getItemByName/{itemName}")
    public ResponseEntity<ItemDto> getItemByNameEndpoint(@PathVariable String itemName) {
        return new ResponseEntity<ItemDto>(
            itemDataService.convertItemDataToDto(
                itemDataService.getItemByName(itemName)
        ), HttpStatus.OK);
    }

}
