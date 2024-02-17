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
package it.palex.demo.interceptors;

import it.palex.demo.dtos.GenericResponse;
import it.palex.demo.services.VersionService;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.jboss.resteasy.reactive.RestResponse;

@AddResponseVersion
@Interceptor
public class AddResponseVersionInterceptor {

    @Inject
    VersionService versionService;

    @AroundInvoke
    public Object addVersionInResponse(InvocationContext context) throws Exception {
        // Invoke the target method
        Object result = context.proceed();

        // Check if the result is a Response object
        if (result instanceof RestResponse) {
            RestResponse<Object> response = (RestResponse) result;

            Object res = ((RestResponse<?>) response).getEntity();
            if(res!=null && res instanceof GenericResponse) {
                GenericResponse<?> park = ((GenericResponse<?>) res);

                if(park!=null) {
                    park.setVersion(versionService.getVersionStr());
                }
            }

            return response;
        }

        // Return the original result if it's not a Response object
        return result;
    }

}
