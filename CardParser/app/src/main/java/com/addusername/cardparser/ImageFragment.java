package com.addusername.cardparser;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.addusername.cardparser.model.Contact;
import com.addusername.cardparser.model.rest.Rect;

import java.util.Collection;

public class ImageFragment extends Fragment {

    private RelativeLayout rl;
    private Bitmap image;
    private Collection<Rect> rects;
    private TextView name;
    private TextView email;
    private TextView phone;
    private TextView currentClickedTextView;
    private Contact contact;


    private View rootView;
    private float mScaleFactor = 1.0f;
    private PointF center;


    public ImageFragment(Bitmap image, Collection<Rect> rects) {

        this.image = image;
        this.rects = rects;
        this.contact = new Contact("","","");
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rl = view.findViewById(R.id.relative_layout_image);

        ImageView img = view.findViewById(R.id.imageView2);

        final GestureDetector gDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {


            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                final int X = (int) e.getRawX();
                final int Y = (int) e.getRawY();
                center = new PointF(X,Y);
                zoom(2f,2f,center);
                return true;
            }
        });
        rl.setOnTouchListener((v, event) -> gDetector.onTouchEvent(event));

        name = view.findViewById(R.id.name);
        name.setOnClickListener(onclick);
        currentClickedTextView = name;

        email = view.findViewById(R.id.email);
        email.setOnClickListener(onclick);
        phone = view.findViewById(R.id.phone);
        phone.setOnClickListener(onclick);

        view.findViewById(R.id.image_fragment_btn).setOnClickListener((v) ->
                ((MainActivity) getActivity()).addContact(contact)
        );


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity) getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        center = new PointF(displayMetrics.widthPixels,displayMetrics.heightPixels);

        view.findViewById(R.id.zoominBtn).setOnClickListener((v) ->{
            zoomOriginal();
        });

        view.findViewById(R.id.deleteName).setOnClickListener(delete);
        view.findViewById(R.id.deleteEmail).setOnClickListener(delete);
        view.findViewById(R.id.deletePhone).setOnClickListener(delete);

        img.setImageBitmap(image);
        for(Rect r: rects){
            showRect(r);
        }
    }

    private void zoom(float scaleX, float scaleY, PointF center) {
        rl.setPivotX(center.x);
        rl.setPivotY(center.y);
        rl.setScaleX(rl.getScaleX() *scaleX);
        rl.setScaleY(rl.getScaleY() *scaleY);
    }
    private void zoomOriginal() {
        rl.setPivotX(center.x);
        rl.setPivotY(center.y);
        rl.setScaleX(1f);
        rl.setScaleY(1f);
    }

    private View.OnClickListener onclick=v -> {
        switch (v.getId()){
            case R.id.name:
                name.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.image_fragment_name_focused));
                phone.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.image_fragment_email));
                email.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.image_fragment_phone));
                currentClickedTextView = name;
                break;
            case R.id.email:
                name.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.image_fragment_name));
                phone.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.image_fragment_email_focused));
                email.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.image_fragment_phone));
                currentClickedTextView = email;
                break;
            case R.id.phone:
                name.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.image_fragment_name));
                phone.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.image_fragment_email));
                email.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.image_fragment_phone_focused));
                currentClickedTextView = phone;
                break;
        }
    };
    private  View.OnClickListener delete = view -> {
        switch (view.getId()){
            case R.id.deleteName:
                name.setText("");
                contact.setName("");
            case R.id.deleteEmail:
                email.setText("");
                contact.setEmail("");
            case R.id.deletePhone:
                phone.setText("");
                contact.setPhone("");
        }
    };

    public void showRect(Rect r){
        RelativeLayout relativeLayout = new RelativeLayout(((MainActivity) getActivity()).getBaseContext());
        ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.layout_image, relativeLayout ,false);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(r.getWidth(), r.getHeight());
        layoutParams.leftMargin = r.getX();
        layoutParams.topMargin = r.getY();
        imageView.setOnClickListener((view) -> {
            Toast.makeText(((MainActivity) getActivity()).getBaseContext(), r.getText(), Toast.LENGTH_SHORT).show();
            addToContact(r.getText());
        });
        relativeLayout.addView(imageView,layoutParams);
        rl.addView(relativeLayout);
    }

    private void addToContact(String text) {

        switch (currentClickedTextView.getId()){
            case R.id.name:
                contact.setName(contact.getName()+" "+text);
                currentClickedTextView.setText(contact.getName());
                break;
            case R.id.email:
                contact.setEmail(contact.getEmail()+text);
                currentClickedTextView.setText(contact.getEmail());
                break;
            case R.id.phone:
                contact.setPhone((contact.getPhone()+text));
                currentClickedTextView.setText(contact.getPhone());
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_image, container, false);
        return this.rootView;
    }

    public void setContact(Contact body) {
        contact = body;
        name.setText(body.getName());
        email.setText(body.getEmail());
        phone.setText(body.getPhone());
    }
}