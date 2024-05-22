package com.example.e_courier.ui.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.e_courier.R;
import com.example.e_courier.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.calculator, container, false);
        Animation slideInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in);

        // Find the view you want to animate
        View viewToAnimate1 = rootView.findViewById(R.id.title);
        View viewToAnimate2 = rootView.findViewById(R.id.weight);
        View viewToAnimate3 = rootView.findViewById(R.id.weightET);
        View viewToAnimate4 = rootView.findViewById(R.id.distance);
        View viewToAnimate5 = rootView.findViewById(R.id.distanceET);
        View viewToAnimate6 = rootView.findViewById(R.id.delivery_type);
        View viewToAnimate7 = rootView.findViewById(R.id.spinner);
        View viewToAnimate8 = rootView.findViewById(R.id.calculate);

        // Apply the animation to the view
        viewToAnimate1.startAnimation(slideInAnimation);
        viewToAnimate2.startAnimation(slideInAnimation);
        viewToAnimate3.startAnimation(slideInAnimation);
        viewToAnimate4.startAnimation(slideInAnimation);
        viewToAnimate5.startAnimation(slideInAnimation);
        viewToAnimate6.startAnimation(slideInAnimation);
        viewToAnimate7.startAnimation(slideInAnimation);
        viewToAnimate8.startAnimation(slideInAnimation);
        Spinner dropdown = rootView.findViewById(R.id.spinner);
        String[] items = new String[]{"Normal", "Premium", "Express", "Premium express"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        Button calculate = rootView.findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double weight = Double.parseDouble(((EditText) rootView.findViewById(R.id.weightET)).getText().toString());
                int distance = Integer.parseInt(((EditText) rootView.findViewById(R.id.distanceET)).getText().toString());
                String type = ((Spinner) rootView.findViewById(R.id.spinner)).getSelectedItem().toString();

                int Normal_min = 50;
                int express_min = 75;
                int premium_min = 100;
                int expresspremium_min = 150;
                double cost = 0;

                if (type.equals("Normal")) {
                    cost = distance / 10 + (weight * distance) / 5;
                    cost = Math.max(cost, Normal_min);
                } else if (type.equals("Premium")) {
                    cost = distance / 9 + (weight * distance) / 3;
                    cost = Math.max(cost, express_min);
                } else if (type.equals("Express")) {
                    cost = distance / 10 + (weight * distance) / 3;
                    cost = Math.max(cost, premium_min);
                } else if (type.equals("Premium express")) {
                    cost = distance / 7 + (weight * distance) / 3;
                    cost = Math.max(cost, expresspremium_min);
                }

                TextView tv = rootView.findViewById(R.id.op);
                tv.setText(String.valueOf(cost));
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
