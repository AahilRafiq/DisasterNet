package org.nitk.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import jakarta.servlet.http.Cookie;
import org.nitk.gateway.security.JwtService;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration.class,
        org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration.class
})
public class GatewayApplication {

    // Resolve downstream service hosts from environment with localhost fallback
    private static String host(String env, String fallback) {
        String v = System.getenv(env);
        return (v == null || v.isBlank()) ? fallback : v;
    }

    public static final String AUTH_URI = "http://" + host("AUTH_HOST", "localhost") + ":5001";
    public static final String ALERT_URI = "http://" + host("ALERT_HOST", "localhost") + ":5002";
    public static final String REQUEST_URI = "http://" + host("REQUEST_HOST", "localhost") + ":5003";

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> authRouter() {
        return route()
                .route(path("/auth/**"), http())
                .before(uri(AUTH_URI))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> alertRouter(JwtService jwtService) {
        return route()
                .route(path("/alerts/**"), http())
                .filter((req, next) -> {
                    if (!isValidToken(req, jwtService)) {
                        return ServerResponse.status(401).build();
                    }
                    return next.handle(req);
                })
                .before(uri(ALERT_URI))
                .build();
    }

    static boolean isValidToken(ServerRequest req, JwtService jwtService) {
        Cookie jwtCookie = req.cookies().getFirst("jwt");
        String token = jwtCookie != null ? jwtCookie.getValue() : null;
        return jwtService.isValid(token);
    }

    @Bean
    public RouterFunction<ServerResponse> requestRouter(JwtService jwtService) {
        return route()
                .route(path("/requests/**"), http())
                .filter((req, next) -> {
                    if (!isValidToken(req, jwtService)) {
                        return ServerResponse.status(401).build();
                    }
                    return next.handle(req);
                })
                .before(uri(REQUEST_URI))
                .build();
    }
}
