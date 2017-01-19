package self.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by user on 16/12/12.
 * 当每页显示用户能看到的时候，会调用onVisibleView方法。videofragment就是集成此类，也就是在此函数离实现了滑动到此页就会播放。
 */

public abstract class VisibleHitFragment extends Fragment {
    private boolean isVisible;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (getView() == null) {
            return;
        }

        onVisibleView(isVisibleToUser);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isVisible) {
            onVisibleView(isVisible);
        }
    }

    protected boolean isVisibleView() {
        return isVisible;
    }

    protected abstract void onVisibleView(boolean visible);
}
