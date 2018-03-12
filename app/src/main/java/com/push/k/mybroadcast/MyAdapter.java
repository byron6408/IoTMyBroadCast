package com.push.k.mybroadcast;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
	private String[] data;
	private static Context context;
	
	static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.textView1);
            text.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(v instanceof TextView) {
						Toast.makeText(context, ((TextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
					}
				}
            });
        }
    }
	
	public MyAdapter(Context context, String[] data) {
		MyAdapter.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_text_view, viewGroup, false);
        // ViewHolder參數一定要是項目的根目錄節點。
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
    	if(i%2==0) {
    		viewHolder.text.setTextColor(0xFF000000);
    	} else {
    		viewHolder.text.setTextColor(0xFFAAAAAA);
    	}
        viewHolder.text.setText(data[i]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    
}
