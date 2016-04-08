package com.qoobico.remindme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qoobico.remindme.R;
import com.qoobico.remindme.model.User;

import java.util.ArrayList;

/**
 * Created by Winner on 29.02.2016.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context Context;
    private ArrayList<User> UsersArrayList;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username, email;

        public ViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.users_name);
           // email = (TextView) view.findViewById(R.id.users_email);

        }
    }


    public UsersAdapter(Context Context, ArrayList<User> UsersArrayList) {
        this.Context = Context;
        this.UsersArrayList = UsersArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User users = UsersArrayList.get(position);
        holder.username.setText(users.getName());
       // holder.email.setText(users.getEmail());
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
