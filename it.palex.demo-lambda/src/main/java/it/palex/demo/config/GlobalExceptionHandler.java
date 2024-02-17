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
package it.palex.demo.config;

import it.palex.demo.dtos.GenericResponse;
import it.palex.demo.services.VersionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Inject
    VersionService versionService;

    @Override
    public Response toResponse(Throwable exception) {
        GenericResponse<String> res = new GenericResponse<String>();
        res.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        res.setMessage(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
        res.setVersion(versionService.getVersionStr());
        res.setSubcode(-1);
        res.setOperationIdentifier(null); //cannot identify the operation id

        exception.printStackTrace();

        // Return an HTTP response with an appropriate status code and message
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(res)
                .build();
    }


}