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
import it.palex.demo.entity.generic.DatabaseCheckableEntity;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@RegisterForReflection
@DynamoDbBean
public class Movie implements Serializable, DatabaseCheckableEntity {

    public static final String TITLE_GLOBAL_SECONDARY_INDEX_NAME = "MovieTitleIndex";

    private static final long serialVersionUID = 1L;

    private static final int MAX_TITLE_LENGTH = 255;

    private UUID id;
    private String title;

    private static final int MAX_AGE_VALUE = 21;

    private Integer minAge;

    public static final int MAX_GENRE_LENGTH = 50;
    private List<String> genre;
    private List<MovieActorDocument> actors;

    public static final int MAX_COVER_OBJECT_PATH_LENGTH = 1024;
    private String coverObjectPath;

    public Movie() {
    }

    @Override
    public boolean canBeInsertedInDatabase() {
        boolean isValid = isValidId(this.id) && isValidTitle(this.title)
                && isValidMinAge(this.minAge)
                && isValidGenre(this.genre)
                && isValidMovieActors(this.actors)
                && isValidCoverObjectPath(this.coverObjectPath);

        return isValid;
    }


    @DynamoDbAttribute("Id")
    @DynamoDbPartitionKey
    public UUID  getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    private static boolean isValidId(UUID id) {
        if(id==null){
            return false;
        }

        return true;
    }

    @DynamoDbAttribute("Title")
    @DynamoDbSecondaryPartitionKey(indexNames = {"MovieTitleIndex"})
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private static boolean isValidTitle(String title) {
        if(title==null){
            return false;
        }
        if(title.length()>Movie.MAX_TITLE_LENGTH){
            return false;
        }

        return true;
    }

    @DynamoDbAttribute("MinAge")
    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    private static boolean isValidMinAge(Integer minAge) {
        if(minAge==null){
            return true;
        }

        if(minAge<0 || minAge>Movie.MAX_AGE_VALUE){
            return false;
        }

        return true;
    }

    @DynamoDbAttribute("Genre")
    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    private static boolean isValidGenre(List<String> genres) {
        if(genres==null){
            return true;
        }
        for(String elem: genres){
            if(elem==null || elem.length() > Movie.MAX_GENRE_LENGTH){
                return false;
            }
        }

        return true;
    }

    @DynamoDbAttribute("Actors")
    public List<MovieActorDocument> getActors() {
        return actors;
    }

    public void setActors(List<MovieActorDocument> actors) {
        this.actors = actors;
    }

    private static boolean isValidMovieActors(List<MovieActorDocument> actors) {
        if(actors==null){
            return true;
        }

        for(MovieActorDocument elem: actors){
            if(elem==null || !elem.canBeInsertedInDatabase()){
                return false;
            }
        }

        return true;
    }

    @DynamoDbAttribute("CoverObjectPath")
    public String getCoverObjectPath() {
        return coverObjectPath;
    }

    public void setCoverObjectPath(String coverObjectPath) {
        this.coverObjectPath = coverObjectPath;
    }

    private boolean isValidCoverObjectPath(String coverFilePath) {
        if(coverFilePath==null){
            return true;
        }
        if(coverFilePath.length()>MAX_COVER_OBJECT_PATH_LENGTH){
            return false;
        }
        return true;
    }
}
