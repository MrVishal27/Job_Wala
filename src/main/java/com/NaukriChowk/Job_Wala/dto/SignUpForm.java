package com.NaukriChowk.Job_Wala.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {

    @NotBlank(message = "firstName can not be blank")
    @Size(min = 3, max = 50)
    private String firstName;


    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 3, max = 50)
    private String lastName;

    @NotBlank
    @Size(max = 60)
    @Email(message = "Please enter email in proper format!")
    private String email;


    @Column(name = "user_age")
    @NotNull(message = "Age must not be null")
    private Integer age;

    private Set<String> role;


    @NotBlank(message = "Password cannot be null")
    @Size(min = 6, max = 40)
    private String password;

}
