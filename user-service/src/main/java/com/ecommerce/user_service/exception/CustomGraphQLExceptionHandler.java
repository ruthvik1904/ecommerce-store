package com.ecommerce.user_service.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomGraphQLExceptionHandler implements DataFetcherExceptionResolver {

    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable exception, DataFetchingEnvironment env) {
        if (exception instanceof MethodArgumentNotValidException) {
            // Handle validation errors
            return handleMethodArgumentNotValidException((MethodArgumentNotValidException) exception, env);
        } else if (exception instanceof UserNotFoundException) {
            // Handle user not found errors
            return handleUserNotFoundException((UserNotFoundException) exception, env);
        } else if (exception instanceof Exception) {
            // Handle generic exceptions
            return handleGenericException((Exception) exception, env);
        }
        return Mono.empty(); // Let other exceptions be handled by the default resolver
    }

    private Mono<List<GraphQLError>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, DataFetchingEnvironment env) {
        Map<String, Object> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });

        GraphQLError error = GraphqlErrorBuilder.newError()
                .message("Validation error")
                .errorType(ErrorType.BAD_REQUEST)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .extensions(errorMap) // Add validation errors to the extensions field
                .build();

        return Mono.just(List.of(error));
    }

    private Mono<List<GraphQLError>> handleUserNotFoundException(UserNotFoundException ex, DataFetchingEnvironment env) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("errorMessage", ex.getMessage());

        GraphQLError error = GraphqlErrorBuilder.newError()
                .message("User Not Found")
                .errorType(ErrorType.NOT_FOUND)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .extensions(errorMap) // Add error details to the extensions field
                .build();

        return Mono.just(List.of(error));
    }

    private Mono<List<GraphQLError>> handleGenericException(Exception ex, DataFetchingEnvironment env) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("errorMessage", ex.getMessage());

        GraphQLError error = GraphqlErrorBuilder.newError()
                .message("Generic error")
                .errorType(ErrorType.INTERNAL_ERROR)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .extensions(errorMap) // Add error details to the extensions field
                .build();

        return Mono.just(List.of(error));
    }
}