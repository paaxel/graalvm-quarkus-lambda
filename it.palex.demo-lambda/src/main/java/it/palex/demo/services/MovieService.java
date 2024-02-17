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
package it.palex.demo.services;

import it.palex.demo.config.S3StorageManager;
import it.palex.demo.dtos.GenericResponse;
import it.palex.demo.dtos.MovieDTO;
import it.palex.demo.dtos.StringDTO;
import it.palex.demo.dtos.mapper.MovieMapper;
import it.palex.demo.entity.Movie;
import it.palex.demo.entity.MovieActorDocument;
import it.palex.demo.entity.enumTypes.Gender;
import it.palex.demo.repository.MovieDynamoRepository;
import it.palex.demo.services.generic.GenericWebService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.*;

@ApplicationScoped
public class MovieService implements GenericWebService {


    @Inject
    MovieDynamoRepository movieDynamoRepository;

    @Inject
    S3StorageManager s3StorageManager;

    @ConfigProperty(name = "app.storage_raw_movie_cover_folder")
    String movieRawCoverStorageFolder;

    @ConfigProperty(name = "app.cover_extensions_accepted")
    String coverAcceptedExtensions;

    private Set<String> acceptedCoverExt;

    @PostConstruct
    private void getCoverAcceptedExtension(){
        final var res = this.coverAcceptedExtensions.split(",");
        this.acceptedCoverExt = new HashSet<>();

        for(int i=0; i<res.length; i++){
            acceptedCoverExt.add(res[i]);
        }
    }

    public GenericResponse<MovieDTO> save(MovieDTO movie){
        if(movie==null){
            return this.buildBadDataResponse();
        }

        Movie toSave = new Movie();
        toSave.setTitle(movie.getTitle());
        toSave.setMinAge(movie.getMinAge());
        toSave.setGenre(movie.getGenre());

        if(movie.getActors()!=null){
            ArrayList<MovieActorDocument> actors = new ArrayList<>(movie.getActors().size());

            for(var elem: movie.getActors()){
                if(elem!=null){
                    MovieActorDocument movieActor = new MovieActorDocument();
                    movieActor.setName(elem.getName());
                    movieActor.setRole(elem.getRole());

                    if(elem.getGender()!=null){
                        if(Gender.isValid(elem.getGender())){
                            movieActor.setGender(elem.getGender());
                        }else{
                            return this.buildBadDataResponse("Invalid movie actor found (gender).");
                        }
                    }

                    actors.add(movieActor);
                }else{
                    return this.buildBadDataResponse("Null movie actor found");
                }
            }

            toSave.setActors(actors);
        }

        toSave.setId(UUID.randomUUID());

        if(!toSave.canBeInsertedInDatabase()){
            return this.buildBadDataResponse();
        }


        Movie movieExistent = this.movieDynamoRepository.findByTitle(movie.getTitle());

        if(movieExistent!=null){
            return this.buildConflictEntity("Already exists a film with this title");
        }

        toSave = this.movieDynamoRepository.save(toSave);

        var res = MovieMapper.mapToDTO(toSave);

        return this.buildOkResponse(res);
    }

    public GenericResponse<MovieDTO> findByKey(String id){
        if(id==null){
            return this.buildBadDataResponse();
        }

        var movie = this.movieDynamoRepository.findByKey(id);

        if(movie==null){
            return this.buildNotFoundResponse();
        }

        var res = MovieMapper.mapToDTO(movie);

        return this.buildOkResponse(res);
    }

    public GenericResponse<MovieDTO> findByTitle(String title){
        if(title==null){
            return this.buildBadDataResponse();
        }

        var movie = this.movieDynamoRepository.findByTitle(title);

        if(movie==null){
            return this.buildNotFoundResponse();
        }

        var res = MovieMapper.mapToDTO(movie);

        return this.buildOkResponse(res);
    }

    public GenericResponse<List<MovieDTO>> findAll() {
        List<Movie> result = this.movieDynamoRepository.findAll();
        var mappedMovies = new ArrayList<MovieDTO>(result.size());

        for(Movie movie: result){
            mappedMovies.add(MovieMapper.mapToDTO(movie));
        }

        return this.buildOkResponse(mappedMovies);
    }

    public GenericResponse<String> deleteByKey(String id) {
        if(id==null){
            return this.buildBadDataResponse();
        }

        var movie = this.movieDynamoRepository.findByKey(id);

        if(movie==null){
            return this.buildNotFoundResponse();
        }

        // concurrent delete do not generate any error so we can skip exception handling
        this.movieDynamoRepository.delete(movie);

        return this.buildOkResponse("Deleted");
    }

    public GenericResponse<StringDTO> generateDownloadCoverUrl(String movieId) {
        if(movieId==null){
            return this.buildBadDataResponse();
        }

        var movie = this.movieDynamoRepository.findByKey(movieId);

        if(movie==null){
            return this.buildNotFoundResponse();
        }

        if(movie.getCoverObjectPath()==null){
            return this.buildNotFoundResponse("Cover file not present, please be sure that movie hasCover before calling this method");
        }

        final String s3PresignedUrl = this.s3StorageManager.generateDownloadPresignedUrl(movie.getCoverObjectPath());

        return this.buildStringOkResponse(s3PresignedUrl);
    }

    public GenericResponse<StringDTO> generateUploadCoverUrl(String movieId, String extension) {
        if(movieId==null || extension==null){
            return this.buildBadDataResponse();
        }

        if(!this.acceptedCoverExt.contains(extension)){
            return this.buildBadDataResponse("Invalid extension");
        }

        var movie = this.movieDynamoRepository.findByKey(movieId);

        if(movie==null){
            return this.buildNotFoundResponse();
        }

        String filename = movie.getId()+extension;

        final String s3PresignedUrl = this.s3StorageManager.generateUploadPresignedUrl(this.movieRawCoverStorageFolder, filename);

        return this.buildStringOkResponse(s3PresignedUrl);
    }

}
