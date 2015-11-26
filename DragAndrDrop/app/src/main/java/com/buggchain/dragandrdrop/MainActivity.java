package com.buggchain.dragandrdrop;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private static final String IMAGEVIEW_TAG = "imageViewTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.image);
        imageView.setTag(IMAGEVIEW_TAG);
        imageView.setOnLongClickListener(new MyClickListener());

        findViewById(R.id.toplinear).setOnDragListener(new MyDragListener());
        findViewById(R.id.bottomlinear).setOnDragListener(new MyDragListener());

    }

    private final class MyClickListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View v) {
            ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
            String[] mimeType = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(v.getTag().toString(),mimeType,item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, // data to be drag
                    shadowBuilder,// drag shadow
                    v,// local data about the drag and drop operation
                    0  // no need flags
            );
            v.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    class MyDragListener implements View.OnDragListener{

        Drawable normalShape = ContextCompat.getDrawable(MainActivity.this,R.drawable.normal_shape);
        Drawable targetShape = ContextCompat.getDrawable(MainActivity.this,R.drawable.target_shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()){
                case DragEvent.ACTION_DRAG_STARTED:

                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackground(targetShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackground(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // if the view is the bottom linear, we accept the drag item
                    if(v == findViewById(R.id.bottomlinear)){
                        View view = (View)event.getLocalState();
                        ViewGroup viewGroup = (ViewGroup)view.getParent();
                        viewGroup.removeView(view);

                        // Change The text
                        TextView text = (TextView)v.findViewById(R.id.text);
                        text.setText("The item is dropped");

                        LinearLayout containView = (LinearLayout)v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);
                    }else {
                        View view = (View)event.getLocalState();

                        ViewGroup viewGroup = (ViewGroup)view.getParent();
                        viewGroup.removeView(view);

                        LinearLayout containView = (LinearLayout)v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackground(normalShape); // go back to normal shape
                    break;
            }
            return true;
        }
    }

}
