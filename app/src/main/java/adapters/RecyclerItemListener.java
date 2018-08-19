package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemListener implements RecyclerView.OnItemTouchListener{

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener listener;
    private GestureDetector gestureDetector;

    public RecyclerItemListener(final OnItemClickListener listener, Context context, final RecyclerView recyclerView) {
        this.listener = listener;
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {

                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if (childView!=null && listener!=null)
                {
                    listener.onItemLongClick(childView,recyclerView.getChildAdapterPosition(childView));
                }

            }

        });
    }



    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

        View childView = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
        if (childView != null && listener != null && gestureDetector.onTouchEvent(motionEvent)) {
            listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
