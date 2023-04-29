package com.bootcamptoprod.retry.service;

import com.bootcamptoprod.retry.entity.Movie;
import com.bootcamptoprod.retry.rest.client.MovieApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

@Service
public class MovieService {

    @Autowired
    private MovieApiClient movieApiClient;

    @Autowired
    private RetryTemplate retryTemplate;

    public Movie getMovieDetails(String movieId) {
        return retryTemplate.execute((RetryCallback<Movie, RestClientException>) context -> {
            Movie movie = null;
            try {
                movie = movieApiClient.getMovieDetails(movieId);
            } catch (HttpServerErrorException httpServerErrorException) {
                System.out.println("Received HTTP server error exception while fetching the movie details. Error Message: " + httpServerErrorException.getMessage());
                throw httpServerErrorException;
            } catch (HttpClientErrorException httpClientErrorException) {
                System.out.println("Received HTTP client error exception while fetching the movie details. Error Message: " + httpClientErrorException.getMessage());
                throw httpClientErrorException;
            } catch (ResourceAccessException resourceAccessException) {
                System.out.println("Received Resource Access exception while fetching the movie details.");
                throw resourceAccessException;
            }
            return movie;
        });
    }
}
