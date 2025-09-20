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

    public Long findUserIdByEmail(String email) {
        Record1<Long> rec = dsl.select(field("id", Long.class))
                .from(table("users"))
                .where(field("email").eq(email))
                .orderBy(field("id").desc())
                .limit(1)
                .fetchOne();
        return rec != null ? rec.value1() : null;
    }

    public Long findUserIdByEmailAndPassword(String email, String password) {
        Record1<Long> rec = dsl.select(field("id", Long.class))
                .from(table("users"))
                .where(field("email").eq(email))
                .and(field("password").eq(password))
                .orderBy(field("id").desc())
                .limit(1)
                .fetchOne();
        return rec != null ? rec.get("id", Long.class) : null;
    }
}

