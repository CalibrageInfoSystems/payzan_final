package com.calibrage.payzanconsumer.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.calibrage.payzanconsumer.Consumer.ConsumerProfileActivity;
import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.HomeFragment;
import com.calibrage.payzanconsumer.fragement.LoginFragment;
import com.calibrage.payzanconsumer.fragement.TransactionMainFragment;
import com.calibrage.payzanconsumer.fragement.UserProfileHome;
import com.calibrage.payzanconsumer.fragement.agent.AgentLoginFragment;
import com.calibrage.payzanconsumer.fragement.agent.Agent_Home_Fragment;
import com.calibrage.payzanconsumer.framework.interfaces.OnChildFragmentToActivityInteractionListener;
import com.calibrage.payzanconsumer.framework.interfaces.OnFragmentInteractionListener;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;

import java.lang.reflect.Field;
import java.util.Locale;


/**
 * Created by Calibrage11 on 9/22/2017.
 */

public class HomeActivity extends AppCompatActivity implements OnFragmentInteractionListener, OnChildFragmentToActivityInteractionListener {
    // CommunicateFragments
    private android.view.Menu menu;
    public static Toolbar toolbar;

    private FrameLayout content_frame;
    private FragmentManager fragmentManager;
    private android.view.Menu Menu;
    private BottomNavigationView bottomNavigationView;

    private TextView textCartItemCount;
    int mCartItemCount = 10;
    int val = 0;
    int usertype = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

      //updateResources(this,"hi");
        usertype = SharedPrefsData.getInstance(getApplicationContext()).getIntFromSharedPrefs("usertype");
        val = SharedPrefsData.getInstance(HomeActivity.this).getIntFromSharedPrefs(CommonConstants.ISLOGIN);
        setupToolbar();
        fragmentManager = getSupportFragmentManager();
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, homeFragment, "homeTag")
                .commit();

        //  Toast.makeText(this, "testing in activity", Toast.LENGTH_SHORT).show();
        // CommonUtil.printKeyHash(this);


        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        BottomNavigationViewHelper bottomNavigationViewHelper = new BottomNavigationViewHelper();
        bottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.action_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        int menuItemId = item.getItemId();
                        if (menuItemId == R.id.action_home) {

                            if (usertype == CommonConstants.ISAGENT) {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_frame, new Agent_Home_Fragment())
                                        .commit();
                                toolbar.setNavigationIcon(null);
                                toolbar.setTitle("");
                            } else {

                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_frame, new HomeFragment())
                                        .commit();
                                toolbar.setNavigationIcon(null);
                                toolbar.setTitle("");
                            }


                             /*  getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_frame, new HomeFragment())
                                        .commit();
                                toolbar.setNavigationIcon(null);
                                toolbar.setTitle("");*/

                           /* getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, new Agent_Home_Fragment())
                                    .commit();
                            toolbar.setNavigationIcon(null);
                            toolbar.setTitle("");*/


//                                SpannableString s = new SpannableString(item.getTitle());
//                                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.accent,null)), 0, s.length(), 0);
//                                item.setTitle(s);
//                                item.setIcon(R.drawable.ic_cart);


                            return true;
                        } else if (menuItemId == R.id.action_login) {
                              /* insted calling activity we need to cal fragment*/
//                                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
//                                startActivity(intent);
//                                Intent intent1 = new Intent(HomeActivity.this,ProfileActivity.class);
//                                startActivity(intent1);
//                                getSupportFragmentManager().beginTransaction()
//                                        .add(R.id.content_frame, new LoginFragment(),"LoginTag")
//                                        .commit();


                                /*    if user alredy login show profile els show login screen */


                            if (usertype == CommonConstants.ISAGENT) {

                                // TODO : Conver Frgment TO Activity
                                Intent I=new Intent(HomeActivity.this, ConsumerProfileActivity.class);
                                startActivity(I);



                               /* if (val == CommonConstants.Login) {
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.content_frame, new UserProfileHome(), "profileHomeTag")
                                            .commit();
                                    toolbar.setNavigationIcon(null);
                                    toolbar.setTitle("");
                                } else {
                                    getSupportFragmentManager().beginTransaction()
                                            .add(R.id.content_frame, new AgentLoginFragment(), "LoginTag")
                                            .commit();
                                }
*/

                            } else {

                                // TODO : Conver Frgment TO Activity
                              /*  Intent I=new Intent(HomeActivity.this, ConsumerProfileActivity.class);
                                startActivity(I);*/
                                if (val == CommonConstants.Login) {
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.content_frame, new UserProfileHome(), "profileHomeTag")
                                            .commit();
                                    toolbar.setNavigationIcon(null);
                                    toolbar.setTitle("");
                                } else {
                                    getSupportFragmentManager().beginTransaction()
                                            .add(R.id.content_frame, new LoginFragment(), "LoginTag")
                                            .commit();
                                }

                            }
                            if (val == CommonConstants.Login) {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_frame, new UserProfileHome(), "profileHomeTag")
                                        .commit();
                                toolbar.setNavigationIcon(null);
                                toolbar.setTitle("");
                            } else {
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.content_frame, new LoginFragment(), "LoginTag")
                                        .commit();
                            }


                            return true;
                        } else if (menuItemId == R.id.action_wallet) {
                            TransactionMainFragment transactionMainFragment = new TransactionMainFragment();
                            // transactionMainFragment.setFragmentCommunication(HomeActivity.this);
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.content_frame, transactionMainFragment, "walletTag")
                                    .commit();

                            return true;
                        } else if (menuItemId == R.id.action_offers) {
                            return true;
                        }

                       /* switch (item.getItemId()) {
                            case R.id.action_home:

                              *//*  getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_frame, new HomeFragment())
                                        .commit();
                                toolbar.setNavigationIcon(null);
                                toolbar.setTitle("");*//*

                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_frame, new Agent_Home_Fragment())
                                        .commit();
                                toolbar.setNavigationIcon(null);
                                toolbar.setTitle("");


//                                SpannableString s = new SpannableString(item.getTitle());
//                                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.accent,null)), 0, s.length(), 0);
//                                item.setTitle(s);
//                                item.setIcon(R.drawable.ic_cart);
                                break;
                            case R.id.action_login:

                                *//* insted calling activity we need to cal fragment*//*
//                                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
//                                startActivity(intent);
//                                Intent intent1 = new Intent(HomeActivity.this,ProfileActivity.class);
//                                startActivity(intent1);
//                                getSupportFragmentManager().beginTransaction()
//                                        .add(R.id.content_frame, new LoginFragment(),"LoginTag")
//                                        .commit();


                                *//*    if user alredy login show profile els show login screen *//*
                                if (val == CommonConstants.Login) {
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.content_frame, new UserProfileHome(), "profileHomeTag")
                                            .commit();
                                    toolbar.setNavigationIcon(null);
                                    toolbar.setTitle("");
                                } else {
                                    getSupportFragmentManager().beginTransaction()
                                            .add(R.id.content_frame, new LoginFragment(), "LoginTag")
                                            .commit();
                                }


                                break;

                            case R.id.action_wallet:
                                TransactionMainFragment transactionMainFragment = new TransactionMainFragment();
                                // transactionMainFragment.setFragmentCommunication(HomeActivity.this);
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.content_frame, transactionMainFragment, "walletTag")
                                        .commit();

                                break;
                            case R.id.action_offers:
                                break;
//                            case R.id.action_doctor:
//                                break;

                        }*/
                        return true;
                    }
                });


    }

    public void ReplcaFragment(Fragment fragment) {
        fragmentManager.beginTransaction().add(R.id.content_frame, fragment).commit();
    }


    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        menu = bottomNavigationView.getMenu();
        if (val == CommonConstants.Login) {
            menu.findItem(R.id.action_login).setTitle(getString(R.string.profile));
        } else {
            menu.findItem(R.id.action_login).setTitle(getString(R.string.login));
        }

//        String valuee="profile";
////        getMenuInflater().inflate(R.menu.bottom_navigation_main, menu);//Menu Resource, Menu
//        MenuItem menuItem = menu.findItem(R.id.action_offers);
//           menuItem.setIcon(buildCounterDrawable(HomeActivity.this,2,  R.drawable.ic_notification));
//
//         final MenuItem menuItem = menu.findItem(R.id.action_cart);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);//Menu Resource, Menu
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        //menuItem.setIcon(buildCounterDrawable(HomeActivity.this,2,  R.drawable.ic_notification));

        // final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
//        String valuee="profile";
//        getMenuInflater().inflate(R.menu.bottom_navigation_main, menu);//Menu Resource, Menu
//     MenuItem menuItem = menu.findItem(R.id.action_login);
//      //   menuItem.setIcon(buildCounterDrawable(HomeActivity.this,2,  R.drawable.ic_notification));
//        menuItem.setTitle(valuee);
        // final MenuItem menuItem = menu.findItem(R.id.action_cart);
        return true;
    }


    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
           // Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();

        }
      /*  else if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        } */
        else if (item.getItemId() == R.id.action_cart) {
            //   item.setIcon(buildCounterDrawable(this,20,  R.drawable.ic_birthday));
           // Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        }
        // handle arrow click here

        return super.onOptionsItemSelected(item);
    }

    /* mahesh commented for back press to close app */
  /*  @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        dialog.setContentView(R.layout.alert_dialouge_home);

        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        *//*if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            this.finish();
        }*//*

    }*/
    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(ContextCompat.getColor(HomeActivity.this, R.color.new_accent));
        toolbar.setTitle("f");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setTitle(getResources().getString(R.string.landline_sname));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_new));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                    toolbar.setNavigationIcon(null);
                    toolbar.setTitle("");
                    getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });


    }

//    @Override
//    public void onFragmentInteraction(String id) {
//        Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
//    }


    @Override
    public void messageFromParentFragmentToActivity(String myString) {

    }

    @Override
    public void messageFromChildFragmentToActivity(String myString) {
        if (myString.equalsIgnoreCase("moveTowallet")) {
            bottomNavigationView.setSelectedItemId(R.id.action_wallet);
        } else if (myString.equalsIgnoreCase("signout")) {
            val = SharedPrefsData.getInstance(HomeActivity.this).getIntFromSharedPrefs(CommonConstants.ISLOGIN);
            setupToolbar();
            menu = bottomNavigationView.getMenu();
            if (val == CommonConstants.Login) {
                menu.findItem(R.id.action_login).setTitle(getString(R.string.profile));
            } else {
                menu.findItem(R.id.action_login).setTitle(getString(R.string.login));
            }
        } else {
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        }

    }


    public class BottomNavigationViewHelper {

        @SuppressLint("RestrictedApi")
        public void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBottomIcons();
    }

    private void updateBottomIcons() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("homeTag");
        if (fragment instanceof HomeFragment) {
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        }
    }
    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            HomeActivity.toolbar.setNavigationIcon(null);
            HomeActivity.toolbar.setTitle("");

            Toast.makeText(this, R.string.toast_obback, Toast.LENGTH_SHORT).show();

            getSupportFragmentManager().popBackStackImmediate();

        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            final Dialog dialog = new Dialog(HomeActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            dialog.setContentView(R.layout.alert_dialouge_home);

            Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
            Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);


            ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                    finish();
                }
            });
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            //dialog.setCancelable(false);
            dialog.show();

        }


    }


}