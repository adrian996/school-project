package com.example.mylittleproject.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.mylittleproject.enumerator.ERole;
import com.example.mylittleproject.model.JwtResponse;
import com.example.mylittleproject.model.LoginRequest;
import com.example.mylittleproject.model.MyUserDetails;
import com.example.mylittleproject.model.Role;
import com.example.mylittleproject.model.SignUpRequest;
import com.example.mylittleproject.model.User;
import com.example.mylittleproject.repo.RoleRepo;
import com.example.mylittleproject.repo.UserRepo;
import com.example.mylittleproject.security.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final UserRepo userRepo;
  private final RoleRepo roleRepository;
  private final PasswordEncoder encoder;
  private final JwtUtils jwtUtils;

  //Login endpoint, validates credentials, generates token.
  //Returns: a custom object - JwtResponse that has the token, username and roles.
  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
                         userDetails.getUsername(),
                         roles));
  }


  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    if (userRepo.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body("Error: Username is already taken!");
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
               encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(studentRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "teacher":
          Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(teacherRole);

          break;
        default:
          Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(studentRole);
        }
      });
    }

    user.setRoles(roles);
    userRepo.save(user);

    return ResponseEntity.ok("User registered successfully!");
  }


}
