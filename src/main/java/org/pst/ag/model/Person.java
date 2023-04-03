package org.pst.ag.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Person {
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private Date dateOfBirth;
    private Long salary;

    public Person() {

    }
}
