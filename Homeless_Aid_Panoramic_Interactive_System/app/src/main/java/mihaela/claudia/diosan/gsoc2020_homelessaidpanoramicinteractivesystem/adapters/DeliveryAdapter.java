package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.logic.Delivery;



public class DeliveryAdapter extends FirestoreRecyclerAdapter<Delivery, DeliveryAdapter.DeliveryHolder> {

    private DeliveryAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(DeliveryAdapter.OnItemClickListener listener){
        mListener = listener;
    }


    public DeliveryAdapter(@NonNull FirestoreRecyclerOptions<Delivery> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DeliveryHolder deliveryHolder, int i, @NonNull Delivery delivery) {
        deliveryHolder.donorUsername.setText(delivery.getDonorUsername());
        deliveryHolder.donationType.setText(delivery.getDonationType());
        deliveryHolder.homelessUsername.setText(delivery.getDonatesTo());
        deliveryHolder.location.setText(delivery.getDonationLocation());
        deliveryHolder.date.setText(delivery.getDonationDate());
        deliveryHolder.time.setText(delivery.getDonationHour());
        deliveryHolder.email.setText(delivery.getDonorEmail());
        deliveryHolder.phone.setText(delivery.getDonorPhone());
    }

    @NonNull
    @Override
    public DeliveryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_custom_view, parent, false);
        return new DeliveryHolder(v, mListener);
    }

    class DeliveryHolder extends RecyclerView.ViewHolder{
        TextView donorUsername;
        TextView donationType;
        TextView homelessUsername;
        TextView location;
        TextView date;
        TextView time;
        TextView email;
        TextView phone;


        public DeliveryHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            donorUsername = itemView.findViewById(R.id.delivery_donor_name);
            donationType = itemView.findViewById(R.id.delivery_donation_type);
            homelessUsername = itemView.findViewById(R.id.delivery_donates_to);
            location = itemView.findViewById(R.id.delivery_location);
            date = itemView.findViewById(R.id.delivery_date);
            time = itemView.findViewById(R.id.delivery_time);
            email = itemView.findViewById(R.id.delivery_donor_email);
            phone = itemView.findViewById(R.id.delivery_donor_phone);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(getSnapshots().getSnapshot(position), position);
                        }
                    }
                }
            });
        }
    }
}
