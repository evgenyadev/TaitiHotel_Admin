package kotov.invisible.taitihoteladmin.fragments;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotov.invisible.taitihoteladmin.ApiEngine.APIAnswer;
import kotov.invisible.taitihoteladmin.ApiEngine.APIError;
import kotov.invisible.taitihoteladmin.ApiEngine.ErrorLog;
import kotov.invisible.taitihoteladmin.ApiEngine.ErrorUtils;
import kotov.invisible.taitihoteladmin.ApiEngine.Order;
import kotov.invisible.taitihoteladmin.App;
import kotov.invisible.taitihoteladmin.OrderAdapter;
import kotov.invisible.taitihoteladmin.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends DialogFragment {

    private RecyclerView rvOrders;
    private ProgressBar pbLoading;
    private Call<List<Order>> getOrdersCall;
    private List<Order> ordersList;
    private Call<APIAnswer> deleteOrderCall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ordersList = new ArrayList<>();
        getOrdersCall = App.getApi().orderGetAll();
        getOrdersCall.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                pbLoading.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ordersList.addAll(response.body());
                        rvOrders.getAdapter().notifyDataSetChanged();
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(getContext(), error.getCode() + " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    ErrorLog.logd(getContext(), error);
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                pbLoading.setVisibility(View.GONE);
                if (!call.isCanceled()) {
                    Toast.makeText(getContext(), "Ошибка. " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onRecViewButtonClick(View v, final Order order) {
        SimpleDateFormat sdfDevice = new SimpleDateFormat(getResources().getString(R.string.format_date_short), Locale.getDefault());
        SimpleDateFormat sdfServer = new SimpleDateFormat(getResources().getString(R.string.format_date_server), Locale.getDefault());
        String strDateBegin = null;
        String strDateEnd = null;
        try {
            strDateBegin = sdfDevice.format(sdfServer.parse(order.getDate_begin()));
            strDateEnd = sdfDevice.format(sdfServer.parse(order.getDate_end()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (v.getId() == R.id.order_deleteButton) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
            else
                builder = new AlertDialog.Builder(getContext());

            builder.setTitle("Удаление заказа")
                    .setMessage("#" + order.getRoom_id() + " c " + strDateBegin + " по " + strDateEnd + " ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteOrderCall = App.getApi().orderDelete(order.getId());
                            deleteOrderCall.enqueue(new Callback<APIAnswer>() {
                                @Override
                                public void onResponse(Call<APIAnswer> call, Response<APIAnswer> response) {
                                    if (response.isSuccessful()) {
                                        ordersList.remove(order);
                                        rvOrders.getAdapter().notifyDataSetChanged();
                                    } else {
                                        APIError error = ErrorUtils.parseError(response);
                                        Toast.makeText(getContext(), error.getCode() + " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        ErrorLog.logd(getContext(), error);
                                    }
                                }

                                @Override
                                public void onFailure(Call<APIAnswer> call, Throwable t) {
                                    if (!call.isCanceled()) {
                                        Toast.makeText(getContext(), "Ошибка. " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orders, container, false);

        rvOrders = (RecyclerView) v.findViewById(R.id.rvOrders);
        pbLoading = (ProgressBar) v.findViewById(R.id.orders_pbLoading);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        OrderAdapter orderAdapter = new OrderAdapter(ordersList, new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Order orderData) {
                onRecViewButtonClick(v, orderData);
            }
        });

        rvOrders.setLayoutManager(layoutManager);
        rvOrders.setAdapter(orderAdapter);

        return v;
    }

}
