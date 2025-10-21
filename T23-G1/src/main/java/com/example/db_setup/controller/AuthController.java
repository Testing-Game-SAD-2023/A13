package com.example.db_setup.controller;

import com.example.db_setup.model.dto.auth.AdminRegistrationDTO;
import com.example.db_setup.model.dto.auth.ChangePasswordRequestDTO;
import com.example.db_setup.model.dto.auth.LoginDTO;
import com.example.db_setup.model.dto.auth.UserRegistrationDTO;
import com.example.db_setup.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testrobotchallenge.commons.models.dto.auth.JwtValidationResponseDTO;
import testrobotchallenge.commons.models.user.Role;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Locale;

@RestController
@CrossOrigin
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;


    @Operation(summary = "Register a new player user", description = "Registers a new player with provided personal details and credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Player registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
            @ApiResponse(responseCode = "409", description = "Email already in use")
    })
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        authService.registerPlayer(userRegistrationDTO.getName(), userRegistrationDTO.getSurname(),
                userRegistrationDTO.getEmail(), userRegistrationDTO.getPassword(), userRegistrationDTO.getPasswordCheck(),
                userRegistrationDTO.getStudies());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Register a new admin user", description = "Registers a new admin with provided personal details and credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
            @ApiResponse(responseCode = "409", description = "Email already in use")
    })
    @PostMapping("/admin/register")
    public ResponseEntity<Object> registerAdmin(@Valid @RequestBody AdminRegistrationDTO adminRegistrationDTO) {
        authService.registerAdmin(adminRegistrationDTO.getName(), adminRegistrationDTO.getSurname(),
                adminRegistrationDTO.getEmail(), adminRegistrationDTO.getPassword(),
                adminRegistrationDTO.getPasswordCheck());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Login as player", description = "Authenticate a player with email and password and receive JWT + refresh token in cookies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, JWT cookies returned"),
            @ApiResponse(responseCode = "404", description = "Player not found")
    })
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        logger.info("[POST /auth/login] request: {}", loginDTO);
        String[] cookies = authService.loginPlayer(loginDTO.getEmail(), loginDTO.getPassword());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookies[0])
                .header(HttpHeaders.SET_COOKIE, cookies[1])
                .build();
    }

    @Operation(summary = "Login as admin", description = "Authenticate an admin with email and password and receive JWT + refresh token in cookies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, JWT cookies returned"),
            @ApiResponse(responseCode = "404", description = "Admin not found")
    })
    @PostMapping("/admin/login")
    public ResponseEntity<Object> loginAdmin(@Valid @RequestBody LoginDTO loginDTO) {
        String[] cookies = authService.loginAdmin(loginDTO.getEmail(), loginDTO.getPassword());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookies[0])
                .header(HttpHeaders.SET_COOKIE, cookies[1])
                .build();
    }

    @Operation(summary = "Logout", description = "Invalidate player/admin JWT and refresh token")
    @ApiResponse(responseCode = "200", description = "Logout successful, expired cookies returned")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(value = "jwt") String jwtToken, @CookieValue(value = "jwt-refresh") String refreshToken) {
        String[] cookies = authService.logout(jwtToken, refreshToken);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookies[0])
                .header(HttpHeaders.SET_COOKIE, cookies[1])
                .build();
    }

    @Operation(summary = "Validate JWT token", description = "Check if a JWT token is valid and return its status")
    @ApiResponse(
            responseCode = "200",
            description = "Validation successful",
            content = @Content(
                    schema = @Schema(implementation = JwtValidationResponseDTO.class)
            ))
    @PostMapping("/validateToken")
    public ResponseEntity<JwtValidationResponseDTO> checkValidityToken(@RequestParam("jwt") String token) {
        logger.info("[POST /auth/validateToken] Received request: {}", token);
        JwtValidationResponseDTO response = authService.validateToken(token);
        logger.info("[POST /auth/validateToken] Request validation result: {}", response);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Refresh JWT token", description = "Generate a new JWT token using the refresh token cookie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Refresh token is invalid")
    })
    @PostMapping("/refreshToken")
    public ResponseEntity<String> refreshJwtToken(@CookieValue(value = "jwt-refresh") String token) {
        String cookie = authService.refreshToken(token);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie)
                //.header(HttpHeaders.SET_COOKIE, newRefreshCookie.toString())
                .body("");
    }

    @Operation(summary = "Request password reset for player user", description = "Send a reset password email to the player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset email sent"),
            @ApiResponse(responseCode = "404", description = "Player not found")
    })
    @PostMapping("/reset_password")
    public ResponseEntity<Object> resetPasswordUser(@RequestParam String email, Locale locale) throws MessagingException {
        authService.requestResetPassword(email, Role.PLAYER, locale);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Request password reset for admin user", description = "Send a reset password email to the admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset email sent"),
            @ApiResponse(responseCode = "404", description = "Admin not found")
    })
    @PostMapping("/admin/reset_password")
    public ResponseEntity<Object> resetPasswordAdmin(@RequestParam String email, Locale locale) throws MessagingException {
        authService.requestResetPassword(email, Role.ADMIN, locale);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Change password for player user",
            description = "Change password for a player using teh password reset token received by mail"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Passwords do not match"),
            @ApiResponse(responseCode = "404", description = "Password reset token invalid/expired or not found"),
            @ApiResponse(responseCode = "403", description = "Role or email not compatible with password reset token"),
    })
    @PostMapping("/change_password")
    public ResponseEntity<Object> changePasswordUser(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        authService.changePassword(changePasswordRequestDTO.getEmail(), changePasswordRequestDTO.getPassword(),
                changePasswordRequestDTO.getPasswordCheck(), changePasswordRequestDTO.getPasswordResetToken(), Role.PLAYER);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Change password for admin user",
            description = "Change password for an admin using the password reset token received by mail"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Passwords do not match"),
            @ApiResponse(responseCode = "404", description = "Invalid or expired reset token"),
            @ApiResponse(responseCode = "403", description = "Role or email not compatible with reset token")
    })
    @PostMapping("/admin/change_password")
    public ResponseEntity<Object> changePasswordAdmin(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        authService.changePassword(changePasswordRequestDTO.getEmail(), changePasswordRequestDTO.getPassword(),
                changePasswordRequestDTO.getPasswordCheck(), changePasswordRequestDTO.getPasswordResetToken(), Role.ADMIN);

        return ResponseEntity.ok().build();
    }
}
