package kotov.invisible.taitihoteladmin.dialogs;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import kotov.invisible.taitihoteladmin.ApiEngine.APIAnswer;
import kotov.invisible.taitihoteladmin.ApiEngine.APIError;
import kotov.invisible.taitihoteladmin.ApiEngine.ErrorLog;
import kotov.invisible.taitihoteladmin.ApiEngine.ErrorUtils;
import kotov.invisible.taitihoteladmin.ApiEngine.Order;
import kotov.invisible.taitihoteladmin.App;
import kotov.invisible.taitihoteladmin.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOrderDialog extends DialogFragment {

    private SimpleDateFormat sdfDevice;
    private SimpleDateFormat sdfServer;

    private Call<APIAnswer> addOrderCall;

    private View.OnClickListener listenerDatePickerButton = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            String date = ((Button) v).getText().toString();
            DialogFragment picker = DatePickerDialog.newInstance(date, new android.app.DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar c = Calendar.getInstance();
                    // год не менять
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    ((Button) v).setText(sdfDevice.format(c.getTime()));
                }
            });

            picker.show(getFragmentManager(), "datePicker");
        }
    };
    private EditText etRoomId;
    private Button btnCheckIn;
    private Button btnCheckOut;
    private ProgressBar pbSendingOrder;
    private Button btnOk;
    private Button btnCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_order_dialog, container, false);

        getDialog().setTitle("Бронь номера");

        sdfDevice = new SimpleDateFormat(getResources().getString(R.string.format_date_full), Locale.getDefault());
        sdfServer = new SimpleDateFormat(getResources().getString(R.string.format_date_server), Locale.getDefault());

        pbSendingOrder = (ProgressBar) v.findViewById(R.id.pbOrderSending);
        etRoomId = (EditText) v.findViewById(R.id.etRoomId);
        btnCheckIn = (Button) v.findViewById(R.id.buttonCheckIn);
        btnCheckOut = (Button) v.findViewById(R.id.buttonCheckOut);
        btnOk = (Button) v.findViewById(R.id.btnOrderDialogOk);
        btnCancel = (Button) v.findViewById(R.id.btnOrderDialogCancel);

        btnCheckIn.setOnClickListener(listenerDatePickerButton);
        btnCheckOut.setOnClickListener(listenerDatePickerButton);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInputData()) return;
                showSendingProcess(true);
                addOrderCall = App.getApi().orderAdd(prepareOrder());
                addOrderCall.enqueue(new Callback<APIAnswer>() {
                    @Override
                    public void onResponse(Call<APIAnswer> call, Response<APIAnswer> response) {
                        showSendingProcess(false);
                        if (response.isSuccessful()) {
                            if (response.body() != null)
                                Toast.makeText(getContext(), response.body().getInfo(), Toast.LENGTH_LONG).show();
                            dismiss();
                        } else {
                            APIError error = ErrorUtils.parseError(response);
                            ErrorLog.logd(getContext(), error);

                            if (error.getBody().equals("Room not exists."))
                                etRoomId.setError("Несуществующий номер");

                            else if (error.getBody().equals("Order not added."))
                                Toast.makeText(getContext(), "Этот номер уже забронирован в данном промежутке времени.", Toast.LENGTH_LONG).show();

                            else
                                Toast.makeText(getContext(), error.getCode() + " " + error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<APIAnswer> call, Throwable t) {
                        showSendingProcess(false);
                        if (!call.isCanceled()) {
                            Toast.makeText(getContext(), "Ошибка. " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (addOrderCall != null) addOrderCall.cancel();
    }

    private void showSendingProcess(boolean b) {
        if (b)
            pbSendingOrder.setVisibility(View.VISIBLE);
        else
            pbSendingOrder.setVisibility(View.GONE);

        btnOk.setEnabled(!b);
        etRoomId.setEnabled(!b);
        btnCheckIn.setEnabled(!b);
        btnCheckOut.setEnabled(!b);
    }

    private boolean checkInputData() {

        try {
            //noinspection ResultOfMethodCallIgnored
            Integer.valueOf(etRoomId.getText().toString());
        } catch (NumberFormatException e) {
            etRoomId.setError("Некорректные данные.");
            return false;
        }

        Calendar in = Calendar.getInstance();
        Calendar out = Calendar.getInstance();
        try {
            in.setTime(sdfDevice.parse(btnCheckIn.getText().toString()));
            out.setTime(sdfDevice.parse(btnCheckOut.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка преобразования даты.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // проверка валидности дат
        if (in.compareTo(out) > 0) {
            Toast.makeText(getContext(), "Дата заезда не может быть позже даты выезда.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (in.compareTo(out) == 0) {
            Toast.makeText(getContext(), "Даты должны отличаться минимум на 1 день", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private Order prepareOrder() {
        Order order = new Order();

        order.setRoom_id(Integer.valueOf(etRoomId.getText().toString()));

        try {
            order.setDate_begin(sdfServer.format(sdfDevice.parse(btnCheckIn.getText().toString())));
            order.setDate_end(sdfServer.format(sdfDevice.parse(btnCheckOut.getText().toString())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return order;
    }

}
