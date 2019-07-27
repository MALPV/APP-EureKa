package com.apps.eureka.eurekapp;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Clase para la persistencia de datos (Sin internet)
 */
public class MyFirebaseApp extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
