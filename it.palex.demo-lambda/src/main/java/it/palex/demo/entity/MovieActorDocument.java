/**
 * Copyright (c) Alessandro Pagliaro. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.palex.demo.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;
import it.palex.demo.entity.enumTypes.Gender;
import it.palex.demo.entity.generic.DatabaseCheckableEntity;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.io.Serializable;

@RegisterForReflection
@DynamoDbBean
public class MovieActorDocument implements Serializable, DatabaseCheckableEntity {

    private static final long serialVersionUID = 1L;

    private static final int MAX_NAME_LENGTH = 255;

    private String name;


    private static final int MAX_ROLE_LENGTH = 255;

    private String role;

    private String gender;


    public MovieActorDocument() {
    }

    @Override
    public boolean canBeInsertedInDatabase() {
        boolean isValid = isValidName(this.name) && isValidRole(this.role) && isValidGender(this.gender);

        return isValid;
    }

    @DynamoDbAttribute("Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDbAttribute("Role")
    public String getRole() {
        return role;
    }

    private static boolean isValidName(String name) {
        if(name==null){
            return false;
        }
        if(name.length()>MovieActorDocument.MAX_NAME_LENGTH){
            return false;
        }
        return true;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private static boolean isValidRole(String role) {
        if(role==null){
            return false;
        }
        if(role.length()>MovieActorDocument.MAX_ROLE_LENGTH){
            return false;
        }
        return true;
    }

    @DynamoDbAttribute("Gender")
    public String getGender() {
        return gender;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }

    private static boolean isValidGender(String gender) {
        if(gender==null){
            return true;
        }

        return Gender.isValid(gender);
    }

}
