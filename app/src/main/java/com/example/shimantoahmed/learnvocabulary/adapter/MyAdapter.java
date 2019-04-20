package com.example.shimantoahmed.learnvocabulary.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shimantoahmed.learnvocabulary.Activity.MainActivity;
import com.example.shimantoahmed.learnvocabulary.R;
import com.example.shimantoahmed.learnvocabulary.model.Word;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;
import java.util.Locale;

/**
 * Created by Shimanto Ahmed on 12/23/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  implements
        TextToSpeech.OnInitListener {

    public String tag="zzz";
    List<Word>listItems,filterList;
    private Context context;
    CustomFilter filter;
    private TextToSpeech tts;
    //private MainActivity mainActivity;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListerner(OnItemClickListener listener)
    {
        mListener=listener;
    }

    public MyAdapter(List<Word> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
        this.filterList=listItems;
        tts = new TextToSpeech(context, this);
//        onClickListener = listener;
    }

//    public MyAdapter(List<Word> listItems, MaterialSearchView.SearchViewListener searchViewListener) {
//        this.listItems = listItems;
//    }
//
//    public MyAdapter(List<Word> lstFound, MaterialSearchView.OnQueryTextListener onQueryTextListener) {
//        this.listItems = listItems;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v,mListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Word listItem=listItems.get(position);
        holder.txtEngWord.setText(listItem.getEngWord().substring(0,1).toUpperCase() + listItem.getEngWord().substring(1));
        holder.txtBangWord.setText(listItem.getBangWord());


//        this.setOnItemClickListerner(new OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
////                Log.d("afser","Clicked On Body");
//            }
//
//
//        });




        holder.btnSoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TextToSpeech tts = new TextToSpeech(context, context);
//                tts.setLanguage(Locale.US);
//                tts.speak(listItem.getEngWord(), TextToSpeech.QUEUE_ADD, null);

                speakOut(listItem.getEngWord());

            }
        });


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {

            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut(String txt) {

        tts.speak(txt, TextToSpeech.QUEUE_FLUSH, null);
    }



//    @Override
//    public Filter getFilter() {
//
//        if(filter==null)
//        {
//            filter=new CustomFilter(filterList,this);
//        }
//
//       // Log.d("zzz","Adapter :"+listItems.toString());
//        return filter;
//    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtEngWord;
        public TextView txtBangWord;
        public ImageButton btnSoundBtn;
        public LinearLayout linearLayoutBody;
        public LinearLayout linearLayoutMic;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            txtEngWord=(TextView) itemView.findViewById(R.id.view_eng);
            txtBangWord=(TextView) itemView.findViewById(R.id.view_bang);
            btnSoundBtn=(ImageButton) itemView.findViewById(R.id.soundButton);
            linearLayoutBody=(LinearLayout) itemView.findViewById(R.id.linearLayout_body);
            linearLayoutMic =(LinearLayout) itemView.findViewById(R.id.linear_layout_mic);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener!=null){
//                        int position=getAdapterPosition();
//                        if (position!=RecyclerView.NO_POSITION){
//                            listener.onItemClick(position);
//                        }
//                    }
//                }
//            });

            linearLayoutBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
//            linearLayoutMic.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener!=null){
//                        int position=getAdapterPosition();
//                        if (position!=RecyclerView.NO_POSITION){
//                            listener.onSpecificItemClick(position);
//                        }
//                    }
//                }
//            });


        }
    }





}
