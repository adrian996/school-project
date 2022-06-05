package com.example.mylittleproject.model;

import java.util.Set;

import javax.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  private Set<String> role;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;
}
