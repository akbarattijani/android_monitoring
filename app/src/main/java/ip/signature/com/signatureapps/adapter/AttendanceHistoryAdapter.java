package ip.signature.com.signatureapps.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ip.signature.com.signatureapps.R;

public class AttendanceHistoryAdapter extends RecyclerView.Adapter<AttendanceHistoryAdapter.AttendanceHistoryViewHolder> {
    private Context context;
    private JSONArray jsonArray;

    public AttendanceHistoryAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @Override
    public AttendanceHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_history_item, parent, false);
        return new AttendanceHistoryAdapter.AttendanceHistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AttendanceHistoryViewHolder holder, int position) {
        try {
            JSONObject object = jsonArray.getJSONObject(position);
            holder.tvAbsen.setText(object.getString("start_date"));
            holder.tvAbsenEnd.setText(object.getString("end_date"));
            holder.tvBreak.setText(object.getString("break_start_date"));
            holder.tvBreakEnd.setText(object.getString("break_end_date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    class AttendanceHistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAbsen;
        public TextView tvAbsenEnd;
        public TextView tvBreak;
        public TextView tvBreakEnd;

        public AttendanceHistoryViewHolder(View itemView) {
            super(itemView);

            tvAbsen = (TextView) itemView.findViewById(R.id.tvAbsen);
            tvAbsenEnd = (TextView) itemView.findViewById(R.id.tvAbsenEnd);
            tvBreak = (TextView) itemView.findViewById(R.id.tvBreak);
            tvBreakEnd = (TextView) itemView.findViewById(R.id.tvBreakEnd);
        }
    }
}
