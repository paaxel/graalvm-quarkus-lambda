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
import it.palex.demo.services.VersionService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/version")
public class VersionResource implements GenericResource {

    @Inject
    VersionService versionService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequestLogging
    @AddResponseVersion
    @PermitAll
    public RestResponse<GenericResponse<StringDTO>> version() {
        GenericResponse<StringDTO> response = versionService.appVersion();

        return this.buildGenericResponse(response);
    }

}
