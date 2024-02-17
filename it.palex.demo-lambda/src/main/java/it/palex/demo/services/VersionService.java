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

import it.palex.demo.dtos.GenericResponse;
import it.palex.demo.dtos.StringDTO;
import it.palex.demo.services.generic.GenericWebService;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class VersionService implements GenericWebService {

    @ConfigProperty(name = "app.version")
    String message;

    public GenericResponse<StringDTO> appVersion() {
        var res = new StringDTO(this.message);

        return this.buildOkResponse(res);
    }

    public String getVersionStr(){
        return message;
    }

}
