package domain.hba;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


public class tokenRequest extends StringRequest {

    public tokenRequest(Response.Listener<String> listener,Response.ErrorListener errorListener) {

        super(String.format("http://188.166.44.160/hba_auth.php?action=%1$s",
                "generateToken"), listener, errorListener);

    }

}
