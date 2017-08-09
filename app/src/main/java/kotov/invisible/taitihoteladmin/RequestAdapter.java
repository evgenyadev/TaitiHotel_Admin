package kotov.invisible.taitihoteladmin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import kotov.invisible.taitihoteladmin.ApiEngine.Request;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<Request> requestsDataList;
    private OnItemClickListener mListener;

    public RequestAdapter(List<Request> roomsGroupList, OnItemClickListener listener) {
        this.requestsDataList = roomsGroupList;
        this.mListener = listener;
    }

    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(kotov.invisible.taitihoteladmin.R.layout.adapter_item_requests, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Request request = requestsDataList.get(position);
        holder.bind(request, mListener);
    }

    @Override
    public int getItemCount() {
        return requestsDataList == null ? 0 : requestsDataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Request request);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView phone;
        ImageButton infoButton;
        ImageButton deleteButton;
        ImageButton callButton;

        ViewHolder(View itemView) {
            super(itemView);
            deleteButton = (ImageButton) itemView.findViewById(kotov.invisible.taitihoteladmin.R.id.deleteButton);
            callButton = (ImageButton) itemView.findViewById(kotov.invisible.taitihoteladmin.R.id.callButton);
            infoButton = (ImageButton) itemView.findViewById(kotov.invisible.taitihoteladmin.R.id.ibtnInfo);
            name = (TextView) itemView.findViewById(kotov.invisible.taitihoteladmin.R.id.name);
            phone = (TextView) itemView.findViewById(kotov.invisible.taitihoteladmin.R.id.phone);
        }

        void bind(final Request request, final OnItemClickListener listener) {
            name.setText(String.valueOf(request.getName()));
            phone.setText(String.valueOf(request.getPhone()));

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, request);
                }
            };

            infoButton.setOnClickListener(onClickListener);
            callButton.setOnClickListener(onClickListener);
            deleteButton.setOnClickListener(onClickListener);
        }

    }

}
