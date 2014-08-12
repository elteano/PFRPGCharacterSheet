package org.elteano.charactersheet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The beginnings of a helper for storing PlayerCharacters in a database rather
 * than a messy assortment of XML files. Further work is currently waiting on
 * some manner of guarantee that no additional fields will need to be saved.
 */
public class CharacterOpenHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "characters";
	public static final String KEY_CNUM = "CharacterNumber";
	public static final String KEY_CNAME = "CharacterName";

	// TODO finish this stuff
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ DATABASE_NAME + " ( ";

	public CharacterOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}

}
