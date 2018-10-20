package app.artyomd.coolapp.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import app.artyomd.coolapp.CommonConstants;
import app.artyomd.coolapp.R;
import app.artyomd.coolapp.db.DB;
import app.artyomd.coolapp.db.DisasterMetadata;

public class ShareFragment extends Fragment {

    private String imagePath;
    private ImageView imageView;
    private DB db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater.inflate(R.layout.fragment_share, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        db = DB.getInstance();
        imageView = view.findViewById(R.id.share_image);
        imagePath = args.getString(CommonConstants.EXTRA_IMAGE_PATH);
        Bitmap image = BitmapFactory.decodeFile(imagePath);
        if (imagePath != null) {
        	imageView.setImageBitmap(image);
		}
		DisasterMetadata metadata = new DisasterMetadata();
        metadata.setImage(image);
        metadata.setLatitude(args.getFloat(CommonConstants.EXTRA_IMAGE_LATITUDE));
        metadata.setLongitude(args.getFloat(CommonConstants.EXTRA_IMAGE_LONGITUDE));
        metadata.setTags(args.getStringArrayList(CommonConstants.EXTRA_IMAGE_TAGS));
        db.uploadDisaster(metadata);
        super.onViewCreated(view, savedInstanceState);
    }
}
