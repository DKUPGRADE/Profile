package com.example.profile.Adapter;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.profile.Other.RepeatMethods;
import com.example.profile.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

import static android.os.Trace.isEnabled;


public class MyItem/*(1)*/ extends AbstractFlexibleItem<MyItem.MyViewHolder/*(2)*/> {

    private String id;
    private String title;
    Uri img_uri;
    Activity activity;
    List<IFlexible> list;
//    private List<T> mItems;
private static final String TAG = "MyItem1212";
    public MyItem(String id , List<IFlexible> list ,Uri img_uri, Activity activity) {
        this.id = id;
        this.list = list;

        this.img_uri = img_uri;
        this.activity = activity;
        Log.d(TAG, "MyItem: ");
    }

    /**
     * When an item is equals to another?
     * Write your own concept of equals, mandatory to implement or use
     * default java implementation (return this == o;) if you don't have unique IDs!
     * This will be explained in the "Item interfaces" Wiki page.
     */
    @Override
    public boolean equals(Object inObject) {
        Log.d(TAG, "equals: ");
        if (inObject instanceof MyItem) {
            MyItem inItem = (MyItem) inObject;
            return this.id.equals(inItem.id);

        }
        return false;
    }

    /**
     * You should implement also this method if equals() is implemented.
     * This method, if implemented, has several implications that Adapter handles better:
     * - The Hash, increases performance in big list during Update & Filter operations.
     * - You might want to activate stable ids via Constructor for RV, if your id
     *   is unique (read more in the wiki page: "Setting Up Advanced") you will benefit
     *   of the animations also if notifyDataSetChanged() is invoked.
     */
    @Override
    public int hashCode() {
        Log.d(TAG, "hashCode: ");
        return id.hashCode();
    }

    /**
     * For the item type we need an int value: the layoutResID is sufficient.
     */
    @Override
    public int getLayoutRes() {
        Log.d(TAG, "getLayoutRes: ");
        return R.layout.item_flexible;
    }

    /**
     * Delegates the creation of the ViewHolder to the user (AutoMap).
     * The inflated view is already provided as well as the Adapter.
     */
    @Override
    public MyViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        Log.d(TAG, "createViewHolder: ");
        return new MyViewHolder(view, adapter);
    }

    /**
     * The Adapter and the Payload are provided to perform and get more specific
     * information.
     */
    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, MyViewHolder holder,
                               int position,
                               List<Object> payloads) {

        Log.d(TAG, "bindViewHolder: ");



        // Title appears disabled if item is disabled



        Glide.with(activity).load(RepeatMethods.ImageFilePath.getPath(activity, img_uri)).skipMemoryCache(true).into(holder.img_upload);

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                list.remove(position);
                adapter.notifyItemRemoved(position);

//                if (!list.isEmpty() && list != null){
//                    int position = holder.getAdapterPosition();
//
//
//
//
//                    list.remove(position);
//                    adapter.notifyItemRemoved(position);
//
//
////                    adapter.notifyItemRangeChanged(position, list.size());
//
//                   Toast.makeText(activity, "Delete Image ", Toast.LENGTH_SHORT).show();
//               }
            }
        });


    }

    /**
     * The ViewHolder used by this item.
     * Extending from FlexibleViewHolder is recommended especially when you will use
     * more advanced features.
     */
    public class MyViewHolder extends FlexibleViewHolder {

        public TextView mTitle;
               ImageView img_upload,close;
        public MyViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);

            img_upload = (ImageView) view.findViewById(R.id.img_upload);
            close = (ImageView) view.findViewById(R.id.close);
        }
    }


}