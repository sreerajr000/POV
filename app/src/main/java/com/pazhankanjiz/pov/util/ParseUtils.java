package com.pazhankanjiz.pov.util;

import com.parse.ParseObject;
import com.pazhankanjiz.pov.model.HomeCardModel;

import static com.pazhankanjiz.pov.constant.DatabaseConstants.BACKGROUND;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.CONTENT;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.DISLIKE;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.FONT;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.HASHTAG;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.LIKE;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.POSTED_BY;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.RATING;

public class ParseUtils {

    public static HomeCardModel cardModelFromParseObject(ParseObject object) {
        HomeCardModel model = new HomeCardModel(object.getString(CONTENT), object.getObjectId(),
                object.getString(POSTED_BY), object.getInt(RATING), object.getInt(LIKE),
                object.getInt(DISLIKE), object.getInt(BACKGROUND), object.getInt(FONT), object.getList(HASHTAG));
        return model;
    }
}
