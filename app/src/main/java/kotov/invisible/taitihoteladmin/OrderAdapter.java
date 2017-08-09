package kotov.invisible.taitihoteladmin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import kotov.invisible.taitihoteladmin.ApiEngine.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> ordersDataList;
    private OnItemClickListener mListener;

    public OrderAdapter(List<Order> ordersList, OnItemClickListener listener) {
        this.ordersDataList = ordersList;
        this.mListener = listener;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(kotov.invisible.taitihoteladmin.R.layout.adapter_item_orders, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = ordersDataList.get(position);
        holder.bind(order, mListener);
    }

    @Override
    public int getItemCount() {
        return ordersDataList == null ? 0 : ordersDataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Order order);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateBegin;
        TextView dateEnd;
        TextView roomId;
        ImageButton deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            deleteButton = (ImageButton) itemView.findViewById(kotov.invisible.taitihoteladmin.R.id.order_deleteButton);
            roomId = (TextView) itemView.findViewById(kotov.invisible.taitihoteladmin.R.id.order_tvRoomId);
            dateBegin = (TextView) itemView.findViewById(kotov.invisible.taitihoteladmin.R.id.order_date_begin);
            dateEnd = (TextView) itemView.findViewById(kotov.invisible.taitihoteladmin.R.id.order_date_end);
        }

        void bind(final Order order, final OnItemClickListener listener) {
            SimpleDateFormat sdfDevice = new SimpleDateFormat(itemView.getResources().getString(kotov.invisible.taitihoteladmin.R.string.format_date_short), Locale.getDefault());
            SimpleDateFormat sdfServer = new SimpleDateFormat(itemView.getResources().getString(kotov.invisible.taitihoteladmin.R.string.format_date_server), Locale.getDefault());

            CharSequence csDateBegin = null;
            CharSequence csDateEnd = null;
            try {
                csDateBegin = sdfDevice.format(sdfServer.parse(order.getDate_begin()));
                csDateEnd = sdfDevice.format(sdfServer.parse(order.getDate_end()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            dateBegin.setText(csDateBegin);
            dateEnd.setText(csDateEnd);
            roomId.setText(String.valueOf(order.getRoom_id()));

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, order);
                }
            });
        }

    }
}
