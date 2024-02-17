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
package it.palex.demo.dtos.paging;


import java.util.List;

public class PageCustomDTO<T> {
    private long totalElements = 0;
    public PageableCustomDTO pageable = null;
    public int size = 0;
    public List<T> content = null;

    public PageCustomDTO(List<T> content, int pageSize, int pageNumber, long totalElements){
        this.content = content;
        this.totalElements = totalElements;
        this.size = pageSize;

        this.pageable = new PageableCustomDTO(pageNumber, this.size);
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return this.getSize() == 0 ? 1 : (int)Math.ceil((double)this.totalElements / (double)this.getSize());
    }
    private int getPageNumber(){
        return this.pageable==null ? 0:this.pageable.getPage();
    }
    public boolean isLast() {
        return this.getPageNumber() + 1 >= this.getTotalPages();
    }

    public boolean isFirst() {
        return  this.getPageNumber() == 0;
    }

    public PageableCustomDTO getPageable() {
        return pageable;
    }

    public int getNumberOfElements() {
        return this.content.size();
    }

    public int getSize() {
        return size;
    }

    public List<T> getContent() {
        return content;
    }
}
