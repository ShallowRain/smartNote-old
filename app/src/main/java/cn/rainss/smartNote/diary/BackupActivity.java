package cn.rainss.smartNote.diary;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import cn.rainss.smartNote.R;

import androidx.appcompat.widget.Toolbar;


public class BackupActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_backup);

        Toolbar toolbar = getToolbar();
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_backup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.diary_list:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
