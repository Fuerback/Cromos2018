package com.fuerback.desenvolvimento.cromos.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.fuerback.desenvolvimento.cromos.R;
import com.fuerback.desenvolvimento.cromos.adapter.CromoAdapter;
import com.fuerback.desenvolvimento.cromos.helper.CromoDAO;
import com.fuerback.desenvolvimento.cromos.entity.Cromo;
import com.fuerback.desenvolvimento.cromos.helper.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigation;
    private RecyclerView recyclerView;
    private CromoAdapter cromoAdapter;
    private List<Cromo> cromos = new ArrayList<>();
    private List<Cromo> cromosSaveCopy = new ArrayList<>();
    private Cromo cromoSelecionado;
    private Vibrator vibe;
    private ItemTouchHelper itemTouchHelper;
    private int navigationIndex;
    private boolean refreshView;
    private CromoDAO cromoDAO;
    private Toast toastObject;
    private static final float SWIPE_LEFT = 1;
    private static final float SWIPE_RIGHT = 0;
    private static final float INPUT_REPETIDAS = 1;
    private static final float INPUT_TENHO = 0;
    private String possuiImportados = "";
    private String repetidosImportados = "";

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

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_falta);
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
                    if(navigationIndex == R.id.navigation_tem){
                        if(dX > 0){
                            if(!cromoSelecionado.isRepetida()) {
                                drawBackgroundSwipe(c, viewHolder, dX, "Repetida", Color.parseColor("#3399ff"), SWIPE_RIGHT);
                            } else{
                                drawBackgroundSwipe(c, viewHolder, dX, "Tenho", Color.parseColor("#8bc34a"), SWIPE_RIGHT);
                            }

                        } else {
                            drawBackgroundSwipe(c, viewHolder, (dX * -1), "Falta", Color.parseColor("#cc0000"), SWIPE_LEFT);
                        }
                    }
                    else if(navigationIndex == R.id.navigation_falta){
                        if(dX > 0){
                            drawBackgroundSwipe(c, viewHolder, dX, "Tenho", Color.parseColor("#8bc34a"), SWIPE_RIGHT);
                        }
                    }
                    else if(navigationIndex == R.id.navigation_repetidas){
                        if(dX > 0){
                            drawBackgroundSwipe(c, viewHolder, dX, "Tenho", Color.parseColor("#8bc34a"), SWIPE_RIGHT);
                        }
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        itemTouchHelper = new ItemTouchHelper( itemTouch );
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void drawBackgroundSwipe(Canvas c, RecyclerView.ViewHolder viewHolder, float customRight, String text, int color, float swipeDirection) {
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();
        RectF leftBackground;

        if(swipeDirection == SWIPE_RIGHT){
            leftBackground = new RectF(itemView.getLeft(), itemView.getTop(),
                    customRight, itemView.getBottom());
        }
        else {
            leftBackground = new RectF(itemView.getRight() - customRight, itemView.getTop(),
                    itemView.getRight(), itemView.getBottom());
        }
        p.setColor(color);
        c.drawRoundRect(leftBackground, corners, corners, p);
        drawText(text, c, leftBackground, p);
    }

    private void drawText(String text, Canvas c, RectF leftBackground, Paint p) {
        float textSize = 60;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, leftBackground.centerX()-(textWidth/2), leftBackground.centerY()+(textSize/2), p);
    }

    private void RemoveCromoRepetido(Cromo cromo){
        CromoDAO cromoDAO = new CromoDAO(getApplicationContext());

        Parcelable recyclerViewState;
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        if( cromoDAO.atualizarRepetidas(cromo, 0)){
            if(navigationIndex == R.id.navigation_tem){
                navigation.setSelectedItemId(R.id.navigation_tem);
            }
            else{
                navigation.setSelectedItemId(R.id.navigation_repetidas);
            }
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            vibe.vibrate(50);
            ShowToast("Removido de repetidas: " + cromo.getNumero());
        }
    }

    private void RemoveCromoFaltante(Cromo cromo){
        CromoDAO cromoDAO = new CromoDAO(getApplicationContext());

        Parcelable recyclerViewState;
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        if( cromoDAO.atualizarPossui(cromo, 1)){
            refreshView = true;
            navigation.setSelectedItemId(R.id.navigation_falta);
            refreshView = false;
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            vibe.vibrate(50);
            ShowToast("Já tenho: " + cromo.getNumero());
        }
    }

    private void AdicionaCromoRepetido(Cromo cromo){
        CromoDAO cromoDAO = new CromoDAO(getApplicationContext());

        Parcelable recyclerViewState;
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        if( cromoDAO.atualizarRepetidas(cromo, 1)){
            navigation.setSelectedItemId(R.id.navigation_tem);
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            vibe.vibrate(50);
            ShowToast("Item Repetido: " + cromo.getNumero());
        }
    }

    private void RemoveCromoPossuido(Cromo cromo){
        CromoDAO cromoDAO = new CromoDAO(getApplicationContext());

        Parcelable recyclerViewState;
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        if( cromoDAO.atualizarPossui(cromo, 0) && cromoDAO.atualizarRepetidas(cromo, 0)){
            navigation.setSelectedItemId(R.id.navigation_tem);
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            vibe.vibrate(50);
            ShowToast("Não tenho: " + cromo.getNumero());
        }
    }

    public void ShowToast(String message){
        if(toastObject != null){
            toastObject.cancel();
        }
        toastObject = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toastObject.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_superior_principal , menu);

        final MenuItem compartilha = menu.findItem(R.id.itemExportar);
        final MenuItem copiar = menu.findItem(R.id.itemCopiar);
        final MenuItem menuItemSobre = menu.findItem(R.id.itemSobre);
        final MenuItem menuItemImportar = menu.findItem(R.id.itemImportar);
        SearchView searchView = (SearchView) menu.findItem(R.id.itemPesquisar).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    List<Cromo> listFound = new ArrayList<Cromo>();
                    for (Cromo cromo:cromosSaveCopy){
                        try{
                            int numero = Integer.parseInt(newText);
                            if(cromo.getNumero() == numero){
                                listFound.add(cromo);
                            }
                        } catch (NumberFormatException e){
                            if(cromo.getNome().toUpperCase().contains(newText.toUpperCase())){
                                listFound.add(cromo);
                            }
                        }
                    }

                    cromoAdapter = new CromoAdapter(listFound);
                    recyclerView.setAdapter(cromoAdapter);
                    cromos = listFound;
                }else{
                    cromoAdapter = new CromoAdapter(cromosSaveCopy);
                    recyclerView.setAdapter(cromoAdapter);
                    cromos = cromosSaveCopy;
                }

                return true;
            }
        });

        if(navigation.getSelectedItemId() == R.id.navigation_todas){
            compartilha.setVisible(false);
            copiar.setVisible(false);
            menuItemSobre.setVisible(true);
            menuItemImportar.setVisible(true);
        }
        else{
            compartilha.setVisible(true);
            copiar.setVisible(true);
            menuItemSobre.setVisible(false);
            menuItemImportar.setVisible(false);
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
            texto = "REPETIDAS: ";
        } else if (navigationIndex == R.id.navigation_falta){
            texto = "PRECISO: ";
        } else if (navigationIndex == R.id.navigation_tem){
            texto = "TENHO: ";
        }

        for(int i=0; i < cromos.size(); i++){
            texto += cromos.get(i).getNumero();
            if(i + 1 != cromos.size()){
                texto += ", ";
            }
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
                    ShowToast("Você precisa ter o WhatsApp instalado para compartilhar.");
                }
                break;
            case R.id.itemCopiar :
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", getTextoCromos());
                clipboard.setPrimaryClip(clip);
                ShowToast("Lista atual copiada!");
                break;
            case R.id.itemSobre :
                Intent sobreIntent = new Intent(getApplicationContext(), SobreActivity.class);
                startActivity(sobreIntent);
                break;
            case R.id.itemTutorial :
                PrefManager prefManager = new PrefManager(this);
                prefManager.setFirstTimeLaunch(true);
                startActivity(new Intent(getApplicationContext(), TutorialActivity.class));
                finish();
                break;
            case R.id.itemImportar :
                ImportarPossui();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ImportarPossui() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cromos que você já possui");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setHint("Ex: 1, 12, 125");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                possuiImportados = input.getText().toString();
                ImportarCromosBanco(possuiImportados, INPUT_TENHO);
                ImportarRepetidos();
            }
        });

        builder.show();
    }

    private void ImportarRepetidos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cromos repetidos");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setHint("Ex: 1, 12, 125");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                repetidosImportados = input.getText().toString();
                ImportarCromosBanco(repetidosImportados, INPUT_REPETIDAS);
                navigation.setSelectedItemId(R.id.navigation_tem);
            }
        });

        builder.show();
    }

    private void ImportarCromosBanco(String listaImportada, float inputText) {
        String message = "Lista importada.";
        List<Cromo> cromosImportados = new ArrayList<>();
        String[] importados;
        if(listaImportada.contains(",")){
            importados = listaImportada.split(",");
        }
        else{
            importados = new String[1];
            importados[0] = listaImportada;
        }

        for(int i=0; i<importados.length; i++){
            try{
                int numero = Integer.parseInt(importados[i].trim().toString());
                Cromo cromo = cromosSaveCopy.get(numero);
                if(inputText == INPUT_TENHO){
                    cromoDAO.atualizarPossui(cromo, 1);
                }
                else {
                    cromoDAO.atualizarRepetidas(cromo, 1);
                }
            } catch (Exception e){
                message = "Você importou valores inválidos.";
                break;
            }
        }

        ShowToast(message);
    }

    public void listaCromosTodos(boolean refreshView){
        invalidateOptionsMenu();

        cromos.clear();
        if(!refreshView){
            cromoDAO = new CromoDAO(getApplicationContext());
        }

        cromos = cromoDAO.listarTodas();
        cromosSaveCopy = cromos;
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
        cromosSaveCopy = cromos;

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
        cromosSaveCopy = cromos;

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
        cromosSaveCopy = cromos;

        cromoAdapter = new CromoAdapter(cromos);
        setTitle(cromos.size() + " repetidas");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cromoAdapter);
        Swipe();
    }
}
