package com.marceldias.birthday;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;

@Validated
public interface HelloApi {

//    @Operation(summary = "Get birthday message", description = "Get birthday message", tags={ "Birthday" })
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
//        @ApiResponse(responseCode = "404", description = "User not found") })
    @RequestMapping(value = "/helloapi/{username}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<String> getBirthdayMessage(
//            @Pattern(regexp="^[a-z,A-Z]")
//            @DecimalMin("1")
//            @DecimalMax("20")
//            @Parameter(in = ParameterIn.PATH, description = "Username identifier", required=true, schema=@Schema())
            @PathVariable("username") String username);


//    @Operation(summary = "Saves/updates the given User", description = "Saves/updates the given user’s name and date of birth in the database", tags={ "Birthday" })
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "204", description = "Successful operation"),
//        @ApiResponse(responseCode = "409", description = "Validation exception - invalid birthday or username") })
    @RequestMapping(value = "/helloapi/{username}",
        consumes = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> savesUser(
//        @Pattern(regexp="^[a-z,A-Z]")
//        @DecimalMin("1")
//        @DecimalMax("20")
        @PathVariable("username") String username,
//        @Parameter(in = ParameterIn.PATH, description = "Username identifier", required=true)
//        @Parameter(in = ParameterIn.DEFAULT, description = "User birthday date", required=true)
        @RequestBody LocalDate dateOfBirth);

}

