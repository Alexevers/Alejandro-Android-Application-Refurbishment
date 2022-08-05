package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.utils.Cities;

public class CitiesCardsAdapter extends RecyclerView.Adapter<CitiesCardsAdapter.CardsAdapterHolder> implements Filterable {
    private List<Cities> citiesData;
    private List<Cities> citiesList;
    private OnItemClickListener mListener;

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Cities> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()){
                filteredList.addAll(citiesList);
            }else{
                for (Cities city : citiesList){
                    if (city.getCity().toLowerCase().contains(constraint.toString().trim()) ||
                            city.getCountry().toLowerCase().contains(constraint.toString().trim())){
                        filteredList.add(city);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            citiesData.clear();
            citiesData.addAll((Collection<? extends Cities>) results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

   public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
   }

    public CitiesCardsAdapter(List<Cities> citiesData) {
        this.citiesData = citiesData;
        this.citiesList = new ArrayList<>(citiesData);
    }

    @NonNull
    @Override
    public CardsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cities_card_design, parent, false);
        return new CardsAdapterHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsAdapterHolder holder, int position) {
        Cities cities = citiesData.get(position);
        holder.city.setText(cities.getCity());
        holder.country.setText(cities.getCountry());

        Glide
                .with(holder.itemView.getContext())
                .load(cities.getImage())
                .placeholder(R.drawable.no_profile_image)
                .into(holder.city_image);
          }

    @Override
    public int getItemCount() {
        return citiesData.size();
    }

    static class  CardsAdapterHolder extends RecyclerView.ViewHolder{
        TextView city;
        TextView country;
        ImageView city_image;


        public CardsAdapterHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            city = itemView.findViewById(R.id.city_name);
            country = itemView.findViewById(R.id.country_name);
            city_image = itemView.findViewById(R.id.city_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }



}
