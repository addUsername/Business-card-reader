package com.addusername.cardparser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Button;
import android.widget.Toast;

import com.addusername.cardparser.interfaces.ModelOps;
import com.addusername.cardparser.interfaces.ViewOps;
import com.addusername.cardparser.model.Contact;
import com.addusername.cardparser.model.Model;
import com.addusername.cardparser.model.rest.Rect;

import java.io.File;
import java.util.Collection;

@RequiresApi(api = Build.VERSION_CODES.R)
public class MainActivity extends AppCompatActivity implements ViewOps {

    private ModelOps mo;
    private ImageFragment imageF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);
        mo = new Model(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.whereFragmentsArePlaced,HomeFragment.getInstance());
        ft.commit();
    }

    @Override
    public void loadImg(Bitmap bitmap, Collection<Rect> rects) {
        runOnUiThread(() -> {
            imageF = new ImageFragment(bitmap, rects);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.whereFragmentsArePlaced, imageF);
            ft.commit();
        });
    }

    public void parseImg(byte[] bytes) {
        mo.parseImg(bytes);
    }

    public void addContact(Contact contact) {
        Toast.makeText(this, contact.getName()+ " "+contact.getEmail()+" "+contact.getPhone(), Toast.LENGTH_LONG).show();
        Intent addContact = new Intent(Intent.ACTION_INSERT);
        addContact.setType(ContactsContract.Contacts.CONTENT_TYPE);
        addContact.putExtra(ContactsContract.Intents.Insert.NAME, contact.getName());
        addContact.putExtra(ContactsContract.Intents.Insert.PHONE, contact.getPhone());
        addContact.putExtra(ContactsContract.Intents.Insert.EMAIL, contact.getEmail());
        startActivity(addContact);
    }
}