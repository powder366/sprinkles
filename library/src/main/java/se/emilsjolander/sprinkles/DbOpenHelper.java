package se.emilsjolander.sprinkles;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;

class DbOpenHelper extends SQLiteOpenHelper {

    private int baseVersion;

    public DbOpenHelper(Context context, String databaseName, int baseVersion) {
        super(context, databaseName, null, Sprinkles.sInstance.mMigrations.size() + baseVersion);
        this.baseVersion = baseVersion;

        SQLiteDatabase.loadLibs(context);
        File databaseFile = context.getDatabasePath(Sprinkles.sInstance.databaseName);
        if(databaseFile.exists()) {
            SQLiteDatabase.openOrCreateDatabase(databaseFile, Sprinkles.sInstance.pwd, null);
        } else {
            databaseFile.mkdirs();
            databaseFile.delete();
            SQLiteDatabase.openOrCreateDatabase(databaseFile, Sprinkles.sInstance.pwd, null);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        executeMigrations(db, baseVersion, Sprinkles.sInstance.mMigrations.size() + baseVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        executeMigrations(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    private void executeMigrations(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; i++) {
            Sprinkles.sInstance.mMigrations.get(i).execute(db);
        }
    }
}
