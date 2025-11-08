package org.nitk.authservice.service;

import org.bson.Document;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.nitk.authservice.dto.UserDTO;
import org.nitk.authservice.dto.UserRole;
import org.springframework.stereotype.Service;
import org.nitk.common.mongo.MongoCollections;
import org.nitk.common.dto.LocationDTO;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Service
public class AuthService {

    private final DSLContext dsl;
    private final MongoCollections mongoCollections;

    public AuthService(DSLContext dsl, MongoCollections mongoCollections) {
        this.dsl = dsl;
        this.mongoCollections = mongoCollections;
    }

    // Return generated user id instead of message
    public Long signup(UserDTO user) {
        Record inserted = dsl.insertInto(table("users"))
                .columns(field("name"), field("email"), field("phone"), field("password"), field("role"))
                .values(
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getPassword(),
                        // Cast string to Postgres enum role
                        DSL.field("?::role", String.class, user.getRole().name())
                )
                .returning(field("id", Long.class))
                .fetchOne();

        Long userId = inserted != null ? inserted.get(field("id", Long.class)) : null;

        if (userId != null && user.getLocation() != null) {
            Document doc = new Document()
                    .append("userId", userId)
                    .append("email", user.getEmail())
                    .append("phone", user.getPhone())
                    .append("role", user.getRole().name())
                    .append("location", new Document()
                            .append("type", user.getLocation().getType())
                            .append("coordinates", user.getLocation().getCoordinates())
                    );

            mongoCollections.getUserCollection().insertOne(doc);
        }

        return userId;
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

    public String findUserRoleById(Long userId) {
        if (userId == null) return null;

        Record rec = dsl.select(field("role"))
                .from(table("users"))
                .where(field("id", Long.class).eq(userId))
                .orderBy(field("id").desc())
                .limit(1)
                .fetchOne();

        if (rec == null) return null;
        Object roleObj = rec.get(0);
        return roleObj != null ? roleObj.toString() : null;
    }

    // Build a full UserDTO by id combining SQL (name/email/phone/role) and Mongo (location)
    public UserDTO getUserById(Long userId) {
        if (userId == null) return null;

        Record rec = dsl.select(field("name"), field("email"), field("phone"), field("role"))
                .from(table("users"))
                .where(field("id", Long.class).eq(userId))
                .limit(1)
                .fetchOne();
        if (rec == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(userId);
        dto.setName((String) rec.get("name"));
        dto.setEmail((String) rec.get("email"));
        dto.setPhone((String) rec.get("phone"));
        Object roleObj = rec.get("role");
        if (roleObj != null) {
            dto.setRole(UserRole.valueOf(roleObj.toString()));
        }

        Document mongoUser = mongoCollections.getUserCollection().find(new Document("userId", userId)).first();
        if (mongoUser != null) {
            Object locObj = mongoUser.get("location");
            if (locObj instanceof Document) {
                Document locDoc = (Document) locObj;
                LocationDTO locationDTO = new LocationDTO();
                locationDTO.setType(locDoc.getString("type"));
                //noinspection unchecked
                locationDTO.setCoordinates((java.util.List<Double>) locDoc.get("coordinates"));
                dto.setLocation(locationDTO);
            }
        }

        return dto;
    }
}
