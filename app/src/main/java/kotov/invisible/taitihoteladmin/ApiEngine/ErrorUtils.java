package kotov.invisible.taitihoteladmin.ApiEngine;

import java.io.IOException;
import java.lang.annotation.Annotation;

import kotov.invisible.taitihoteladmin.App;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

    public static APIError parseError(Response<?> response) {

        Converter<ResponseBody, APIError> converter = App.retrofit.responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException exception) {
            error = new APIError();
            error.setBody("Application error converting response body");
        }

        error.setCode(response.code());
        error.setMessage(response.message());

        return error;
    }
}
