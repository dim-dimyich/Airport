package com.qoobico.remindme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import com.qoobico.remindme.R;
import com.qoobico.remindme.app.MyApplication;
import com.qoobico.remindme.model.User;
import com.qoobico.remindme.util.CircularNetworkImageView;


import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Winner on 29.02.2016.
 */
public class UsersAdapter extends android.support.v7.widget.RecyclerView.Adapter {

    private Context Context;
    private ArrayList<User> UsersArrayList;
    private String UserId = MyApplication.getInstance().getPrefManager().getUser().getId();
    ImageLoader StaffImage = MyApplication.getInstance().getImageLoader();

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {

        @Bind(R.id.users_name)
        TextView username;

        @Bind(R.id.position)
        TextView Position;

        @Bind(R.id.intialization_user)
        TextView initial;

        @Bind(R.id.staff_image)
        CircularNetworkImageView StaffPfoto;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        public void bindViewHolder(User user) {
            username.setText(user.getName());
            Position.setText(user.getPosition());
            StaffPfoto.setImageUrl(user.getImageUser(), StaffImage);
            if(user.getId().equals(UserId)){
                initial.setVisibility(View.VISIBLE);
            } else {
                initial.setVisibility(View.GONE);
            }
        }
    }
//-------------------------------------------------------------------------------------------------

    public UsersAdapter(Context Context, ArrayList<User> UsersArrayList) {
        this.Context = Context;
        this.UsersArrayList = UsersArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_staff, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        User user = UsersArrayList.get(position);
        ((ViewHolder) holder).bindViewHolder(user);
    }

    @Override
    public int getItemCount() {
        return UsersArrayList.size();
    }

    public interface UserClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private UsersAdapter.UserClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final UsersAdapter.UserClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
