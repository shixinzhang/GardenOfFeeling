package sxkeji.net.dailydiary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import sxkeji.net.dailydiary.Todo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TODO.
*/
public class TodoDao extends AbstractDao<Todo, Long> {

    public static final String TABLENAME = "TODO";

    /**
     * Properties of entity Todo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Date = new Property(1, java.util.Date.class, "date", false, "DATE");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property Color = new Property(3, int.class, "color", false, "COLOR");
        public final static Property HasReminder = new Property(4, boolean.class, "hasReminder", false, "HAS_REMINDER");
        public final static Property ShowOnLockScreen = new Property(5, Boolean.class, "showOnLockScreen", false, "SHOW_ON_LOCK_SCREEN");
    };


    public TodoDao(DaoConfig config) {
        super(config);
    }
    
    public TodoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TODO' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'DATE' INTEGER," + // 1: date
                "'CONTENT' TEXT NOT NULL ," + // 2: content
                "'COLOR' INTEGER NOT NULL ," + // 3: color
                "'HAS_REMINDER' INTEGER NOT NULL ," + // 4: hasReminder
                "'SHOW_ON_LOCK_SCREEN' INTEGER);"); // 5: showOnLockScreen
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TODO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Todo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(2, date.getTime());
        }
        stmt.bindString(3, entity.getContent());
        stmt.bindLong(4, entity.getColor());
        stmt.bindLong(5, entity.getHasReminder() ? 1l: 0l);
 
        Boolean showOnLockScreen = entity.getShowOnLockScreen();
        if (showOnLockScreen != null) {
            stmt.bindLong(6, showOnLockScreen ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Todo readEntity(Cursor cursor, int offset) {
        Todo entity = new Todo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)), // date
            cursor.getString(offset + 2), // content
            cursor.getInt(offset + 3), // color
            cursor.getShort(offset + 4) != 0, // hasReminder
            cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0 // showOnLockScreen
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Todo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDate(cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)));
        entity.setContent(cursor.getString(offset + 2));
        entity.setColor(cursor.getInt(offset + 3));
        entity.setHasReminder(cursor.getShort(offset + 4) != 0);
        entity.setShowOnLockScreen(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Todo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Todo entity) {
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
