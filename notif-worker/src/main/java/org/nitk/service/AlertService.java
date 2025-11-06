package org.nitk.service;

import com.mongodb.client.model.Filters;
import org.nitk.helpers.AppContext;
import org.bson.types.ObjectId;
import org.nitk.common.dto.AlertDTO;
import org.nitk.common.mongo.MongoCollections;

import java.util.HashMap;
import java.util.Map;

public class AlertService {
    private Map<String, Object> cache;

    public AlertService() {
        this.cache = new HashMap<>();
    }

    public AlertDTO getAlert(String alertId) {
        if (cache.containsKey(alertId)) {
            return (AlertDTO) cache.get(alertId);
        } else {
            AlertDTO alert = getAlertFresh(alertId);
            if (alert != null) {
                cache.put(alertId, alert);
            }
            return alert;
        }
    }

    private AlertDTO getAlertFresh(String alertId) {
        AppContext ctx = AppContext.getInstance();
        MongoCollections mongoCollections = ctx.getMongoCollections();
        return mongoCollections.getAlertCollection().find(
                Filters.eq("_id", new ObjectId(alertId))
        ).first();
    }
}
