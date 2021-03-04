package com.studentsac.iachat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.studentsac.iachat.R;
import com.studentsac.iachat.adapters.ViewPageradapter;
import com.studentsac.iachat.fragments.ChatFragment;
import com.studentsac.iachat.fragments.FriendsFragment;
import com.studentsac.iachat.fragments.HistoriesFragment;
import com.studentsac.iachat.fragments.PhotoFragment;
import com.studentsac.iachat.providers.MainActivity;

public class HomeActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    FirebaseAuth mAuth;

    MaterialSearchBar mMaterialSearchBar;
    TabLayout mTabLayout;
    ViewPager mViewPayer;

    PhotoFragment mPhotoFragment;
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
        mPhotoFragment = new PhotoFragment();

        mViewPageradapter.addFragment(mPhotoFragment,"");
        mViewPageradapter.addFragment(mChatFragment,"CHATS");
        mViewPageradapter.addFragment(mHistoriesFragment,"HISTORIAS");
        mViewPageradapter.addFragment(mFriendsFragment,"AMIGOS");
        mViewPayer.setAdapter(mViewPageradapter);
        mViewPayer.setCurrentItem(1);

        mTabLayout.setupWithViewPager(mViewPayer);


        tabIcon();

        mMaterialSearchBar.setOnSearchActionListener(this);
        mMaterialSearchBar.inflateMenu(R.menu.menu_main);
        mMaterialSearchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.itemSignOut){
                    signOut();
                }
                else if(item.getItemId() == R.id.itemProfile){
                    goProfile();
                }
                return true;
            }
        });
    }

    private void goProfile() {
        Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
        startActivity(intent);
    }

    private void tabIcon() {
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        LinearLayout linearLayout = ((LinearLayout)((LinearLayout)mTabLayout.getChildAt(0)).getChildAt(0));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.weight = 0.5f;
        linearLayout.setLayoutParams(layoutParams);
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