package com.example.mylittleproject.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


//Custom response object, returned on a successful login and passed to front end.
@Data
@NoArgsConstructor
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private String username;
  private List<String> roles;

  public JwtResponse(String accessToken, String username, List<String> roles) {
    this.token = accessToken;
    this.username = username;
    this.roles = roles;
  }
}
