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
package it.palex.demo.dtos.mapper;

import it.palex.demo.dtos.MovieActorDTO;
import it.palex.demo.dtos.MovieDTO;
import it.palex.demo.entity.Movie;

import java.util.ArrayList;

public class MovieMapper {

    public static MovieDTO mapToDTO(Movie movie) {
        if(movie==null){
            return null;
        }
        MovieDTO res = new MovieDTO();
        if(movie.getId()!=null){
            res.setId(movie.getId().toString());
        }
        res.setTitle(movie.getTitle());
        res.setMinAge(movie.getMinAge());
        res.setGenre(movie.getGenre());

        if(movie.getCoverObjectPath()!=null){
            res.setHasCover(true);
        }else{
            res.setHasCover(false);
        }

        if(movie.getActors()!=null){
            ArrayList<MovieActorDTO> actors = new ArrayList<>(movie.getActors().size());

            for(var elem: movie.getActors()){
                if(elem!=null){
                    actors.add(MovieActorMapper.mapToDTO(elem));
                }else{
                    actors.add(null);
                }

            }

            res.setActors(actors);
        }

        return res;
    }




}
