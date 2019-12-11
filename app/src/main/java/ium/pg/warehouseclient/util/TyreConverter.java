package ium.pg.warehouseclient.util;

import org.json.JSONException;
import org.json.JSONObject;

import ium.pg.warehouseclient.domain.Tyre;

public class TyreConverter {

    public Tyre convert(JSONObject json) {
        try {
            return Tyre.builder()
                    .id(json.getLong("id"))
                    .producer(json.getString("producer"))
                    .name(json.getString("name"))
                    .rimSize(json.getInt("rimSize"))
                    .quantity(json.getInt("quantity"))
                    .price(json.getDouble("price"))
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
