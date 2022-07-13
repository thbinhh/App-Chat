package com.example.demoapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.demoapp.Fragments.CallsFragment;
import com.example.demoapp.Fragments.ChatsFragment;
import com.example.demoapp.Fragments.ContactsFragment;
import com.example.demoapp.Fragments.StatusFragment;
import com.example.demoapp.Fragments.StoryFragment;
import com.example.demoapp.R;

public class FragmentsAdapter extends FragmentPagerAdapter {
    public FragmentsAdapter (@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
                case 0: return new ChatsFragment();
                case 1: return new ContactsFragment();
                case 2: return new StoryFragment();
                default: return new ChatsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    private int[] imageResId = {
            R.drawable.add,
            R.drawable.add,
            R.drawable.add
    };
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;

        if(position == 0)
        {
            title = "CHATS";
        }

        if(position == 1)
        {
            title = "FRIENDS";
        }
        if(position == 2)
        {
            title = "STORY";
        }
        return title;
    }
}
