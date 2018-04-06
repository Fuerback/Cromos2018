package com.schulze.desenvolvimento.cromos.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.schulze.desenvolvimento.cromos.R;
import com.schulze.desenvolvimento.cromos.adapter.CromoAdapter;
import com.schulze.desenvolvimento.cromos.helper.CromoDAO;
import com.schulze.desenvolvimento.cromos.helper.DataBase;
import com.schulze.desenvolvimento.cromos.entity.Cromo;
import com.schulze.desenvolvimento.cromos.helper.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigation;
    private RecyclerView recyclerView;
    private CromoAdapter cromoAdapter;
    private List<Cromo> cromos = new ArrayList<>();
    private Cromo cromoSelecionado;
    private Vibrator vibe;
    private ItemTouchHelper itemTouchHelper;
    private Paint p = new Paint();
    private int customRight;
    private int navigationIndex;
    private boolean refreshView;
    private CromoDAO cromoDAO;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_todas:
                    navigationIndex = R.id.navigation_todas;
                    listaCromosTodos(refreshView);
                    return true;
                case R.id.navigation_tem:
                    navigationIndex = R.id.navigation_tem;
                    listaCromosPossui(refreshView);
                    return true;
                case R.id.navigation_falta:
                    navigationIndex = R.id.navigation_falta;
                    listaCromosFalta(refreshView);
                    return true;
                case R.id.navigation_repetidas:
                    navigationIndex = R.id.navigation_repetidas;
                    listaCromosRepetidos(refreshView);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerListaCromos);

        vibe = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE) ;

        refreshView = false;

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_todas);
    }

    private void Swipe(){

        if(itemTouchHelper != null){
            itemTouchHelper.attachToRecyclerView(null);
        }

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                cromoSelecionado = cromos.get(position);

                if(navigationIndex == R.id.navigation_todas){
                    return 0;
                }

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlagsBoth = ItemTouchHelper.START | ItemTouchHelper.END;
                int swipeFlagsEnd = ItemTouchHelper.END;

                if(navigationIndex == R.id.navigation_tem){
                    return makeMovementFlags(dragFlags, swipeFlagsBoth);
                }
                else{
                    return makeMovementFlags(dragFlags, swipeFlagsEnd);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                cromoSelecionado = cromos.get(position);

                if (direction == ItemTouchHelper.START){
                    if(navigationIndex == R.id.navigation_tem){
                        RemoveCromoPossuido(cromoSelecionado);
                    }
                } else {
                    if(navigationIndex == R.id.navigation_tem){
                        if(cromoSelecionado.isRepetida()){
                            RemoveCromoRepetido(cromoSelecionado);
                            navigation.setSelectedItemId(R.id.navigation_tem);
                        }
                        else{
                            AdicionaCromoRepetido(cromoSelecionado);
                        }
                    }
                    else if(navigationIndex == R.id.navigation_falta){
                        RemoveCromoFaltante(cromoSelecionado);
                    }
                    else if(navigationIndex == R.id.navigation_repetidas){
                        RemoveCromoRepetido(cromoSelecionado);
                    }
                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                int position = viewHolder.getAdapterPosition();
                cromoSelecionado = cromos.get(position);

                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(navigationIndex == R.id.navigation_tem){
                        if(dX > 0){
                            if(!cromoSelecionado.isRepetida()) {
                                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_done_all_blue_24dp);
                                if ((int) dX <= drawable.getIntrinsicWidth() + 40) {
                                    customRight = (int) dX;
                                }

                                drawable.setBounds(itemView.getLeft(), itemView.getTop(), customRight, itemView.getBottom());
                                drawable.draw(c);
                            } else{
                                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_done_green_24dp);
                                if ((int) dX <= drawable.getIntrinsicWidth() + 40) {
                                    customRight = (int) dX;
                                }

                                drawable.setBounds(itemView.getLeft(), itemView.getTop(), customRight, itemView.getBottom());
                                drawable.draw(c);
                            }

                        } else {
                            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_red_24dp);
                            if((int)dX * -1 <= drawable.getIntrinsicWidth() + 40){
                                customRight = itemView.getRight() + (int)dX;
                            }

                            drawable.setBounds( customRight,  itemView.getTop(), itemView.getRight(), itemView.getBottom());
                            drawable.draw(c);
                        }
                    }
                    else if(navigationIndex == R.id.navigation_falta){
                        if(dX > 0){
                            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_done_green_24dp);
                            if((int)dX <= drawable.getIntrinsicWidth() + 40){
                                customRight = (int)dX;
                            }

                            drawable.setBounds(itemView.getLeft(), itemView.getTop(), customRight ,itemView.getBottom());
                            drawable.draw(c);
                        }
                    }
                    else if(navigationIndex == R.id.navigation_repetidas){
                        if(dX > 0){
                            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_done_green_24dp);
                            if((int)dX <= drawable.getIntrinsicWidth() + 40){
                                customRight = (int)dX;
                            }

                            drawable.setBounds(itemView.getLeft(), itemView.getTop(), customRight ,itemView.getBottom());
                            drawable.draw(c);
                        }
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        itemTouchHelper = new ItemTouchHelper( itemTouch );
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void RemoveCromoRepetido(Cromo cromo){
        CromoDAO cromoDAO = new CromoDAO(getApplicationContext());
        int position = cromos.indexOf(cromo);

        Parcelable recyclerViewState;
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        if( cromoDAO.atualizarRepetidas(cromo, 0)){
            int scrollPosition = recyclerView.getVerticalScrollbarPosition();
            if(navigationIndex == R.id.navigation_tem){
                navigation.setSelectedItemId(R.id.navigation_tem);
            }
            else{
                navigation.setSelectedItemId(R.id.navigation_repetidas);
            }
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            vibe.vibrate(50);
            Toast.makeText(getApplicationContext(), "Removido de repetidas: " + cromo.getNumero(), Toast.LENGTH_SHORT).show();
        }
    }

    private void RemoveCromoFaltante(Cromo cromo){
        CromoDAO cromoDAO = new CromoDAO(getApplicationContext());
        int position = cromos.indexOf(cromo);

        Parcelable recyclerViewState;
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        if( cromoDAO.atualizarPossui(cromo, 1)){
            refreshView = true;
            navigation.setSelectedItemId(R.id.navigation_falta);
            refreshView = false;
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            vibe.vibrate(50);
            Toast.makeText(getApplicationContext(), "Já tenho: " + cromo.getNumero(), Toast.LENGTH_SHORT).show();
        }
    }

    private void AdicionaCromoRepetido(Cromo cromo){
        CromoDAO cromoDAO = new CromoDAO(getApplicationContext());
        int position = cromos.indexOf(cromo);

        Parcelable recyclerViewState;
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        if( cromoDAO.atualizarRepetidas(cromo, 1)){
            navigation.setSelectedItemId(R.id.navigation_tem);
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            vibe.vibrate(50);
            Toast.makeText(getApplicationContext(), "Item Repetido: " + cromo.getNumero(), Toast.LENGTH_SHORT).show();
        }
    }

    private void RemoveCromoPossuido(Cromo cromo){
        CromoDAO cromoDAO = new CromoDAO(getApplicationContext());
        int position = cromos.indexOf(cromo);

        Parcelable recyclerViewState;
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        if( cromoDAO.atualizarPossui(cromo, 0) && cromoDAO.atualizarRepetidas(cromo, 0)){
            navigation.setSelectedItemId(R.id.navigation_tem);
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            vibe.vibrate(50);
            Toast.makeText(getApplicationContext(), "Não tenho: " + cromo.getNumero(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_superior_principal , menu);
        MenuItem compartilha = menu.findItem(R.id.itemExportar);

        if(navigation.getSelectedItemId() == R.id.navigation_todas){
            compartilha.setVisible(false);
        }
        else{
            compartilha.setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private String getTextoCromos(){
        String texto = "";

        if(navigationIndex == R.id.navigation_repetidas){
            texto = "Minhas figurinhas REPETIDAS: ";
        } else if (navigationIndex == R.id.navigation_falta){
            texto = "Figurinhas que NÃO tenho: ";
        } else if (navigationIndex == R.id.navigation_tem){
            texto = "Figurinhas que TENHO: ";
        }

        for(int i=0; i < cromos.size(); i++){
            texto += " - " + cromos.get(i).getNumero();
        }

        return texto;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.itemExportar :
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getTextoCromos());
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                boolean installed = appInstalledOrNot("com.whatsapp");
                if(installed){
                    startActivity(sendIntent);
                }else{
                    Toast.makeText(getApplicationContext(), "Você precisa ter o WhatsApp instalado para compartilhar.", Toast.LENGTH_LONG).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void listaCromosTodos(boolean refreshView){
        invalidateOptionsMenu();

        cromos.clear();
        if(!refreshView){
            cromoDAO = new CromoDAO(getApplicationContext());
        }

        cromos = cromoDAO.listarTodas();
        setTitle(R.string.app_name);

        cromoAdapter = new CromoAdapter(cromos);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cromoAdapter);
        Swipe();
    }

    public void listaCromosPossui(boolean refreshView){
        invalidateOptionsMenu();

        cromos.clear();
        if(!refreshView){
            cromoDAO = new CromoDAO(getApplicationContext());
        }
        cromos = cromoDAO.listarPossui();

        cromoAdapter = new CromoAdapter(cromos);
        setTitle("Tenho " + cromos.size());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cromoAdapter);
        Swipe();
    }

    public void listaCromosFalta(boolean refreshView){
        invalidateOptionsMenu();

        cromos.clear();
        if(!refreshView){
            cromoDAO = new CromoDAO(getApplicationContext());
        }
        cromos = cromoDAO.listarFalta();

        cromoAdapter = new CromoAdapter(cromos);
        setTitle("Falta " + cromos.size());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cromoAdapter);
        Swipe();
    }

    public void listaCromosRepetidos(boolean refreshView){
        invalidateOptionsMenu();

        cromos.clear();
        if(!refreshView){
            cromoDAO = new CromoDAO(getApplicationContext());
        }
        cromos = cromoDAO.listarRepetidas();

        cromoAdapter = new CromoAdapter(cromos);
        setTitle(cromos.size() + " repetidas");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cromoAdapter);
        Swipe();
    }
}
