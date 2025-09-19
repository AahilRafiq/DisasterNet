package org.nitk.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@SpringBootApplication
public class GatewayApplication {

    public static final String AUTH_URI = "http://httpbin.org:80"; // Replace with actual auth service URI
    public static final String NOTIF_URI = "https://reqres.in"; // Replace with actual notification service URI

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
    public RouterFunction<ServerResponse> notificationRouter() {
        return route()
                .route(path("/notifications/**"), http())
                .filter((req,next) -> {
                    if(req.headers().firstHeader("damn") == null) {
                        return ServerResponse.status(401).build();
                    }

                    // Any checks for auth can be done here

                    return next.handle(req);
                })
                .before(uri(NOTIF_URI))
                .build();
    }
}
