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
import java.util.UUID;

public class GenericResponse<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String version = null;
	private T data = null;
	private int code = 0;
	private String message = null;
	private int subcode = 0;
	private String operationIdentifier;
	
	public GenericResponse(){
		super();
		this.operationIdentifier = UUID.randomUUID().toString();
	}

	/**
	 * 
	 * @param data
	 * @param code
	 * @param message
	 */
	public GenericResponse(T data, int code, String message) {
		this();
		this.data = data;
		this.code = code;
		this.message = message;
	}
	
	public GenericResponse(T data, int code, String message, int subcode) {
		this();
		this.data = data;
		this.code = code;
		this.message = message;
		this.subcode = subcode;
	}

	/**
	 * 
	 * @param version
	 * @param data
	 * @param code
	 * @param message
	 */
	public GenericResponse(String version, T data, int code, String message) {
		this();
		this.version = version;
		this.data = data;
		this.code = code;
		this.message = message;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getSubcode() {
		return subcode;
	}
	
	public void setSubcode(int subcode) {
		this.subcode = subcode;
	}

	/**
	 * @return the operationIdentifier
	 */
	public String getOperationIdentifier() {
		return operationIdentifier;
	}
	
	public void setOperationIdentifier(String uuid) {
		this.operationIdentifier = uuid;
	}

	
	@Override
	public String toString() {
		return "GenericResponse [version=" + version + ", data=" + data + ", code=" + code + ", message=" + message
				+ ", subcode=" + subcode + ", operationIdentifier=" + operationIdentifier + "]";
	}

}

