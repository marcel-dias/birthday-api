package com.marceldias.birthday.user;

import com.marceldias.birthday.exceptions.NotValidException;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.Objects;

@Table("users")
public class User {
    @Id
    private String name;
    @Column("date_of_birth")
    private LocalDate dateOfBirth;

    public User() {};
    public User(String name, LocalDate dateOfBirth) {
//        Objects.requireNonNull(name);
//        Objects.requireNonNull(dateOfBirth);
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void validate() {
        isNameOnlyLetter();
        isDateOfBirthBeforeThanToday();
    }

    public void isNameOnlyLetter() {
        if (! name.matches("^[a-zA-Z]+\\.?")) {
            throw new NotValidException("Username must contain only letters");
        }
    }

    public void isDateOfBirthBeforeThanToday() {
        if (dateOfBirth.isAfter(LocalDate.now().minusDays(1))) {
            throw new NotValidException(dateOfBirth+" must be a date before the today date");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!name.equals(user.name)) return false;
        return Objects.equals(dateOfBirth, user.dateOfBirth);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("name='").append(name).append('\'');
        sb.append(", dateOfBirth=").append(dateOfBirth);
        sb.append('}');
        return sb.toString();
    }
}
