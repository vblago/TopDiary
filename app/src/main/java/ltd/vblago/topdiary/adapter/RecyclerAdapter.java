package ltd.vblago.topdiary.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ltd.vblago.topdiary.R;
import ltd.vblago.topdiary.model.Entry;
import ltd.vblago.topdiary.model.NURE.TimeTable.TimeTable;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MainViewHolder> {
    private List<Entry> entryList;
    private AdapterCallback adapterCallback;

    public RecyclerAdapter(ArrayList<Entry> entryList, AdapterCallback adapterCallback) {
        this.entryList = entryList;
        this.adapterCallback = adapterCallback;
    }

    public interface AdapterCallback {
        void onItemClick(int position, boolean checked);

        void onShortClick(int position);

        void onLongClick(int position);

        void shiftBtnClicked(int position);
    }

    @Override
    public int getItemViewType(int position) {
        return entryList.get(position).type;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_previous, parent, false);
                return new PreviousViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_without_time, parent, false);
                return new WithoutTimeViewHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, parent, false);
                return new TimeViewHolder(view);
            case 4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, parent, false);
                return new TimeViewHolder(view);
            case 5:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timetable, parent, false);
                return new TimeTableHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, final int position) {
        Entry entry = entryList.get(position);
        holder.title.setText(entry.title);
        holder.place.setText(entry.place);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallback.onShortClick(position);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (entryList.get(position).type != 5) {
                    adapterCallback.onLongClick(position);
                    notifyItemChanged(position);
                }
                return false;
            }
        });

        switch (entry.type) {
            case 1:

                ((PreviousViewHolder) holder).shiftBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        adapterCallback.shiftBtnClicked(position);
                    }
                });
                ((PreviousViewHolder) holder).checkBox.setChecked(entry.done);
                ((PreviousViewHolder) holder).checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapterCallback.onItemClick(position, !entryList.get(position).done);
                        notifyItemChanged(position);
                    }
                });
                break;
            case 2:
                ((WithoutTimeViewHolder) holder).checkBox.setChecked(entry.done);
                ((WithoutTimeViewHolder) holder).checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapterCallback.onItemClick(position, !entryList.get(position).done);
                        notifyItemChanged(position);
                    }
                });
                break;
            case 3:
                ((TimeViewHolder) holder).time.setText(entry.getTimeString());
                ((TimeViewHolder) holder).checkBox.setChecked(entry.done);
                ((TimeViewHolder) holder).checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapterCallback.onItemClick(position, !entryList.get(position).done);
                        notifyItemChanged(position);
                    }
                });
                break;
            case 4:
                ((TimeViewHolder) holder).time.setText(entry.getTimeString());
                ((TimeViewHolder) holder).checkBox.setChecked(entry.done);
                ((TimeViewHolder) holder).checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapterCallback.onItemClick(position, !entryList.get(position).done);
                        notifyItemChanged(position);
                    }
                });
                break;
            case 5:
                ((TimeTableHolder) holder).time.setText(entry.getTimeString());
                break;

        }
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.place)
        TextView place;
        @BindView(R.id.cardView)
        CardView cardView;

        MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class TimeTableHolder extends MainViewHolder {

        @BindView(R.id.time)
        TextView time;

        TimeTableHolder(View itemView) {
            super(itemView);

        }
    }

    public class TimeViewHolder extends MainViewHolder {

        @BindView(R.id.checkBox)
        CheckBox checkBox;
        @BindView(R.id.time)
        TextView time;

        TimeViewHolder(View itemView) {
            super(itemView);

        }
    }

    public class WithoutTimeViewHolder extends MainViewHolder {

        @BindView(R.id.checkBox)
        CheckBox checkBox;

        WithoutTimeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class PreviousViewHolder extends MainViewHolder {

        @BindView(R.id.checkBox)
        CheckBox checkBox;
        @BindView(R.id.shiftBtn)
        TextView shiftBtn;

        PreviousViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
