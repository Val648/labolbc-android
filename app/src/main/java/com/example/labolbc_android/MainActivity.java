package com.example.labolbc_android;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private NavController navController;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        navView = findViewById(R.id.bottom_navigation);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // On définit les options de navigation pour garder un comportement fluide (évite de recréer les fragments)
            NavOptions navOptions = new NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .setPopUpTo(navController.getGraph().getStartDestinationId(), false, true)
                    .build();

            // Gestion manuelle complète des clics
            navView.setOnItemSelectedListener(item -> {
                int id = item.getItemId();

                if (id == R.id.navigation_all_visits) {
                    navController.navigate(id, null, navOptions);
                    return true;
                } 
                
                if (id == R.id.navigation_profile) {
                    if (sessionManager.isLoggedIn()) {
                        navController.navigate(R.id.navigation_profile, null, navOptions);
                    } else {
                        navController.navigate(R.id.navigation_login, null, navOptions);
                    }
                    return true;
                } 
                
                if (id == R.id.navigation_my_visits) {
                    if (sessionManager.isLoggedIn()) {
                        navController.navigate(id, null, navOptions);
                        return true;
                    } else {
                        // Toast informatif
                        Toast.makeText(this, "Veuillez vous connecter pour accéder à cet onglet", Toast.LENGTH_SHORT).show();
                        // Redirection vers login si non connecté
                        navController.navigate(R.id.navigation_login, null, navOptions);
                        // On retourne false pour que l'onglet "Mes Visites" ne reste pas allumé indûment
                        return false; 
                    }
                }
                
                return false;
            });

            // Synchronisation de l'état visuel des onglets
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int destId = destination.getId();
                
                if (destId == R.id.navigation_all_visits) {
                    navView.getMenu().findItem(R.id.navigation_all_visits).setChecked(true);
                } else if (destId == R.id.navigation_my_visits) {
                    navView.getMenu().findItem(R.id.navigation_my_visits).setChecked(true);
                } else if (destId == R.id.navigation_profile || destId == R.id.navigation_login || destId == R.id.navigation_register) {
                    // Si on est sur une page liée au profil/auth, on surligne l'onglet du centre
                    navView.getMenu().findItem(R.id.navigation_profile).setChecked(true);
                }
            });
        }
    }
}
