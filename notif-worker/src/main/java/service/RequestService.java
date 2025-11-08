package service;

import com.mongodb.client.model.Filters;
import helpers.AppContext;
import org.bson.types.ObjectId;
import org.nitk.common.dto.ResourceRequestDTO;
import org.nitk.common.mongo.MongoCollections;

public class RequestService {
    public ResourceRequestDTO getRequest(String requestId) {
        AppContext ctx = AppContext.getInstance();
        MongoCollections mongoCollections = ctx.getMongoCollections();
        return mongoCollections.getRequestCollection().find(
                Filters.eq("_id", new ObjectId(requestId))
        ).first();
    }
}

