package pl.hekko.kesser.dominikkesselring;/*
 * Created by kesser on 24.01.16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

class LazyAdapter extends BaseAdapter {

    private final ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    private final ImageLoader imageLoader;
    public LazyAdapter(MainActivity mainActivity, ArrayList<HashMap<String, String>> dataList) {
        data=dataList;
        inflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(mainActivity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_item, null);

        TextView title = (TextView)vi.findViewById(R.id.titleData);
        TextView desc = (TextView)vi.findViewById(R.id.descData);
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.miniImage);

        HashMap<String, String> dataHash;
        dataHash = data.get(position);

        // Setting all values in listview
        title.setText(dataHash.get(MainActivity.TAG_TITLE));
        desc.setText(dataHash.get(MainActivity.TAG_DESC));
        imageLoader.DisplayImage(dataHash.get(MainActivity.TAG_URL), thumb_image);
        return vi;
    }
}