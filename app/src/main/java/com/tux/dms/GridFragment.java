package com.tux.dms;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tux.dms.constants.AttachmentConst;
import com.tux.dms.constants.TicketConst;
import com.tux.dms.dto.Attachment;
import com.tux.dms.dto.Image;
import com.tux.dms.dto.Pdf;

import java.util.ArrayList;
import java.util.List;

import adapter.AttachmentAdapter;
import adapter.ImageAdapter;
import adapter.PdfAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridFragment extends Fragment {

    GridView gridView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GridFragment newInstance(String param1, String param2) {
        GridFragment fragment = new GridFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        gridView = view.findViewById(R.id.gridview);
        Bundle attachmentBundle = this.getArguments();
        List<String> imagePaths = attachmentBundle.getStringArrayList(TicketConst.TICKET_IMG_PATH);
        List<String> pdfPaths = attachmentBundle.getStringArrayList(TicketConst.TICKET_PDF_PATH);

        ArrayList<Attachment> attachmentList = new ArrayList<Attachment>();
        for (int i = 0; i < imagePaths.size(); i++)
            attachmentList.add(new Attachment(imagePaths.get(i), R.drawable.ic_baseline_image_24, AttachmentConst.ATTACHMENT_IMAGE_TYPE));

        for (int i = 0; i < pdfPaths.size(); i++)
            attachmentList.add(new Attachment(pdfPaths.get(i), R.drawable.ic_baseline_picture_as_pdf_24,AttachmentConst.ATTACHMENT_PDF_TYPE));


        AttachmentAdapter attachmentAdapter = new AttachmentAdapter(getContext(), attachmentList);
        gridView.setAdapter(attachmentAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity

                if(attachmentList.get(position).getType().equals(AttachmentConst.ATTACHMENT_IMAGE_TYPE)){
                ShowImageFragment showImageFragment = new ShowImageFragment();
                Bundle attachmentBundle=new Bundle();
                attachmentBundle.putString(TicketConst.TICKET_IMG_PATH,attachmentList.get(position).getAttachmentPath() );
                showImageFragment.setArguments(attachmentBundle);
                FragmentManager manager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, showImageFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                }
                else if(attachmentList.get(position).getType().equals(AttachmentConst.ATTACHMENT_PDF_TYPE)){
                    ShowPdfFragment showpdfFragment = new ShowPdfFragment();
                    Bundle attachmentBundle=new Bundle();
                    attachmentBundle.putString(TicketConst.TICKET_PDF_PATH,attachmentList.get(position).getAttachmentPath() );
                    showpdfFragment.setArguments(attachmentBundle);
                    FragmentManager manager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, showpdfFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            }
        });

        return view;
    }
}