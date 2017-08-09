package kotov.invisible.taitihoteladmin.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;

import kotov.invisible.taitihoteladmin.ApiEngine.APIAnswer;
import kotov.invisible.taitihoteladmin.ApiEngine.APIError;
import kotov.invisible.taitihoteladmin.ApiEngine.ErrorLog;
import kotov.invisible.taitihoteladmin.ApiEngine.ErrorUtils;
import kotov.invisible.taitihoteladmin.ApiEngine.Request;
import kotov.invisible.taitihoteladmin.App;
import kotov.invisible.taitihoteladmin.RequestAdapter;
import kotov.invisible.taitihoteladmin.R;
import kotov.invisible.taitihoteladmin.dialogs.ShowRequestInfoDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestsFragment extends DialogFragment {
    private ProgressBar pbLoading;

    private List<Request> requestsList;
    private Call<List<Request>> getRequestsCall;
    private Call<APIAnswer> deleteRequestCall;
    private RecyclerView rvRequests;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestsList = new ArrayList<>();
        getRequestsCall = App.getApi().requestGetAll();
        getRequestsCall.enqueue(new Callback<List<Request>>() {
            @Override
            public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                pbLoading.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        requestsList.addAll(response.body());
                        rvRequests.getAdapter().notifyDataSetChanged();
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(getContext(), error.getCode() + " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    ErrorLog.logd(getContext(), error);
                }
            }

            @Override
            public void onFailure(Call<List<Request>> call, Throwable t) {
                pbLoading.setVisibility(View.GONE);
                if (!call.isCanceled()) {
                    Toast.makeText(getContext(), "Ошибка. " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_requests, container, false);

        rvRequests = (RecyclerView) v.findViewById(R.id.rvRequests);
        pbLoading = (ProgressBar) v.findViewById(R.id.requests_pbLoading);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RequestAdapter requestAdapter = new RequestAdapter(requestsList, new RequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Request orderData) {
                onRecViewButtonClick(v, orderData);
            }
        });

        rvRequests.setLayoutManager(layoutManager);
        rvRequests.setAdapter(requestAdapter);

        return v;
    }

    private void onRecViewButtonClick(View v, final Request request) {
        if (v.getId() == R.id.ibtnInfo)
            ShowRequestInfoDialog.newInstance(request.getName(), request.toString())
                    .show(getFragmentManager(), "ShowOrderInfo");

        if (v.getId() == R.id.deleteButton) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
            else
                builder = new AlertDialog.Builder(getContext());

            builder.setTitle("Удаление заявки")
                    .setMessage("Удалить заявку " + request.getName() + " ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteRequestCall = App.getApi().requestDelete(request.getId());
                            deleteRequestCall.enqueue(new Callback<APIAnswer>() {
                                @Override
                                public void onResponse(Call<APIAnswer> call, Response<APIAnswer> response) {
                                    if (response.isSuccessful()) {
                                        requestsList.remove(request);
                                        rvRequests.getAdapter().notifyDataSetChanged();
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

        if (v.getId() == R.id.callButton) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + request.getPhone()));
            startActivity(intent);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (getRequestsCall != null) getRequestsCall.cancel();
        if (deleteRequestCall != null) deleteRequestCall.cancel();
    }
}
