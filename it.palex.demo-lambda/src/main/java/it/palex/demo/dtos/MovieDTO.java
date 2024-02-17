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
package it.palex.demo.dtos;

import java.io.Serializable;
import java.util.List;

public class MovieDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private Integer minAge;
    private List<String> genre;
    private List<MovieActorDTO> actors;
    private Boolean hasCover;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public List<MovieActorDTO> getActors() {
        return actors;
    }

    public void setActors(List<MovieActorDTO> actors) {
        this.actors = actors;
    }

    public Boolean getHasCover() {
        return hasCover;
    }

    public void setHasCover(Boolean hasCover) {
        this.hasCover = hasCover;
    }
}
