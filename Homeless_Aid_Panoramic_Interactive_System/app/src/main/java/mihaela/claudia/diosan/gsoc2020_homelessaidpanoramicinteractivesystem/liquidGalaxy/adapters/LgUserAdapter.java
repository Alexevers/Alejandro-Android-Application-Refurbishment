package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.utils.LgUser;

public class LgUserAdapter extends RecyclerView.Adapter<LgUserAdapter.LgUserHolder> implements Filterable {

    private List<LgUser> userData;
    private List<LgUser> userList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position) throws IOException;
        void onBioClick(int position);
        void onTransactionClick(int position);
        void onOrbitClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<LgUser> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()){
                filteredList.addAll(userList);
            }else{
                for (LgUser user: userList){
                    if (user.getUsername().toLowerCase().contains(constraint.toString().trim())){
                        filteredList.add(user);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userData.clear();
            userData.addAll((Collection<? extends LgUser>) results.values);
            notifyDataSetChanged();
        }
    };


    public LgUserAdapter(List<LgUser> userData){
        this.userData = userData;
        this.userList = new ArrayList<>(userData);
    }

    @NonNull
    @Override
    public LgUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lg_user_design, parent, false);
        return new LgUserHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LgUserHolder holder, int position) {
        LgUser user = userData.get(position);
        holder.username.setText(user.getUsername());
        holder.cardView.setCardBackgroundColor(user.getColor());
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }




    static class LgUserHolder extends RecyclerView.ViewHolder{
        TextView username;
        MaterialCardView cardView;
        MaterialButton showBio;
        MaterialButton showTransactions;
        MaterialButton showOrbit;

        public LgUserHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            username = itemView.findViewById(R.id.lg_username);
            cardView = itemView.findViewById(R.id.user_card_view);
            showBio = itemView.findViewById(R.id.show_bio);
            showTransactions = itemView.findViewById(R.id.show_transactions);
            showOrbit = itemView.findViewById(R.id.orbit);

            itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        try {
                            listener.onItemClick(position);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            });

            showBio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onBioClick(position);
                        }
                    }
                }
            });

            showTransactions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onTransactionClick(position);
                        }
                    }
                }
            });

            showOrbit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onOrbitClick(position);
                        }
                    }
                }
            });
        }
    }

}
