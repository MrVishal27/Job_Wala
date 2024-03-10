package com.NaukriChowk.Job_Wala.dto.authrequest;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class  SignUpForm {

    @NotBlank(message = "firstName can not be blank")
    @Size(min = 3, max = 50)
    @Pattern(regexp = "[A-Z]{1}[a-z]{2,}")
    private String firstName;


    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 3, max = 50)
    @Pattern(regexp = "[A-Z]{1}[a-z]{2,}")
    private String lastName;

    @NotBlank
    @Size(max = 60)
    @Email(message = "Please enter email in proper format!")
    private String email;


    @Column(name = "user_age")
    @NotNull(message = "Age must not be null")
    @Min(value = 18, message = "Age must be greater than or equal to 18")
    private Integer age;

   // @Pattern(regexp = "[mf]", message = "gender must be 'm' or 'f'")
    private String gender;

    private Set<String> role;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 6, max = 40)
    private String password;

}
