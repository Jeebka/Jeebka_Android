package edu.escuelaing.ieti.jeebka.LoginView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewStateAdapter extends FragmentStateAdapter {

    public ViewStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    //Could be better
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new LoginTabFragment();
        }
        return new SignupTabFragment();
    }

    @Override
    //Could be better x2
    public int getItemCount() {
        return 2;
    }
}