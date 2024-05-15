package com.example.e_courier.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_courier.DataClass;
import com.example.e_courier.R;
import com.example.e_courier.databinding.FragmentSlideshowBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    ImageView uploadImage;
    EditText name,mail,mobile,address;
    Button save,edit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_slideshow, container, false);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        uploadImage=root.findViewById(R.id.profile_pic);
        name=root.findViewById(R.id.name);
        mail=root.findViewById(R.id.mail);
        mobile=root.findViewById(R.id.mobile);
        address=root.findViewById(R.id.Address);
        save = root.findViewById(R.id.upload);
        edit= root.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setEnabled(true);
                edit.setEnabled(false);
                name.setEnabled(true);
                mail.setEnabled(true);
                mobile.setEnabled(true);
                address.setEnabled(true);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameText= name.getText().toString();
                String mailText= mail.getText().toString();
                String mobileText= mobile.getText().toString();
                String addressText= address.getText().toString();
                DataClass dataClass = new DataClass(nameText,addressText,mobileText,mailText);

                FirebaseDatabase.getInstance().getReference().child(nameText).setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            name.setEnabled(false);
                            mail.setEnabled(false);
                            mobile.setEnabled(false);
                            address.setEnabled(false);
                            save.setEnabled(false);
                            edit.setEnabled(true);
                            Toast.makeText(requireContext(),"Successfully Inserted",Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(),"Some Error occurred!",Toast.LENGTH_LONG).show();
                    }
                });
            }


        });
       // Toast.makeText(requireContext(),"Successfully Inserted",Toast.LENGTH_LONG).show();
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}