package com.ladwa.aditya.twitone;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.ladwa.aditya.twitone.data.local.TwitterContract;
import com.ladwa.aditya.twitone.data.local.TwitterDbHelper;
import com.ladwa.aditya.twitone.data.local.models.User;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOContentResolverDeleteResolver;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOContentResolverGetResolver;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOContentResolverPutResolver;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOSQLiteDeleteResolver;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOSQLiteGetResolver;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOSQLitePutResolver;
import com.pushtorefresh.storio.contentresolver.ContentResolverTypeMapping;
import com.pushtorefresh.storio.contentresolver.StorIOContentResolver;
import com.pushtorefresh.storio.contentresolver.impl.DefaultStorIOContentResolver;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.Query;

import timber.log.Timber;

/**
 * A test to check the working of a Database and Content Provider Provider
 * Created by Aditya on 07-Jul-16.
 */
public class DataTest extends AndroidTestCase {

    private TwitterDbHelper mTwitterDbHelper;
    private SQLiteDatabase db;
    private StorIOSQLite mStorIOSQLite;
    private User user;
    private StorIOContentResolver mStorIOContentResolver;


    public DataTest() {


    }

    public void initUser() {
        user = new User();
        user.setId((long) 99999);
        user.setName("Aditya");
        user.setScreenName("ladwa");
        user.setProfileUrl("profile_url");
        user.setBannerUrl("banner.url");
    }

    public void initDatabase() {
        mTwitterDbHelper = new TwitterDbHelper(mContext);
        mStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(mTwitterDbHelper)
                .addTypeMapping(User.class, SQLiteTypeMapping.<User>builder()
                        .putResolver(new UserStorIOSQLitePutResolver())
                        .getResolver(new UserStorIOSQLiteGetResolver())
                        .deleteResolver(new UserStorIOSQLiteDeleteResolver())
                        .build()
                )
                .build();
    }

    public void initContentResolver() {
        mStorIOContentResolver = DefaultStorIOContentResolver.builder()
                .contentResolver(mContext.getContentResolver())
                .addTypeMapping(User.class, ContentResolverTypeMapping.<User>builder()
                        .putResolver(new UserStorIOContentResolverPutResolver())
                        .getResolver(new UserStorIOContentResolverGetResolver())
                        .deleteResolver(new UserStorIOContentResolverDeleteResolver())
                        .build())
                .build();
    }

    public void testCreateDb() throws Throwable {


        mContext.deleteDatabase(TwitterDbHelper.DATABASE_NAME);
        SQLiteDatabase database = new TwitterDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, database.isOpen());
        database.close();
        Timber.d("Testing testCreateDb()");
    }


    public void testDbinsert() throws Throwable {
        initUser();
        initDatabase();


        PutResult putResult = mStorIOSQLite.put().object(user).prepare().executeAsBlocking();
        assertTrue(putResult.insertedId() != -1);

    }

    public void testReadDb() throws Throwable {
        initUser();
        initDatabase();

        User userResult = mStorIOSQLite.get()
                .object(User.class)
                .withQuery(Query.builder()
                        .table(TwitterContract.User.TABLE_NAME)
                        .where(TwitterContract.User.COLUMN_ID + " = ? ")
                        .whereArgs(user.getId())
                        .build()
                )
                .prepare()
                .executeAsBlocking();

        assertEquals(user.getName(), userResult.getName());

    }

    public void testInsertProvider() throws Throwable {
        initUser();
        initContentResolver();

        com.pushtorefresh.storio.contentresolver.operations.put.PutResult result = mStorIOContentResolver.put().object(user).prepare().executeAsBlocking();
        Timber.d(result.numberOfRowsUpdated().toString());

        assertTrue(result.wasUpdated());

    }

    public void testReadProvider() throws Throwable {
        initUser();
        initContentResolver();

        User userResult = mStorIOContentResolver.get()
                .object(User.class)
                .withQuery(
                        com.pushtorefresh.storio.contentresolver.queries.Query.builder()
                                .uri(TwitterContract.User.CONTENT_URI)
                                .where(TwitterContract.User.COLUMN_ID + " = ? ")
                                .whereArgs(this.user.getId()).build()
                ).prepare().executeAsBlocking();

        assertEquals(user.getName(), userResult.getName());
    }

}
