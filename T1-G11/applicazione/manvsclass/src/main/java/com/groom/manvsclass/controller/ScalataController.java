package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.dto.LevelDTO;
import com.groom.manvsclass.model.dto.ScalataDTO;
import com.groom.manvsclass.service.ScalataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/scalata")
public class ScalataController {

    private final ScalataService scalataService;

    public ScalataController(ScalataService scalataService) {
        this.scalataService = scalataService;
    }

    /**
     * POST /create
     * Insert a new ladder
     */
    @Operation(
            summary = "Create a new ladder",
            description = "Checks for duplicates, minimum number of levels, maximum time, and different UT classes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ladder created successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid levels or negative maximum time",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "409", description = "Ladder already exists",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<String> createScalata(@RequestBody ScalataDTO scalataDTO,
                                                @CookieValue(name = "jwt", required = false) String jwt) {
        scalataService.createScalata(scalataDTO);
        return ResponseEntity.ok("Ladder created");
    }

    /**
     * GET /getAll
     * Retrieve all ladders
     */
    @Operation(summary = "Retrieve all available ladders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of ladders retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ScalataDTO.class))))
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<ScalataDTO>> getAllScalata(@CookieValue(name = "jwt", required = false) String jwt) {
        List<ScalataDTO> scalateDTO = scalataService.getAll();
        return ResponseEntity.ok(scalateDTO);
    }

    /**
     * GET /get/{name}
     * Retrieve a ladder by name
     */
    @Operation(
            summary = "Retrieve a ladder by name",
            description = "Returns the ladder if it exists, otherwise throws ScalataNotFoundException")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ladder retrieved",
                    content = @Content(schema = @Schema(implementation = ScalataDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ladder not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/get/{name}")
    public ResponseEntity<ScalataDTO> getScalata(@PathVariable String name,
                                                 @CookieValue(name = "jwt", required = false) String jwt) {
        ScalataDTO scalataDTO = scalataService.getScalataByName(name);
        return ResponseEntity.ok(scalataDTO);
    }

    /**
     * GET /getLevel/{name}/{number}
     * Retrieve a specific level from a ladder searched by name and level number
     */
    @Operation(
            summary = "Retrieve a specific level of a ladder",
            description = "Returns the level if both the ladder and the level exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Level retrieved successfully",
                    content = @Content(schema = @Schema(implementation = LevelDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ladder or Level not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/getLevel/{name}/{number}")
    public ResponseEntity<LevelDTO> getLevel(@PathVariable String name,
                                             @PathVariable int number,
                                             @CookieValue(name = "jwt", required = false) String jwt) {

        LevelDTO levelDTO = scalataService.getLevel(name, number);
        return ResponseEntity.ok(levelDTO);
    }

    /**
     * GET /getLevels/{name}
     * Retrieve the list of levels from a ladder searched by name
     */
    @Operation(
            summary = "Retrieve the levels of a ladder",
            description = "Returns the list of levels if the ladder exists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Levels retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = LevelDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Ladder not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/getLevels/{name}")
    public ResponseEntity<List<LevelDTO>> getLevels(@PathVariable String name,
                                                    @CookieValue(name = "jwt", required = false) String jwt) {
        List<LevelDTO> levelsDTO = scalataService.getLevels(name);
        return ResponseEntity.ok(levelsDTO);
    }

    /**
     * DELETE /delete/{name}
     * Delete a ladder by name
     */
    @Operation(
            summary = "Delete a ladder by name",
            description = "Removes the ladder if it exists, otherwise throws ScalataNotFoundException")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ladder deleted successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Ladder not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deleteScalata(@PathVariable String name,
                                                @CookieValue(name = "jwt", required = false) String jwt) {
        scalataService.deleteScalata(name);
        return ResponseEntity.ok("Ladder deleted");
    }

}
