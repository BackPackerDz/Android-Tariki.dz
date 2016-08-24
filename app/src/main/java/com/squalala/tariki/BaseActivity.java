package com.squalala.tariki;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.squalala.tariki.App;
import com.squalala.tariki.AppComponent;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.GnuLesserGeneralPublicLicense21;
import de.psdev.licensesdialog.licenses.GnuLesserGeneralPublicLicense3;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

/*
 * Copyright 2016 Fayçal KADDOURI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponent(App.get(this).component());
    }

    protected abstract void setupComponent(AppComponent appComponent);

    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.license:

                    showLicenses();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showLicenses() {
        final Notices notices = new Notices();
        notices.addNotice(new Notice("Tariki.dz", "http://tariki.dz/", "Chaouki BOUKHAZANI  si.cgn@mdn.dz", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("osmdroid", "https://github.com/osmdroid/osmdroid", null, new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("OSMBonusPack", "https://github.com/MKergall/osmbonuspack", null, new GnuLesserGeneralPublicLicense3()));
        notices.addNotice(new Notice("ButterKnife", "http://jakewharton.github.io/butterknife/", "Jake Wharton", new GnuLesserGeneralPublicLicense21()));

        new LicensesDialog.Builder(this)
                .setNotices(notices)
                .setIncludeOwnLicense(true)
                .build()
                .show();
    }
}
