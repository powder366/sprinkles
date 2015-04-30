package se.emilsjolander.sprinkles;

import net.sqlcipher.database.SQLiteDatabase;

public abstract class Migration {

    final void execute(SQLiteDatabase db) {
        onPreMigrate();
        doMigration(db);
        onPostMigrate();

    }

    protected void onPreMigrate(){}
    protected abstract void doMigration(SQLiteDatabase db);
    protected void onPostMigrate(){}

}
