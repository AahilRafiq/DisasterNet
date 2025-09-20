package org.nitk.authservice.service;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;
import org.nitk.authservice.dto.UserDTO;
import org.springframework.stereotype.Service;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Service
public class AuthService {

    private final DSLContext dsl;

    public AuthService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public String signup(UserDTO user) {
        dsl.insertInto(table("users"))
                .columns(field("name"), field("email"), field("phone"), field("password"), field("role"))
                .values(
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getPassword(),
                        // Cast string to Postgres enum user_role
                        DSL.field("?::user_role", String.class, user.getRole().name())
                )
                .execute();
        return "Entry created";
    }

    // Checks if a user exists using email + phone (simple check)
    public String signin(UserDTO user) {
        Record1<Integer> result = dsl.select(DSL.count())
                .from(table("users"))
                .where(field("email").eq(user.getEmail()))
                .and(field("password").eq(user.getPassword()))
                .fetchOne();

        int count = result != null && result.value1() != null ? result.value1() : 0;
        return count > 0 ? "Sign-in successful" : "Sign-in failed";
    }
}

