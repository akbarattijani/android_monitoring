package ip.signature.com.signatureapps.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ip.signature.com.signatureapps.R;

/**
 * Created by I R F A N on 8/9/2017.
 */

public class NavigationMenuAdapter extends RecyclerView.Adapter<NavigationMenuAdapter.NavigationMenuViewHolder> {
    public interface OnMenuClickListener{
        void onMenuClick(int position);
    }

    private Context context;
    private OnMenuClickListener listener;
    private String[] menu = new String[]{
            "Profil", "Riwayat", "Absensi",
            "Bantuan", "Keluar Akun",
            "Powered By"
    };
    private int[] menuIcon = new int[]{
            R.drawable.arrow_menu, R.drawable.arrow_menu, R.drawable.arrow_menu,
            R.drawable.arrow_menu, R.drawable.arrow_menu
    };

    private String countNotification = "0";

    public NavigationMenuAdapter(Context context) {
        this.context = context;
    }
    public void setMenuClickListener(OnMenuClickListener onMenuClickListener){
        this.listener = onMenuClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public NavigationMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == menu.length-1){
            View v = LayoutInflater.from(context).inflate(R.layout.row_navigation_menu3, parent, false);
            return new NavigationMenuViewHolder(v);
        }else if(viewType == menu.length-2){
            View v = LayoutInflater.from(context).inflate(R.layout.row_navigation_menu2, parent, false);
            return new NavigationMenuViewHolder(v);
        }else {
            View v = LayoutInflater.from(context).inflate(R.layout.row_navigation_menu1, parent, false);
            return new NavigationMenuViewHolder(v);
        }
    }

    public void update(String countNotification) {
        this.countNotification = countNotification;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(NavigationMenuViewHolder holder, final int position) {
        if (position == menu.length-1) {

        } else if (position == menu.length-2) {
            holder.llLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onMenuClick(position);
                    }
                }
            });
        } else {
            if (position == 4 && !countNotification.equals("0") && !countNotification.equals("") && countNotification != null) {
                holder.flNotification.setVisibility(View.VISIBLE);
                holder.tvNotification.setText(countNotification);
            } else {
                holder.flNotification.setVisibility(View.GONE);
            }

            holder.tvMenu.setText(menu[position]);
            holder.ivMenuIcon.setImageDrawable(context.getResources().getDrawable(menuIcon[position]));
            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onMenuClick(position);
                    }
                }
            });
        }
//        else {
//            TextView tvKebijakanPrivasi = (TextView) holder.itemView.findViewById(R.id.tvKebijakanPrivasi);
//            holder.tvMenu.setText(menu[position]);
//            holder.tvMenu.setPaintFlags(holder.tvMenu.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//            tvKebijakanPrivasi.setPaintFlags(holder.tvMenu.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//            holder.tvMenu.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(listener != null){
//                        listener.onMenuClick(position);
//                    }
//                }
//            });
//            tvKebijakanPrivasi.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, EmoneyKebijakanPrivasiActivity.class);
//                    context.startActivity(intent);
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return menu.length;
    }

    class NavigationMenuViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMenu;
        public ImageView ivMenuIcon;
        public LinearLayout llContainer;
        public LinearLayout llLogout;
        public FrameLayout flNotification;
        public TextView tvNotification;

        public NavigationMenuViewHolder(View itemView) {
            super(itemView);
            tvMenu = (TextView) itemView.findViewById(R.id.tvMenu);
            ivMenuIcon = (ImageView) itemView.findViewById(R.id.ivMenuIcon);
            llContainer = (LinearLayout) itemView.findViewById(R.id.llContainer);
            llLogout = (LinearLayout) itemView.findViewById(R.id.llLogout);
            flNotification = (FrameLayout) itemView.findViewById(R.id.flNotification);
            tvNotification = (TextView) itemView.findViewById(R.id.tvNotification);
        }
    }
}
