package com.NaukriChowk.Job_Wala.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

    @NotBlank(message = "The email field can't be blank")
    @Column(unique = true)
    @Email(message = "Please enter email in proper format!")
    private String email;

    @NotNull(message = "Age must not be null")
    @Column(name = "user_age")
    private Integer age;

    @Column
    @NotNull(message = "Password cannot be null")
    @Size(min = 5, message = "The password must have at least 5 characters")
    private String password;

    @Column
    @NotBlank(message = "firstName can not be blank")
    private String firstName;

    @Column
//    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Column(name="user_phone_number")
//    @NotBlank(message = "The userPhone field can't be blank")
    private String userPhone;

    @Column(nullable = false)
    private Boolean active;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}
