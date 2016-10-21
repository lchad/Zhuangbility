package com.liuchad.zhuangbility.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.Mode;
import com.liuchad.zhuangbility.event.MultiPicSelectedEvent;
import com.liuchad.zhuangbility.event.SinglePicSelectedEvent;
import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.Arg;
import in.workarounds.bundler.annotations.RequireBundler;
import in.workarounds.bundler.annotations.Required;
import java.io.File;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;

@RequireBundler
public class MultiImageSelectorActivity extends AppCompatActivity
        implements MultiImageSelectorFragment.Callback {

    private ArrayList<String> resultList = new ArrayList<>();
    @Bind(R.id.back) ImageView mBack;

    public @Arg @Required() int selectMode;
    public @Arg @Required(false) int maxSelectCount = 10;
    public @Arg @Required() boolean showCamera;

    @SuppressWarnings("FieldCanBeLocal") private MenuItem mFinishMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector);
        Bundler.inject(this);
        ButterKnife.bind(this);


        getSupportFragmentManager().beginTransaction()
            .add(R.id.image_grid, Bundler.multiImageSelectorFragment(Mode.MODE_SINGLE, true).create())
            .commit();
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_select, menu);
        mFinishMenu = menu.findItem(R.id.action_finish);
        mFinishMenu.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish:
                MultiPicSelectedEvent event = new MultiPicSelectedEvent();
                event.selectedPaths = resultList;
                EventBus.getDefault().post(event);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSingleImageSelected(String path) {
        SinglePicSelectedEvent event = new SinglePicSelectedEvent();
        event.path = path;
        EventBus.getDefault().post(event);
    }

    @Override
    public void onImageSelected(String path) {
        if (!resultList.contains(path)) {
            resultList.add(path);
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if (resultList.contains(path)) {
            resultList.remove(path);
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            SinglePicSelectedEvent event = new SinglePicSelectedEvent();
            event.path = imageFile.getAbsolutePath();
            EventBus.getDefault().post(event);
        }
    }


}
