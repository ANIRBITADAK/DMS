package com.tux.dms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tux.dms.constants.TicketConst;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.DownloadImageTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowImageFragment extends Fragment {

    ImageView imageView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShowImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowImageFragment newInstance(String param1, String param2) {
        ShowImageFragment fragment = new ShowImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_image, container, false);
        imageView = view.findViewById(R.id.showImgView);


        Bundle ticketImageBundle = getArguments();
        String imagePath = (String) ticketImageBundle.get(TicketConst.TICKET_IMG_PATH);
        if (imagePath != null) {
            String imageUrl = "http://" + ApiClient.getIpAddress() + ":" + ApiClient.getPORT() + imagePath;
            new DownloadImageTask(imageView)
                    .execute(imageUrl);
        }


        return view;
    }
}