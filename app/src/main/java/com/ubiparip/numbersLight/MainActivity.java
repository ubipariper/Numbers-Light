package com.ubiparip.numbersLight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainListAdapter.ItemClickListener{

    MainListAdapter adapter;
    DataCrawler dataCrawler;
    PopupWindow popupWindow;
    int openedNumber = -1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showData();
    }

    @Override
    public void onItemClick(View view, int position) {
        openDetails(view, position);
    }

    void openDetails(View view, int position){
        openedNumber = position;
        String detailedImage = dataCrawler.downloadDetailedImage(adapter.getItem(position));
        DetailedDataContainer detailedDataContainer = dataCrawler.packDetailedData(detailedImage);

        view.findViewById(R.id.BackgroundList).setSelected(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            landscapeDetails(detailedDataContainer);
        } else {
            portraitDetails(view, detailedDataContainer);
        }
    }

    void portraitDetails(View view, DetailedDataContainer detailedDataContainer){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.details_view, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popupView, width, height, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                openedNumber = -1;
                return true;
            }
        });

        ((TextView)popupView.findViewById(R.id.DetailedName)).setText(detailedDataContainer.name);
        Picasso.get().load(detailedDataContainer.image).into((ImageView)popupView.findViewById(R.id.DetailedImage));
        ((TextView)popupView.findViewById(R.id.DetailedDescription)).setText(detailedDataContainer.text);
    }

    void landscapeDetails(DetailedDataContainer detailedDataContainer){
        ((TextView)findViewById(R.id.detailedNameLand)).setText(detailedDataContainer.name);
        Picasso.get().load(detailedDataContainer.image).into((ImageView)findViewById(R.id.detailedImageLand));
        ((TextView)findViewById(R.id.detailedDescriptionLand)).setText(detailedDataContainer.text);
    }

    void showData(){
        dataCrawler = new DataCrawler();
        String rawData = dataCrawler.getData();
        if(rawData == ""){
            findViewById(R.id.retryButton).setVisibility(View.VISIBLE);
        }
        else {
            ArrayList<DataContainer> data = dataCrawler.packData(rawData);

            RecyclerView recyclerView = findViewById(R.id.mainList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new MainListAdapter(this, data);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        }
    }

    public void refresh(View view) {
        showData();
        findViewById(R.id.retryButton).setVisibility(View.INVISIBLE);
    }
}