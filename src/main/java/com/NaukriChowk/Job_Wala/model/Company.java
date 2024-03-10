package com.NaukriChowk.Job_Wala.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "company", uniqueConstraints = { @UniqueConstraint(columnNames = { "companyId" }) })
public class Company {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long companyId;



    @Column(unique = true)
    @NotNull
    private String companyName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "companyDetails")
    @JsonIgnore
    private Set<JobDetail> jobsOpening=new HashSet<>();
}
