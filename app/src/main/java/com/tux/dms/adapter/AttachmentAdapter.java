package com.tux.dms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tux.dms.R;
import com.tux.dms.dto.Attachment;

import java.util.ArrayList;

public class AttachmentAdapter extends ArrayAdapter<Attachment> {

public AttachmentAdapter(@NonNull Context context, ArrayList<Attachment> attachmentArrayList) {
        super(context, 0, attachmentArrayList);
        }

@NonNull
@Override
public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
        // Layout Inflater inflates each item to be displayed in GridView.
        listitemView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }
        Attachment attachment = getItem(position);
//        TextView courseTV = listitemView.findViewById(R.id.idTVCourse);
        ImageView courseIV = listitemView.findViewById(R.id.imgView);
//        courseTV.setText(pdf.getCourse_name());
        courseIV.setImageResource(attachment.getAttachmentId());
        return listitemView;
        }
}
