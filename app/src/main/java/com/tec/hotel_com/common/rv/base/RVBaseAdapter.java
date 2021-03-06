package com.tec.hotel_com.common.rv.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tec.hotel_com.room_manage.entity.RoomInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HIAPAD on 2017/12/2.
 */

public abstract class RVBaseAdapter<C extends RVBaseCell> extends RecyclerView.Adapter<RVBaseViewHolder> {

    public static final String TAG = "RVBaseAdapter";

    protected List<C> mData;

    private Context mContext;

    private RVBaseViewHolder mHolder;

    public RVBaseAdapter() {
        mData = new ArrayList<>();
    }

    public void setData(List<C> data) {
        addAll(data);
        notifyDataSetChanged();
    }

    public void setDataByRemove(List<C> data) {
//        mData.clear();
//        addAll(data);
//        notifyDataSetChanged();
        remove(0, mData.size());
        setData(data);
    }

    public List<C> getData() {
        return mData;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        for (int i = 0; i < getItemCount(); i++) {
            if (viewType == mData.get(i).getItemType()) {
                RVBaseViewHolder holder = mData.get(i).onCreateViewHolder(parent, viewType);
                mHolder = holder;
                return holder;
            }
        }


        throw new RuntimeException("wrong viewType");
    }

    protected Context getContext() {
        return mContext;
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        onViewHolderBound(holder, position);
        mData.get(position).onBindViewHolder(holder, position, mContext, this);
        onViewClick(holder, position);
        providerAllData(mData, holder);
    }

    @Override
    public void onViewDetachedFromWindow(RVBaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        //释放资源
        int position = holder.getAdapterPosition();
        //越界检查
        if (position < 0 || position >= mData.size()) {
            return;
        }
        //当item不可见的时候做回收资源处理
        mData.get(position).releaseResource();
    }


    public void setClick(int viewId, View.OnClickListener listener) {
        mHolder.getView(viewId).setOnClickListener(listener);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getItemType();
    }

    public void reset(List<C> data) {
        mData.clear();
        setData(data);
    }

    /**
     * add one cell
     *
     * @param cell
     */
    public void add(C cell) {
        mData.add(cell);
        int index = mData.indexOf(cell);
        notifyItemChanged(index);
    }

    public void add(int index, C cell) {
        mData.add(index, cell);
        notifyItemChanged(index);
    }

    /**
     * remove a cell
     *
     * @param cell
     */
    public void remove(C cell) {
        int indexOfCell = mData.indexOf(cell);
        remove(indexOfCell);
    }

    public void remove(int index) {
        mData.remove(index);
        notifyItemRemoved(index);
    }

    /**
     * @param start
     * @param count
     */
    public void remove(int start, int count) {
        if ((start + count) > mData.size()) {
            return;
        }
//        int size = getItemCount();
//        for (int i = start; i < size; i++) {
//            mData.remove(i);
//        }

        for (int i = 0; i < count; i++) {
            //因为list删除一条之后size就会减1
            mData.remove(start);
        }

        notifyItemRangeRemoved(start, count);
    }

    /**
     * add a cell list
     *
     * @param cells
     */
    public void addAll(List<C> cells) {
        if (cells == null || cells.size() == 0) {
            return;
        }
        mData.addAll(cells);
        notifyItemRangeChanged(mData.size() - cells.size(), mData.size());
//        notifyDataSetChanged();
    }

    public void addAll(int index, List<C> cells) {
        if (cells == null || cells.size() == 0) {
            return;
        }
        mData.addAll(index, cells);
        notifyItemRangeChanged(index, index + cells.size());
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }


    /**
     * 如果子类需要在onBindViewHolder回调之前做相关操作可以在这个方法里做
     * 在Recycler.Adapter中的onBindViewHolder方法中调用这里
     *
     * @param holder
     * @param position
     */
    protected abstract void onViewHolderBound(RVBaseViewHolder holder, int position);


    /*如果需要点击，子类重写*/
    protected void onViewClick(RVBaseViewHolder holder, int position) {

    }

    /*提供全部数据，供给子类使用*/
    protected void providerAllData(List<C> list, RVBaseViewHolder holder) {

    }


}



