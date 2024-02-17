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
package it.palex.demo.entity.enumTypes;

public enum Gender {
    MALE,
    FEMALE,
    OTHER;

    public static boolean isValid(String value) {
        if(value==null) {
            return false;
        }
        try {
            Gender.valueOf(value);
            //if no IllegalArgumentException exception is thrown
            return true;
        }catch(IllegalArgumentException e) {
            return false;
        }
    }
}
