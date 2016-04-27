package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {
    public static void main(String[] args) throws Exception {
        int version = 2;
        Schema schema = new Schema(version, "sxkeji.net.dailydiary");
        addArticle(schema);
        addToDo(schema);

        new DaoGenerator().generateAll(schema,
                "C:\\zsx\\DD\\codes\\GardenOfFeeling\\app\\src\\main\\java-gen");
    }

    /**
     * 创建ToDo表
     *
     * @param schema
     */
    private static void addToDo(Schema schema) {
        Entity toDo = schema.addEntity("Todo");

        toDo.addIdProperty();
        toDo.addDateProperty("date");
        toDo.addStringProperty("content").notNull();
        toDo.addIntProperty("color").notNull();
        toDo.addBooleanProperty("hasReminder").notNull();
        toDo.addBooleanProperty("showOnLockScreen");
    }

    /**
     * 创建文章表
     *
     * @param schema
     */
    public static void addArticle(Schema schema) {
        Entity entity = schema.addEntity("Article");

        entity.addIdProperty();
        entity.addStringProperty("date").notNull();
        entity.addStringProperty("address");
        entity.addStringProperty("weather");
        entity.addStringProperty("title").notNull();
        entity.addStringProperty("content").notNull();
        entity.addIntProperty("type");
        entity.addStringProperty("img_path");
        entity.setHasKeepSections(true);

    }
}

