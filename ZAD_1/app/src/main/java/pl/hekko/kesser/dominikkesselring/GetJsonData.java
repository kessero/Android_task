package pl.hekko.kesser.dominikkesselring;/*
 * Created by kesser on 27.01.16.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

class GetJsonData extends AsyncTask<Void, Void, String> {

    private final String ERROR_GET_DATA_FROM_SERVER = "error";
    private final Context context;
    private final String url;
    private final AsyncInterface asyncInterface;


    public GetJsonData(Context context, String url, AsyncInterface asyncInterface){
        this.context = context;
        this.url = url;
        this.asyncInterface = asyncInterface;
    }

    @Override
    protected String doInBackground(Void... params) {
        try{
            JsonRequest jsonRequest = new JsonRequest();
            String jsonResponse = jsonRequest.makeWebServiceCall(url, JsonRequest.GET);
            Log.d("response: ", jsonResponse);
            if (jsonResponse.length()<1){
                return ERROR_GET_DATA_FROM_SERVER;
            }else {
                return jsonResponse;
            }

        }catch (Exception e){
            e.printStackTrace();
            return ERROR_GET_DATA_FROM_SERVER;
        }

    }
    @Override
    protected void onPostExecute(String result) {
        if (asyncInterface != null)
            asyncInterface.data(result);
    }

}
