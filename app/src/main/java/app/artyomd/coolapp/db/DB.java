package app.artyomd.coolapp.db;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class DB {

    private static DB db;
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference reference;

    private DB() {

    }

    public static DB getInstance() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("disasters");
        return db;
    }

    public void uploadDisaster(DisasterMetadata metadata) {
        reference.child(UUID.randomUUID().toString()).setValue(metadata);
    }
}
