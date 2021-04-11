/*
 * Author: Asad Jawaid
 * */
package com.example.bookapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogoutFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance(); // root node
        DatabaseReference reference = rootNode.getReference("Users");
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // get current user id

        Button logout = (Button) view.findViewById(R.id.logout_user);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        TextView userName = (TextView) view.findViewById(R.id.user_name);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                alert.setMessage("Are you sure you want to logout?").setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        // remove any previous activities. This will prevent the user from going back to this activity after they logout!
                        // After the user is directed to the main activity and if they click the back button it will direct them to their device's homepage
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", null);

                AlertDialog ac = alert.create();
                ac.show();
            }
        });

        reference.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameOfUser = "User: " + snapshot.child("name").getValue().toString();
                Log.d("Username", nameOfUser);
                userName.setText(nameOfUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}