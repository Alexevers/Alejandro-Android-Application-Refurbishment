package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.volunteer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.adapters.HomelessAdapter;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.logic.Homeless;

import static android.content.Context.MODE_PRIVATE;


public class HomeVolunteerFragment extends Fragment implements View.OnClickListener{


    /*Views*/
    private View view;
    LinearLayout firstHomeless;
    /*Floating Action menu*/
    private FloatingActionButton newHomelessProfile;
    private FloatingActionButton sendDeliveryNotification;

    /*Firebase*/
    private FirebaseFirestore mFirestore;
    private FirebaseUser user;

    /*Shared Preferences*/
    private SharedPreferences preferences;

    /*SearchView bar*/
    private SearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_volunteer, container, false);

        preferences = getActivity().getSharedPreferences("homelessInfo", MODE_PRIVATE);

        initViews();
        firebaseInit();

        setUpRecyclerView();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        newHomelessProfile.setOnClickListener(this);
        sendDeliveryNotification.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_homeless_profile:
                startActivity(new Intent(getActivity(), CreateHomelessProfile.class));
                break;
            case R.id.send_delivery_notification:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DeliveryFragment())
                        .addToBackStack(null).commit();
                break;
        }
    }

    private void initViews(){
        newHomelessProfile = view.findViewById(R.id.new_homeless_profile);
        sendDeliveryNotification = view.findViewById(R.id.send_delivery_notification);
        firstHomeless = view.findViewById(R.id.add_first_homeless);
        firstHomeless.setVisibility(View.VISIBLE);
        searchView = view.findViewById(R.id.volunteer_search);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        searchView.setVisibility(View.GONE);
    }

    private void firebaseInit(){
        mFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void setUpRecyclerView(){

        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final List<Homeless> homelesses = new ArrayList<>();

        mFirestore.collection("homeless").whereEqualTo("volunteerEmail",user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String image = document.getString("image");
                                final String username = document.getString("homelessUsername");
                                final String phone = document.getString("homelessPhoneNumber");
                                final String birthday = document.getString("homelessBirthday");
                                final String lifeHistory = document.getString("homelessLifeHistory");
                                final String schedule = document.getString("homelessSchedule");
                                final String address = document.getString("homelessAddress");
                                String need = document.getString("homelessNeed");

                                final Homeless homeless = new Homeless(image, username, phone, birthday, lifeHistory, address, schedule, need);
                                homelesses.add(homeless);
                                HomelessAdapter homelessAdapter = new HomelessAdapter(homelesses);
                                searchText(homelessAdapter);
                                recyclerView.setAdapter(homelessAdapter);

                                if (homelessAdapter.getItemCount() != 0){
                                    searchView.setVisibility(View.VISIBLE);
                                    firstHomeless.setVisibility(View.GONE);

                                }

                                homelessAdapter.setOnItemClicklistener(new HomelessAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("homelessUsername",  homelesses.get(position).getHomelessUsername()).apply();

                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditHomelessFragment())
                                                .addToBackStack(null).commit();
                                    }
                                });
                            }
                        }
                    }
                });

    }

    private void searchText(final HomelessAdapter homelessAdapter){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                homelessAdapter.getFilter().filter(newText);

                return false;
            }
        });
    }
}
