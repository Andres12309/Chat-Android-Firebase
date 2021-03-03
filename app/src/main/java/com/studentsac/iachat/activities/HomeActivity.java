package com.studentsac.iachat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.studentsac.iachat.R;
import com.studentsac.iachat.adapters.ViewPageradapter;
import com.studentsac.iachat.fragments.ChatFragment;
import com.studentsac.iachat.fragments.FriendsFragment;
import com.studentsac.iachat.fragments.HistoriesFragment;
import com.studentsac.iachat.providers.MainActivity;

public class HomeActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    FirebaseAuth mAuth;

    MaterialSearchBar mMaterialSearchBar;
    TabLayout mTabLayout;
    ViewPager mViewPayer;

    ChatFragment mChatFragment;
    FriendsFragment mFriendsFragment;
    HistoriesFragment mHistoriesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        mMaterialSearchBar = findViewById(R.id.searchBar);

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPayer = findViewById(R.id.viewpager);

        mViewPayer.setOffscreenPageLimit(3);

        ViewPageradapter mViewPageradapter = new ViewPageradapter(getSupportFragmentManager());

        mChatFragment = new ChatFragment();
        mFriendsFragment = new FriendsFragment();
        mHistoriesFragment = new HistoriesFragment();

        mViewPageradapter.addFragment(mChatFragment,"CHATS");
        mViewPageradapter.addFragment(mHistoriesFragment,"HISTORIAS");
        mViewPageradapter.addFragment(mFriendsFragment,"AMIGOS");
        mViewPayer.setAdapter(mViewPageradapter);

        mTabLayout.setupWithViewPager(mViewPayer);

        mMaterialSearchBar.setOnSearchActionListener(this);
        mMaterialSearchBar.inflateMenu(R.menu.menu_main);
        mMaterialSearchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.itemSignOut){
                    signOut();
                }
                return true;
            }
        });
    }

    private void signOut(){
        mAuth.signOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}