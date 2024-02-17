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
package it.palex.demo.services.generic;

import it.palex.demo.dtos.GenericResponse;
import it.palex.demo.dtos.StringDTO;
import it.palex.demo.dtos.paging.PageCustomDTO;
import jakarta.ws.rs.core.Response;

import java.util.List;

public interface GenericWebService {

    default <T> GenericResponse<PageCustomDTO<T>> buildPageableOkResponse(List<T> list, long totalCount, int pageSize, int pageNumber){
        GenericResponse<PageCustomDTO<T>> res = new GenericResponse<>();
        res.setData(new PageCustomDTO<>(list, pageSize, pageNumber, totalCount));
        res.setCode(Response.Status.OK.getStatusCode());

        return res;
    }

    default <T> GenericResponse<PageCustomDTO<T>> buildPageableOkResponse(List<T> list,
                                                                          long totalCount, int pageSize, int pageNumber, StandardReturnCode error){
        GenericResponse<PageCustomDTO<T>> res = new GenericResponse<>();
        res.setData(new PageCustomDTO<>(list, pageSize, pageNumber, totalCount));
        res.setCode(Response.Status.OK.getStatusCode());
        res.setMessage(error.getMess());
        res.setSubcode(error.getCode());

        return res;
    }


    default GenericResponse<StringDTO> buildStringOkResponse(String data){
        return new GenericResponse<StringDTO>(new StringDTO(data), Response.Status.OK.getStatusCode(), "Success");
    }

    default GenericResponse<StringDTO> buildStringOkResponse(String data, StandardReturnCode error) {
        if(error==null) {
            return this.buildStringOkResponse(data);
        }
        return this.buildStringOkResponse(data, error.getMess(), error.getCode());
    }

    default GenericResponse<StringDTO> buildStringOkResponse(String data, int subcode){
        return new GenericResponse<StringDTO>(new StringDTO(data), Response.Status.OK.getStatusCode(), "Success", subcode);
    }

    default GenericResponse<StringDTO> buildStringOkResponse(String data, String msg){
        return new GenericResponse<StringDTO>(new StringDTO(data), Response.Status.OK.getStatusCode(), msg);
    }

    default GenericResponse<StringDTO> buildStringOkResponse(String data, String msg, int subcode){
        return new GenericResponse<StringDTO>(new StringDTO(data), Response.Status.OK.getStatusCode(), msg, subcode);
    }

    default <T> GenericResponse<T> buildOkResponse(T data, int subcode){
        return new GenericResponse<T>(data, Response.Status.OK.getStatusCode(), "Success", subcode);
    }

    default <T> GenericResponse<T> buildOkResponse(T data, StandardReturnCode error) {
        if(error==null) {
            return this.buildOkResponse(data);
        }
        return this.buildOkResponse(data, error.getMess(), error.getCode());
    }

    default <T> GenericResponse<T> buildOkResponse(T data){
        return new GenericResponse<T>(data, Response.Status.OK.getStatusCode(), "Success");
    }

    default <T> GenericResponse<T> buildOkResponse(T data, String msg, int subcode){
        return new GenericResponse<T>(data, Response.Status.OK.getStatusCode(), msg, subcode);
    }

    default <T> GenericResponse<T> buildOkResponse(T data, String msg){
        return new GenericResponse<T>(data, Response.Status.OK.getStatusCode(), msg);
    }

    default <T> GenericResponse<T> buildBadDataResponse(){
        return new GenericResponse<T>(null, Response.Status.BAD_REQUEST.getStatusCode(), "Bad Data");
    }

    default <T> GenericResponse<T> buildBadDataResponse(String msg){
        return new GenericResponse<T>(null, Response.Status.BAD_REQUEST.getStatusCode(), msg);
    }

    default <T> GenericResponse<T> buildBadDataResponse(String msg, int subcode){
        return new GenericResponse<T>(null, Response.Status.BAD_REQUEST.getStatusCode(), msg, subcode);
    }

    default <T> GenericResponse<T> buildBadDataResponse(StandardReturnCode error) {
        if(error==null) {
            return this.buildBadDataResponse();
        }
        return this.buildBadDataResponse(error.getMess(), error.getCode());
    }

    default <T> GenericResponse<T> buildForbiddenResponse(){
        return new GenericResponse<T>(null, Response.Status.FORBIDDEN.getStatusCode(), "Forbidden");
    }

    default <T> GenericResponse<T> buildForbiddenResponse(String msg){
        return new GenericResponse<T>(null, Response.Status.FORBIDDEN.getStatusCode(), msg);
    }

    default <T> GenericResponse<T> buildForbiddenResponse(String msg, int subcode){
        return new GenericResponse<T>(null, Response.Status.FORBIDDEN.getStatusCode(), msg, subcode);
    }

    default <T> GenericResponse<T> buildForbiddenResponse(StandardReturnCode error) {
        if(error==null) {
            return this.buildForbiddenResponse();
        }
        return this.buildForbiddenResponse(error.getMess(), error.getCode());
    }


    default <T> GenericResponse<T> buildUnauthorizedResponse(){
        return new GenericResponse<T>(null, Response.Status.UNAUTHORIZED.getStatusCode(), "Unauthorized");
    }

    default <T> GenericResponse<T> buildUnauthorizedResponse(String msg){
        return new GenericResponse<T>(null, Response.Status.UNAUTHORIZED.getStatusCode(), msg);
    }

    default <T> GenericResponse<T> buildUnauthorizedResponse(String msg, int subcode){
        return new GenericResponse<T>(null, Response.Status.UNAUTHORIZED.getStatusCode(), msg, subcode);
    }

    default <T> GenericResponse<T> buildUnauthorizedResponse(StandardReturnCode error) {
        if(error==null) {
            return this.buildUnauthorizedResponse();
        }
        return this.buildUnauthorizedResponse(error.getMess(), error.getCode());
    }



    default <T> GenericResponse<T> buildNotFoundResponse(){
        return new GenericResponse<T>(null, Response.Status.NOT_FOUND.getStatusCode(), "Not found");
    }

    default <T> GenericResponse<T> buildNotFoundResponse(String msg){
        return new GenericResponse<T>(null, Response.Status.NOT_FOUND.getStatusCode(), msg);
    }

    default <T> GenericResponse<T> buildNotFoundResponse(String msg, int subcode){
        return new GenericResponse<T>(null, Response.Status.NOT_FOUND.getStatusCode(), msg, subcode);
    }

    default <T> GenericResponse<T> buildNotFoundResponse(StandardReturnCode error) {
        if(error==null) {
            return this.buildNotFoundResponse();
        }
        return this.buildNotFoundResponse(error.getMess(), error.getCode());
    }


    default <T> GenericResponse<T> buildNotAcceptableResponse(){
        return new GenericResponse<T>(null, Response.Status.NOT_ACCEPTABLE.getStatusCode(), "Not Acceptable");
    }

    default <T> GenericResponse<T> buildNotAcceptableResponse(String msg){
        return new GenericResponse<T>(null, Response.Status.NOT_ACCEPTABLE.getStatusCode(), msg);
    }

    default <T> GenericResponse<T> buildNotAcceptableResponse(String msg, int subcode){
        GenericResponse<T> res =  new GenericResponse<T>(null, Response.Status.NOT_ACCEPTABLE.getStatusCode(), msg, subcode);
        return res;
    }

    default <T> GenericResponse<T> buildNotAcceptableResponse(StandardReturnCode error) {
        if(error==null) {
            return this.buildNotAcceptableResponse();
        }
        return this.buildNotAcceptableResponse(error.getMess(), error.getCode());
    }


    default <T> GenericResponse<T> buildTooManyRequest(){
        return new GenericResponse<T>(null, Response.Status.TOO_MANY_REQUESTS.getStatusCode(), "Too Many Requests");
    }

    default <T> GenericResponse<T> buildTooManyRequest(String msg){
        return new GenericResponse<T>(null, Response.Status.TOO_MANY_REQUESTS.getStatusCode(), msg);
    }

    default <T> GenericResponse<T> buildTooManyRequest(String msg, int subcode){
        return new GenericResponse<T>(null, Response.Status.TOO_MANY_REQUESTS.getStatusCode(), msg, subcode);
    }

    default <T> GenericResponse<T> buildTooManyRequest(StandardReturnCode error) {
        if(error==null) {
            return this.buildTooManyRequest();
        }
        return this.buildTooManyRequest(error.getMess(), error.getCode());
    }


    default <T> GenericResponse<T> buildConflictEntity(){
        return new GenericResponse<T>(null, Response.Status.CONFLICT.getStatusCode(), "Entities are conflicting");
    }

    default <T> GenericResponse<T> buildConflictEntity(String msg){
        return new GenericResponse<T>(null, Response.Status.CONFLICT.getStatusCode(), msg);
    }

    default <T> GenericResponse<T> buildConflictEntity(StandardReturnCode error) {
        if(error==null) {
            return this.buildConflictEntity();
        }
        return this.buildConflictEntity( error.getMess(), error.getCode());
    }

    default <T> GenericResponse<T> buildConflictEntity(String msg, int subcode){
        return new GenericResponse<T>(null, Response.Status.CONFLICT.getStatusCode(), msg, subcode);
    }



    default <T> GenericResponse<T> buildInternalServerError(){
        return new GenericResponse<T>(null, Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Internal Server Error");
    }

    default <T> GenericResponse<T> buildInternalServerError(String msg){
        return new GenericResponse<T>(null, Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), msg);
    }

    default <T> GenericResponse<T> buildInternalServerError(String msg, int subcode){
        return new GenericResponse<T>(null, Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), msg, subcode);
    }

    default <T> GenericResponse<T> buildInternalServerError(StandardReturnCode error) {
        if(error==null) {
            return this.buildInternalServerError();
        }
        return this.buildInternalServerError(error.getMess(), error.getCode());
    }

}
