package edu.escuelaing.ieti.jeebka.GroupsView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

import edu.escuelaing.ieti.jeebka.R;

public class StaticRvAdapter  extends  RecyclerView.Adapter<StaticRvAdapter.StaticRVViewHolder> {
    private ArrayList<StatiGroupTypeRvModel> items;
    int row_index = -1;
    UpdateRecyclerView updateRecyclerView;
    Activity activity;
    boolean check = true;
    boolean select = true;

    public StaticRvAdapter(ArrayList<StatiGroupTypeRvModel> items, Activity activity, UpdateRecyclerView updateRecyclerView) {
        this.items = items;
        this.activity = activity;
        this.updateRecyclerView = updateRecyclerView;
    }

    public class StaticRVViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        ImageView imageView;
        LinearLayout linearLayout;

        public StaticRVViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            nameText = itemView.findViewById(R.id.text);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    @NonNull
    @Override
    public StaticRvAdapter.StaticRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.static_rv_item, parent, false);
        StaticRVViewHolder staticRVViewHolder = new StaticRVViewHolder(view);
        return staticRVViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull StaticRvAdapter.StaticRVViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        StatiGroupTypeRvModel currentItem = items.get(position);
        holder.imageView.setImageResource(currentItem.getImage());
        holder.nameText.setText(currentItem.getText());
        if (check) {
            ArrayList<DynamicGroupRvModel> items = new ArrayList<>();
            for(int i = 0; i < 10; i++)
                items.add(generateTestGroup(0, false));

            updateRecyclerView.callBack(position, items);
            check = false;
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();

                if (position == 0) {
                    ArrayList<DynamicGroupRvModel> items = new ArrayList<>();
                    for(int i = 0; i < 10; i++)
                        items.add(generateTestGroup(0, false));

                    updateRecyclerView.callBack(position, items);
                } else if (position == 1) {
                    ArrayList<DynamicGroupRvModel> items = new ArrayList<>();
                    for(int i = 0; i < 10; i++)
                        items.add(generateTestGroup(1, true));;
                    updateRecyclerView.callBack(position, items);
                }
            }
        });

        if (select) {
            if (position == 0)
                holder.linearLayout.setBackgroundResource(R.drawable.static_rv_selected_bg);
            select = false;
        } else {
            if (row_index == position) {
                holder.linearLayout.setBackgroundResource(R.drawable.static_rv_selected_bg);
            } else {
                holder.linearLayout.setBackgroundResource(R.drawable.static_rv_bg);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DynamicGroupRvModel generateTestGroup(int pos, boolean isPublic){
        Random random = new Random();
        int color = generateRandomColor(Color.valueOf(227, 209, 209));
        String description =  "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam vehicula gravida imperdiet. Sed leo odio, rhoncus quis est eget, ultricies commodo nulla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Nullam dignissim cursus porta. Nulla ornare, dolor efficitur efficitur sollicitudin.";
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        ArrayList<String> members =  new ArrayList<>();
        members.add("asd");
        int max = 20, min = 7;
        for(int i = 0; i < (random.nextInt(max - min) + min); i++) members.add("asd");
        ArrayList<String> links =  new ArrayList<>();
        links.add("asd");
        if(!isPublic)
            isPublic = random.nextBoolean();
        for(int i = 0; i < (random.nextInt(max - min) + min); i++) links.add("asd");
        return new DynamicGroupRvModel("Grupo " + random.nextInt(200), description,isPublic , hexColor, pos, members, links);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int generateRandomColor(Color mix) {
        Random random = new Random();
        float red = random.nextInt(256);
        float green = random.nextInt(256);
        float blue = random.nextInt(256);

        // mix the color
        if (mix != null) {
            red = (red + (int)mix.red()) / 2;
            green = (green + (int)mix.green()) / 2;
            blue = (blue + (int)mix.blue()) / 2;
        }

        Color color = Color.valueOf(red, green, blue);
        return color.toArgb();
    }

}
