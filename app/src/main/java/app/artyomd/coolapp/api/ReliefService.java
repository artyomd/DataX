package app.artyomd.coolapp.api;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import app.artyomd.coolapp.db.DisasterMetadata;
import com.google.gson.*;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReliefService {

    public static void getData(final ReliefCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        final Request request = new Request.Builder().url("https://api.reliefweb.int/v1/disasters?appname=apidoc&sort[]=date:desc&fields[include][]=url&fields[include][]=name&fields[include][]=description&fields[include][]=primary_country").get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    JsonParser parser = new JsonParser();
                    JsonElement object = parser.parse(response.body().string());
                    JsonArray data = object.getAsJsonObject().get("data").getAsJsonArray();
                    List<DisasterMetadata> dataSet = new ArrayList<>();
                    for(JsonElement element:data){
                        JsonObject fields = element.getAsJsonObject().get("fields").getAsJsonObject();
                        DisasterMetadata item = new DisasterMetadata();
                        String name = fields.get("name").getAsString();
                        String description = fields.get("description").getAsString();
                        JsonObject location = fields.get("primary_country").getAsJsonObject().get("location").getAsJsonObject();
                        Double longitude = location.get("lon").getAsDouble();
                        Double latitude = location.get("lat").getAsDouble();

                        item.setTitle(name);
                        item.setComment(description);
                        item.setLatitude(latitude);
                        item.setLongitude(longitude);
                        item.setTag(DisasterMetadata.TAG_NATURAL);
                        dataSet.add(item);
                    }
                    callback.onData(dataSet);
                }
            }
        });
    }


    public interface ReliefCallback{
        void onData(List<DisasterMetadata> data);
    }

    public static Bitmap createResizedScaledBitmap(Bitmap old, int width, int height, @Nullable Bitmap.Config config) {
        if (config == null) {
            config = old.getConfig();
        }
        Bitmap newBitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        Rect sourceRect = new Rect(0, 0, old.getWidth(), old.getHeight());
        Rect destRect = new Rect(0, 0, width, height);
        canvas.drawBitmap(old, sourceRect, destRect, paint);
        return newBitmap;
    }

}
