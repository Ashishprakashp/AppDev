package com.example.e_courier.ui.profile;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.e_courier.DataClass;
import com.example.e_courier.R;
import com.example.e_courier.databinding.FragmentSlideshowBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SlideshowFragment extends Fragment {
    private static final String TAG = "SlideshowFragment";
    private static final int GALLERY_REQUEST_CODE = 123;
    private String imageUrl = "";
    private FragmentSlideshowBinding binding;
    private ImageView uploadImage;
    private EditText name, mail, mobile, address;
    private Button save, edit;
    private String KEY = "";
    private DatabaseReference usersRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        uploadImage = root.findViewById(R.id.profile_pic);
        name = root.findViewById(R.id.name);
        mail = root.findViewById(R.id.mail);
        mobile = root.findViewById(R.id.mobile);
        address = root.findViewById(R.id.Address);
        save = root.findViewById(R.id.upload);
        edit = root.findViewById(R.id.edit);

        Intent intent = getActivity().getIntent();
        String val = intent.getStringExtra("mail");
        if (val != null) {
            KEY = val.replace('.', ',');
            Toast.makeText(root.getContext(), "id: " + val, Toast.LENGTH_LONG).show();
        }

        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(KEY);

        edit.setOnClickListener(v -> enableEditing(true));

        save.setOnClickListener(v -> saveUserData());

        uploadImage.setOnClickListener(v -> openGallery());

        loadUserData();

        return root;
    }

    private void enableEditing(boolean enable) {
        save.setEnabled(enable);
        edit.setEnabled(!enable);
        name.setEnabled(enable);
        address.setEnabled(enable);
    }

    private void saveUserData() {
        String nameText = name.getText().toString();
        String mailText = mail.getText().toString();
        String mobileText = mobile.getText().toString();
        String addressText = address.getText().toString();

        DataClass dataClass = new DataClass(nameText, addressText, mobileText, mailText, imageUrl);

        usersRef.setValue(dataClass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        enableEditing(false);
                        Toast.makeText(requireContext(), "Successfully Inserted", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(requireContext(), "Some Error occurred!", Toast.LENGTH_LONG).show());
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void loadUserData() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String addressText = dataSnapshot.child("address").getValue(String.class);
                    String emailText = dataSnapshot.child("mail").getValue(String.class);
                    String mobileText = dataSnapshot.child("mobile").getValue(String.class);
                    String nameText = dataSnapshot.child("name").getValue(String.class);
                    imageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);

                    if (nameText != null) {
                        name.setText(nameText);
                    } else {
                        name.setText("");
                    }

                    if (emailText != null) {
                        mail.setText(emailText);
                    } else {
                        mail.setText("");
                    }

                    if (mobileText != null) {
                        mobile.setText(mobileText);
                    } else {
                        mobile.setText("");
                    }

                    if (addressText != null) {
                        address.setText(addressText);
                    } else {
                        address.setText("");
                    }

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        loadProfileImage(imageUrl);
                    } else {
                        uploadImage.setImageResource(R.drawable.upload);
                    }
                } else {
                    Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void loadProfileImage(String imageUrl) {
        Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(R.drawable.upload)
                .error(R.drawable.ic_menu_gallery)
                .into(uploadImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                uploadImageToFirebase(imageUri);
            } else {
                Toast.makeText(requireContext(), "Failed to get image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String imageName = KEY + ".jpg";
        StorageReference imageRef = storageRef.child(imageName);

        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            imageUrl = uri.toString();
            usersRef.child("profileImageUrl").setValue(imageUrl);
            loadProfileImage(imageUrl);
            Toast.makeText(requireContext(), "Profile changed!", Toast.LENGTH_SHORT).show();
        })).addOnFailureListener(exception -> Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
