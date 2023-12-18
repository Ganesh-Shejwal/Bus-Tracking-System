package com.health.conductorapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConductorListAdapter extends RecyclerView.Adapter<ConductorListAdapter.ViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> conductorList;
    ConductorList conductorList3;

    public ConductorListAdapter(Context context, ArrayList<HashMap<String, String>> conductorList, ConductorList conductorList3) {
        this.context = context;
        this.conductorList = conductorList;
        this.conductorList3 = conductorList3;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conductor_list_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HashMap<String, String> hm = conductorList.get(position);

        holder.txtConductorName.setText("" + hm.get("name"));
        holder.txtConductorContactNumber.setText("" + hm.get("contactNumber"));
        holder.txtConductorVehicleNumber.setText("" + hm.get("vehicleNumber"));
        holder.id.setText(""+hm.get("id"));

        holder.conductorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("contactNumber", hm.get("contactNumber"));
                context.startActivity(intent);


            }
        });

        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conductorList3.deleteConductor(hm.get("id"));
            }
        });

        holder.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp(hm.get("id"),hm.get("name"),hm.get("contactNumber"),hm.get("vehicleNumber"));
            }
        });

    }



    public void showPopUp(String strId, String strName, String strContact, String strVehicle) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.edit_conductor_popup);
        dialog.setTitle("Edit Conductor Record");

        TextView conductorId = (TextView) dialog.findViewById(R.id.txt_popup_id_number);
        EditText conductorName = (EditText) dialog.findViewById(R.id.edit_conductor_name);
        EditText conductorConact = (EditText) dialog.findViewById(R.id.edit_contact_conductor);
        EditText vehicleNumber = (EditText) dialog.findViewById(R.id.edit_vehicle);

        Button updateStudent = dialog.findViewById(R.id.btn_update);

        conductorId.setText("" + strId);
        conductorName.setText("" + strName);
        conductorConact.setText("" + strContact);
        vehicleNumber.setText("" + strVehicle);

        // if button is clicked, close the custom dialog
        updateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conductorName.getText().toString().matches("^[a-zA-Z]*$")&&conductorConact.getText().toString().length()==10)
                {
                    dialog.dismiss();
                    updateConductor(conductorId.getText().toString(),conductorName.getText().toString(),conductorConact.getText().toString(),vehicleNumber.getText().toString());
                }
                else {
                    Toast.makeText(context, "Please enter valid details..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();

    }




    public void updateConductor(String strId, String strName, String strContact, String strVehicle)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_CONDUCTOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("responseData", "" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("responseMessage");
                    if (code.equals("1")) {
                        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
                        conductorList3.conductorList();

                    } else {
                        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("exception", "" + e);
                    Toast.makeText(context, "PLEASE TRY AFTER SOME TIME", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "" + error);
                Toast.makeText(context, "PLEASE TRY AFTER SOME TIME", Toast.LENGTH_SHORT).show();

            }
        }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", strId);
                hashMap.put("name", strName);
                hashMap.put("contactNumber", strContact);
                hashMap.put("vehicleNumber", strVehicle);
                return hashMap;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }



    @Override
    public int getItemCount() {
        return conductorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtConductorName, txtConductorContactNumber, txtConductorVehicleNumber,id,txtDelete,txtEdit;
        CardView conductorCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtConductorName = itemView.findViewById(R.id.txt_conductor_name);
            txtConductorContactNumber = itemView.findViewById(R.id.txt_conductor_mobile_number);
            txtConductorVehicleNumber = itemView.findViewById(R.id.txt_conductor_vehicle_number);
            conductorCard = itemView.findViewById(R.id.conductor_card);
            id = itemView.findViewById(R.id.txt_conductor_id);

            txtEdit = itemView.findViewById(R.id.txt_edit_conductor);
            txtDelete = itemView.findViewById(R.id.txt_delete_conductor);

        }
    }

}
