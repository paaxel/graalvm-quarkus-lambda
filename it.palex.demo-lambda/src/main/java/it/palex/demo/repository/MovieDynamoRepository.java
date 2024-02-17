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
package it.palex.demo.repository;

import it.palex.demo.entity.Movie;
import it.palex.demo.repository.exceptions.DataCannotBeInsertedInDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MovieDynamoRepository implements MovieRepository {


    @ConfigProperty(name = "app.movie_table_name")
    String movieTableName;
    
    @Inject
    DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Override
    public Movie save(Movie movie) {
        if(movie==null){
            throw new NullPointerException();
        }
        if(!movie.canBeInsertedInDatabase()){
            throw new DataCannotBeInsertedInDatabase();
        }
        DynamoDbTable<Movie> table = dynamoDbEnhancedClient.table(this.movieTableName, TableSchema.fromBean(Movie.class));
        table.putItem((PutItemEnhancedRequest.Builder<Movie> requestBuilder) ->
                        requestBuilder.item(movie));
        table.putItem(movie);

        return movie;
    }

    @Override
    public Movie findByTitle(String movieTitle) {
        if(movieTitle==null){
            throw new NullPointerException();
        }

        DynamoDbTable<Movie> table = dynamoDbEnhancedClient.table(this.movieTableName, TableSchema.fromBean(Movie.class));

        Expression keyCondition = Expression.builder()
                .expression("Title = :movieTitle")
                .putExpressionValue(":movieTitle", AttributeValue.builder().s(movieTitle).build())
                .build();

        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(movieTitle).build());

        var resultsIterator = table.index(Movie.TITLE_GLOBAL_SECONDARY_INDEX_NAME)
                            .query(r -> r.queryConditional(queryConditional))
                            .iterator();

        if (resultsIterator.hasNext()) {
            Page<Movie> firstPage = resultsIterator.next();
            var resultItems = firstPage.items();

            if(resultItems.size()==0){
                return null;
            }

            if(resultItems.size()>1){
                throw new RuntimeException("Found more items with same id");
            }
            if(resultsIterator.hasNext()){
                resultItems = resultsIterator.next().items();

                if(resultItems.size()!=0){
                    throw new RuntimeException("Found more items with same id");
                }
            }

            return resultItems.get(0);
        }

        return null;
    }

    @Override
    public Movie findByKey(String id) {
        if(id==null){
            throw new NullPointerException();
        }

        // Create a DynamoDbTable object
        DynamoDbTable<Movie> table = dynamoDbEnhancedClient.table(this.movieTableName, TableSchema.fromBean(Movie.class));

        // Set up the query condition
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(id).build());

        PageIterable<Movie> resultPages =
                table.query(r -> r.queryConditional(queryConditional));

        List<Movie> queryRes = resultPages.items().stream().collect(Collectors.toList());


        if(queryRes.size()==0){
            return null;
        }

        if(queryRes.size()>1){
            throw new RuntimeException("Found more items with same id");
        }

        return queryRes.get(0);
    }

    @Override
    public List<Movie> findAll() {
        DynamoDbTable<Movie> table = dynamoDbEnhancedClient.table(this.movieTableName, TableSchema.fromBean(Movie.class));

        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .consistentRead(false)
                .build();

        var response = table.scan(request).items();
        // Process the scan results
        List<Movie> queryRes = response.stream().collect(Collectors.toList());

        return queryRes;
    }

    public void delete(Movie movie) {
        if(movie==null){
            throw new NullPointerException();
        }

        DynamoDbTable<Movie> table = dynamoDbEnhancedClient.table(this.movieTableName, TableSchema.fromBean(Movie.class));

        if (movie == null) {
            throw new NullPointerException("Movie cannot be null");
        }

        // Execute the delete operation
        table.deleteItem(Key.builder().partitionValue(movie.getId().toString()).build());
    }
}
