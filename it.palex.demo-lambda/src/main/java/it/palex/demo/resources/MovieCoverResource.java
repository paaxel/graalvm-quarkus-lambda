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
package it.palex.demo.resources;

import it.palex.demo.dtos.GenericResponse;
import it.palex.demo.dtos.StringDTO;
import it.palex.demo.interceptors.AddResponseVersion;
import it.palex.demo.interceptors.RequestLogging;
import it.palex.demo.resources.generic.GenericResource;
import it.palex.demo.services.MovieService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/movie-covers")
public class MovieCoverResource implements GenericResource {

    @Inject
    MovieService movieService;

    @POST
    @Path("build-download-url/{movie-id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequestLogging
    @AddResponseVersion
    public RestResponse<GenericResponse<StringDTO>> generateDownloadCoverUrl(@PathParam("movie-id") String movieId) {
        GenericResponse<StringDTO> response = movieService.generateDownloadCoverUrl(movieId);

        return this.buildGenericResponse(response);
    }

    @POST
    @Path("build-upload-url/{movie-id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequestLogging
    @AddResponseVersion
    public RestResponse<GenericResponse<StringDTO>> generateUploadCoverUrl(@PathParam("movie-id") String movieId, @QueryParam("extension") String extension) {
        GenericResponse<StringDTO> response = movieService.generateUploadCoverUrl(movieId, extension);

        return this.buildGenericResponse(response);
    }
}
