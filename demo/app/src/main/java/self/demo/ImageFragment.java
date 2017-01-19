package self.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by user on 16/12/2.
 * 主要用于显示图片的fragment。
 */

public class ImageFragment extends Fragment {
    private int redius;

    public static Fragment newInstance(int redius) {
        Fragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt("redius", redius);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redius = getArguments().getInt("redius");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.widget_preview);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                getResources(), bitmap);
        roundedBitmapDrawable.setCornerRadius(redius);
        ImageView imageView = new ImageView(inflater.getContext());
        imageView.setImageDrawable(roundedBitmapDrawable);
        imageView.setBackgroundResource(R.drawable.round_corner_background);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,
                getResources().getDisplayMetrics());
        imageView.setPadding(padding, padding, padding, padding);
        return imageView;
    }
}
