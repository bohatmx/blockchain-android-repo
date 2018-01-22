package com.aftarobot.mlibrary.data;

/**
 * Created by aubreymalabie on 1/12/18.
 */

public abstract class Person {


    private String $class;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String idNumber;
    private String address;
    private String cellPhone;
    public String getClassz() { return this.$class; }

    public String getFullName() {
        return firstName.concat(" ").concat(lastName);
    }
    public void setClass(String $class) { this.$class = $class; }

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return this.firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return this.middleName; }

    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return this.lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getIdNumber() { return this.idNumber; }

    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }

    public String getAddress() { return this.address; }

    public void setAddress(String address) { this.address = address; }

    public String getCellPhone() { return this.cellPhone; }

    public void setCellPhone(String cellPhone) { this.cellPhone = cellPhone; }
}
