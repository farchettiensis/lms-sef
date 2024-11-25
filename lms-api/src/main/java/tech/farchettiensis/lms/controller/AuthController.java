package tech.farchettiensis.lms.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.farchettiensis.lms.dto.AuthRequestDTO;
import tech.farchettiensis.lms.dto.AuthResponseDTO;
import tech.farchettiensis.lms.service.TokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            TokenService tokenService,
            UserDetailsService userDetailsService
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticateUser(@Valid @RequestBody AuthRequestDTO authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenService.generateToken(authentication.getPrincipal());

        AuthResponseDTO response = new AuthResponseDTO(jwt, "Bearer");

        return ResponseEntity.ok(response);
    }
}
