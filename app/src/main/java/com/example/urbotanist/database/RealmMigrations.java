package com.example.urbotanist.database;

import java.util.Date;
// realm by MongoDB https://realm.io/
import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class RealmMigrations implements RealmMigration {

  @Override
  public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
    RealmSchema schema = realm.getSchema();

    if (oldVersion == 0) {
      schema.create("FavouritePlant")
          .addRealmObjectField("plant",schema.get("Plant"))
          .addField("favouriteTime", Date.class);
      oldVersion++;
    }

    if (oldVersion == 1) {
      schema.get("FavouritePlant")
          .addField("plantId", int.class)
          .addPrimaryKey("plantId");
      oldVersion++;
    }
  }
}
