package sxkeji.net.dailydiary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import sxkeji.net.dailydiary.Article;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ARTICLE.
*/
public class ArticleDao extends AbstractDao<Article, Long> {

    public static final String TABLENAME = "ARTICLE";

    /**
     * Properties of entity Article.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Date = new Property(1, String.class, "date", false, "DATE");
        public final static Property Address = new Property(2, String.class, "address", false, "ADDRESS");
        public final static Property Weather = new Property(3, String.class, "weather", false, "WEATHER");
        public final static Property Title = new Property(4, String.class, "title", false, "TITLE");
        public final static Property Content = new Property(5, String.class, "content", false, "CONTENT");
        public final static Property Type = new Property(6, Integer.class, "type", false, "TYPE");
        public final static Property Img_path = new Property(7, String.class, "img_path", false, "IMG_PATH");
    };


    public ArticleDao(DaoConfig config) {
        super(config);
    }
    
    public ArticleDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ARTICLE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'DATE' TEXT NOT NULL ," + // 1: date
                "'ADDRESS' TEXT," + // 2: address
                "'WEATHER' TEXT," + // 3: weather
                "'TITLE' TEXT NOT NULL ," + // 4: title
                "'CONTENT' TEXT NOT NULL ," + // 5: content
                "'TYPE' INTEGER," + // 6: type
                "'IMG_PATH' TEXT);"); // 7: img_path
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ARTICLE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Article entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getDate());
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(3, address);
        }
 
        String weather = entity.getWeather();
        if (weather != null) {
            stmt.bindString(4, weather);
        }
        stmt.bindString(5, entity.getTitle());
        stmt.bindString(6, entity.getContent());
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(7, type);
        }
 
        String img_path = entity.getImg_path();
        if (img_path != null) {
            stmt.bindString(8, img_path);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Article readEntity(Cursor cursor, int offset) {
        Article entity = new Article( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // date
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // address
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // weather
            cursor.getString(offset + 4), // title
            cursor.getString(offset + 5), // content
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // type
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // img_path
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Article entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDate(cursor.getString(offset + 1));
        entity.setAddress(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setWeather(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTitle(cursor.getString(offset + 4));
        entity.setContent(cursor.getString(offset + 5));
        entity.setType(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setImg_path(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Article entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Article entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
