package app.artyomd.coolapp.api;

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
                    List<ReliefData> dataSet = new ArrayList<>();
                    for(JsonElement element:data){
                        JsonObject fields = element.getAsJsonObject().get("fields").getAsJsonObject();
                        ReliefData item = new ReliefData();
                        String name = fields.get("name").getAsString();
                        String description = fields.get("description").getAsString();
                        JsonObject location = fields.get("primary_country").getAsJsonObject().get("location").getAsJsonObject();
                        Double longitude = location.get("lon").getAsDouble();
                        Double latitude = location.get("lat").getAsDouble();

                        item.setName(name);
                        item.setDescription(description);
                        item.setLat(latitude);
                        item.setLon(longitude);
                        dataSet.add(item);
                    }
                    callback.onData(dataSet);
                }
            }
        });
    }


    public interface ReliefCallback{
        void onData(List<ReliefData> data);
    }

}
